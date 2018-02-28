package view;

import controller.LoginController;
import controller.ViewSourceController;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import model.LoginModel;

import java.util.Observable;
import java.util.Observer;

/**
 * Classe view per la schermata recoevry password, implementa observer
 *
 * @author ingSW20
 * @see Observer
 * @see java.util.Observer
 */
public class RecoveryView implements Observer {
    /**
     * textfield per l'inserimento dell'email
     */
    private TextField recoveryEmail;
    /**
     * stringa contenente l'email
     */
    private String email;
    /**
     * instanza del controller della view
     */
    private ViewSourceController viewSourceController;

    /**
     * instanza del model
     */
    private LoginModel loginModel;

    /**
     * costruttore per la valorizzazione degli elementi della view
     *
     * @param recoveryEmail        textfield per l'inserimento dell'email
     * @param recoveryButton       bottone per c onfermare l'email
     * @param recoveryBackButton   tasto per tornare indietro
     * @param viewSourceController istanza di ViewSourceController
     */
    public RecoveryView(TextField recoveryEmail, Button recoveryButton, Button recoveryBackButton, ViewSourceController viewSourceController) {
        this.recoveryEmail = recoveryEmail;
        this.viewSourceController = viewSourceController;
        loginModel = LoginModel.getInstance();//si valorizza l'instanza del model
        loginModel.addObserver(this); //si setta la view come observer del model
        recoveryEmail.setText(loginModel.getUserName()); //si preimposta l'user nella textbox
        recoveryButton.setDefaultButton(true); //si setta il bottone di default
        recoveryBackButton.setOnAction(event -> viewSourceController.toLoginView()); //si setta il listener per il bottone recovery toInsertView
        recoveryButton.setOnAction(event -> DataControl()); //si setta il listener per il bottone recovery email
    }

    /**
     * metodo che fornisce un feedback all'utente in caso di errore
     */
    private void DataControl() {
        email = recoveryEmail.getText();
        loginModel.setUserName(email); //setta l'user nel model
        //in se almeno uno dei due campi è vuoto e si prova a loggare
        //verranno colorati di rosso
        if (email.isEmpty()) {
            recoveryEmail.setStyle("-fx-background-color: red");
        } else {
            recoveryEmail.setStyle("-fx-background-color: white; -fx-border-color: black");
            Recovery();
        }
    }

    /**
     * metodo che si occupa di far partire il metodo per il recovery del view conrroller
     */
    private void Recovery() {

        LoginController loginController = LoginController.getInstance();
        String kind = loginController.passRecovery(email);//chiama from recovery e valorizza il risultato
        if (!kind.contains("java.io")) {
            //recupero riuscito, da notifica all'utente e torna alla schermata di login
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Recupero Password");
            alert.setHeaderText(null);
            alert.setContentText("Email spedita!");
            alert.showAndWait();
            viewSourceController.toLoginView();
        } else {
            //recupero fallito, notifica l'utente
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Recupero Password");
            alert.setHeaderText(null);
            alert.setContentText("Email non corretta!");
            alert.showAndWait();
        }
    }

    /**
     * metodo per l'aggiornamento della view
     *
     * @param o   model
     * @param arg null
     */
    public void update(Observable o, Object arg) {
        recoveryEmail.setText(loginModel.getUserName());//update di username
    }
}
