package controller;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import model.EventModel;
import model.LoginModel;
import view.DashBoardView;
import view.LoginView;
import view.RecoveryView;
import view.charts.PieChartClass;

import java.util.concurrent.ExecutionException;


/**
 * Controller che gestisce l'FXML per le view
 *
 * @author ingSW20
 */
public class ViewSourceController {
    DashBoardView dashBoardView;

    /**
     * Vbox prinicipale, contenente tutte le schemate
     */
    @FXML
    private PieChart dashBoardGraph2PieChart;
    @FXML
    private AnchorPane mainAnchorPane;
    @FXML
    private Button dashBoardImage1Button;
    @FXML private ImageView dasboardImage1;
    @FXML
    private Button dashBoardImage2Button;
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
        dashBoardImage1Button.setOnAction(event -> changeView(eventListBox));
        dashBoardImage2Button.setOnAction(event -> changeView(eventListBox));
        //listener per il bottone sulla prima immagine nei risultati di ricerca
        eventListResult1Button.setOnAction(event -> changeView(eventBox));
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
        DashBoardView.fromView(dashBoardImage1Button, dasboardImage1);
        changeView(dashBoardBox);
        System.out.println("cambioToDashBoard");

    }


    public void shutdown() {

        LoginController.getInstance().shutdown();
    }

}
