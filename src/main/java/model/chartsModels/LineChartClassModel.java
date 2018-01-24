package model.chartsModels;

import java.util.Observable;

public class LineChartClassModel extends Observable {
    private static LineChartClassModel ourInstance = new LineChartClassModel();

    private Integer[] ticketsSaled = new Integer[12];

    public static LineChartClassModel getInstance() {
        return ourInstance;
    }

    private LineChartClassModel() {
        if (ourInstance == null) {
            for (int i = 0; i < ticketsSaled.length; i++) {
                ticketsSaled[i] = 0;
            }
        }
    }


    public void add(Integer num, Integer accesses) {
        ticketsSaled[num] = ticketsSaled[num] + accesses;
        setChanged();
        notifyObservers();
    }

    public Integer[] getTicketsSaled() {
        return ticketsSaled;
    }

    public void initializeArray(){
        for (int i =0;i<ticketsSaled.length;i++){
            ticketsSaled[i] = 0;
        }
    }
}
