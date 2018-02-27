package model;

import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;

/**
 * Model contenente i dati del singolo evento, estende observable, come definito.
 * dall'architettura MVC
 */
public class EventModel extends Observable {

  /**
   * sottoclasse contenente i dati dei singoli settori.
   */
  public class Sectors {

    /**
     * nome del settore.
     */
    private String name = "";
    /**
     * numero di posti a sedere per settore.
     */
    private int seats = 0;
    /**
     * prezzo base per settore.
     */
    private Double price = 0.0;
    /**
     * flag per verificare se la riduzione è attiva o meno.
     */
    private boolean reduction = false;

    /**
     * getter per {@link #name}.
     *
     * @return {@link #name}
     */
    public String getName() {
      return name;
    }

    /**
     * setter per {@link #name}.
     *
     * @param name {@link #name}
     */
    public void setName(String name) {
      this.name = name;
    }

    /**
     * getter per {@link #price}.
     *
     * @return {@link #price}
     */
    public Double getPrice() {
      return price;
    }

    /**
     * setter per {@link #price}.
     *
     * @param price {@link #price}
     */
    public void setPrice(Double price) {
      this.price = price;
    }

    /**
     * getter per {@link #reduction}.
     *
     * @return {@link #reduction}
     */
    public boolean isReduction() {
      return reduction;
    }

    /**
     * setter per {@link #reduction}.
     *
     * @param reduction {@link #reduction}
     */
    public void setReduction(boolean reduction) {
      this.reduction = reduction;
    }

    /**
     * getter per {@link #seats}.
     *
     * @return {@link #seats}
     */
    public int getSeats() {
      return seats;
    }

    /**
     * setter {@link #seats}.
     *
     * @param seats {@link #seats}
     */
    public void setSeats(int seats) {
      this.seats = seats;
    }

  }

  /**
   * lista contentente i nomi dei settori.
   */
  private List<String> sectorNameList = new ArrayList<>();
  /**
   * lista dei settori.
   */
  private List<Sectors> sectorList = new ArrayList<>();
  /**
   * chiave dell'evento.
   */
  private String eventKey = "";
  /**
   * indice dell'evento.
   */
  private int index = 0;
  /**
   * nome dell'evento.
   */
  private String eventName = "";
  /**
   * flag di attività dell'evento.
   */
  private boolean active = false;
  /**
   * immagine della locandina.
   */
  private Image billboard;
  /**
   * data inizio.
   */
  private String startingDate = "";
  /**
   * data Fine.
   */
  private String endingDate = "";
  /**
   * descrizione dell'evento.
   */
  private String eventDescription = "";
  /**
   * nome della location.
   */
  private String locationName = "";
  /**
   * indirizzo della location.
   */
  private String locationAddress = "";
  /**
   * percentuale di riduzione per gli anziani.
   */
  private double eldersReduction = 0.0;
  /**
   * percentuale di riduzione per i bambini.
   */
  private double childrenReduction = 0.0;
  /**
   * percentuale di riduzione per gli studenti.
   */
  private double studentReduction = 0.0;
  /**
   * array di biglietti venduti per mese.
   */
  private Integer[] ticketsSoldPerMonth = new Integer[12];
  /**
   * array con i guadagni divisi per mese.
   */
  private Integer[] revenuePerMonth = new Integer[12];
  /**
   * biglietti venduti.
   */
  private Integer ticketSold = 0;
  /**
   * massimo dei visitatori.
   */
  private Integer maxVisitors = 0;
  /**
   * hashmap dei biglietti venduti.
   */
  private HashMap<String, Integer> soldPerSectorList = new HashMap<>();
  /**
   * lista di url della galleria immagini.
   */
  private List<Image> slideshow = new ArrayList<>();
  /**
   * prezzo.
   */
  private Double price = 0.0;

  /**
   * id della location.
   */
  private String locationID = "";

  /**
   * setter per {@link #price}.
   *
   * @param price {@link #price}
   */
  public void setPrice(Double price) {
    this.price = price;
  }

  /**
   * getter per {@link #price}.
   *
   * @return {@link #price}
   */
  public Double getPrice() {
    return price;
  }

  /**
   * getter di sectorNameList.
   *
   * @return lista con il nome dei settori
   */
  public List<String> getSectorNameList() {
    return sectorNameList;
  }

  /**
   * setter di sectorNameList.
   *
   * @param sectorNameList lista con il nome dei settori
   */
  public void setSectorNameList(List<String> sectorNameList) {
    this.sectorNameList.clear();
    this.sectorNameList.addAll(sectorNameList);
    setChanged(); //attiva il flag per gli observer
    notifyObservers(); //notifica gli observer
  }

  /**
   * getter di soldPerSectorList.
   *
   * @return vendita divisa per settori
   */
  public HashMap<String, Integer> getSoldPerSectorList() {
    return soldPerSectorList;
  }

