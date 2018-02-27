package model.chartsModels;

import java.util.Observable;

import javafx.application.Platform;



/**
 * La classe rappresenta il model utile alla view per i grafici di tipo {@link
 * javafx.scene.chart.PieChart PieChart}. La comunicazione tra il model e le rispettive view avviene
 * secondo i canoni del design pattern MVC.
 *
 * @author ingSW20
 */
public class PieChartModel extends Observable {

  /**
   * istanza corrente della classe.
   */
  private static PieChartModel ourInstance = new PieChartModel();

  /**
   * numero massimo di biglietti.
   */
  private Double maxTickets;

  /**
   * biglietti venduti.
   */
  private Double ticketsSold;

  /**
   * getter per l'istanza corrente della classe.
   *
   * @return {@link #ourInstance}
   */
  public static PieChartModel getInstance() {
    return ourInstance;
  }

  /**
   * contruttore vuoto e privato.
   */
  private PieChartModel() {
  }

  /**
   * getter per la variabile {@link #maxTickets}.
   *
   * @return {@link #maxTickets}
   */
  public Double getMaxTickets() {
    return maxTickets;
  }

  /**
   * setter per la variabile {@link #maxTickets}.
   *
   * @param maxTickets {@link #maxTickets}
   */
  public void setMaxTickets(Double maxTickets) {
    this.maxTickets = maxTickets;
    setChanged();
  }

  /**
   * getter per la variabile {@link #ticketsSold}.
   *
   * @return {@link #ticketsSold}
   */
  public Double getTicketsSold() {
    return ticketsSold;
  }

  /**
   * setter per la variabile {@link #ticketsSold}.
   *
   * @param ticketsSold {@link #ticketsSold}
   */
  public void setTicketsSold(Double ticketsSold) {
    this.ticketsSold = ticketsSold;
    setChanged();
    Platform.runLater(this::notifyObservers);
  }
}
