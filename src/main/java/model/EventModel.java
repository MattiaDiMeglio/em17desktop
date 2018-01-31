package model;

import java.util.HashMap;
import java.util.List;
import java.util.Observable;

import com.google.firebase.database.IgnoreExtraProperties;
import javafx.scene.image.Image;

/**
 * Model contenente i dati del singolo evento
 *
 * Estende observable, come definito dall'architettura MVC
 */
@IgnoreExtraProperties
public class EventModel extends Observable {

    /**
     * sottoclasse contenente i dati dei singoli settori
     */
    private class Sectors {
        /**
         * nome del settore
         */
        private String name;
        /**
         * prezzo base per settore
         */
        private int prize;
        /**
         * flag
         */
        private boolean reduction;
    }

    /**
     * lista contentente i nomi dei settori
     */
    private List sectorNameList;
    /**
     * lista dei settori
     */
    private List<Sectors> sectorList;
    /**
     * chiave dell'evento
     */
    private String eventKey;
    /**
     * indice dell'evento
     */
    private int index;
    /**
     * nome dell'evento
     */
    private String eventName;
    /**
     * flag di attività dell'evento
     */
    private boolean active;
    /**
     * immagine della locandina
     */
    private Image billboard;
    /**
     * data inizio
     */
    private String startingDate;
    /**
     * data Fine
     */
    private String endingDate;
    /**
     * descrizione dell'evento
     */
    private String eventDescription;
    /**
     * nome della location
     */
    private String locationName;
    /**
     * indirizzo della location
     */
    private String locationAddress;
    /**
     * percentuale di riduzione per gli anziani
     */
    private double eldersReduction;
    /**
     * percentuale di riduzione per i bambini
     */
    private double childrenReduction;
    /**
     * percentuale di riduzione per gli studenti
     */
    private double studentReduction;
    /**
     * array di biglietti venduti per mese
     */
    private Integer[] ticketsSoldPerMonth = new Integer[12];
    private Integer[] revenuePerMonth = new Integer[12];
    /**
     * biglietti venduti
     */
    private Integer ticketSold;
    /**
     * massimo dei visitatori
     */
    private Integer maxVisitors;
    /**
     * hashmap dei biglietti venduti
     */
    private HashMap<String,Integer> soldPerSectorList;
    /**
     * lista di url della galleria immagini
     */
    private List<Image> slideshow;


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
    public Image getBillboard() {
        return billboard;
    }

    /**
     * setter di Locdandina
     * @param billboard
     */
    public void setBillboard(Image billboard) {
        this.billboard = billboard;
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
     * @param num mese
     * @param accesses numero di biglietti
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
     * getter di revenuePerMonth
     * @return {@link #revenuePerMonth}
     */
    public Integer[] getRevenuePerMonth() {
        return revenuePerMonth;
    }

    /**
     * metodo per l'inizializzazione degli elementi di revenuePerMonth a 0
     */
    public void initializeRevenuePerMonth(){
        for (int i = 0; i< revenuePerMonth.length; i++){
            revenuePerMonth[i] = 0;
        }
        setChanged();
        notifyObservers();
    }

    /**
     * metodo che aggiunge somma il parametro in ingresso "revenue" a quello contenuto nella stessa posizione di "num"
     * @param num mese
     * @param revenues guadagni da somare
     */
    public void addOneRevenuePerMonth(Integer num, Integer revenues) {
        revenuePerMonth[num] = revenuePerMonth[num] + revenues;
        setChanged();
        notifyObservers();
    }

    /**
     * getter di Slideshow
     * @return
     */
    public List<Image> getSlideshow() {
        return slideshow;
    }

    /**
     * setter di slideshow
     * @param slideshow
     */
    public void setSlideshow(List<Image> slideshow) {
        this.slideshow = slideshow;
        setChanged();
        notifyObservers();
    }
}