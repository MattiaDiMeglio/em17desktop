package controller;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * classe controller per il login.
 *
 * @author ingSW20
 */
public class LoginController {

  /**
   * URL base per la richiesta del webservice.
   */
  private static final String BASE_URL = "https://www.googleapis.com/identitytoolkit/v3/relyingparty/";
  /**
   * Parte dell'url con l'operazione base per l'autenticazione.
   */
  private static final String OPERATION_AUTH = "verifyPassword";
  /**
   * parte dell'url con l'operazione di recovery password.
   */
  private static final String OPERATION_PASSWORD_RECOVERY = "getOobConfirmationCode";

  /**
   * thread per il login.
   */
  private ExecutorService loginThread = Executors.newSingleThreadExecutor();

  /**
   * thread per il password recovery.
   */
  private ExecutorService recoveryThread = Executors.newSingleThreadExecutor();

  /**
   * key unica per il database.
   */
  private String firebaseKey;

  /**
   * instanza della classe.
   */
  private static LoginController instance = null;

  /**
   * costruttore protetto, che valorizza la chiave univoca.
   */
  private LoginController() {
    firebaseKey = "AIzaSyB57Dfpr-VzZLPvDxSvWjH7mLgvOmAFOcA";
  }

  /**
   * metodo get per l'instanza della classe.
   *
   * @return instance
   */
  public static LoginController getInstance() {
    if (instance == null) {
      instance = new LoginController();
    }
    return instance;
  }

  /**
   * Metodo per l'autenticazione.
   *
   * @param username username scelto dall'utente
   * @param password password scelta dall'utente
   * @return token per la verifica del Login
   * @throws Exception comu8nica al metodo chiamante il problema riscontrato
   */
  public String auth(String username, String password) throws Exception {

    HttpURLConnection[] urlRequest = {null};
    String[] token = {null};
    CountDownLatch latch = new CountDownLatch(1);

    try {

      loginThread.execute(new Thread(() -> {
        Thread.currentThread().setName("loginThread");

        try {
          URL url = new URL(
              BASE_URL + OPERATION_AUTH + "?key=" + firebaseKey); //instanzia e valorizza l'url
          urlRequest[0] = (HttpURLConnection) url.openConnection();//instanzia la urlRequest
          urlRequest[0].setDoOutput(true);
          urlRequest[0].setRequestProperty("Content-Type", "application/json; charset=UTF-8");
          OutputStream os = urlRequest[0].getOutputStream(); //instanzia un outpit stream
          OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
          osw.write("{\"email\":\"" + username + "\",\"password\":\"" + password
              + "\",\"returnSecureToken\":true}"); //scrive la richiesta
          osw.flush();
          osw.close();
          os.close();
          urlRequest[0].connect(); //apre la connessione per mandare la request
          JsonParser jp = new JsonParser(); //from gson
          JsonElement root; //Converte  l'input stream a json element
          root = jp.parse(new InputStreamReader((InputStream) urlRequest[0].getContent()));
          JsonObject rootobj = root.getAsJsonObject(); //trasforma la root in object
          token[0] = rootobj.get("idToken").getAsString(); //ottiene il token univoco dell'account

        } catch (IOException e) {
          e.printStackTrace();
          token[0] = e.toString();

        } finally {
          urlRequest[0].disconnect();
          latch.countDown();

        }

      }, "loginThread"));


    } catch (Exception e) {
      latch.countDown();
      System.out.println("errore in auth");
      e.printStackTrace();
      token[0] = e.toString();

    }
    latch.await();

    return token[0];
  }

  /**
   * metodo per il recovery della password.
   *
   * @param email email per il recupero password
   * @return risposta http per il recupero password
   */
  public String passRecovery(String email) {
    HttpURLConnection[] urlRequest = {null};
    String[] kind = {null};
    CountDownLatch latch = new CountDownLatch(1);
    try {
      recoveryThread.execute(new Thread(() -> {
        Thread.currentThread().setName("recoveryThread");
        try {
          URL url = new URL(BASE_URL + OPERATION_PASSWORD_RECOVERY + "?key="
              + firebaseKey); //instanzia e valorizza l'url
          urlRequest[0] = (HttpURLConnection) url.openConnection();//instanzia la urlRequest
          urlRequest[0].setDoOutput(true);
          urlRequest[0].setRequestProperty("Content-Type", "application/json; charset=UTF-8");
          OutputStream os = urlRequest[0].getOutputStream();//instanzia un outpit stream
          OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
          osw.write("{\"requestType\":\"PASSWORD_RESET\",\"email\":\"" + email
              + "\"}");//scrive la richiesta
          System.out.println(osw.toString());
          osw.flush();
          osw.close();
          os.close();
          urlRequest[0].connect(); //apre la connessione per mandare la request
          JsonParser jp = new JsonParser(); //from gson
          JsonElement root; //Converte  l'input stream a json element
          root = jp.parse(new InputStreamReader((InputStream) urlRequest[0].getContent()));
          JsonObject rootobj = root.getAsJsonObject(); //trasforma la root in object

          kind[0] = rootobj.get("kind").getAsString(); //ottiene il risultato kind dal Json

        } catch (IOException e) {
          kind[0] = e.toString();
        } finally {
          urlRequest[0].disconnect();
          latch.countDown();
        }

      }, "recoveryThread"));
    } catch (Exception e) {
      kind[0] = e.toString();
      latch.countDown();
    }

    try {
      latch.await();
    } catch (InterruptedException e) {
      e.printStackTrace();
      kind[0] = e.toString();
    }
    return kind[0];
  }

  /**
   * metodo per la terminazione dei thread all'uscita del programma.
   */
  public void shutdown() {
    loginThread.shutdown();
    recoveryThread.shutdown();
  }


}
