package model;

import java.util.Observable;

/**
 * Classe model per i dati di login, implementata come singleton.
 *
 * @author ingSW20
 * @see Observable
 */
public class LoginModel extends Observable {

  /**
   * instanza della classe.
   */
  private static LoginModel instance = null;
  /**
   * username da utilizzare per l'accesso.
   */
  private String username;
  /**
   * password da utilizzare per l'accesso.
   */
  private String password;

  /**
   * Metodo per ottenere l'instanza della classe, se esiste o chiamare il costruttore,
   * indirettamente, in caso contrario.
   *
   * @return istanza corrente
   */
  public static LoginModel getInstance() {
    if (instance == null) {
      instance = new LoginModel();
    }
    return instance;

  }

  /**
   * Costruttore della classe.
   */
  private LoginModel() {
  }

  /**
   * metodo set per {@link #password}.
   *
   * @param password da utilizzare per l'accesso
   */
  public void setPassWord(String password) {
    this.password = password;
    setChanged();//attiva il flag per gli observers
    notifyObservers(); //notifica gli observer
  }

  /**
   * metodo set per {@link #username}.
   *
   * @param username da utilizzare per l'accesso
   */
  public void setUserName(String username) {
    this.username = username;
    setChanged(); //attiva il flag per gli observer
    notifyObservers(); //notifica gli observer
  }

  /**
   * metodo get per la password.
   *
   * @return password da utilizzare per l'accesso {@link #password}
   */
  public String getPassWord() {
    return password;
  }

  /**
   * metodo get per l'username.
   *
   * @return username da utilizzare per l'accesso {@link #username}
   */
  public String getUserName() {
    return username;
  }

}
