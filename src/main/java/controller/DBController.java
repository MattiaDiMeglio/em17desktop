package controller;



import com.google.api.core.SettableApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.*;
import javafx.event.EventTarget;
import model.EventModel;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;


public class DBController {

    private static final String DATABASE_URL = "https://ingws-20.firebaseio.com/";
    private DatabaseReference database;
    private String ris;

    public DBController() {
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

        try {
            System.out.println("try");
            risultatoQuery("cacca");
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("cerco  nel database...");
        //System.out.println(risultatoQuery("bob"));

    }


    private void risultatoQuery(String string) throws ExecutionException, InterruptedException {
        final SettableApiFuture<String> tcs = SettableApiFuture.create();


      //  Query query = database.child("Eventi").equalTo("EventoX1");

        database.orderByChild("Eventi").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChildName) {
                System.out.println("sonodrento");
                if(snapshot.child("Eventi").getValue() != null) {
                    EventModel event = new EventModel();
                    event.setLocandina(snapshot.child("Eventi").getChildren().iterator().next().child("copertina").getValue().toString());
                    event.setNomeEvento(snapshot.child("Eventi").getChildren().iterator().next().child("nome").getValue().toString());
                    event.setAttivo((boolean) snapshot.child("Eventi").getChildren().iterator().next().child("attivo").getValue());
                    /*event.setDataInizio((Date) snapshot.child("Eventi").getChildren().iterator().next().child("data").child("inizio").getValue());
                    event.setDataFine((Date) snapshot.child("Eventi").getChildren().iterator().next().child("data").child("fine").getValue());*/
                    event.setDescrizione(snapshot.child("Eventi").getChildren().iterator().next().child("descrizione").getValue().toString());
                    System.out.println(event.getLocandina());
                    System.out.println(snapshot.child("Eventi").getChildren().iterator().next().getValue().toString());
                    //event.setNomeEvento(snapshot.child("Eventi").child("nome").getValue().toString());
                    System.out.println("caccaculo" + event.getNomeEvento() + " " + event.isAttivo() +  " " + event.getDescrizione());
                } else {
                    System.out.println("andreachiattissimo");
                }

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

                if(!snapshot.exists()){
                    System.out.println("no");
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

        System.out.println("aspetto");

        return ;
    }


}
