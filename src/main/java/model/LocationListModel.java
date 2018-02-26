package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import javafx.application.Platform;


/**
 * classe che contiene la lista di tutte le location contenute nel database locale.
 *
 * @author ingSW20
 */
public class LocationListModel {

  /**
   * istanza della classe corrente.
   */
  private static LocationListModel instance = new LocationListModel();
  /**
   * lista con le location trovate nel database.
   */
  private List<LocationModel> locationList = new ArrayList<>();

  /**
   * getter per l'istanza corrente.
   *
   * @return istanza corrente
   */
  public static LocationListModel getInstance() {
    return instance;
  }

  /**
   * costruttore vuoto.
   */
  private LocationListModel() {
  }

  /**
   * getter per {@link #locationList}.
   *
   * @return {@link #locationList}
   */
  public List<LocationModel> getLocationList() {
    return locationList;
  }

  /**
   * setter per {@link #locationList}.
   *
   * @param location location da aggiungere a {@link #locationList}
   */
  public void setLocationList(LocationModel location) {
    this.locationList.add(location);
  }
}


