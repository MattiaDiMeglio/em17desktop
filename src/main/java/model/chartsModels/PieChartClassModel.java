package model.chartsModels;

import java.util.Observable;

public class PieChartClassModel extends Observable{
    private static PieChartClassModel ourInstance = new PieChartClassModel();

    public static PieChartClassModel getInstance() {
        return ourInstance;
    }

    private Double maxTickets;
    private Double ticketsSold;
    private PieChartClassModel() {}

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
        notifyObservers();
    }
}
