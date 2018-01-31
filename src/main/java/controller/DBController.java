package controller;


import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.*;
import controller.chartsController.ChartsController;
import javafx.scene.image.Image;
import model.EventListModel;
import model.EventModel;
import view.LoadingPopupView;

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
import java.util.concurrent.CountDownLatch;


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

            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            InputStream inputStream = loader.getResourceAsStream("ingws-20-firebase-adminsdk.json");

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(inputStream))
                    .setDatabaseUrl(DATABASE_URL)
                    .build();
            FirebaseApp.initializeApp(options);
            System.out.println("database.Database inizializzato\n");
        } catch (IOException e) {
            System.out.println("ERROR: invalid service account credentials. Il Json potrebbe essere sminchiato.");
            e.printStackTrace();

            System.exit(1);
        }


        System.out.println("Collegamento al database...");
        //Scelgo la root di partenza del database
        database = FirebaseDatabase.getInstance().getReference("luogo");
        ChartsController.getInstance().setDatabase(database);
    }

    public void dashBoard() {
        CountDownLatch latch = new CountDownLatch(2);
        new Thread(() -> ChartsController.getInstance().populateCharts("2018", latch)).start();
        new Thread(() -> database.addListenerForSingleValueEvent(new ValueEventListener() {

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
                            event.setSectorNameList(settoriName);
                            event.setSoldPerSectorList(settoriMap);
                            event.setIndex(i);
                            event.setMaxVisitors(totTickets);
                            event.setTicketSold(ticketSold);
                            event.setLocationAddress(locationSnap.child("indirizzo").getValue().toString());
                            event.setLocationName(locationSnap.child("nome").getValue().toString());
                            event.setEventName(eventiSnap.child("nome").getValue().toString());
                            event.setActive((boolean) eventiSnap.child("attivo").getValue());
                            event.setEventDescription(eventiSnap.child("descrizione").getValue().toString());

                            CountDownLatch latch1 = new CountDownLatch(1);
                            new Thread(() -> {
                                System.out.println("scarico l'immagine");
                                Image image = new Image(eventiSnap.child("copertina").getValue().toString());
                                event.setBillboard(image);
                                System.out.println("settato");
                                latch1.countDown();
                            }).start();

                            DataSnapshot dataSnapshot = eventiSnap.child("data");
                            DataSnapshot timeSnapshot = eventiSnap.child("ora");
                            String eventStartDate = dataSnapshot.child("inizio").getValue().toString();
                            try {
                                Date eventStartTime = new SimpleDateFormat("dd/MM/yyyy").parse(eventStartDate);
                                LocalDate localDate = eventStartTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                                String date = localDate.getDayOfMonth() + "/" + localDate.getMonthValue() + "/" + localDate.getYear();

                                event.setStartingDate(date);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            String eventEndDate = dataSnapshot.child("fine").getValue().toString();
                            try {
                                Date eventEndTime = new SimpleDateFormat("dd/MM/yyyy").parse(eventEndDate);
                                LocalDate localDate = eventEndTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                                String date = localDate.getDayOfMonth() + "/" + localDate.getMonthValue() + "/" + localDate.getYear();

                                event.setEndingDate(date);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            try {
                                List<Image> slidehow = new ArrayList<>();
                                DataSnapshot slideSnap = eventiSnap.child("galleria");
                                Integer j = 0;

                                CountDownLatch latchSlideShow = new CountDownLatch(Math.toIntExact(slideSnap.getChildrenCount()));
                                while (j < slideSnap.getChildrenCount()) {
                                    Integer finalJ = j;
                                    new Thread(() -> {
                                        String index = finalJ.toString();
                                        slidehow.add(new Image(slideSnap.child(index).getValue().toString()));
                                        latchSlideShow.countDown();
                                    }).start();

                                    j++;
                                }
                                latchSlideShow.await();
                                event.setSlideshow(slidehow);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            latch1.await();
                            eventListModel.setListaEventi(event);
                            i++;
                        }


                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                latch.countDown();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.out.println(error.getMessage());
                latch.countDown();
            }
        })).start();
        new LoadingPopupView(latch);
    }

    public boolean delete(String key) {

        database.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                try {
                    Iterable<DataSnapshot> location = snapshot.getChildren();
                    while (location.iterator().hasNext()) {
                        DataSnapshot locationSnap = location.iterator().next();
                        Iterable<DataSnapshot> eventi = locationSnap.child("Eventi").getChildren();
                        while (eventi.iterator().hasNext()) {
                            DataSnapshot eventiSnap = eventi.iterator().next();
                            if (key.equals(eventiSnap.getKey())){
                                eventiSnap.getRef().removeValue();
                            }

                            //event.setEventKey(eventiSnap.getKey());

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
                System.out.println(error.getMessage());
            }
        });


        return false;
    }



}
