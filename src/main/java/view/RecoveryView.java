package view;

import controller.LoginController;
import controller.ViewSourceController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import model.LoginModel;

import javax.swing.*;
import java.util.Observable;
import java.util.Observer;

/**
 * Classe view per la schermata recoevry password, implementa observer
 *
 * @author ingSW20
 * @see Observer
 * @see java.util.Observer
 * @see javafx.application.Application
 */
public class RecoveryView implements Observer {
    @FXML
    private TextField recoveryEmail;
    @FXML
    private Button recoveryButton;
    private String email;
    private ViewSourceController viewSourceController; //instanza del controller della view

    private LoginModel loginModel; //instanza del model

    public RecoveryView(TextField recoveryEmail, Button recoveryButton, Button recoveryBackButton, ViewSourceController viewSourceController) {
        this.recoveryButton = recoveryButton;
        this.recoveryEmail = recoveryEmail;
        this.viewSourceController = viewSourceController;

        loginModel = LoginModel.getInstance();//si valorizza l'instanza del model

        loginModel.addObserver(this); //si setta la view come observer del model

        recoveryEmail.setText(loginModel.getUserName()); //si preimposta l'user nella textbox

        recoveryButton.setDefaultButton(true); //si setta il bottone di default

        recoveryBackButton.setOnAction(event -> viewSourceController.toLoginView()); //si setta il listener per il bottone recovery toInsertView

        recoveryButton.setOnAction(event -> DataControl()); //si setta il listener per il bottone recovery email


    }

    private void DataControl() {
        email = recoveryEmail.getText();
        loginModel.setUserName(email); //setta l'user nel model
        //in se almeno uno dei due campi è vuoto e si prova a loggare
        //verranno colorati di rosso
        if (email.isEmpty()) {
            recoveryEmail.setStyle("-fx-background-color: red");
        } else {
            recoveryEmail.setStyle("-fx-background-color: white; -fx-border-color: black");

        }

        if (!email.isEmpty()) {
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
            JOptionPane.showMessageDialog(null, "Email spedita!", "Recupero Password", JOptionPane.INFORMATION_MESSAGE);
            viewSourceController.toLoginView();
        } else {
            //recupero fallito, notifica l'utente
            JOptionPane.showMessageDialog(null, "Email non corretta!", "Recupero Password", JOptionPane.ERROR_MESSAGE);

        }


    }

    public void update(Observable o, Object arg) {
        recoveryEmail.setText(loginModel.getUserName());//update di username
    }
}
