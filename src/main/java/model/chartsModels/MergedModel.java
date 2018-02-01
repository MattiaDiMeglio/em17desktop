package model.chartsModels;

import javafx.application.Platform;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

/**
 * La classe rappresenta il model utile alla view per i grafici presente in {@link view.EventListView EventListView}.
 * La comunicazione tra il model e le rispettive view avviene secondo i canoni del design pattern MVC
 *
 * @author ingSW20
 */
public class MergedModel extends Observable {

    /**
     * istanza corrente della classe
     */
    private static MergedModel ourInstance = new MergedModel();

    /**
     * Array di interi di 12 elementi (uno per mese) contenente i biglietti venduti divisi per mese
     */
    private Integer[] ticketsSoldArray = new Integer[12];

    /**
     * numero massimo di visitatori
     */
    private Double maxVisitors = 0.0;

    /**
     * numero di biglietti venduti
     */
    private Double ticketsSold = 0.0;

    /**
     * numero di biglietti venduti divisi per evento
     */
    private List<Double> soldPerEvent = new ArrayList<>();

    /**
     * lista con i nomi degli eventi
     */
    private List<String> eventNames = new ArrayList<>();

    /**
     * getter per l'istanza corrente della classe
     *
     * @return {@link #ourInstance}
     */
    public static MergedModel getInstance() {
        return ourInstance;
    }

    /**
     * costruttore vuoto e privato
     */
    private MergedModel() {
    }

    /**
     * il metodo si occupa di resettare tutti i dati nel model così da essere riempito con i nuovi  dati
     */
    public void resetModel() {
        initializeArray();
        soldPerEvent.clear();
        soldPerEvent.add(0.0);
        eventNames.clear();
        eventNames.add("");
        maxVisitors = 0.0;
        ticketsSold = 0.0;
        setChanged();
        Platform.runLater(this::notifyObservers);
    }

    /**
     * setter per la variabile {@link #ticketsSoldArray}
     *
     * @param ticketSoldArray {@link #ticketsSoldArray}
     */
    public void setTicketSoldArray(Integer[] ticketSoldArray) {
        this.ticketsSoldArray = ticketSoldArray;
        setChanged();
    }

    /**
     * getter per la variabile {@link #ticketsSoldArray}
     *
     * @return {@link #ticketsSoldArray}
     */
    public Integer[] getTicketsSoldArray() {
        return ticketsSoldArray;
    }

    /**
     * inizializza la variabile {@link #ticketsSoldArray} dando il valore 0 a tutte le sue posizioni
     */
    private void initializeArray() {
        for (int i = 0; i < ticketsSoldArray.length; i++) {
            ticketsSoldArray[i] = 0;
        }
    }

    /**
     * getter per la variabile {@link #maxVisitors}
     *
     * @return
     */
    public Double getMaxVisitors() {
        return maxVisitors;
    }

    /**
     * setter per la variabile {@link #maxVisitors}
     *
     * @param maxVisitors {@link #maxVisitors}
     */
    public void setMaxVisitors(Double maxVisitors) {
        this.maxVisitors = maxVisitors;
        setChanged();
    }

    /**
     * getter per la variabile {@link #ticketsSoldArray}
     *
     * @return {@link #ticketsSold}
     */
    public Double getTicketsSold() {
        return ticketsSold;
    }

    /**
     * setter per la variabile {@link #ticketsSold}
     *
     * @param ticketsSold {@link #ticketsSold}
     */
    public void setTicketsSold(Double ticketsSold) {
        this.ticketsSold = ticketsSold;
        setChanged();
    }


    /**
     * getter per la variabile {@link #soldPerEvent}
     *
     * @return {@link #soldPerEvent}
     */
    public List<Double> getSoldPerEvent() {
        return soldPerEvent;
    }

    /**
     * setter per la variabile {@link #soldPerEvent}
     *
     * @param soldPerEvent {@link #soldPerEvent}
     */
    public void setSoldPerEvent(List<Double> soldPerEvent) {
        this.soldPerEvent = soldPerEvent;
        setChanged();
        Platform.runLater(this::notifyObservers);
    }

    /**
     * getter per la variabile {@link #eventNames}
     *
     * @return {@link #eventNames}
     */
    public List<String> getEventNames() {
        return eventNames;
    }

    /**
     * setter per la variabile {@link #eventNames}
     *
     * @param eventNames {@link #eventNames}
     */
    public void setEventNames(List<String> eventNames) {
        this.eventNames = eventNames;
    }
}
