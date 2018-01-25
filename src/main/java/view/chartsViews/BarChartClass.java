package view.chartsViews;

import javafx.scene.chart.BarChart;
import model.EventListModel;

import java.util.Observable;
import java.util.Observer;

public class BarChartClass implements Observer {

    private BarChart barChart;
    public BarChartClass(BarChart barChart, int index) {
        this.barChart = barChart;
        //initializeBarChart();
        barChart.setTitle("Vendita per Settore");

        EventListModel.getInstance().getListaEventi().get(index).addObserver(this);

        update(EventListModel.getInstance().getListaEventi().get(index), null);
    }

    @Override
    public void update(Observable o, Object arg) {

    }
}
