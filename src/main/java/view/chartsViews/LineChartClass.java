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
import javafx.scene.chart.StackedAreaChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import model.EventListModel;
import model.EventModel;
import model.chartsModels.LineChartClassModel;
import model.chartsModels.LineChartClassSalesModel;

import java.util.*;

/**
 * this class is responsible for creating and managing the chart dedicated for cpu load
 *
 * @author Andrea Bravaccino
 */
public class LineChartClass implements Observer, ChartInterface {
    private List<XYChart.Data<String, Number>> datas;
    private XYChart.Series<String, Number> series;

    private LineChart lineChart;
    private StackedAreaChart stackedAreaChart;
    /**
     * the constructor is responsible for initialize the chart (lower and upper buonds, name, color)
     *
     * @param lineChart              is the type of chart for cpu load
     * @param comboBox
     */
    public LineChartClass(LineChart lineChart, ComboBox comboBox) {
        this.lineChart = lineChart;
        initializeCharts();
        lineChart.setTitle("Vendita biglietti");

        comboBox.valueProperty().addListener((ChangeListener<Integer>) (observable, oldValue, newValue) -> {
            ChartsController.populateCharts(String.valueOf(newValue));
            lineChart.setTitle("Vendita biglietti " + String.valueOf(newValue));
        });

        LineChartClassModel.getInstance().addObserver(this);
    }

    public LineChartClass(LineChart lineChart, int index) {
        this.lineChart = lineChart;
        initializeCharts();
        lineChart.setTitle("Vendita biglietti");

        EventListModel.getInstance().getListaEventi().get(index).addObserver(this);
        update(EventListModel.getInstance().getListaEventi().get(index), null);
    }

    public LineChartClass(StackedAreaChart stackedAreaChart, ComboBox comboBox) {
        this.stackedAreaChart = stackedAreaChart;
        initializeCharts();
        stackedAreaChart.setTitle("Vendita biglietti");

        comboBox.valueProperty().addListener((ChangeListener<Integer>) (observable, oldValue, newValue) -> {
            ChartsController.populateCharts(String.valueOf(newValue));
            stackedAreaChart.setTitle("Vendita biglietti " + String.valueOf(newValue));
        });

        LineChartClassSalesModel.getInstance().addObserver(this);
    }

    public LineChartClass(StackedAreaChart stackedAreaChart, int index) {
        this.stackedAreaChart = stackedAreaChart;
        initializeCharts();
        stackedAreaChart.setTitle("Guadagni nel tempo");

        EventListModel.getInstance().getListaEventi().get(index).addObserver(this);
        update(EventListModel.getInstance().getListaEventi().get(index), null);
    }

    @Override
    public void update(Observable o, Object arg) {
        Integer[] vendite;
        if (o instanceof LineChartClassModel) {
            LineChartClassModel lineChartClassModel = (LineChartClassModel) o;
            vendite = lineChartClassModel.getTicketsSaled();

        }else if (o instanceof LineChartClassSalesModel) {
            LineChartClassSalesModel salesModel = (LineChartClassSalesModel) o;
            vendite = salesModel.getTicketsSaled();
        }else {
            EventModel eventModel = (EventModel) o;
            vendite = eventModel.getTicketsSoldPerMonth();
        }

        for (int i = 0; i < datas.size(); i++) {
            datas.get(i).setYValue(vendite[i]);
        }
    }

    @Override
    public void initializeCharts() {
        if (lineChart == null) {
            stackedAreaChart.getXAxis().setAnimated(false);
            if (stackedAreaChart.getData().isEmpty()) {
                initilizeSeries();
                stackedAreaChart.getData().add(series);
            } else {
                resetSeries();
            }
        } else {
            lineChart.getXAxis().setAnimated(false);

            if (lineChart.getData().isEmpty()) {
                initilizeSeries();
                lineChart.getData().add(series);
            } else {
                resetSeries();
            }
        }
    }

    private void resetSeries(){
        if (lineChart == null){
            series = (XYChart.Series<String, Number>) stackedAreaChart.getData().get(0);
        }else {
            series = (XYChart.Series<String, Number>) lineChart.getData().get(0);
        }
        datas = new ArrayList<>();
        for (int i = 0; i < series.getData().size(); i++) {
            datas.add(series.getData().get(i));
        }
    }

    private void initilizeSeries(){
        series = new XYChart.Series<>();
        series.setName("Biglietti venduti");
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
    }
}
