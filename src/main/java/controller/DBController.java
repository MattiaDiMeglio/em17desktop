package controller;


import com.google.api.client.util.ArrayMap;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.*;
import controller.chartsController.ChartsController;
import javafx.scene.image.Image;
import model.EventListModel;
import model.EventModel;
import model.LocationListModel;
import model.LocationModel;
import view.LoadingPopupView;

import java.io.IOException;
import java.io.InputStream;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.CountDownLatch;


public class DBController {

    private static final String DATABASE_URL = "https://ingws-20.firebaseio.com/";
    private DatabaseReference database;
    private static DBController instance = new DBController();
    private EventListModel eventListModel;
    private LocationListModel locationListModel;

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
        databaseListener();
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
                    locationListModel = LocationListModel.getInstance();
                    LocationModel locationModel = new LocationModel();


                    int i = 0;
                    while (location.iterator().hasNext()) {
                        DataSnapshot locationSnap = location.iterator().next();

                        Iterable<DataSnapshot> settori = locationSnap.child("settori").getChildren();
                        Integer totTickets = 0;

                        List<String> settoriName = new ArrayList<>();
                        List<String> settoriSeats = new ArrayList<>();
                        while (settori.iterator().hasNext()) {
                            DataSnapshot settoriSnap = settori.iterator().next();
                            settoriName.add(settoriSnap.getKey());
                            settoriSeats.add(settoriSnap.getValue().toString());
                            totTickets = totTickets + Integer.valueOf(settoriSnap.getValue().toString());
                        }
                        locationModel.setSectorList(settoriName);
                        locationModel.setSeatsList(settoriSeats);
                        locationModel.setLocationAddress(locationSnap.child("indirizzo").getValue().toString());
                        locationModel.setLocationName(locationSnap.child("nome").getValue().toString());
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
                            event.initializeRevenuePerMonth();
                            Iterable<DataSnapshot> eventiIteable = eventiSnap.child("biglietti").getChildren();
                            while (eventiIteable.iterator().hasNext()) {
                                DataSnapshot bigliettiSnap = eventiIteable.iterator().next();
                                Integer accesses = Integer.valueOf(bigliettiSnap.child("accessi").getValue().toString());

                                settoriMap.put(bigliettiSnap.child("settore").getValue().toString(),
                                        settoriMap.get(bigliettiSnap.child("settore").getValue().toString()) + accesses);

                                ticketSold = ticketSold + accesses;
                                Integer revenue = accesses * Integer.valueOf(bigliettiSnap.child("prezzo").getValue().toString());

                                String eventEndDate = bigliettiSnap.child("data vendita").getValue().toString();
                                Date eventEndTime = new SimpleDateFormat("dd/MM/yyyy").parse(eventEndDate);

                                event.addOneSoldPerMonth(eventEndTime.getMonth(), accesses);
                                event.addOneRevenuePerMonth(eventEndTime.getMonth(), revenue);
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
                                Image image = new Image(eventiSnap.child("copertina").getValue().toString());
                                event.setBillboard(image);
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
                        locationListModel.setListaEventi(locationModel);

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

    /**
     * @param key
     * @return
     */
    public void delete(String key) {

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
                            if (key.equals(eventiSnap.getKey())) {
                                eventiSnap.getRef().removeValue();
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.out.println(error.getMessage());
            }
        });
    }

    private void databaseListener(){
        database.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChildName) {

            }

            @Override
            public void onChildChanged(DataSnapshot snapshot, String previousChildName) {
                System.out.println("cambiato");
                // todo all'eliminazione si deve riaggiornare la dash. Se aggiorno i model la view rimane uguale
            }

            @Override
            public void onChildRemoved(DataSnapshot snapshot) {
                System.out.println("rimosso");
            }

            @Override
            public void onChildMoved(DataSnapshot snapshot, String previousChildName) {

            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }


}
