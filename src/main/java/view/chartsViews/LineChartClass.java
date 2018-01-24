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
import model.chartsModels.LineChartClassModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * this class is responsible for creating and managing the chart dedicated for cpu load
 *
 * @author Andrea Bravaccino
 */
public class LineChartClass implements Observer {
    private List<XYChart.Data<String, Number>> datas;

    /**
     * X axis variable
     */
    private NumberAxis xAxis;

    /**
     * the constructor is responsible for initialize the chart (lower and upper buonds, name, color)
     *
     * @param lineChart              is the type of chart for cpu load
     * @param dashboardYearComboBox1
     */
    public LineChartClass(LineChart lineChart, ComboBox dashboardYearComboBox1) {
        lineChart.setTitle("Vendita biglietti");
        lineChart.getXAxis().setAnimated(false);

        XYChart.Series<String, Number> series = new XYChart.Series<>();
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

        lineChart.getData().add(series);

        dashboardYearComboBox1.valueProperty().addListener(new ChangeListener<Integer>() {
            @Override
            public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
                ChartsController.populateCharts(String.valueOf(newValue));
                lineChart.setTitle("Vendita biglietti " + String.valueOf(newValue));
            }
        });

        LineChartClassModel.getInstance().addObserver(this);
    }

    public LineChartClass(LineChart lineChart) {
        lineChart.setTitle("Vendita biglietti");
        lineChart.getXAxis().setAnimated(false);

        XYChart.Series<String, Number> series = new XYChart.Series<>();
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

        lineChart.getData().add(series);


        LineChartClassModel.getInstance().addObserver(this);
    }

    @Override
    public void update(Observable o, Object arg) {
        Integer[] vendite = LineChartClassModel.getInstance().getTicketsSaled();

        for(int i =0;i<datas.size();i++){
            datas.get(i).setYValue(vendite[i]);
        }
    }
}
