package view.chartsViews;

import controller.chartsController.ChartsController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.chart.PieChart;
import javafx.scene.control.ComboBox;
import model.EventListModel;
import model.EventModel;
import model.chartsModels.MergedModel;
import model.chartsModels.PieChartModel;
import view.LoadingPopupView;

import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.CountDownLatch;

/**
 * Questa classe rappresenta la view corrispondente al grafico {@link javafx.scene.chart.PieChart PieChart}.
 * L'aggiornamento della view è eseguita secondo i canoni del design pattern MVC
 *
 * @author ingsw20
 */
public class PieChartView implements Observer, ChartInterface {

    /**
     * variabile per la gestione del grafico
     */
    private PieChart pieChart;

    /**
     * il construttore inizializza {@link #pieChart} per la gestione del grafico,
     * inoltre registra la view presso il model.
     * In particolare, questo costruttore è utilizzato per la visualizzazione delle statistiche relativo ad un anno scelto.
     *
     * @param pieChart tipo di grafico da gestire
     * @param dashboardYearComboBox2 combobox per scegliere l'anno
     */
    public PieChartView(PieChart pieChart, ComboBox dashboardYearComboBox2) {

        this.pieChart = pieChart;
        initializeCharts();

        dashboardYearComboBox2.valueProperty().addListener(new ChangeListener<Integer>() {
            @Override
            public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
                CountDownLatch latch = new CountDownLatch(1);
                ChartsController.getInstance().populateCharts(String.valueOf(newValue), latch);
                new LoadingPopupView(latch);
            }
        });
        PieChartModel.getInstance().addObserver(this);
    }

    /**
     * Costruttore per l'inizializzazione del LineChart relativo ad un singolo evento, inoltre registra la view presso il model.
     *
     * @param pieChart tipo di grafico
     * @param index     indice da utilizzare per identificare l'evento desiderato
     */
    public PieChartView(PieChart pieChart, int index) {
        this.pieChart = pieChart;
        initializeCharts();
        EventListModel.getInstance().getListaEventi().get(index).addObserver(this);
        update(EventListModel.getInstance().getListaEventi().get(index), null);
    }

    /**
     * costruttore per il grafico contenente i dati di più eventi
     *
     * @param pieChart tipo di grafico da gestire
     */
    public PieChartView(PieChart pieChart) {
        this.pieChart = pieChart;
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
        if (o instanceof PieChartModel) {
            System.out.println("dash");
            PieChartModel classModel = (PieChartModel) o;
            Double ticketsPerc = (classModel.getTicketsSold() / classModel.getMaxTickets()) * 100;
            updateChart(ticketsPerc);
        } else if (o instanceof  EventModel){
            System.out.println("event");
            EventModel eventModel = (EventModel) o;
            Double ticketsPerc = (eventModel.getTicketSold() / eventModel.getMaxVisitors()) * 100;
            updateChart(ticketsPerc);
        }else {
            MergedModel mergedModel = (MergedModel) o;
            Double ticketsPerc = (mergedModel.getTicketsSold() / mergedModel.getMaxVisitors()) * 100;
            updateChart(ticketsPerc);
        }
    }

    /**
     * metodo per troncare un double ad un numero con cifre decimali scelto
     * @param value valore da arrotondare
     * @param places numero di cifre decimali scelto
     * @return valore arrotondato
     */
    private double round(double value, int places) {
        if (places > 0) {
            long factor = (long) Math.pow(10, places);
            value = value * factor;
            long tmp = Math.round(value);
            return (double) tmp / factor;
        }
        return value;
    }

    /**
     * metodo per l'inizializzazione del grafico
     */
    @Override
    public void initializeCharts() {
        if (pieChart.getData().isEmpty()) {
            PieChart.Data slice1 = new PieChart.Data("Biglietti venduti", 0);
            PieChart.Data slice2 = new PieChart.Data("Biglietti non venduti", 0);
            pieChart.getData().add(slice1);
            pieChart.getData().add(slice2);
        }
        pieChart.setTitle("Vendita biglietti");
        pieChart.animatedProperty().setValue(false);
        pieChart.legendVisibleProperty().setValue(false);
    }

    /**
     * metodo per l'aggiornamento del grafico in base al valore passato per parametro
     * @param ticketsPerc percentuale da rappresentare
     */
    private void updateChart(Double ticketsPerc){
        pieChart.getData().get(0).setName(round(ticketsPerc, 2) + "%\nBiglietti Venduti");
        pieChart.getData().get(0).setPieValue(ticketsPerc);
        pieChart.getData().get(1).setName((100 - round(ticketsPerc, 2) + "%\nBiglietti non Venduti"));
        pieChart.getData().get(1).setPieValue(100 - ticketsPerc);

        pieChart.getData().get(0).getNode().setStyle("-fx-pie-color: #bbdefb");
        pieChart.getData().get(1).getNode().setStyle("-fx-pie-color: #78909c");
    }
}