  /**
   * setter di soldPerSectorList.
   *
   * @param soldPerSectorList vendita divisa per settori
   */
  public void setSoldPerSectorList(HashMap<String, Integer> soldPerSectorList) {
    this.soldPerSectorList.clear();
    this.soldPerSectorList.putAll(soldPerSectorList);
    setChanged(); //attiva il flag per gli observer
    notifyObservers(); //notifica gli observer
  }

  /**
   * getter di eventKey.
   *
   * @return key dell'evento
   */
  public String getEventKey() {
    return eventKey;
  }

  /**
   * getter di eventKey.
   *
   * @param eventKey key dell'evento
   */
  public void setEventKey(String eventKey) {
    this.eventKey = eventKey;
    setChanged(); //attiva il flag per gli observer
    notifyObservers(); //notifica gli observer
  }

  /**
   * getter di sectorList.
   *
   * @return lista dei settori
   */
  public List<Sectors> getSectorList() {
    return sectorList;
  }

  /**
   * setter di sectorList.
   *
   * @param sectorList lista dei settori
   */
  public void setSectorList(List<Sectors> sectorList) {
    this.sectorList.clear();
    this.sectorList.addAll(sectorList);
    setChanged(); //attiva il flag per gli observer
    notifyObservers(); //notifica gli observer
  }

  /**
   * getter dell'index.
   *
   * @return indice dell'evento
   */
  public int getIndex() {
    return index;
  }

  /**
   * setter dell'index.
   *
   * @param index indice dell'evento
   */
  public void setIndex(int index) {
    this.index = index;
    setChanged(); //attiva il flag per gli observer
    notifyObservers(); //notifica gli observer
  }

  /**
   * getter di active.
   *
   * @return flag per la verifica dell'annullamento dell'evento
   */
  public boolean isActive() {
    return active;
  }

  /**
   * setter di active.
   *
   * @param active flag per la verifica dell'annullamento dell'evento
   */
  public void setActive(boolean active) {
    this.active = active;
    setChanged(); //attiva il flag per gli observer
    notifyObservers(); //notifica gli observer
  }

  /**
   * getter di locandina.
   *
   * @return immagine della locandina
   */
  public Image getBillboard() {
    return billboard;
  }

  /**
   * setter di Locdandina.
   *
   * @param billboard immagine della locandina
   */
  public void setBillboard(Image billboard) {
    this.billboard = billboard;
    setChanged(); //attiva il flag per gli observer
    notifyObservers(); //notifica gli observer
  }

  /**
   * getter di eventName.
   *
   * @return nome dell'evento
   */
  public String getEventName() {
    return eventName;
  }

  /**
   * setter di eventName.
   *
   * @param eventName nome dell'evento
   */
  public void setEventName(String eventName) {
    this.eventName = eventName;
    setChanged(); //attiva il flag per gli observer
    notifyObservers(); //notifica gli observer
  }

  /**
   * getter di startingDate.
   *
   * @return data d'inizio
   */
  public String getStartingDate() {
    return startingDate;
  }

  /**
   * setter di startingDate.
   *
   * @param startingDate data d'inizio
   */
  public void setStartingDate(String startingDate) {
    this.startingDate = startingDate;
    setChanged(); //attiva il flag per gli observer
    notifyObservers(); //notifica gli observer
  }

  /**
   * getter di endingDate.
   *
   * @return data di fine evento
   */
  public String getEndingDate() {
    return endingDate;
  }

  /**
   * setter di endingDate.
   *
   * @param endingDate data di fine evento
   */
  public void setEndingDate(String endingDate) {
    this.endingDate = endingDate;
    setChanged(); //attiva il flag per gli observer
    notifyObservers(); //notifica gli observer
  }

  /**
   * getter di eventDescription.
   *
   * @return descrizione dell'evento
   */
  public String getEventDescription() {
    return eventDescription;
  }

  /**
   * setter per la descrizione dell'evento.
   *
   * @param eventDescription descrizione dell'evento
   */
  public void setEventDescription(String eventDescription) {
    this.eventDescription = eventDescription;
    setChanged(); //attiva il flag per gli observer
    notifyObservers(); //notifica gli observer
  }

  /**
   * getter di locationName.
   *
   * @return nome della location dell'evento
   */
  public String getLocationName() {
    return locationName;
  }

  /**
   * setter di locationName.
   *
   * @param locationName nome della location dell'evento
   */
  public void setLocationName(String locationName) {
    this.locationName = locationName;
    setChanged(); //attiva il flag per gli observer
    notifyObservers(); //notifica gli observer
  }

  /**
   * getter di locationAddress.
   *
   * @return indirizzo della location
   */
  public String getLocationAddress() {
    return locationAddress;
  }

  /**
   * setter di locationAddress.
   *
   * @param locationAddress indirizzo della location
   */
  public void setLocationAddress(String locationAddress) {
    this.locationAddress = locationAddress;
  }

  /**
   * getter di eldersReduction.
   *
   * @return percentuale di sconto per gli anziani
   */
  public double getEldersReduction() {
    return eldersReduction;
  }

