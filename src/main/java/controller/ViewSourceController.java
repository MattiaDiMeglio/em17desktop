package controller;

import com.google.api.services.storage.Storage;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.EventListModel;
import model.EventModel;
import view.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;


/**
 * Controller che gestisce l'FXML per le view
 *
 * @author ingSW20
 */
public class ViewSourceController extends Application {

    /**
     * Attributo stage, che rappresenta lo stage principale dell'applicazione
     *
     * @see javafx.stage.Stage
     */
    private Stage primaryStage;

    /**
     * Vbox prinicipale, contenente tutte le schemate
     */
    @FXML
    private AnchorPane mainAnchorPane;
    @FXML
    private Button eventListResult1Button;
    @FXML
    private Button eventoBackButton;
    @FXML
    private Button loginButton;
    @FXML
    private TextField userName;
    @FXML
    private PasswordField password;
    @FXML
    private TextField recoveryEmail;
    @FXML
    private Button recoveryButton;
    @FXML
    private Button recoveryLabelButton;
    @FXML
    private Button recoveryBackButton;
    @FXML
    private HBox dashSlide;
    @FXML
    private TabPane eventoTabPane;
    @FXML
    private TabPane dashBoardTabPane;
    @FXML
    private Label eventoTitleLabel;
    @FXML
    private Text eventLocationText;
    @FXML
    private Text eventoDataInizioText;
    @FXML
    private Text eventoDataFineText;
    @FXML
    private TextArea eventTextArea;
    @FXML
    private Text eventoPrezzoText;
    @FXML
    private Text eventoMaxVisitatoriText;
    @FXML
    private Text eventoBigliettiVendutiText;
    @FXML
    private ImageView eventPlaybillImageView;
    @FXML
    private Button dashBoardSlideShowLeftButton;
    @FXML
    private Button dashBoardSlideShowRightButton;
    @FXML
    private Button eventoDeleteButton;
    @FXML
    private HBox eventSlide;
    @FXML
    private Button eventSlideShowLeftButton;
    @FXML
    private Button eventSlideShowRightButton;
    @FXML
    private Button dashBoardInsertButton;
    @FXML
    private TabPane eventListTabPane;
    @FXML
    private TextField insertNameLabel;
    @FXML
    private TextField insertLocationLabel;
    @FXML
    private TextArea insertTextArea;
    @FXML
    private HBox insertSlideshow;
    @FXML
    private Button insertCancelButton;
    @FXML
    private Button insertConfirmButton;
    @FXML
    private DatePicker insertInizioDataPicker;
    @FXML
    private DatePicker insertFineDataPicker;
    @FXML
    private TextField insertMaxGuestsLabel;
    @FXML
    private ToolBar searchToolBar;
    @FXML
    private VBox eventListViewVBox;


    /**
     * Schermata Dasboard
     */
    private Node dashBoardBox;
    /**
     * Schermata EventList
     */
    private Node eventListBox;
    /**
     * Schermata Event
     */
    private Node eventBox;

    /**
     * Schermata Login
     */
    private Node loginBox;

    /**
     * Schermata password recovery
     */
    private Node recoveryBox;

    /**
     * Schermata insert
     */
    private Node insertBox;

    private Node insertTicketBox;

    /**
     * Main dell'applicazione, richiama il metono launch che fa partire la schermata di javafx
     *
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
     * @param primaryStage stage primario dell'applicativo
     * @see javafx.scene.Parent
     * @see javafx.scene.Scene
     * @see javafx.fxml.FXMLLoader
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
        this.primaryStage.setMaximized(true);
        this.primaryStage.setOnCloseRequest(event -> {
            ViewSourceController viewSourceController = new ViewSourceController();
            viewSourceController.shutdown();
            Platform.exit();

        });
    }

    /**
     * Metodo per l'inizializzazione del loader di FXML
     */
    @FXML
    private void initialize() {
        //ottenimento dei box contenenti le varie schermate
        dashBoardBox = mainAnchorPane.getChildren().get(0);
        eventListBox = mainAnchorPane.getChildren().get(1);
        eventBox = mainAnchorPane.getChildren().get(2);
        loginBox = mainAnchorPane.getChildren().get(3);
        recoveryBox = mainAnchorPane.getChildren().get(4);
        insertBox = mainAnchorPane.getChildren().get(5);
        insertTicketBox = mainAnchorPane.getChildren().get(6);


        //metodo per passare alla loginview
        toLoginView();

        //listener per il bottone sulla prima immagine nella dashboard
        //dashBoardImage1Button.setOnAction(event -> changeView(eventListBox));
        //dashBoardImage2Button.setOnAction(event -> changeView(eventListBox));
        //listener per il bottone sulla prima immagine nei risultati di ricerca
        eventListResult1Button.setOnAction(event -> changeView(eventBox));
        //eventListResult1Button.setOnAction(event -> changeView(eventBox));
        //listener per il bottone "torna alla dashboard" della schermata evento
        eventoBackButton.setOnAction(event -> changeView(dashBoardBox));
    }


    /**
     * Metodo che si occupa di cambiare le schermate
     * prende in input il node da usare come nuova schermata
     *
     * @param view
     */
    private void changeView(Node view) {
        mainAnchorPane.getChildren().removeAll(dashBoardBox, eventBox, eventListBox, loginBox, recoveryBox, insertBox, insertTicketBox);
        mainAnchorPane.getChildren().add(view);
    }

    /**
     * metodo che si occupa di creare la loginView e cambiare la schermata a loginbox
     */
    public void toLoginView() {
        new LoginView(userName, password, loginButton, recoveryLabelButton, this);
        changeView(loginBox);


    }

    /**
     * metodo che si occupa di creare la RecoveryView
     */
    public void toRecoveryView() {
        new RecoveryView(recoveryEmail, recoveryButton, recoveryBackButton, this);
        changeView(recoveryBox);
        primaryStage.setTitle("Em-17 - Password Recovery");

    }


    /**
     * metodo che si occupa di creare la dashboardview e cambiare la schermata
     */
    public void toDashBoardView() throws ExecutionException, InterruptedException {
                dashBoardTabPane, dashBoardInsertButton, this, searchToolBar);
        changeView(dashBoardBox);
        DBController dbController = DBController.getInstance();
        dbController.dashBoard();

    }

    public void toEventView(int index) {
        List<Text> texts = new ArrayList<>();
        texts.add(eventLocationText);
        texts.add(eventoDataInizioText);
        texts.add(eventoDataFineText);
        texts.add(eventoPrezzoText);
        texts.add(eventoMaxVisitatoriText);
        texts.add(eventoBigliettiVendutiText);
        EventController eventController = new EventController();


        new EventView(eventController, eventoDeleteButton, eventPlaybillImageView,
                eventoTabPane, index, texts, eventoTitleLabel, eventTextArea, eventSlide, eventSlideShowLeftButton, eventSlideShowRightButton, this);
        changeView(eventBox);

    }

    public void toInsertView() {

        InsertController insertController = new InsertController(this);
        new InsertView(insertController, insertCancelButton, insertConfirmButton, insertTextArea,
                insertLocationLabel, insertNameLabel, insertSlideshow, insertInizioDataPicker, insertFineDataPicker, insertMaxGuestsLabel);

        changeView(insertBox);

    }

    public void toInsetTicketView(){
        changeView(insertTicketBox);
    }

    public void shutdown() {

        LoginController.getInstance().shutdown();
    }

    public void toEventListView(List<EventModel> foundedEventInSearch) {
        new EventListView(eventListTabPane, foundedEventInSearch, eventListViewVBox);
        changeView(eventListBox);
    }
}
