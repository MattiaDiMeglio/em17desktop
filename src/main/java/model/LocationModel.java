package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

/**
 * model contenente le informazioni su una location.
 *
 * @author ingSW20
 */
public class LocationModel extends Observable {

  /**
   * nome della location.
   */
  private String locationName;
  /**
   * indirizzo della location.
   */
  private String locationAddress;
  /**
   * lista dei settori della location.
   */
  private List<String> sectorList = new ArrayList<>();
  /**
   * lista dei posti a sedere della location divisi per ettore.
   */
  private List<String> seatsList = new ArrayList<>();
  /**
   * id della location.
   */
  private String locationID;

  /**
   * getter per {@link #locationName}.
   *
   * @return {@link #locationName}
   */
  public String getLocationName() {
    return locationName;
  }

  /**
   * setter per {@link #locationName}.
   *
   * @param locationName valore da impostare a  {@link #locationName}
   */
  public void setLocationName(String locationName) {
    this.locationName = locationName;
  }

  /**
   * getter per {@link #locationAddress}.
   *
   * @return {@link #locationAddress}
   */
  public String getLocationAddress() {
    return locationAddress;
  }

  /**
   * setter per {@link #locationAddress}.
   *
   * @param locationAddress valore da impostare a {@link #locationAddress}
   */
  public void setLocationAddress(String locationAddress) {
    this.locationAddress = locationAddress;
  }

  /**
   * getter per {@link #sectorList}.
   *
   * @return {@link #sectorList}
   */
  public List<String> getSectorList() {
    return sectorList;
  }

  /**
   * setter per {@link #sectorList}.
   *
   * @param sectorList valore da impostare a {@link #sectorList}
   */
  public void setSectorList(List<String> sectorList) {
    this.sectorList = sectorList;
  }

  /**
   * getter per {@link #seatsList}.
   *
   * @return {@link #seatsList}
   */
  public List<String> getSeatsList() {
    return seatsList;
  }

  /**
   * setter per {@link #seatsList}.
   *
   * @param seatsList valore da impostare a {@link #seatsList}
   */
  public void setSeatsList(List<String> seatsList) {
    this.seatsList = seatsList;
  }

  /**
   * setter per {@link #locationID}.
   *
   * @param locationID valore da impostare a {@link #locationID}
   */
  public void setLocationID(String locationID) {
    this.locationID = locationID;
  }

  /**
   * getter per {@link #locationID}.
   *
   * @return {@link #locationID}
   */
  public String getLocationID() {
    return locationID;
  }
}
