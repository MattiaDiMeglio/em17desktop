package controller;


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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * Classe controller del database
 */
public class DBController {

    /**
     * Url del database
     */
    private static final String DATABASE_URL = "https://ingws-20.firebaseio.com/";
    /**
     * reference al database
     */
    private DatabaseReference database;
    /**
     * instanza del dbcontroller
     */
    private static DBController instance = new DBController();
    /**
     * instanza di eventListModel
     */
    private EventListModel eventListModel;
    /**
     * instanza di locationListModel
     */
    private LocationListModel locationListModel;
    /**
     * flag per identificare il primo update del database locale
     */
    private boolean firstDatabaseUpdate = true;

    /**
     * listener per l'aggiornamento del database
     */
    private ChildEventListener childEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot snapshot, String previousChildName) {
            updateLocalDatabase(snapshot);
        }

        @Override
        public void onChildChanged(DataSnapshot snapshot, String previousChildName) {
            updateLocalDatabase(snapshot);
        }

        @Override
        public void onChildRemoved(DataSnapshot snapshot) {
            updateLocalDatabase(snapshot);
        }

        @Override
        public void onChildMoved(DataSnapshot snapshot, String previousChildName) {
            updateLocalDatabase(snapshot);
        }

        @Override
        public void onCancelled(DatabaseError error) {
            System.out.println(error.getMessage());
        }
    };

    /**
     * metodo che restituisce l'instanza
     *
     * @return
     */
    public static DBController getInstance() {
        return instance;
    }

    /**
     * costruttore che chiama il metodo per inizializzare il db
     */
    private DBController() {
        initializeDatabase();
    }

    /**
     * metodo per l'inizializzazione del database
     */
    private void initializeDatabase() {

        System.out.println("Inizializzo database...");

        try {
            //si crea il loader che andrà a caricare il json con i dati admin
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            //si fa caricare il json con i dati admin
            InputStream inputStream = loader.getResourceAsStream("ingws-20-firebase-adminsdk.json");
            //si inizializza l'app con le opzioni di firebase
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(inputStream))
                    .setDatabaseUrl(DATABASE_URL).setStorageBucket("ingws-20.appspot.com")
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
        database = FirebaseDatabase.getInstance().getReference();
        ChartsController.getInstance().setDatabase(database.child("luogo"));//si setta il db nel controller per i charts
        databaseListener(); //inizializza i listener per il cambio del database
    }

    private void databaseListener() {
        database.addChildEventListener(childEventListener);
    }

    public void insert(EventModel newEvent) {
        database.removeEventListener(childEventListener);
        database.child("luogo").addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                try {
                    Iterable<DataSnapshot> location = snapshot.getChildren();
                    while (location.iterator().hasNext()) {
                        DataSnapshot locationSnap = location.iterator().next();
                        if ((locationSnap.child("nome").getValue().toString().equals(newEvent.getLocationName()))
                                && locationSnap.child("indirizzo").getValue().toString().equals(newEvent.getLocationAddress())) {
                            DatabaseReference insert = locationSnap.getRef().child("Eventi").push().getRef();
                            newEvent.setActive(true);
                            insert.child("attivo").setValueAsync(newEvent.isActive());
                            //insert.child("copertina").setValueAsync(newEvent.getBillboard());
                            DatabaseReference data = insert.child("data").getRef();
                            data.child("inizio").setValueAsync(newEvent.getStartingDate());
                            data.child("fine").setValueAsync(newEvent.getStartingDate());
                            insert.child("nome").setValueAsync(newEvent.getEventName());
                            insert.child("descrizione").setValueAsync(newEvent.getEventDescription());
                            insert.child("prezzo").setValueAsync(newEvent.getPrice());
                            insert.child("copertina").setValueAsync(newEvent.getBillboard().impl_getUrl());
                            DatabaseReference reduction = insert.child("riduzioni").getRef();
                            reduction.child("Anziani").setValueAsync(newEvent.getEldersReduction());
                            reduction.child("Bambini").setValueAsync(newEvent.getChildrenReduction());
                            reduction.child("Studenti").setValueAsync(newEvent.getStudentReduction());
                            DatabaseReference sectors = insert.child("settori").getRef();
                            for (int i = 0; i < newEvent.getSectorList().size(); i++) {
                                sectors.child(newEvent.getSectorList().get(i).getName());
                                String nome = newEvent.getSectorList().get(i).getName();
                                DatabaseReference inSector = sectors.child(nome).getRef();
                                inSector.child("posti").setValueAsync(newEvent.getSectorList().get(i).getSeats());
                                inSector.child("prezzo").setValueAsync(newEvent.getSectorList().get(i).getPrice());
                                inSector.child("riduzione").setValueAsync(newEvent.getSectorList().get(i).isReduction());
                            }
                            DatabaseReference gallery = insert.child("galleria").getRef();
                            for (Integer i = 0; i < newEvent.getSlideshow().size(); i++) {
                                gallery.child(i.toString()).setValueAsync(newEvent.getSlideshow().get(i).impl_getUrl());
                            }
                        }
                    }
                    databaseListener();
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

    void updateChild(EventModel eventModel, String oldLocation) {
        database.removeEventListener(childEventListener);
        DatabaseReference fromPath = database.child("luogo").child(oldLocation).child("Eventi").child(eventModel.getEventKey());
        DatabaseReference toPath = database.child("luogo").child(eventModel.getLocationID()).child("Eventi");
        fromPath.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                toPath.child(dataSnapshot.getKey())
                        .setValue(dataSnapshot.getValue(), (databaseError, databaseReference) -> {
                            if (databaseError == null) {
                                System.out.println("onComplete: success " + databaseReference.toString());
                                // In order to complete the move, we are going to erase
                                // the original copy by assigning null as its value.
                                fromPath.setValue(null, (error, ref) -> {
                                    if (error == null) {
                                        updateChild(eventModel);
                                    }else {
                                        databaseListener();
                                    }
                                });
                            } else {
                                System.out.println("onComplete: failure:" + databaseError.getMessage() + ": "
                                        + databaseError.getDetails());
                                databaseListener();
                            }
                        });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("onCancelled: " + databaseError.getMessage() + ": "
                        + databaseError.getDetails());
            }
        });

    }

    public void updateChild(EventModel eventModel) {
        database.removeEventListener(childEventListener);
        database.child("luogo").child(eventModel.getLocationID()).child("Eventi").child(eventModel.getEventKey()).
                addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        try {
                            DatabaseReference insert = snapshot.getRef();
                            eventModel.setActive(true);
                            insert.child("attivo").setValueAsync(eventModel.isActive());
                            //insert.child("copertina").setValueAsync(newEvent.getBillboard());
                            DatabaseReference data = insert.child("data").getRef();
                            data.child("inizio").setValueAsync(eventModel.getStartingDate());
                            data.child("fine").setValueAsync(eventModel.getStartingDate());
                            insert.child("nome").setValueAsync(eventModel.getEventName());
                            insert.child("descrizione").setValueAsync(eventModel.getEventDescription());
                            insert.child("prezzo").setValueAsync(eventModel.getPrice());
                            insert.child("copertina").setValueAsync(eventModel.getBillboard().impl_getUrl());
                            DatabaseReference reduction = insert.child("riduzioni").getRef();
                            reduction.child("Anziani").setValueAsync(eventModel.getEldersReduction());
                            reduction.child("Bambini").setValueAsync(eventModel.getChildrenReduction());
                            reduction.child("Studenti").setValueAsync(eventModel.getStudentReduction());
                            DatabaseReference sectors = insert.child("settori").getRef();
                            for (int i = 0; i < eventModel.getSectorList().size(); i++) {
                                sectors.child(eventModel.getSectorList().get(i).getName());
                                String nome = eventModel.getSectorList().get(i).getName();
                                DatabaseReference inSector = sectors.child(nome).getRef();
                                inSector.child("posti").setValueAsync(eventModel.getSectorList().get(i).getSeats());
                                inSector.child("prezzo").setValueAsync(eventModel.getSectorList().get(i).getPrice());
                                inSector.child("riduzione").setValueAsync(eventModel.getSectorList().get(i).isReduction());
                            }
                            DatabaseReference gallery = insert.child("galleria").getRef();
                            for (Integer i = 0; i < eventModel.getSlideshow().size(); i++) {
                                gallery.child(i.toString()).setValueAsync(eventModel.getSlideshow().get(i).impl_getUrl());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }finally {
                            databaseListener();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        System.out.println(error.getMessage());
                    }
                });

    }

    /**
     * metodo per aggiornare il database locale
     *
     * @param snapshot istantanea del database sul server
     */
    private void updateLocalDatabase(DataSnapshot snapshot) {
        //if (flag){
        CountDownLatch latch = new CountDownLatch(2);
        new Thread(() -> {
            try {
                //si ottiene un iterable con le location
                Iterable<DataSnapshot> location = snapshot.getChildren();
                eventListModel = EventListModel.getInstance(); //si ottiene l'instanza di eventListModel
                eventListModel.getListaEventi().clear();
                locationListModel = LocationListModel.getInstance(); //si ottiene l'instanza di locationlistModel
                locationListModel.getLocationList().clear();

                int i = 0;
                //finché esistono elementi nell'iterator location
                while (location.iterator().hasNext()) {
                    LocationModel locationModel = new LocationModel(); //creiamo un nuovo locationModel

                    DataSnapshot locationSnap = location.iterator().next(); //snap con gli elementi della location

                    Iterable<DataSnapshot> settori = locationSnap.child("settori").getChildren(); //snapshot con i settori
                    Integer totTickets = 0;

                    //si settano nome e indirizzo della location
                    locationModel.setLocationAddress(locationSnap.child("indirizzo").getValue().toString());
                    locationModel.setLocationName(locationSnap.child("nome").getValue().toString());
                    locationModel.setLocationID(locationSnap.getKey());

                    //si creano le liste di nomi e posti per settore
                    List<String> settoriName = new ArrayList<>();
                    List<String> settoriSeats = new ArrayList<>();
                    //si valorizzano le liste appena create
                    while (settori.iterator().hasNext()) {
                        DataSnapshot settoriSnap = settori.iterator().next();
                        settoriName.add(settoriSnap.getKey());
                        settoriSeats.add(settoriSnap.getValue().toString());
                        totTickets = totTickets + Integer.valueOf(settoriSnap.getValue().toString());
                    }
                    //si settano le liste nel locationModel creato in precedenza
                    locationModel.setSectorList(settoriName);
                    locationModel.setSeatsList(settoriSeats);
                    //iterable con gli eventi per la location
                    Iterable<DataSnapshot> eventi = locationSnap.child("Eventi").getChildren();
                    //finché esistono altri eventi nella location
                    while (eventi.iterator().hasNext()) {
                        DataSnapshot eventiSnap = eventi.iterator().next(); //snap con i datii dell'evento

                        //si crea un'hashmap con i settori
                        HashMap<String, Integer> settoriMap = new HashMap<>();
                        for (String aSettoriName : settoriName) {
                            settoriMap.put(aSettoriName, 0);
                        }

                        EventModel event = new EventModel();//creo un nuovo event model
                        //se c'è il prezzo lo si inserisce nell'eventModel
                        if (eventiSnap.hasChild("prezzo")) {
                            event.setPrice(Double.valueOf(eventiSnap.child("prezzo").getValue().toString()));
                        } else {
                            event.setPrice(0.0); //altrimenti è settato a 0
                        }

                        Integer ticketSold = 0;
                        //si inizializzano ticketPerMonth e revenuePerMonth
                        event.initializeTicketPerMonth();
                        event.initializeRevenuePerMonth();

                        //iterable per i biglietti dell'evento
                        Iterable<DataSnapshot> eventiIterable = eventiSnap.child("biglietti").getChildren();

                        //si valorizzano i dati relativi ai biglietti venduti
                        while (eventiIterable.iterator().hasNext()) {
                            DataSnapshot bigliettiSnap = eventiIterable.iterator().next();
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
                        //si settano i dati dell'evento
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
                        event.setLocationID(locationSnap.getKey());

                        //nuovo latch per il thread di download della locandina
                        CountDownLatch latch1 = new CountDownLatch(1);
                        new Thread(() -> {
                            try {
                                Image image = new Image(eventiSnap.child("copertina").getValue().toString());
                                event.setBillboard(image);
                                latch1.countDown();
                            } catch (Exception e) {
                                e.printStackTrace();
                                latch1.countDown();
                            }

                        }).start();

                        //snap per data e ora
                        DataSnapshot dataSnapshot = eventiSnap.child("data");
                        DataSnapshot timeSnapshot = eventiSnap.child("ora");

                        //si valorizza la data d'inizio nell'evento
                        String eventStartDate = dataSnapshot.child("inizio").getValue().toString();
                        try {
                            Date eventStartTime = new SimpleDateFormat("dd/MM/yyyy").parse(eventStartDate);
                            LocalDate localDate = eventStartTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                            String date = String.format("%02d", localDate.getDayOfMonth()) + "/" + String.format("%02d", localDate.getMonthValue()) + "/" + localDate.getYear();
                            event.setStartingDate(date);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        //si valorizza la data di fine dell'evento
                        String eventEndDate = dataSnapshot.child("fine").getValue().toString();
                        try {
                            Date eventEndTime = new SimpleDateFormat("dd/MM/yyyy").parse(eventEndDate);
                            LocalDate localDate = eventEndTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                            String date = String.format("%02d", localDate.getDayOfMonth()) + "/" + String.format("%02d", localDate.getMonthValue()) + "/" + localDate.getYear();

                            event.setEndingDate(date);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        //si ottengono le immagini della galleria e le si inserisce nell'evento
                        try {
                            List<Image> slidehow = new ArrayList<>();
                            DataSnapshot slideSnap = eventiSnap.child("galleria");
                            Integer j = 0;

                            //l'inserimento viene fatto tramite thread
                            CountDownLatch latchSlideShow = new CountDownLatch(Math.toIntExact(slideSnap.getChildrenCount()));
                            try {
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
                                int count = Math.toIntExact(latchSlideShow.getCount());
                                for (i = 0; i < count; i++) {
                                    latchSlideShow.countDown();
                                }
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        latch1.await();
                        eventListModel.setListaEventi(event);
                        i++;
                    }
                    locationListModel.setListaLocation(locationModel);

                }
            } catch (Exception e) {
                e.printStackTrace();
                latch.countDown();
            }
            latch.countDown();
            ChartsController.getInstance().updateChart("2018", latch, snapshot);
        }).start();

        try {
            if (firstDatabaseUpdate) {
                new LoadingPopupView(latch);
                firstDatabaseUpdate = false;
            } else {
                try {
                    System.out.println("aspetto");
                    latch.await();
                    System.out.println("finito di aspettare");
                    ViewSourceController.showNotificationPane();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
       /* }
        if(firstDatabaseUpdate || executorService.isShutdown()) {
            System.out.println("sono nell'if");
            executorService.execute(new Thread(() -> {
                try {
                    Thread.sleep(10000);
                    flag = true;
                    firstDatabaseUpdate = false;
                    System.out.println("imposto true");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }));
        }*/
    }

    /**
     * @param key
     * @return
     */
    public void delete(String key) {

        database.child("luogo").addListenerForSingleValueEvent(new ValueEventListener() {

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

                                StorageController storageController = new StorageController();

                                storageController.deleteFolder(eventiSnap);
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


}
