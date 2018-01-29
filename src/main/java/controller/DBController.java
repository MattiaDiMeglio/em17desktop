package controller;


import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.*;
import controller.chartsController.ChartsController;
import model.EventListModel;
import model.EventModel;

import java.io.IOException;
import java.io.InputStream;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class DBController {

    private static final String DATABASE_URL = "https://ingws-20.firebaseio.com/";
    private DatabaseReference database;
    private static DBController instance = new DBController();
    private EventListModel eventListModel;

    public static DBController getInstance() {
        return instance;
    }

    private DBController() {
        initializeDatabase();
    }

    private void initializeDatabase() {

        System.out.println("Inizializzo database...");

        try {
            // [START initialize]

            /*
             * Utilizzando FileInputStream, come nella guida, non funziona una cazzo di niente, quindi utilizzo l'InputStream
             * che preleva direttamente il file invece di andarlo a creare con il contenuto.
             * Per fare questo mi vado a prendere il contesto della classe per caricare gli oggetti (getClassLoader)
             * legato al Thread sul quale sto lavorando, da qui accedo al file direttamente caricato come stream (getResourcesAsStream).
             *
             * Per vedere lo spaccimma del Json si deve andare a caricare a mano la cartella resources
             * nelle impostazioni dell'artifact in Intellij
             */
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            InputStream inputStream = loader.getResourceAsStream("ingws-20-firebase-adminsdk.json");

            //adesso prendo lo stream e lo butto dove controllerà le credenziali d'accesso
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(inputStream))
                    .setDatabaseUrl(DATABASE_URL)
                    .build();
            FirebaseApp.initializeApp(options);
            System.out.println("database.Database inizializzato\n");
            // [END initialize]
        } catch (IOException e) {
            System.out.println("ERROR: invalid service account credentials. Il Json potrebbe essere sminchiato.");
            e.printStackTrace();

            //esco dal programma
            System.exit(1);
        }


        System.out.println("Collegamento al database...");
        //Scelgo la root di partenza del database
        database = FirebaseDatabase.getInstance().getReference("luogo");

        ChartsController.getInstance().setDatabase(database);
        ChartsController.getInstance().populateCharts("2018");


    }


    public void dashBoard() {
        database.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                try {
                    Iterable<DataSnapshot> location = snapshot.getChildren();
                    eventListModel = EventListModel.getInstance();//ottengo l'instanza di event controller
                    int i = 0;
                    while (location.iterator().hasNext()) {
                        DataSnapshot locationSnap = location.iterator().next();

                        Iterable<DataSnapshot> settori = locationSnap.child("settori").getChildren();
                        Integer totTickets = 0;

                        List<String> settoriName = new ArrayList<>();
                        while (settori.iterator().hasNext()) {
                            DataSnapshot settoriSnap = settori.iterator().next();
                            settoriName.add(settoriSnap.getKey());
                            totTickets = totTickets + Integer.valueOf(settoriSnap.getValue().toString());
                        }
                        Iterable<DataSnapshot> eventi = locationSnap.child("Eventi").getChildren();
                        while (eventi.iterator().hasNext()) {
                            DataSnapshot eventiSnap = eventi.iterator().next();
                            HashMap<String, Integer> settoriMap = new HashMap<>();
                            for (String aSettoriName : settoriName) {
                                settoriMap.put(aSettoriName, 0);
                            }
                            EventModel event = new EventModel();//creo un nuovo event model
                            Integer ticketSold = 0;
                            event.initializeTicketPerMonth();
                            Iterable<DataSnapshot> eventiIteable = eventiSnap.child("biglietti").getChildren();
                            while (eventiIteable.iterator().hasNext()) {
                                DataSnapshot bigliettiSnap = eventiIteable.iterator().next();
                                Integer accesses = Integer.valueOf(bigliettiSnap.child("accessi").getValue().toString());

                                settoriMap.put(bigliettiSnap.child("settore").getValue().toString(),
                                        settoriMap.get(bigliettiSnap.child("settore").getValue().toString()) + accesses);

                                ticketSold = ticketSold + accesses;

                                String eventEndDate = bigliettiSnap.child("data vendita").getValue().toString();
                                Date eventEndTime = new SimpleDateFormat("dd/MM/yyyy").parse(eventEndDate);

                                event.addOneSoldPerMonth(eventEndTime.getMonth(), accesses);
                            }
                            event.setEventKey(eventiSnap.getKey());
                            System.out.println(event.getEventKey());
                            event.setListaSettoriName(settoriName);
                            event.setListaVenditaPerSettori(settoriMap);
                            event.setIndex(i);
                            event.setMaxVisitatori(totTickets);
                            event.setTicketSold(ticketSold);
                            event.setLocationAddress(locationSnap.child("indirizzo").getValue().toString());
                            event.setNomeLocation(locationSnap.child("nome").getValue().toString());
                            event.setNomeEvento(eventiSnap.child("nome").getValue().toString());
                            event.setAttivo((boolean) eventiSnap.child("attivo").getValue());
                            event.setDescrizione(eventiSnap.child("descrizione").getValue().toString());
                            event.setLocandina(eventiSnap.child("copertina").getValue().toString());
                            DataSnapshot dataSnapshot = eventiSnap.child("data");
                            DataSnapshot timeSnapshot = eventiSnap.child("ora");
                            String eventStartDate = dataSnapshot.child("inizio").getValue().toString();
                            try {
                                Date eventStartTime = new SimpleDateFormat("dd/MM/yyyy").parse(eventStartDate);
                                LocalDate localDate = eventStartTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                                String date = localDate.getDayOfMonth() + "/" + localDate.getMonthValue() + "/" + localDate.getYear();

                                event.setDataInizio(date);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            String eventEndDate = dataSnapshot.child("fine").getValue().toString();
                            try {
                                Date eventEndTime = new SimpleDateFormat("dd/MM/yyyy").parse(eventEndDate);
                                LocalDate localDate = eventEndTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                                String date = localDate.getDayOfMonth() + "/" + localDate.getMonthValue() + "/" + localDate.getYear();

                                event.setDataFine(date);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            try {
                                List <String> slidehow = new ArrayList<>();
                                DataSnapshot slideSnap = eventiSnap.child("galleria");
                                Integer j=0;
                                while (j < slideSnap.getChildrenCount()) {
                                    String index = j.toString();
                                    slidehow.add(slideSnap.child(index).getValue().toString());
                                    j++;
                                }
                                event.setSlideshow(slidehow);

                            } catch (Exception e){
                                e.printStackTrace();
                            }
                            eventListModel.setListaEventi(event);
                            i++;

                        }


                    }
                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });


        return;
    }

    public boolean delete(String key) {


        return false;
    }


}
