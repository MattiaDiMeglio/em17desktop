package view;

import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;
import view.chartsViews.BarChartClass;
import view.chartsViews.LineChartClass;
import view.chartsViews.PieChartClass;

import java.util.Observable;
import java.util.Observer;

/**
 * Classe View per la schermata Evento
 * <p>
 * Implementa Observer, come definito dall'architettura MVC implementata per il progetto
 */
public class EventView implements Observer {
    public EventView(TabPane eventoTabPane, int index) {

        initializeCharts(eventoTabPane, index);
    }

    private void initializeCharts(TabPane tabPane, int index){
        VBox eventoVboxLinechart = (VBox) tabPane.getTabs().get(0).getContent();
        LineChart lineChart = (LineChart) eventoVboxLinechart.getChildren().get(0);

        VBox eventoVboxPieChart = (VBox) tabPane.getTabs().get(1).getContent();
        PieChart pieChart = (PieChart) eventoVboxPieChart.getChildren().get(0);

        VBox eventoVboxBarChart = (VBox) tabPane.getTabs().get(2).getContent();
        BarChart barChart = (BarChart) eventoVboxBarChart.getChildren().get(0);

        new BarChartClass(barChart, index);
        new PieChartClass(pieChart, index);
        new LineChartClass(lineChart, index);
    }

    @Override
    public void update(Observable o, Object arg) {

    }
}
