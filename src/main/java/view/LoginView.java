package view;

import controller.LoginController;
import controller.ViewSourceController;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import model.LoginModel;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Observable;
import java.util.Observer;

/**
 * classe View di Login
 *
 * @author ingSW20
 * @see java.util.Observable
 * @see java.util.Observer
 * @see javafx.application.Application
 */
public class LoginView implements Observer {

  /**
   * usernameField da cui ottenere l'email
   */
  private TextField usernameField;
  /**
   * passwordField da cui ottenere la password
   */
  private PasswordField passwordField;
  /**
   * stringa utilizzata come username per l'accesso
   */
  private String username;
  /**
   * stringa utilizzata come password per l'accesso
   */
  private String password;
  /**
   * istanza del controller delle view
   */
  private ViewSourceController viewSourceController;
  /**
   * model corrispondente
   */
  private LoginModel loginModel;


  /**
   * Costruttore della classe view
   *
   * @param usernameField textfield per l'inserimento dello username
   * @param passwordField textfield per l'inserimento della password
   * @param loginButton bottone per il login
   * @param recoveryButton bottone per il recupero password
   * @param viewSourceController istanza del controller delle view
   */
  public LoginView(TextField usernameField, PasswordField passwordField, Button loginButton,
      Button recoveryButton, ViewSourceController viewSourceController) {
    this.usernameField = usernameField;
    usernameField.setText("mat.dimeglio@gmail.com");
    this.passwordField = passwordField;
    passwordField.setText("SaltaPicchio3");
    this.viewSourceController = viewSourceController;
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
   * metodo DataControl controlla inizialmente che i due campi non siano vuoti e blocca
   * l'avanzamento in caso contrario. Nel primo caso chiama il metodo di ViewSourceController che
   * restituierà un boolean in caso sia falso c'è stato un errore di login
   */
  private void DataControl() {
    username = usernameField.getText();
    password = passwordField.getText();

    //setta i dati inseriti nel model
    loginModel.setPassWord(password);
    loginModel.setUserName(username);

    //in se almeno uno dei due campi è vuoto e si prova a loggare
    //verranno colorati di rosso
    if (username.isEmpty()) {
      usernameField.setStyle("-fx-background-color: red");
    } else {
      usernameField.setStyle("-fx-background-color: white; -fx-border-color: black");

    }
    if (password.isEmpty()) {
      passwordField.setStyle("-fx-background-color: red");
    } else {
      passwordField.setStyle("-fx-background-color: white; ; -fx-border-color: black");
    }

    if (!username.isEmpty() && !password.isEmpty()) {
      Login();
    }
  }

  /**
   * metodo per l'avvio del login
   */
  private void Login() {
    LoginController loginController = LoginController.getInstance();
    try {
      String token = loginController
          .auth(username, password);//ottiene il token univoco dell'account
      if (token != null) {
        if (token.contains("java.io.IOException")) {
          Alert alert = new Alert(Alert.AlertType.ERROR);
          alert.setTitle("Errore");
          alert.setHeaderText(null);
          alert.setContentText("I dati inseriti non sono corretti");
          alert.showAndWait();
        } else if (token.contains("java.net.UnknownHostException")) {
          Alert alert = new Alert(Alert.AlertType.ERROR);
          alert.setTitle("Errore");
          alert.setHeaderText(null);
          alert.setContentText("Errore di Connessione");
          alert.showAndWait();
        } else {
          viewSourceController.toDashBoardView();
        }
      }
    } catch (Exception e) {

      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle("Errore");
      alert.setHeaderText("Errore imprevisto!");
      alert.setContentText("Si prega di contattare l'amministratore");

      // crea un pannello espandibile per l'eccezione.
      StringWriter sw = new StringWriter();
      PrintWriter pw = new PrintWriter(sw);
      e.printStackTrace(pw);
      String exceptionText = sw.toString();

      Label label = new Label("Errore:");

      TextArea textArea = new TextArea(exceptionText);
      textArea.setEditable(false);
      textArea.setWrapText(true);

      textArea.setMaxWidth(Double.MAX_VALUE);
      textArea.setMaxHeight(Double.MAX_VALUE);
      GridPane.setVgrow(textArea, Priority.ALWAYS);
      GridPane.setHgrow(textArea, Priority.ALWAYS);

      GridPane expContent = new GridPane();
      expContent.setMaxWidth(Double.MAX_VALUE);
      expContent.add(label, 0, 0);
      expContent.add(textArea, 0, 1);
      alert.getDialogPane().setExpandableContent(expContent);

      alert.showAndWait();
      e.printStackTrace();
    }
  }

  /**
   * metodo per riempire i campi username e password
   *
   * @param o model chiamante
   * @param arg null
   */
  @Override
  public void update(Observable o, Object arg) {
    passwordField.setText(loginModel.getPassWord());
    usernameField.setText(loginModel.getUserName());
  }
}
