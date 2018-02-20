package controller;


import com.google.cloud.WriteChannel;
import com.google.cloud.storage.*;
import com.google.firebase.cloud.StorageClient;
import com.google.firebase.database.DataSnapshot;
import javafx.scene.image.Image;
import model.EventModel;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Controller che si occupa dell'upload delle immagini nello storage
 */
public class StorageController {

    //crea il thread in cui verrà eseguito l'upload
    private ExecutorService uploadThread = Executors.newSingleThreadExecutor();

    //creazione del bucket per l'upload
    private Bucket bucket;


    /**
     * Costruttore vuoto
     */
    StorageController() {
    }

    /**
     * Metodo che si occupa dell'upload delle immagini
     * e restituisce una lista di immagini con i link ottenuti dall'upload
     *
     *
     * @param newEvent
     * @param imageList
     * @param playbill
     * @param latch
     * @return
     * @throws InterruptedException
     */
    public List<Image> upload(EventModel newEvent, List<Image> imageList, Image playbill, CountDownLatch latch) throws InterruptedException {

        List<Image> imagesUploaded = new ArrayList<>(); //lista di immagini caricate
        CountDownLatch latch1 = new CountDownLatch(1);
        imageList.add(0, playbill); //si inscerisce la copertina alla prima posizione della lista
        //inizio thread
        uploadThread.execute(new Thread(() -> {
            Thread.currentThread().setName("uploadThread");
            bucket = StorageClient.getInstance().bucket();//si ottiene il bucket dallo storage
            try {
                for (Image image : imageList) {
                    //per ogni immagine nella lista
                    String[] name = image.impl_getUrl().split("/"); //si spliutta il percorso sul pc, per ottenere il nome del file
                    String blobName = newEvent.getEventName() + "/" + name[name.length - 1]; //si setta il nome del blob col nome del file (ultima parte dello slpit
                    String[] type = image.impl_getUrl().split("\\.");//si splitta nuovamente il percorso, dai punti, per ottenere il tipo del file
                    BlobId blobId = BlobId.of(bucket.getName(), blobName);//si setta l'id del blob

                    //si da il percorso del file all'input stream. Substring per eliminare "file:" dal percorso dell'Image
                    //InputStream inputStream = new FileInputStream(new File(image.impl_getUrl().substring(5)).getAbsolutePath().replaceAll("%20", " "));
                    InputStream inputStream = null;
                    try {
                        inputStream = new FileInputStream(new File(image.impl_getUrl().substring(5)).getAbsolutePath().replaceAll("%20", " "));
                    }catch (Exception e){
                        //e.printStackTrace();
                        inputStream = new URL(image.impl_getUrl()).openStream();
                    }

                    //si costruiscono le info del blob col nome del bucket, del
                    BlobInfo blobInfo = BlobInfo.newBuilder(bucket.getName(), blobName).setContentType("image/" + type[type.length - 1]).build();

                    Storage storage = bucket.getStorage(); //ottengo lo storage

                    //si crea un weiter con un bufferbyte, con un byte da 1024
                    try (WriteChannel writer = storage.writer(blobInfo)) {
                        byte[] buffer = new byte[1024];
                        int limit;

                        try {
                            //si fa il write
                            while ((limit = inputStream.read(buffer)) >= 0) {
                                writer.write(ByteBuffer.wrap(buffer, 0, limit));
                            }

                        } catch (Exception ex) {
                            ex.printStackTrace();
                        } finally {
                            //si chiude il writer
                            writer.close();
                            //si aggiunge il link dell'immagine caricata
                            imagesUploaded.add(new Image("https://storage.googleapis.com/ingws-20.appspot.com/" + blobName));
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                //si chiudono i latch
                latch.countDown();
                latch1.countDown();
            }

        }, "uploadThread"));
        latch1.await();
        return imagesUploaded;
    }

    //Eliminazione della cartella contenente tutte le immagini dell'evento
    public void deleteFolder(DataSnapshot eventiSnap){

        StorageController storageController = new StorageController();

        //Parte di stringa da eliminare dall'indirizzo
        String link = "https://storage.googleapis.com/ingws-20.appspot.com/";

        //Eliminazione dell'immagine di copertina
        storageController.deleteFile(
                eventiSnap.child("copertina").getValue().toString()
                        .replace(link, ""));

        //Eliminazione delle immagini nella galleria
        for (Integer i = 0; i < eventiSnap.child("galleria").getChildrenCount() ; i++){
            storageController.deleteFile(
                    eventiSnap.child("galleria").child(i.toString()).getValue().toString()
                            .replace(link, ""));
            }

        //Eliminazione della cartella ormai vuota
        try {
            bucket = StorageClient.getInstance().bucket();

            BlobId blobId = BlobId.of(bucket.getName(), eventiSnap.getKey() + "/");

            Blob blob = bucket.get(blobId.getName());

            blob.delete();


            System.out.println("fatto");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //Eliminazione del file specifico
    private void deleteFile(String key){

        bucket.get(key).delete();
    }
}