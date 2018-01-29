package model.chartsModels;

import javafx.application.Platform;

import java.util.Observable;

public class PieChartModel extends Observable{
    private static PieChartModel ourInstance = new PieChartModel();

    public static PieChartModel getInstance() {
        return ourInstance;
    }

    private Double maxTickets;
    private Double ticketsSold;
    private PieChartModel() {}

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
        Platform.runLater(this::notifyObservers);
    }
}
