package model;

import java.util.HashMap;
import java.util.List;
import java.util.Observable;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Model contenente i dati del singolo evento
 *
 * Estende observable, come definito dall'architettura MVC
 */
@IgnoreExtraProperties
public class EventModel extends Observable {
    private class Sectors {
        private String nome;
        private int prize;
        private boolean riduzione;
    }

    private List sectorNameList; //lista contentente i nomi dei settori
    List<Sectors> sectorList; //lista dei settori
    private String eventKey; //chiave dell'evento
    private int index; //indice dell'evento
    private String eventName; //nome dell'evento
    private boolean active; //flah di attività dell'evento
    private String locandina; //url della locandina
    private String startingDate; //data inizio
    private String endingDate; //data Fine
    private String eventDescription; //descrizione dell'evento
    private String locationName; //nome della location
    private String locationAddress; //indirizzo della location
    private double eldersReduction; //percentuale di riduzione per gli anziani
    private double childrenReduction;//percentuale di riduzione per i bambini
    private double studentReduction;//percentuale di riduzione per gli studenti
    private Integer[] ticketsSoldPerMonth = new Integer[12]; //array di biglietti venduti per mese
    private Integer ticketSold; //biglietti venduti
    private Integer maxVisitors; //massimo dei visitatori
    private HashMap<String,Integer> soldPerSectorList; //hashmap dei biglietti venduti
    private List<String> slideshow; //lista di url della galleria immagini


    /**
     * getter di sectorNameList
     * @return
     */
    public List getSectorNameList() {
        return sectorNameList;
    }

    /**
     * setter di sectorNameList
     * @param sectorNameList
     */
    public void setSectorNameList(List<String> sectorNameList) {
        this.sectorNameList = sectorNameList;
        setChanged(); //attiva il flag per gli observer
        notifyObservers(); //notifica gli observer
    }

    /**
     * getter di soldPerSectorList
     * @return
     */
    public HashMap<String, Integer> getSoldPerSectorList() {
        return soldPerSectorList;
    }

    /**
     * setter di soldPerSectorList
     * @param soldPerSectorList
     */
    public void setSoldPerSectorList(HashMap<String,Integer> soldPerSectorList) {
        this.soldPerSectorList = soldPerSectorList;
        setChanged(); //attiva il flag per gli observer
        notifyObservers(); //notifica gli observer
    }

    /**
     * getter di eventKey
     * @return
     */
    public String getEventKey() {
        return eventKey;
    }

    /**
     * getter di eventKey
     * @param eventKey
     */
    public void setEventKey(String eventKey) {
        this.eventKey = eventKey;
        setChanged(); //attiva il flag per gli observer
        notifyObservers(); //notifica gli observer
    }


    /**
     * getter di sectorList
     * @return
     */
    public List<Sectors> getSectorList() {
        return sectorList;
    }

    /**
     * setter di sectorList
     * @param sectorList
     */
    public void setSectorList(List<Sectors> sectorList) {
        this.sectorList = sectorList;
        setChanged(); //attiva il flag per gli observer
        notifyObservers(); //notifica gli observer
    }

    /**
     * getter dell'index
     * @return
     */
    public int getIndex() {
        return index;
    }

    /**
     * setter dell'index
     * @param index
     */
    public void setIndex(int index) {
        this.index = index;
        setChanged(); //attiva il flag per gli observer
        notifyObservers(); //notifica gli observer
    }

    /**
     * getter di active
     * @return
     */
    public boolean isActive() {
        return active;
    }

    /**
     * setter di active
     * @param active
     */
    public void setActive(boolean active) {
        this.active = active;
        setChanged(); //attiva il flag per gli observer
        notifyObservers(); //notifica gli observer
    }

    /**
     * getter di locandina
     * @return
     */
    public String getLocandina() {
        return locandina;
    }

    /**
     * setter di Locdandina
     * @param locandina
     */
    public void setLocandina(String locandina) {
        this.locandina = locandina;
        setChanged(); //attiva il flag per gli observer
        notifyObservers(); //notifica gli observer
    }

    /**
     * getter di eventName
     * @return
     */
    public String getEventName() {
        return eventName;
    }

    /**
     * setter di eventName
     * @param eventName
     */
    public void setEventName(String eventName) {
        this.eventName = eventName;
        setChanged(); //attiva il flag per gli observer
        notifyObservers(); //notifica gli observer
    }


    /**
     * getter di startingDate
     * @return
     */
    public String getStartingDate() {
        return startingDate;
    }

    /**
     * setter di startingDate
     * @param startingDate
     */
    public void setStartingDate(String startingDate) {
        this.startingDate = startingDate;
        setChanged(); //attiva il flag per gli observer
        notifyObservers(); //notifica gli observer
    }

