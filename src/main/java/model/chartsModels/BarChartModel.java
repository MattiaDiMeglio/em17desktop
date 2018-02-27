package model.chartsModels;

import java.util.HashMap;
import java.util.List;
import java.util.Observable;

import javafx.application.Platform;


/**
 * Model per la gestione dei grafici PieChart secondo i canoni del design pattern MVC.
 *
 * @author ingSW20
 */
public class BarChartModel extends Observable {

  /**
   * biglietti venduti per location.
   */
  private List<Integer> soldPerLocation;
  /**
   * lista di nomi delle location.
   */
  private List<String> locationNames;
  /**
   * hashmap contenente come key gli indici (0 - n) i quali fanno riferimento agli UID contenuti.
   * come valori dell'hashmap
   */
  private HashMap<Integer, String> locationIdMap;

  /**
   * istanza corrente della classe.
   */
  private static BarChartModel ourInstance = new BarChartModel();

  /**
   * getter per l'istanza corrente della classe.
   *
   * @return {@link #ourInstance}
   */
  public static BarChartModel getInstance() {
    return ourInstance;
  }

  /**
   * costruttore vuoto e privato.
   */
  private BarChartModel() {
  }

  /**
   * setter per la variabile {@link #soldPerLocation}.
   *
   * @param soldPerLocation {@link #soldPerLocation}
   */
  public void setSoldPerLocation(List<Integer> soldPerLocation) {
    this.soldPerLocation = soldPerLocation;
    setChanged();
  }

  /**
   * setter per la variabile {@link #locationNames}.
   *
   * @param locationNames {@link #locationNames}
   */
  public void setLocationNames(List<String> locationNames) {
    this.locationNames = locationNames;
    setChanged();
  }

  /**
   * setter per la variabile {@link #locationIdMap}.
   *
   * @param locationIdMap {@link #locationIdMap}
   */
  public void setLocationIdMap(HashMap<Integer, String> locationIdMap) {
    this.locationIdMap = locationIdMap;
    setChanged();
    Platform.runLater(this::notifyObservers);
  }

  /**
   * getter per la variabile {@link #soldPerLocation}.
   *
   * @return {@link #soldPerLocation}
   */
  public List<Integer> getSoldPerLocation() {
    return soldPerLocation;
  }

  /**
   * getter per la variabile {@link #locationNames}.
   *
   * @return {@link #locationNames}
   */
  public List<String> getLocationNames() {
    return locationNames;
  }

  /**
   * getter per la variabile {@link #locationIdMap}.
   *
   * @return {@link #locationIdMap}
   */
  public HashMap<Integer, String> getLocationIdMap() {
    return locationIdMap;
  }
}
