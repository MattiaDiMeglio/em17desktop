package view;

import controller.DBController;
import controller.EventController;
import controller.ViewSourceController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import model.EventModel;

import java.io.IOException;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ExecutionException;

/**
 * Classe view per la schermata DashBoard, nonché main dell'applicativo.
 * Implementa Observer, come definito dall'architettura MVC implementata per il progetto
 * Estende Application per poter utilizzare JavaFX
 *
 *@see java.util.Observable
 * @see java.util.Observer
 * @see javafx.application.Application
 *
 * @author ingSW20
 */
public class DashBoardView extends Application implements Observer {

    /**
     * Attributo stage, che rappresenta lo stage principale dell'applicazione
     * @see javafx.stage.Stage
     *
     */
    private Stage primaryStage;

    /**
     * Main dell'applicazione, richiama il metono launch che fa partire la schermata di javafx
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }


    /**
     * Metodo start ereditato da Application. Seleziona lo stage da far partire,
     * carica l'FXML tramite FXMLLoader. Carica il root e se questo non è vuoto,
     * setta la nuova scena.
     * Selezionata la scena, setta Resizable a falso, in modo che la dimensione schermata non possa essere modificata,
     * mostra e centra la schermata. Ne setta il titolo e la imposta a pieno schermo
     *
     * @see javafx.scene.Parent
     * @see javafx.scene.Scene
     * @see javafx.fxml.FXMLLoader
     * @param primaryStage stage primario dell'applicativo
     */
    @Override
    public void start(Stage primaryStage) {

        this.primaryStage = primaryStage;
        //carica il file fxml da cui prendere le info sulla grafica. Il file ViewSource.fxml contiene tutte le scermate
        //in sequenza
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ViewSource.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //root del fxml, che si basa su una struttura ad albero
        if (root != null) {
            this.primaryStage.setScene(new Scene(root));
        }

        //schermata non ridimensionabile
        this.primaryStage.setResizable(true);
        this.primaryStage.setMinHeight(800);
        this.primaryStage.setMinWidth(800);
        this.primaryStage.show();
        this.primaryStage.centerOnScreen();
        this.primaryStage.setTitle("Em-17");
        this.primaryStage.setOnCloseRequest(event -> {
            ViewSourceController viewSourceController = new ViewSourceController();
            viewSourceController.shutdown();
            Platform.exit();

        });




    }

    /**
     * metodo update ereditato da Observer
     * @see java.util.Observer
     * @see java.util.Observable
     *
     * @param o
     * @param arg
     *
     */
    public void update(Observable o, Object arg) {

    }

    public static void fromView(Button bottone, ImageView image1) throws ExecutionException, InterruptedException {
        DBController dbController = DBController.getInstance();
        dbController.dashBoard();
        EventController eventController = EventController.getInstance();
        List<EventModel> lista = eventController.getListaEventi();
       /* System.out.println(lista.get(0).getLocandina().toString());
        Image image = new Image(lista.get(0).getLocandina().toString());
        image1.setImage(image);

        bottone.setDisable(true);*/
    }
}
