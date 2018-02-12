package controller;


import com.google.cloud.WriteChannel;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.firebase.cloud.StorageClient;
import javafx.scene.image.Image;

import java.io.*;
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
    public StorageController() {
    }

    /**
     * Metodo che si occupa dell'upload delle immagini
     * e restituisce una lista di immagini con i link ottenuti dall'upload
     *
     * @param imageList
     * @param playbill
     * @param latch
     * @return
     * @throws InterruptedException
     */
    public List<Image> upload(List<Image> imageList, Image playbill, CountDownLatch latch) throws InterruptedException {

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
                    String blobName = name[name.length - 1]; //si setta il nome del blob col nome del file (ultima parte dello slpit
                    String[] type = image.impl_getUrl().split("\\.");//si splitta nuovamente il percorso, dai punti, per ottenere il tipo del file
                    BlobId blobId = BlobId.of(bucket.getName(), blobName);//si setta l'id del blob
                    //si da il percorso del file all'input stream. Substring per eliminare "file:" dal percorso dell'Image
                    InputStream inputStream = new FileInputStream(new File(image.impl_getUrl().substring(5)).getAbsolutePath().replaceAll("%20", " "));
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
}