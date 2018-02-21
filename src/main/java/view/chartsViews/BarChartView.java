package view.chartsViews;

import controller.chartsController.ChartsController;
import javafx.beans.value.ChangeListener;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import model.EventListModel;
import model.EventModel;
import model.chartsModels.BarChartModel;
import model.chartsModels.MergedModel;
import view.LoadingPopupView;

import java.util.*;
import java.util.concurrent.CountDownLatch;

/**
 * Questa classe rappresenta la view corrispondente al grafico {@link javafx.scene.chart.BarChart BarChart}.
 * L'aggiornamento della view è eseguita secondo i canoni del design pattern MVC
 *
 * @author ingsw20
 */
public class BarChartView implements Observer, ChartInterface {

    /**
     * variabile per la gestione del grafico
     */
    private BarChart barChart;

    /**
     * dati mostrati sul grfico
     */
    private List<XYChart.Data<String, Number>> datas;

    /**
     * serie contenente i dati da mostrare sul grafico
     */
    private XYChart.Series<String, Number> series;

    /**
     * array contenente alcuni colori che verranno scelti casualmente
     */
    private String[] colors = {"navy", "lightskyblue", "lightslategray", "lightsalmon", "limegreen"};

    /**
     * Costruttore per l'inizializzazione del grafico relativo ad un singolo evento, inoltre registra la view presso il model.
     *
     * @param barChart tipo di grafico
     * @param index    indice da utilizzare per identificare l'evento desiderato
     */
    public BarChartView(BarChart barChart, int index) {
        this.barChart = barChart;
        initializeCharts();
        EventListModel.getInstance().getListaEventi().get(index).addObserver(this);
        update(EventListModel.getInstance().getListaEventi().get(index), null);
    }

    /**
     * il construttore inizializza {@link #barChart} per la gestione del grafico,
     * inoltre registra la view presso il model.
     * In particolare, questo costruttore è utilizzato per la visualizzazione delle statistiche relativo ad un anno scelto.
     *
     * @param barChart tipo di grafico da gestire
     * @param comboBox combobox per scegliere l'anno
     */
    public BarChartView(BarChart barChart, ComboBox comboBox) {
        this.barChart = barChart;
        barChart.getXAxis().setAnimated(false);
        barChart.setTitle("Biglietti venduti per Location");
        initializeCharts();

        comboBox.valueProperty().addListener((ChangeListener<Integer>) (observable, oldValue, newValue) -> {
            CountDownLatch latch = new CountDownLatch(1);
            ChartsController.getInstance().populateCharts(String.valueOf(newValue), latch);
            new LoadingPopupView(latch);
            barChart.setTitle("Biglietti venduti per Location " + String.valueOf(newValue));
        });
        BarChartModel.getInstance().addObserver(this);
    }

    /**
     * costruttore per il grafico contenente i dati di più eventi
     *
     * @param barChart tipo di grafico da gestire
     */
    public BarChartView(BarChart barChart) {
        this.barChart = barChart;
        barChart.getXAxis().setAnimated(false);
        barChart.setTitle("Biglietti venduti per Evento");
        initializeCharts();

        MergedModel.getInstance().addObserver(this);
    }

    /**
     * metodo necessario secondo il design pattern MVC, utile per l'aggiornamento del grafico in tempo reale
     *
     * @param o   Model dal quale è stato invocato il metodo
     * @param arg null
     */
    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof EventModel) {
            EventModel eventModel = (EventModel) o;
            updateChart(eventModel);

        } else if (o instanceof BarChartModel) {
            BarChartModel barChartModel = (BarChartModel) o;
            updateChart(barChartModel);
        } else {
            MergedModel mergedModel = (MergedModel) o;
            updateChart(mergedModel);
        }
    }

    /**
     * metodo per l'inizializzazione del grafico
     */
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
            datas.addAll(series.getData());
        }
    }

    /**
     * il metodo si occupa di impostare un colore casuale per ogni entry del grafico
     *
     * @param data dati da colorare
     */
    private void setRandomColor(XYChart.Data<String, Number> data) {
        int randomIndex = new Random().nextInt(colors.length);
        String randomColor = (colors[randomIndex]);
        data.nodeProperty().addListener((ov, oldNode, newNode) -> {
            if (newNode != null) {
                newNode.setStyle("-fx-bar-fill: " + randomColor + ";");
            }
        });
    }

    /**
     * aggiornamento del grafico utilizzando il model passato per parametro
     *
     * @param eventModel model per il prelievo dei dati
     */
    private void updateChart(EventModel eventModel) {
        HashMap<String, Integer> settori = eventModel.getSoldPerSectorList();
        List<String> nomeSettori = eventModel.getSectorNameList();

        if (barChart.getData().isEmpty()) {
            datas = new ArrayList<>();
        } else {
            barChart.getData().clear();
        }
        series.getData().clear();
        datas.clear();

        for (int i = 0; i < settori.size(); i++) {
            datas.add(new XYChart.Data<>(nomeSettori.get(i), settori.get(nomeSettori.get(i))));
        }

        for (XYChart.Data<String, Number> data : datas) {
            setRandomColor(data);
            series.getData().add(data);
        }
        barChart.getData().add(series);
    }

    /**
     * aggiornamento del grafico utilizzando il model passato per parametro
     *
     * @param barChartModel model per il prelievo dei dati
     */
    private void updateChart(BarChartModel barChartModel) {
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
            datas.add(new XYChart.Data<>(locationNames.get(i), soldPerLocation.get(i)));
        }
        for (XYChart.Data<String, Number> data : datas) {
            setRandomColor(data);

            series.getData().add(data);
        }
        barChart.getData().add(series);
    }

    /**
     * aggiornamento del grafico utilizzando il model passato per parametro
     *
     * @param mergedModel model per il prelievo dei dati
     */
    private void updateChart(MergedModel mergedModel) {
        List<String> eventNames = mergedModel.getEventNames();
        List<Double> soldPerEvent = mergedModel.getSoldPerEvent();
        if (barChart.getData().isEmpty()) {
            datas = new ArrayList<>();
        } else {
            barChart.getData().clear();
        }
        series.getData().clear();
        try {
            datas.clear();
        } catch (NullPointerException ignored) {
        }

        int repetitions = 1;
        for (int i = 0; i < eventNames.size(); i++) {
            boolean b = false;
            for (int j = 0; j < datas.size(); j++) {
                if (datas.get(j).getXValue().equals(eventNames.get(i))) {
                    datas.add(new XYChart.Data<>(eventNames.get(i) + " (" + repetitions + ")", soldPerEvent.get(i)));
                    repetitions++;
                    b = true;
                    break;
                }
            }
            if (!b) {
                datas.add(new XYChart.Data<>(eventNames.get(i), soldPerEvent.get(i)));
            }

        }
        for (XYChart.Data<String, Number> data : datas) {
            setRandomColor(data);

            series.getData().add(data);
        }
        barChart.getData().add(series);
    }
}