    /**
     * getter di endingDate
     * @return
     */
    public String getEndingDate() {
        return endingDate;
    }

    /**
     * setter di endingDate
     * @param endingDate
     */
    public void setEndingDate(String endingDate) {
        this.endingDate = endingDate;
        setChanged(); //attiva il flag per gli observer
        notifyObservers(); //notifica gli observer
    }

    /**
     * getter di eventDescription
     * @return
     */
    public String getEventDescription() {
        return eventDescription;
    }

    /**
     * @param eventDescription
     */
    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
        setChanged(); //attiva il flag per gli observer
        notifyObservers(); //notifica gli observer
    }

    /**
     * getter di locationName
     * @return
     */
    public String getLocationName() {
        return locationName;
    }

    /**
     * setter di locationName
     * @param locationName
     */
    public void setLocationName(String locationName) {
        this.locationName = locationName;
        setChanged(); //attiva il flag per gli observer
        notifyObservers(); //notifica gli observer
    }

    /**
     * getter di locationAddress
     * @return
     */
    public String getLocationAddress() {
        return locationAddress;
    }

    /**
     * setter di locationAddress
     * @param locationAddress
     */
    public void setLocationAddress(String locationAddress) {
        this.locationAddress = locationAddress;
    }

    /**
     * getter di eldersReduction
     * @return
     */
    public double getEldersReduction() {
        return eldersReduction;
    }

    /**
     * setter di eldersReduction
     * @param eldersReduction
     */
    public void setEldersReduction(double eldersReduction) {
        this.eldersReduction = eldersReduction;
        setChanged(); //attiva il flag per gli observer
        notifyObservers(); //notifica gli observer
    }

    /**
     * getter di chidlredReduction
     * @return
     */
    public double getChildrenReduction() {
        return childrenReduction;
    }

    /**
     * getter di chidlredReduction
     * @param childrenReduction
     */
    public void setChildrenReduction(double childrenReduction) {
        this.childrenReduction = childrenReduction;
        setChanged(); //attiva il flag per gli observer
        notifyObservers(); //notifica gli observer
    }

    /**
     * getter di studentReduction
     * @return
     */
    public double getStudentReduction() {
        return studentReduction;
    }

    /**
     * setter di studentReduction
     * @param studentReduction
     */
    public void setStudentReduction(double studentReduction) {
        this.studentReduction = studentReduction;
        setChanged(); //attiva il flag per gli observer
        notifyObservers(); //notifica gli observer
    }

    /**
     * setter di ticketSold
     * @param ticketSold
     */
    public void setTicketSold(Integer ticketSold) {
        this.ticketSold = ticketSold;
        setChanged(); //attiva il flag per gli observer
        notifyObservers(); //notifica gli observer
    }

    /**
     * getter di ticketSold
     * @return
     */
    public Double getTicketSold() {
        return Double.valueOf(ticketSold);
    }

    /**
     * setter di max visitors
     * @param maxVisitors
     */
    public void setMaxVisitors(Integer maxVisitors) {
        this.maxVisitors = maxVisitors;
        setChanged(); //attiva il flag per gli observer
        notifyObservers(); //notifica gli observer
    }

    /**
     * getter maxVisitors
     * @return
     */
    public Double getMaxVisitors() {
        return Double.valueOf(maxVisitors);
    }

    /**
     * metodo che aggiunge un'elemento a ticketsSoldPerMonth, in posizione num
     * @param num
     * @param accesses
     */
    public void addOneSoldPerMonth(Integer num, Integer accesses) {
        ticketsSoldPerMonth[num] = ticketsSoldPerMonth[num] + accesses;
        setChanged();
        notifyObservers();
    }

    /**
     * getter di ticketsSoldPerMonth
     * @return
     */
    public Integer[] getTicketsSoldPerMonth() {
        return ticketsSoldPerMonth;
    }

    /**
     * metodo per l'inizializzazione degli elementi di ticketsSoldPerMonth a 0
     */
    public void initializeTicketPerMonth(){
        for (int i = 0; i< ticketsSoldPerMonth.length; i++){
            ticketsSoldPerMonth[i] = 0;
        }
        setChanged();
        notifyObservers();
    }

    /**
     * getter di Slideshow
     * @return
     */
    public List<String> getSlideshow() {
        return slideshow;
    }

    /**
     * setter di slideshow
     * @param slideshow
     */
    public void setSlideshow(List<String> slideshow) {
        this.slideshow = slideshow;
        setChanged();
        notifyObservers();
    }
}