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
import javafx.scene.chart.PieChart;
import javafx.scene.control.ComboBox;
import model.EventListModel;
import model.EventModel;
import model.chartsModels.PieChartClassModel;

import java.util.Observable;
import java.util.Observer;

/**
 * this class is responsible for creating and managing the Piechart dedicated for memory storage archive.
 * This Piechart has only 2 slice: one for used space and another for unused space of file system
 *
 * @author Andrea Bravaccino
 */
public class PieChartClass implements Observer, ChartInterface {
    /**
     * a slice of the "cake"
     */
    private PieChart.Data slice1;
    /**
     * second slice of "cake"
     */
    private PieChart.Data slice2;

    private PieChart pieChart;

    /**
     * the constructor takes care of initializing two slices (used and unused space) and set
     * proprieties of chart (legend visibility, animations and sizes
     *
     * @param pieChart               is the type of chart for cpu load
     * @param dashboardYearComboBox2
     */
    public PieChartClass(PieChart pieChart, ComboBox dashboardYearComboBox2) {

        this.pieChart = pieChart;
        initializeCharts();

        dashboardYearComboBox2.valueProperty().addListener(new ChangeListener<Integer>() {
            @Override
            public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
                ChartsController.populateCharts(String.valueOf(newValue));
            }
        });
        PieChartClassModel.getInstance().addObserver(this);
    }

    public PieChartClass(PieChart pieChart, int index) {
        this.pieChart = pieChart;
        initializeCharts();

        EventListModel.getInstance().getListaEventi().get(index).addObserver(this);

        update(EventListModel.getInstance().getListaEventi().get(index), null);
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof PieChartClassModel) {
            System.out.println("dash");
            Double ticketsPerc = (PieChartClassModel.getInstance().getTicketsSold() / PieChartClassModel.getInstance().getMaxTickets()) * 100;
            slice1.setName(round(ticketsPerc, 2) + "%\nBiglietti Venduti");
            slice1.setPieValue(ticketsPerc);
            slice2.setName((100 - round(ticketsPerc, 2) + "%\nBiglietti non Venduti"));
            slice2.setPieValue(100 - ticketsPerc);
            slice1.getNode().setStyle("-fx-pie-color: #bbdefb");
            slice2.getNode().setStyle("-fx-pie-color: #78909c");
        } else {
            System.out.println("event");
            EventModel eventModel = (EventModel) o;
            Double ticketsPerc = (eventModel.getTicketSold() / eventModel.getMaxVisitatori()) * 100;
            pieChart.getData().get(0).setName(round(ticketsPerc, 2) + "%\nBiglietti Venduti");
            pieChart.getData().get(0).setPieValue(ticketsPerc);
            pieChart.getData().get(1).setName((100 - round(ticketsPerc, 2) + "%\nBiglietti non Venduti"));
            pieChart.getData().get(1).setPieValue(100 - ticketsPerc);

            pieChart.getData().get(0).getNode().setStyle("-fx-pie-color: #bbdefb");
            pieChart.getData().get(1).getNode().setStyle("-fx-pie-color: #78909c");
        }
    }

    private double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    @Override
    public void initializeCharts() {
        if (pieChart.getData().isEmpty()) {
            slice1 = new PieChart.Data("Biglietti venduti", 0);
            slice2 = new PieChart.Data("Biglietti non venduti", 0);
            pieChart.getData().add(slice1);
            pieChart.getData().add(slice2);
        }
        pieChart.setTitle("Vendita biglietti");
        pieChart.animatedProperty().setValue(false);
        pieChart.legendVisibleProperty().setValue(false);
    }
}
