package model.chartsModels;

import javafx.application.Platform;

import java.util.Observable;

/**
 * La classe rappresenta il model utile alla view per i grafici di tipo {@link javafx.scene.chart.StackedAreaChart StackedAreaChart}.
 * La comunicazione tra il model e le rispettive view avviene secondo i canoni del design pattern MVC
 *
 * @author ingSW20
 */
public class StackedAreaChartModel extends Observable {

    /**
     * istanza corrente della classe
     */
    private static StackedAreaChartModel ourInstance = new StackedAreaChartModel();

    /**
     * Array di interi di 12 elementi (uno per mese) contenente i ricavi dovuti alla vendita dei biglietti divisi per mese
     */
    private Integer[] earingsFromTicketsSold = new Integer[12];

    /**
     * getter per l'istanza corrente della classe
     *
     * @return {@link #ourInstance}
     */
    public static StackedAreaChartModel getInstance() {
        return ourInstance;
    }

    /**
     * costruttore vuoto e privato
     */
    private StackedAreaChartModel() {
    }

    /**
     * aggiunge il ricavo al mese corrispondente
     *
     * @param num      mese della vendita
     * @param accesses ricavo da aggiungere
     */
    public void add(Integer num, Integer accesses) {
        earingsFromTicketsSold[num] = earingsFromTicketsSold[num] + accesses;
        setChanged();
        Platform.runLater(this::notifyObservers);
    }

    /**
     * getter per la variabile {@link #earingsFromTicketsSold}
     *
     * @return {@link #earingsFromTicketsSold}
     */
    public Integer[] getEaringsFromTicketsSold() {
        return earingsFromTicketsSold;
    }

    /**
     * inizializza la variabile {@link #earingsFromTicketsSold} impostando il valore 0 in tutte le sue posizioni
     */
    public void initializeArray() {
        for (int i = 0; i < earingsFromTicketsSold.length; i++) {
            earingsFromTicketsSold[i] = 0;
        }
        setChanged();
        notifyObservers();
    }
}
