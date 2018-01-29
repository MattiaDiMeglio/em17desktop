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
import model.chartsModels.MergedModel;

import java.util.*;

public class BarChartView implements Observer, ChartInterface {

    private BarChart barChart;
    private List<XYChart.Data<String, Number>> datas;
    private XYChart.Series<String, Number> series;
    private String[] colors = {"navy", "lightskyblue", "lightslategray", "lightsalmon", "limegreen"};

    public BarChartView(BarChart barChart, int index) {
        this.barChart = barChart;
        initializeCharts();
        EventListModel.getInstance().getListaEventi().get(index).addObserver(this);
        update(EventListModel.getInstance().getListaEventi().get(index), null);
    }

    public BarChartView(BarChart barChart, ComboBox comboBox) {

        this.barChart = barChart;
        barChart.getXAxis().setAnimated(false);
        barChart.setTitle("Biglietti venduti per Location");
        initializeCharts();

        comboBox.valueProperty().addListener(new ChangeListener<Integer>() {
            @Override
            public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
                ChartsController.getInstance().populateCharts(String.valueOf(newValue));
                barChart.setTitle("Biglietti venduti per Location " + String.valueOf(newValue));
            }
        });
        BarChartModel.getInstance().addObserver(this);
    }

    public BarChartView(BarChart barChart) {
        this.barChart = barChart;
        barChart.getXAxis().setAnimated(false);
        barChart.setTitle("Biglietti venduti per Evento");
        initializeCharts();

        MergedModel.getInstance().addObserver(this);
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof EventModel) {
            EventModel eventModel = (EventModel) o;
            updateChart(eventModel);

        } else if (o instanceof BarChartModel){
            BarChartModel barChartModel = (BarChartModel) o;
            updateChart(barChartModel);
        } else {
            MergedModel mergedModel = (MergedModel) o;
            updateChart(mergedModel);
        }
    }

    @Override
    public void initializeCharts() {
        barChart.setAnimated(false);
        barChart.getXAxis().setAnimated(false);
        barChart.legendVisibleProperty().setValue(false);
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

    private void setRandomColor(XYChart.Data<String, Number> data) {
        int randomIndex = new Random().nextInt(colors.length);
        String randomColor = (colors[randomIndex]);
        data.nodeProperty().addListener((ov, oldNode, newNode) -> {
            if (newNode != null) {
                newNode.setStyle("-fx-bar-fill: " + randomColor + ";");
            }
        });
    }

    private void updateChart(EventModel eventModel){
        HashMap settori = eventModel.getSoldPerSectorList();
        List<String> nomeSettori = eventModel.getSectorNameList();

        if (barChart.getData().isEmpty()) {
            datas = new ArrayList<>();
        } else {
            barChart.getData().clear();
        }
        series.getData().clear();
        datas.clear();

        for (int i = 0; i < settori.size(); i++) {
            datas.add(new XYChart.Data(nomeSettori.get(i), settori.get(nomeSettori.get(i))));
        }

        for (XYChart.Data<String, Number> data : datas) {
            setRandomColor(data);
            series.getData().add(data);
        }
        barChart.getData().add(series);
    }

    private void updateChart(BarChartModel barChartModel){
        HashMap<Integer, String> locationIdMap = barChartModel.getLocationIdMap();
        List<String> locationNames = barChartModel.getLocationNames();
        List<Integer> soldPerLocation = barChartModel.getSoldPerLocation();
        if (barChart.getData().isEmpty()) {
            datas = new ArrayList<>();
        } else {
            barChart.getData().clear();
        }
        series.getData().clear();
        datas.clear();

        for (int i = 0; i < locationIdMap.size(); i++) {
            datas.add(new XYChart.Data(locationNames.get(i), soldPerLocation.get(i).intValue()));
        }
        for (XYChart.Data<String, Number> data : datas) {
            setRandomColor(data);

            series.getData().add(data);
        }
        barChart.getData().add(series);
    }

    private void updateChart(MergedModel mergedModel) {
        List<String> eventNames = mergedModel.getEventNames();
        List<Double> soldPerEvent = mergedModel.getSoldPerEvent();
        if (barChart.getData().isEmpty()) {
            datas = new ArrayList<>();
        } else {
            barChart.getData().clear();
        }
        series.getData().clear();
        datas.clear();

        for (int i = 0; i < eventNames.size(); i++) {
            datas.add(new XYChart.Data(eventNames.get(i), soldPerEvent.get(i)));
        }
        for (XYChart.Data<String, Number> data : datas) {
            setRandomColor(data);

            series.getData().add(data);
        }
        barChart.getData().add(series);
    }
}
