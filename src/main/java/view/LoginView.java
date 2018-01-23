package view;

import controller.LoginController;
import controller.ViewSourceController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import model.LoginModel;

import javax.swing.*;
import java.util.Observable;
import java.util.Observer;

/**
 * classe View di Login
 *
 * @see java.util.Observable
 * @see java.util.Observer
 * @see javafx.application.Application
 *
 * @author ingSW20
 */
public class LoginView implements Observer {
    @FXML private TextField usernameField; //usernameField da cui ottenere l'email
    @FXML private PasswordField passwordField;//passwordField da cui ottenere la passwor
    @FXML private Button loginButton; //bottone login
    @FXML private Button recoveryButton; //bottone recovery pass
    private String username;
    private String password;


    private ViewSourceController viewSourceController; //istanza del controller delle view

    private LoginModel loginModel; //model corrispondente


    /**
     * Costruttore della classe view
     *
     * @param usernameField
     * @param passwordField
     * @param loginButton
     * @param recoveryButton
     * @param viewSourceController
     */
    public LoginView(TextField usernameField, PasswordField passwordField, Button loginButton, Button recoveryButton, ViewSourceController viewSourceController){
        this.usernameField=usernameField;
        usernameField.setText("mat.dimeglio@gmail.com");
        this.passwordField=passwordField;
        passwordField.setText("SaltaPicchio3");
        this.loginButton=loginButton;
        this.viewSourceController = viewSourceController;
        this.recoveryButton = recoveryButton;
        loginModel = LoginModel.getInstance(); //ottiene l'instanza del model
        loginModel.addObserver(this); //si setta la view come observer del model

        //bottone settato a defalt, per essere premuto con invio
        loginButton.setDefaultButton(true);

        //action listener per il bottone login, che fa partire il metodo DataControl
        loginButton.setOnAction(event -> DataControl());
        //action listener per il bottone recoveryButton
        recoveryButton.setOnAction(event -> viewSourceController.toRecoveryView());

    }

    /**
     * metodo DataControl controlla inizialmente che i due campi non siano vuoti
     * e blocca l'avanzamento in caso contrario.
     * Nel primo caso chiama il metodo di ViewSourceController che restituierà un boolean
     * in caso sia falso c'è stato un errore di login
     *
     */
    private void DataControl(){
        username = usernameField.getText();
        password = passwordField.getText();

        //setta i dati inseriti nel model
        loginModel.setPassWord(password);
        loginModel.setUserName(username);

        //in se almeno uno dei due campi è vuoto e si prova a loggare
        //verranno colorati di rosso
        if (username.isEmpty()){
            usernameField.setStyle("-fx-background-color: red");
        } else {
            usernameField.setStyle("-fx-background-color: white; -fx-border-color: black");

        }
        if (password.isEmpty()){
            passwordField.setStyle("-fx-background-color: red");
        } else {
            passwordField.setStyle("-fx-background-color: white; ; -fx-border-color: black");
        }

        if (!username.isEmpty() && !password.isEmpty()){
            Login ();
        }
    }

    private void Login (){


        LoginController loginController = LoginController.getInstance();
        try {
            String token = loginController.auth(username, password);//ottiene il token univoco dell'account
            if (token != null) {
                if (token.contains("java.io.IOException")) {
                    System.out.println(token);
                    JOptionPane.showMessageDialog(null, "I dati inseriti non sono corretti", "login Error",
                            JOptionPane.ERROR_MESSAGE);
                    } else if (token.contains("java.net.UnknownHostException")) {
                        System.out.println(token);
                        JOptionPane.showMessageDialog(null, "Errore di Connessione", "login Error",
                                JOptionPane.ERROR_MESSAGE);
                    } else {
                        viewSourceController.toDashBoardView();
                    }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.toString(), "Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }


    }




    @Override
    public void update(Observable o, Object arg) {
        passwordField.setText(loginModel.getPassWord());
        usernameField.setText(loginModel.getUserName());
    }
}
