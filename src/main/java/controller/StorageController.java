package controller;


import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.Tuple;
import com.google.cloud.WriteChannel;
import com.google.cloud.storage.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseCredentials;
import com.google.firebase.cloud.StorageClient;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import sun.nio.cs.UTF_32;
import view.LoadingPopupView;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class StorageController {

    private ExecutorService uploadThread = Executors.newSingleThreadExecutor();

    private Bucket bucket;


    public StorageController() throws IOException {

    }

    public List<Image> upload (List<Image> imageList, CountDownLatch latch) throws FileNotFoundException, InterruptedException {
        List<Image> imagesUploaded = new ArrayList<>();
        CountDownLatch latch1 = new CountDownLatch(1);
        uploadThread.execute(new Thread(() -> {
            Thread.currentThread().setName("loginThread");
            bucket = StorageClient.getInstance().bucket();
            try {
                for (Image image:imageList) {
                    String blobName = image.impl_getUrl().substring(5);
                    BlobId blobId = BlobId.of(bucket.getName(), blobName);
                    InputStream inputStream;
                    inputStream = new FileInputStream(image.impl_getUrl().substring(5));
                    BlobInfo blobInfo = BlobInfo.newBuilder(bucket.getName(), image.impl_getUrl().substring(5)).setContentType("image/").build();

                    Storage storage = bucket.getStorage();

                    try (WriteChannel writer = storage.writer(blobInfo)) {
                        byte[] buffer = new byte[1024];
                        int limit;
                        try {
                            while ((limit = inputStream.read(buffer)) >= 0) {
                                writer.write(ByteBuffer.wrap(buffer, 0, limit));
                            }

                        } catch (Exception ex) {
                            // handle exception
                        } finally {
                            writer.close();
                            System.out.println(storage.get(blobId).getMediaLink());
                            imagesUploaded.add(new Image(storage.get(blobId).getMediaLink()));
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                latch.countDown();
                latch1.countDown();
                System.out.println("Finelly");
            }

        }, "loginThread"));
        latch1.await();
        return imagesUploaded;
    }
}