package view.chartsViews;

import controller.chartsController.ChartsController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import model.EventListModel;
import model.EventModel;
import model.chartsModels.BarChartModel;

import java.util.*;

public class BarChartClass implements Observer, ChartInterface {

    private BarChart barChart;
    private List<XYChart.Data<String, Number>> datas;
    private XYChart.Series<String, Number> series;

    public BarChartClass(BarChart barChart, int index) {
        this.barChart = barChart;
        initializeCharts();
        EventListModel.getInstance().getListaEventi().get(index).addObserver(this);
        update(EventListModel.getInstance().getListaEventi().get(index), null);
    }

    public BarChartClass(BarChart barChart, ComboBox comboBox) {

        this.barChart = barChart;
        barChart.getXAxis().setAnimated(false);
        barChart.setTitle("Biglietti venduti per Location");
        initializeCharts();

        comboBox.valueProperty().addListener(new ChangeListener<Integer>() {
            @Override
            public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
                ChartsController.populateCharts(String.valueOf(newValue));
                barChart.setTitle("Biglietti venduti per Location " + String.valueOf(newValue));
            }
        });

        BarChartModel.getInstance().addObserver(this);
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof EventModel) {
            EventModel eventModel = (EventModel) o;
            HashMap settori = eventModel.getListaVenditaPerSettori();
            List<String> nomeSettori = eventModel.getListaSettoriName();

            if (barChart.getData().isEmpty()) {
                datas = new ArrayList<>();

                for (int i = 0; i < settori.size(); i++) {
                    datas.add(new XYChart.Data(nomeSettori.get(i), settori.get(nomeSettori.get(i))));
                }
                for (XYChart.Data<String, Number> data : datas) {
                    series.getData().add(data);
                }

                barChart.getData().add(series);
            } else {
                series.getData().clear();
                datas.clear();

                for (int i = 0; i < settori.size(); i++) {
                    datas.add(new XYChart.Data(nomeSettori.get(i), settori.get(nomeSettori.get(i))));
                }

                for (XYChart.Data<String, Number> data : datas) {
                    series.getData().add(data);
                }
            }
        } else {
            BarChartModel barChartModel = (BarChartModel) o;
            HashMap<Integer, String> locationIdMap = barChartModel.getLocationIdMap();
            List<String> locationNames = barChartModel.getLocationNames();
            List<Integer> soldPerLocation = barChartModel.getSoldPerLocation();
            if (barChart.getData().isEmpty()) {
                datas = new ArrayList<>();

                for (int i = 0; i < locationIdMap.size(); i++) {
                    datas.add(new XYChart.Data(locationNames.get(i), soldPerLocation.get(i).intValue()));
                }
                for (XYChart.Data<String, Number> data : datas) {
                    series.getData().add(data);
                }

                barChart.getData().add(series);
            } else {
                series.getData().clear();
                datas.clear();

                for (int i = 0; i < locationIdMap.size(); i++) {
                    datas.add(new XYChart.Data(locationNames.get(i), soldPerLocation.get(i).intValue()));
                }

                for (XYChart.Data<String, Number> data : datas) {
                    series.getData().add(data);
                }
            }
        }
    }

    @Override
    public void initializeCharts() {
        if (barChart.getData().isEmpty()) {
            series = new XYChart.Series<>();
        } else {
            series = (XYChart.Series<String, Number>) barChart.getData().get(0);
            datas = new ArrayList<>();
            for (int i = 0; i < series.getData().size(); i++) {
                datas.add(series.getData().get(i));
            }
        }
    }
}
