package model.chartsModels;

import java.util.Observable;

import javafx.application.Platform;


/**
 * La classe rappresenta il model utile alla view relativa al grafico di tipo {@link
 * javafx.scene.chart.LineChart Linechart} oppure di tipo {@link javafx.scene.chart.StackedAreaChart
 * StackedAreaChart}. La comunicazione tra il model e le rispettive view avviene secondo i canoni
 * del design pattern MVC.
 *
 * @author ingSW20
 */
public class LineChartModel extends Observable {

  /**
   * istanza corrente della classe.
   */
  private static LineChartModel ourInstance = new LineChartModel();

  /**
   * Array di interi di 12 elementi (uno per mese) contenente i biglietti venduti divisi per mese.
   */
  private Integer[] ticketsSold = new Integer[12];

  /**
   * getter per l'istanza corrente della classe.
   *
   * @return {@link #ourInstance}
   */
  public static LineChartModel getInstance() {
    return ourInstance;
  }

  /**
   * costruttore vuoto e privato.
   */
  private LineChartModel() {
  }

  /**
   * aggiunge il numero di biglietti venduti al mese corrispondente.
   *
   * @param num mese della vendita
   * @param accesses biglietti venduti
   */
  public void add(Integer num, Integer accesses) {
    ticketsSold[num] = ticketsSold[num] + accesses;
    setChanged();
    Platform.runLater(this::notifyObservers);
  }

  /**
   * getter per la variabile {@link #ticketsSold}.
   *
   * @return {@link #ticketsSold}
   */
  public Integer[] getTicketsSold() {
    return ticketsSold;
  }

  /**
   * inizializza la variabile {@link #ticketsSold} impostando il valore 0 in tutte le sue posizioni.
   */
  public void initializeArray() {
    for (int i = 0; i < ticketsSold.length; i++) {
      ticketsSold[i] = 0;
    }
    setChanged();
    Platform.runLater(this::notifyObservers);
  }
}
