/*
 * This is the source code of PC-status.
 * It is licensed under GNU AGPL v3 or later.
 * You should have received a copy of the license in this archive (see LICENSE).
 *
 * Copyright Andrea Bravaccino.
 */
package view.chartsViews;

import controller.chartsController.ChartsController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import model.EventListModel;
import model.EventModel;
import model.chartsModels.LineChartClassModel;

import java.util.*;

/**
 * this class is responsible for creating and managing the chart dedicated for cpu load
 *
 * @author Andrea Bravaccino
 */
public class LineChartClass implements Observer {
    private List<XYChart.Data<String, Number>> datas;
    private XYChart.Series<String, Number> series;

    private LineChart lineChart;
    /**
     * the constructor is responsible for initialize the chart (lower and upper buonds, name, color)
     *
     * @param lineChart              is the type of chart for cpu load
     * @param dashboardYearComboBox1
     */
    public LineChartClass(LineChart lineChart, ComboBox dashboardYearComboBox1) {
        this.lineChart = lineChart;
        initializeLineChart();
        lineChart.setTitle("Vendita biglietti");

        dashboardYearComboBox1.valueProperty().addListener(new ChangeListener<Integer>() {
            @Override
            public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
                ChartsController.populateCharts(String.valueOf(newValue));
                lineChart.setTitle("Vendita biglietti " + String.valueOf(newValue));
            }
        });

        LineChartClassModel.getInstance().addObserver(this);
    }

    public LineChartClass(LineChart lineChart, int index) {
        this.lineChart = lineChart;
        initializeLineChart();
        lineChart.setTitle("Vendita biglietti");

        EventListModel.getInstance().getListaEventi().get(index).addObserver(this);

        update(EventListModel.getInstance().getListaEventi().get(index), null);
    }

    @Override
    public void update(Observable o, Object arg) {
        Integer[] vendite;
        if (o instanceof LineChartClassModel) {
            vendite = LineChartClassModel.getInstance().getTicketsSaled();

        }else {
            EventModel eventModel = (EventModel) o;
            vendite = ((EventModel) o).getTicketsSoldPerMonth();
        }

        for (int i = 0; i < datas.size(); i++) {
            datas.get(i).setYValue(vendite[i]);
        }
    }

    private void initializeLineChart(){
        lineChart.getXAxis().setAnimated(false);

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Biglietti venduti");
        if (lineChart.getData().isEmpty()) {
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

            lineChart.getData().add(series);

        }else {
            this.series = (XYChart.Series<String, Number>) lineChart.getData().get(0);
            this.datas = Collections.singletonList(this.series.getData().get(0));
        }
    }
}