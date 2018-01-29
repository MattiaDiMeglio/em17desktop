package model.chartsModels;

import javafx.application.Platform;

import java.util.Observable;

public class StackedAreaChartModel extends Observable {
    private static StackedAreaChartModel ourInstance = new StackedAreaChartModel();

    private Integer[] ticketsSold = new Integer[12];

    public static StackedAreaChartModel getInstance() {
        return ourInstance;
    }

    private StackedAreaChartModel() {
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
