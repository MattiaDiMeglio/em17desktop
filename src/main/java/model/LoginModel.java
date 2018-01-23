package model;


import view.LoginView;
import view.RecoveryView;

import java.util.Observable;
import java.util.Observer;

/**
 * Classe model per i dati di login, implementata come singleton
 *
 * @see Observable
 *
 */
public class LoginModel extends Observable {

    /**
     * instanza della classe
     */
    private static LoginModel instance = null;
    private String username;
    private String password;

    /**
     * Metodo per ottenere l'instanza della classe, se esiste
     * o chiamare il costruttore, indirettamente, in caso contrario
     * @return
     */
    public static LoginModel getInstance (){
        if (instance == null) {
            instance=new LoginModel();
        }
        return instance;

    }

    /**
     * Costruttore della classe
     */
    protected LoginModel(){
    };

    /**
     * metodo set per la password
     * @param password
     */
    public void setPassWord(String password) {
        this.password = password;
        setChanged();//attiva il flag per gli observers
        notifyObservers(); //notifica gli observer
    }

    /**
     * metodo set per l'username
     * @param username
     */
    public void setUserName(String username) {
        this.username = username;
        setChanged(); //attiva il flag per gli observer
        notifyObservers(); //notifica gli observer
    }

    /**
     * metodo get per la password
     * @return
     */
    public String getPassWord() {
        return password;
    }

    /**
     * metodo get per l'username
     * @return
     */
    public String getUserName() {
        return username;
    }

}
