package model.chartsModels;

import java.util.Observable;

import javafx.application.Platform;


/**
 * La classe rappresenta il model utile alla view per i grafici di tipo {@link
 * javafx.scene.chart.StackedAreaChart StackedAreaChart}. La comunicazione tra il model e le
 * rispettive view avviene secondo i canoni del design pattern MVC.
 *
 * @author ingSW20
 */
public class StackedAreaChartModel extends Observable {

  /**
   * istanza corrente della classe.
   */
  private static StackedAreaChartModel ourInstance = new StackedAreaChartModel();

  /**
   * Array di interi di 12 elementi (uno per mese) contenente i ricavi dovuti alla vendita dei
   * biglietti divisi per mese.
   */
  private Integer[] earningsFromTicketsSold = new Integer[12];

  /**
   * getter per l'istanza corrente della classe.
   *
   * @return {@link #ourInstance}
   */
  public static StackedAreaChartModel getInstance() {
    return ourInstance;
  }

  /**
   * costruttore vuoto e privato.
   */
  private StackedAreaChartModel() {
  }

  /**
   * aggiunge il ricavo al mese corrispondente.
   *
   * @param num mese della vendita
   * @param accesses ricavo da aggiungere
   */
  public void add(Integer num, Integer accesses) {
    earningsFromTicketsSold[num] = earningsFromTicketsSold[num] + accesses;
    setChanged();
    Platform.runLater(this::notifyObservers);
  }

  /**
   * getter per la variabile {@link #earningsFromTicketsSold}.
   *
   * @return {@link #earningsFromTicketsSold}
   */
  public Integer[] getEarningsFromTicketsSold() {
    return earningsFromTicketsSold;
  }

  /**
   * inizializza la variabile {@link #earningsFromTicketsSold} impostando il valore 0 in tutte le sue
   * posizioni.
   */
  public void initializeArray() {
    for (int i = 0; i < earningsFromTicketsSold.length; i++) {
      earningsFromTicketsSold[i] = 0;
    }
    setChanged();
    notifyObservers();
  }
}
