package model.chartsModels;

import java.util.List;
import java.util.Observable;

public class MergedModel extends Observable {
    private static MergedModel ourInstance = new MergedModel();
    private Integer[] ticketsSoldArray = new Integer[12];
    private Integer[] earningsArray = new Integer[12];
    private Double maxTickets;
    private Double ticketsSold;
    private List<Integer> soldPerEvent;
    private List<String> eventNames;

    public static MergedModel getInstance() {
        return ourInstance;
    }

    private MergedModel() {}

    public void resetModel(){
        initializeArray();
        soldPerEvent.clear();
        eventNames.clear();
    }

    public void setTicketSoldArray(Integer[] ticketSoldArray) {
        this.ticketsSoldArray = ticketSoldArray;
        setChanged();
        // Platform.runLater(this::notifyObservers);
    }


    public Integer[] getEarningsArray() {
        return earningsArray;
    }

    public Integer[] getTicketsSoldArray() {
        return ticketsSoldArray;
    }

    private void initializeArray() {
        for (int i = 0; i < ticketsSoldArray.length; i++) {
            earningsArray[i] = 0;
            ticketsSoldArray[i] = 0;
        }
    }

    // metodi per il Piechart
    public Double getMaxTickets() {
        return maxTickets;
    }

    public void setMaxTickets(Double maxTickets) {
        this.maxTickets = maxTickets;
        setChanged();
    }

    public Double getTicketsSold() {
        return ticketsSold;
    }

    public void setTicketsSold(Double ticketsSold) {
        this.ticketsSold = ticketsSold;
        setChanged();
    }

    // metodi per il barchart

    public List<Integer> getSoldPerEvent() {
        return soldPerEvent;
    }

    public void setSoldPerEvent(List<Integer> soldPerEvent) {
        this.soldPerEvent = soldPerEvent;
    }

    public List<String> getEventNames() {
        return eventNames;
    }

    public void setEventNames(List<String> eventNames) {
        this.eventNames = eventNames;
    }
}
