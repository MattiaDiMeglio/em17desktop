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
import model.chartsModels.PieChartClassModel;

import java.util.Observable;
import java.util.Observer;

/**
 * this class is responsible for creating and managing the Piechart dedicated for memory storage archive.
 * This Piechart has only 2 slice: one for used space and another for unused space of file system
 *
 * @author Andrea Bravaccino
 */
public class PieChartClass implements Observer {
    /**
     * a slice of the "cake"
     */
    private PieChart.Data slice1;
    /**
     * second slice of "cake"
     */
    private PieChart.Data slice2;


    /**
     * the constructor takes care of initializing two slices (used and unused space) and set
     * proprieties of chart (legend visibility, animations and sizes
     *
     * @param pieChart is the type of chart for cpu load
     * @param dashboardYearComboBox2
     */
    public PieChartClass(PieChart pieChart, ComboBox dashboardYearComboBox2) {

        slice1 = new PieChart.Data("Biglietti venduti", 0);
        slice2 = new PieChart.Data("Biglietti non venduti", 0);

        pieChart.setTitle("Vendita biglietti 2018");
        pieChart.getData().add(slice1);
        pieChart.getData().add(slice2);
        //pieChart.setPrefSize(400, 300);
        pieChart.animatedProperty().setValue(false);
        pieChart.legendVisibleProperty().setValue(false);

        dashboardYearComboBox2.valueProperty().addListener(new ChangeListener<Integer>() {
            @Override
            public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
                ChartsController.populateCharts(String.valueOf(newValue));
            }

           /* @Override public void changed(ObservableValue ov, String t, String t1) {
                System.out.println(ov);
                System.out.println(t);
                System.out.println(t1);
            }*/
        });
        PieChartClassModel.getInstance().addObserver(this);
    }

    @Override
    public void update(Observable o, Object arg) {
        Double ticketsPerc = (PieChartClassModel.getInstance().getTicketsSold() / PieChartClassModel.getInstance().getMaxTickets()) * 100;
        System.out.println(ticketsPerc);
        slice1.setName(round(ticketsPerc,2) + "%\nBiglietti Venduti");
        slice1.setPieValue(ticketsPerc);
        slice2.setName((100 - round(ticketsPerc,2) + "%\nBiglietti non Venduti"));
        slice2.setPieValue(100 - ticketsPerc);

        slice1.getNode().setStyle("-fx-pie-color: #bbdefb");
        slice2.getNode().setStyle("-fx-pie-color: #78909c");
    }

    private double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }
}
