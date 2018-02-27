package view;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * la classe crea un popup con un'animazione durante un caricamento
 *
 * @author ingws20
 */
public class LoadingPopupView {

    /**
     * costruttore che crea e mostra il popup
     *
     * @param latch latch per segnalare il termine dell'elaborazione e quindi la chiusura del popup
     */
    public LoadingPopupView(CountDownLatch latch) {
        Platform.runLater(() -> {
            Stage dialogStage = new Stage();
            dialogStage.initStyle(StageStyle.DECORATED);
            dialogStage.setResizable(false);
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setTitle("LOADING");
            final HBox hb = new HBox();
            hb.setSpacing(5);
            hb.setAlignment(Pos.CENTER);
            ProgressIndicator progressIndicator = new ProgressIndicator();
            hb.getChildren().addAll(progressIndicator);
            Scene scene = new Scene(hb);
            dialogStage.setScene(scene);
            dialogStage.show();
            Executor popupThread = Executors.newSingleThreadExecutor();
            popupThread.execute(new Thread(() -> {
                try {
                    latch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Platform.runLater(dialogStage::close);
            }));
        });
    }
}
