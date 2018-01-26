package model.chartsModels;

import javafx.application.Platform;

import java.util.Observable;

public class LineChartClassSalesModel extends Observable {
    private static LineChartClassSalesModel  ourInstance = new LineChartClassSalesModel ();

    private Integer[] ticketsSaled = new Integer[12];

    public static LineChartClassSalesModel  getInstance() {
        return ourInstance;
    }

    private LineChartClassSalesModel () {
        if (ourInstance == null) {
            for (int i = 0; i < ticketsSaled.length; i++) {
                ticketsSaled[i] = 0;
            }
        }
    }


    public void add(Integer num, Integer accesses) {
        ticketsSaled[num] = ticketsSaled[num] + accesses;
        setChanged();
        Platform.runLater(this::notifyObservers);
    }

    public Integer[] getTicketsSaled() {
        return ticketsSaled;
    }

    public void initializeArray(){
        for (int i =0;i<ticketsSaled.length;i++){
            ticketsSaled[i] = 0;
        }
        setChanged();
        notifyObservers();
    }
}
