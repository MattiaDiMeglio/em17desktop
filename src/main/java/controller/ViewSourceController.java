package controller;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import model.EventListModel;
import model.EventModel;
import model.LoginModel;
import view.DashBoardView;
import view.LoginView;
import view.RecoveryView;
import view.chartsViews.LineChartClass;
import view.chartsViews.PieChartClass;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.concurrent.ExecutionException;


/**
 * Controller che gestisce l'FXML per le view
 *
 * @author ingSW20
 */
public class ViewSourceController extends Application {

    /**
     * Attributo stage, che rappresenta lo stage principale dell'applicazione
     * @see javafx.stage.Stage
     *
     */
    private Stage primaryStage;

    /**
     * Vbox prinicipale, contenente tutte le schemate
     */
    @FXML
    private PieChart dashBoardGraph2PieChart;
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
    private ComboBox dashboardYearComboBox1;
    @FXML
    private ComboBox dashBoardYearComboBox2;
    @FXML
    private LineChart dashBoardGraph1LineChart;


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


        //metodo per passare alla loginview
        toLoginView();

        //listener per il bottone sulla prima immagine nella dashboard
        //dashBoardImage1Button.setOnAction(event -> changeView(eventListBox));
        //dashBoardImage2Button.setOnAction(event -> changeView(eventListBox));
        //listener per il bottone sulla prima immagine nei risultati di ricerca
        eventListResult1Button.setOnAction(event -> changeView(eventBox));
        //listener per il bottone "torna alla dashboard" della schermata evento
        eventoBackButton.setOnAction(event -> changeView(dashBoardBox));

        Date date = new Date();
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        int year = localDate.getYear();

        int year1 = year - 4;
        int year2 = year - 3;
        int year3 = year - 2;
        int year4 = year - 1;

        System.out.println("Stampo anno: " + year);
        dashboardYearComboBox1.getItems().setAll(year, year4, year3, year2, year1);
        dashboardYearComboBox1.getSelectionModel().selectFirst();

        dashBoardYearComboBox2.getItems().setAll(year, year4, year3, year2, year1);
        dashBoardYearComboBox2.getSelectionModel().selectFirst();



    }



    /**
     * Metodo che si occupa di cambiare le schermate
     * prende in input il node da usare come nuova schermata
     *
     * @param view
     */
    private void changeView(Node view) {
        mainAnchorPane.getChildren().removeAll(dashBoardBox, eventBox, eventListBox, loginBox, recoveryBox, insertBox);
        mainAnchorPane.getChildren().add(view);
    }

    /**
     * metodo che si occupa di creare la loginView e cambiare la schermata a loginbox
     */
    public void toLoginView() {
        LoginView loginView = new LoginView(userName, password, loginButton, recoveryLabelButton, this);
        changeView(loginBox);

    }

    /**
     * metodo che si occupa di creare la RecoveryView
     */
    public void toRecoveryView() {
        RecoveryView recoveryView = new RecoveryView(recoveryEmail, recoveryButton, recoveryBackButton, this);
        changeView(recoveryBox);
        System.out.println("a recovery");
    }


    /**
     * metodo che si occupa di creare la dashboardview e cambiare la schermata
     */
    public void toDashBoardView() throws ExecutionException, InterruptedException {
        new DashBoardView( dashBoardGraph2PieChart, dashBoardYearComboBox2, dashBoardGraph1LineChart, dashboardYearComboBox1, this);
        changeView(dashBoardBox);
        System.out.println("cambioToDashBoard");
        DBController dbController = DBController.getInstance();
        dbController.dashBoard();
    }

    @FXML  public void createSlide (int i, Button button){

        System.out.println("sdfnpoasnipnipniaf");
        HBox hBox = new HBox();
        dashSlide.setAlignment(Pos.CENTER);
        dashSlide.getChildren().add(button);

    }


    public void shutdown() {

        LoginController.getInstance().shutdown();
    }

}
