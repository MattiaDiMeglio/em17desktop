package view.chartsViews;

import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import model.EventListModel;
import model.EventModel;

import java.util.*;

public class BarChartClass implements Observer, ChartInterface {

    private BarChart barChart;
    private List<XYChart.Data<String, Number>> datas;
    private XYChart.Series<String, Number> series;
    private int index;

    public BarChartClass(BarChart barChart, int index) {
        this.barChart = barChart;
        //initializeCharts();
        this.index = index;
        EventListModel.getInstance().getListaEventi().get(index).addObserver(this);
        update(EventListModel.getInstance().getListaEventi().get(index), null);
    }

    @Override
    public void update(Observable o, Object arg) {
        if (barChart.getData().isEmpty()){
            initializeCharts();
        }else {
            for (int i = 0; i < datas.size(); i++) {
               // datas.get(i).setYValue(vendite[i]);
            }
        }
    }

    @Override
    public void initializeCharts() {
        barChart.setTitle("Vendita per Settore");
        EventModel eventModel = EventListModel.getInstance().getListaEventi().get(index);
        XYChart.Series<String, Number> series = new XYChart.Series<>();

        series.setName("Biglietti venduti");
        if (barChart.getData().isEmpty()) {
            datas = new ArrayList<>();

            datas.add(new XYChart.Data("Gen", 0));
            datas.add(new XYChart.Data("Feb", 0));
            datas.add(new XYChart.Data("Mar", 0));
            datas.add(new XYChart.Data("Apr", 0));
            datas.add(new XYChart.Data("Mag", 0));
            datas.add(new XYChart.Data("Giu", 0));
            datas.add(new XYChart.Data("Lug", 0));
            datas.add(new XYChart.Data("Ago", 0));
            datas.add(new XYChart.Data("Set", 0));
            datas.add(new XYChart.Data("Ott", 0));
            datas.add(new XYChart.Data("Nov", 0));
            datas.add(new XYChart.Data("Dic", 0));

            for (XYChart.Data<String, Number> data : datas) {
                series.getData().add(data);
            }

            barChart.getData().add(series);

        }else {
            this.series = (XYChart.Series<String, Number>) barChart.getData().get(0);
            this.datas = Collections.singletonList(this.series.getData().get(0));
        }
    }
}