  /**
   * setter di eldersReduction.
   *
   * @param eldersReduction percentuale di sconto per gli anziani
   */
  public void setEldersReduction(double eldersReduction) {
    this.eldersReduction = eldersReduction;
    setChanged(); //attiva il flag per gli observer
    notifyObservers(); //notifica gli observer
  }

  /**
   * getter di chidlredReduction.
   *
   * @return percentuale di sconto per i bambini
   */
  public double getChildrenReduction() {
    return childrenReduction;
  }

  /**
   * getter di chidlredReduction.
   *
   * @param childrenReduction percentuale di sconto per i bambini
   */
  public void setChildrenReduction(double childrenReduction) {
    this.childrenReduction = childrenReduction;
    setChanged(); //attiva il flag per gli observer
    notifyObservers(); //notifica gli observer
  }

  /**
   * getter di studentReduction.
   *
   * @return percentuale di sconto per gli studenti
   */
  public double getStudentReduction() {
    return studentReduction;
  }

  /**
   * setter di studentReduction.
   *
   * @param studentReduction percentuale di sconto per gli studenti
   */
  public void setStudentReduction(double studentReduction) {
    this.studentReduction = studentReduction;
    setChanged(); //attiva il flag per gli observer
    notifyObservers(); //notifica gli observer
  }

  /**
   * setter di ticketSold.
   *
   * @param ticketSold numero di biglietti venduti
   */
  public void setTicketSold(Integer ticketSold) {
    this.ticketSold = ticketSold;
    setChanged(); //attiva il flag per gli observer
    notifyObservers(); //notifica gli observer
  }

  /**
   * getter di ticketSold.
   *
   * @return numero di biglietti venduti
   */
  public Integer getTicketSold() {
    return ticketSold;
  }

  /**
   * setter di max visitors.
   *
   * @param maxVisitors numero massimo di visitatori
   */
  public void setMaxVisitors(Integer maxVisitors) {
    this.maxVisitors = maxVisitors;
    setChanged(); //attiva il flag per gli observer
    notifyObservers(); //notifica gli observer
  }

  /**
   * getter maxVisitors.
   *
   * @return numero massimo di visitatori
   */
  public Integer getMaxVisitors() {
    return maxVisitors;
  }

  /**
   * metodo che aggiunge un'elemento a ticketsSoldPerMonth, in posizione num.
   *
   * @param num mese
   * @param accesses numero di biglietti
   */
  public void addOneSoldPerMonth(Integer num, Integer accesses) {
    ticketsSoldPerMonth[num] = ticketsSoldPerMonth[num] + accesses;
    setChanged();
    notifyObservers();
  }

  /**
   * getter di ticketsSoldPerMonth.
   *
   * @return biglietti venduti divisi per mese
   */
  public Integer[] getTicketsSoldPerMonth() {
    return ticketsSoldPerMonth;
  }

  /**
   * metodo per l'inizializzazione degli elementi di ticketsSoldPerMonth a 0.
   */
  public void initializeTicketPerMonth() {
    for (int i = 0; i < ticketsSoldPerMonth.length; i++) {
      ticketsSoldPerMonth[i] = 0;
    }
    setChanged();
    notifyObservers();
  }

  /**
   * getter di revenuePerMonth.
   *
   * @return {@link #revenuePerMonth}
   */
  public Integer[] getRevenuePerMonth() {
    return revenuePerMonth;
  }

  /**
   * metodo per l'inizializzazione degli elementi di revenuePerMonth a 0.
   */
  public void initializeRevenuePerMonth() {
    for (int i = 0; i < revenuePerMonth.length; i++) {
      revenuePerMonth[i] = 0;
    }
    setChanged();
    notifyObservers();
  }

  /**
   * metodo che aggiunge somma il parametro in ingresso "revenue" a quello contenuto nella stessa.
   * posizione di "num"
   *
   * @param num mese
   * @param revenues guadagni da somare
   */
  public void addOneRevenuePerMonth(Integer num, Integer revenues) {
    revenuePerMonth[num] = revenuePerMonth[num] + revenues;
    setChanged();
    notifyObservers();
  }

  /**
   * getter di Slideshow.
   *
   * @return lista di immagini per lo slideshow
   */
  public List<Image> getSlideshow() {
    return slideshow;
  }

  /**
   * setter di slideshow.
   *
   * @param slideshow lista di immagini per lo slideshow
   */
  public void setSlideshow(List<Image> slideshow) {
    this.slideshow.clear();
    this.slideshow.addAll(slideshow);
    setChanged();
    notifyObservers();
  }

  /**
   * metodo per forzare la notifica agli observer.
   */
  public void notifyMyObservers() {
    setChanged();
    notifyObservers();
  }

  /**
   * setter per l'id della location.
   *
   * @param id id della location
   */
  public void setLocationID(String id) {
    locationID = id;
  }

  /**
   * getter per l'id della location.
   *
   * @return id della location
   */
  public String getLocationID() {
    return locationID;
  }
}