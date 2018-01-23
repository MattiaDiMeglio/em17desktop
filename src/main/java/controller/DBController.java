package controller;


import com.google.api.core.SettableApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.*;
import model.EventModel;

import java.io.IOException;
import java.io.InputStream;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class DBController {

    private static final String DATABASE_URL = "https://ingws-20.firebaseio.com/";
    private DatabaseReference database;
    private String ris;
    private static DBController instance = new DBController();
    EventController eventController;

    public static DBController getInstance() {
        return instance;
    }

    protected DBController() {
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
        database = FirebaseDatabase.getInstance().getReference();


    }


    public void dashBoard() throws ExecutionException, InterruptedException {
        database.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChildName) {
                eventController = EventController.getInstance();//ottengo l'instanza di event controller
                EventModel event = new EventModel();//creo un nuovo event model
                int i = 0;
                int j=0;
                try {

                    i=0;
                    List<String> locationName = new ArrayList<>();
                    Iterable<DataSnapshot> location = snapshot.getChildren();//ottengo l'iterable location, posizionato al root
                    //ottengo i nomi delle location
                    while (location.iterator().hasNext()) {
                        locationName.add(location.iterator().next().child("nome").getValue().toString());
                        System.out.println("location: " +  locationName.get(i));
                        i++;
                    }

                    i=0;
                    List<String> locationAdd = new ArrayList<>();
                    location = snapshot.getChildren();//riposiziono l'iterable al root
                    //ottengo il nome delle location
                    while (location.iterator().hasNext()) {
                        locationName.add(location.iterator().next().child("indirizzo").getValue().toString());
                        //System.out.println("location: " +  locationAdd.get(i));
                        i++;
                    }


                    i=0;
                    location = snapshot.getChildren();//iterable al root
                    while (location.iterator().hasNext()) {
                        j=0;
                        //per ogni location posiziono un'iterable sotto eventi, in modo da ottenere i dati di tutti gli eventi
                        Iterable<DataSnapshot> eventi = location.iterator().next().child("Eventi").getChildren();
                        //ottengo il nome di tutti gli eventi
                        while (eventi.iterator().hasNext()) {
                            event.setIndex(j);
                            event.setNomeLocation(locationName.get(i));
                            //event.setLocationAddress(locationAdd.get(i));
                            event.setNomeEvento(eventi.iterator().next().child("nome").getValue().toString());
                            System.out.println("nome evento" + event.getIndex() + ", in loc" + event.getNomeLocation() + i + ": " + event.getNomeEvento());
                            eventController.setListaEventi(event);
                            j++;
                        }
                        i++;
                    }

                    location = snapshot.getChildren();//iterable al root
                    while (location.iterator().hasNext()) {
                        j=0;
                        //per ogni location posiziono un'iterable sotto eventi, in modo da ottenere i dati di tutti gli eventi
                        Iterable<DataSnapshot> eventi = location.iterator().next().child("Eventi").getChildren();
                        //ottengo il valore di attività di tutti gli eventi
                        while (eventi.iterator().hasNext()) {
                            //eventController.getListaEventi().get(j).setLocationAddress(locationAdd.get(i));
                            eventController.getListaEventi().get(j).setAttivo((boolean) eventi.iterator().next().child("attivo").getValue());
                            System.out.println("attivo: " + eventController.getListaEventi().get(j).isAttivo() );
                            j++;
                        }
                        i++;
                    }
                    i=0;

                    location = snapshot.getChildren();//iterable al root
                    while (location.iterator().hasNext()) {
                        j=0;
                        //per ogni location posiziono un'iterable sotto eventi, in modo da ottenere i dati di tutti gli eventi
                        Iterable<DataSnapshot> eventi = location.iterator().next().child("Eventi").getChildren();
                        //ottengo la locandina di tutti gli eventi
                        while (eventi.iterator().hasNext()) {
                            eventController.getListaEventi().get(j).setLocandina(eventi.iterator().next().child("copertina").getValue().toString());
                            System.out.println("locandina: " + eventController.getListaEventi().get(j).getLocandina() );
                            j++;
                        }
                        i++;
                    }
                    i=0;

                    location = snapshot.getChildren();//iterable al root
                    while (location.iterator().hasNext()) {
                        j=0;
                        //per ogni location posiziono un'iterable sotto eventi, in modo da ottenere i dati di tutti gli eventi
                        Iterable<DataSnapshot> eventi = location.iterator().next().child("Eventi").getChildren();
                        //ottengo la descrizione tutti gli eventi
                        while (eventi.iterator().hasNext()) {
                            eventController.getListaEventi().get(j).setDescrizione(eventi.iterator().next().child("descrizione").getValue().toString());
                            System.out.println("Descrizione: " + eventController.getListaEventi().get(j).getDescrizione() );                            j++;
                        }
                        i++;
                    }


                } catch (NullPointerException e) {
                    e.printStackTrace();
                }

                eventController.setListaEventi(event);

            }

            @Override
            public void onChildChanged(DataSnapshot snapshot, String previousChildName) {

            }

            @Override
            public void onChildRemoved(DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot snapshot, String previousChildName) {

            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                if (!snapshot.exists()) {
                    System.out.println("no");
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

        return;
    }


}
