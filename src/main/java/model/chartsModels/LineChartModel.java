package model.chartsModels;

import javafx.application.Platform;

import java.util.Observable;

public class LineChartModel extends Observable {
    private static LineChartModel ourInstance = new LineChartModel();

    private Integer[] ticketsSold = new Integer[12];

    public static LineChartModel getInstance() {
        return ourInstance;
    }

    private LineChartModel() {
        if (ourInstance == null) {
            for (int i = 0; i < ticketsSold.length; i++) {
                ticketsSold[i] = 0;
            }
        }
    }

    public void add(Integer num, Integer accesses) {
        ticketsSold[num] = ticketsSold[num] + accesses;
        setChanged();
        Platform.runLater(this::notifyObservers);
    }

    public Integer[] getTicketsSold() {
        return ticketsSold;
    }

    public void initializeArray(){
        for (int i = 0; i< ticketsSold.length; i++){
            ticketsSold[i] = 0;
        }
        setChanged();
        notifyObservers();
    }
}
