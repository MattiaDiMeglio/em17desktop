package view;

import controller.chartsController.MergedController;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;
import model.EventListModel;
import model.EventModel;
import view.chartsViews.BarChartView;
import view.chartsViews.LineChartView;
import view.chartsViews.PieChartView;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Classe View per la schermata Event List
 *
 * Implementa Observer, come definito dall'architettura MVC implementata per il progetto
 */
public class EventListView  implements Observer {

    public EventListView(TabPane eventListTabPane){
        initializeCharts(eventListTabPane);
        List<EventModel> eventModels = new ArrayList<>();
        eventModels.add(EventListModel.getInstance().getListaEventi().get(0));
        eventModels.add(EventListModel.getInstance().getListaEventi().get(1));
        new MergedController(eventModels);
    }

    private void initializeCharts(TabPane tabPane) {
        VBox vboxLinechart = (VBox) tabPane.getTabs().get(0).getContent();
        LineChart lineChart = (LineChart) vboxLinechart.getChildren().get(0);

        VBox vboxPieChart = (VBox) tabPane.getTabs().get(1).getContent();
        PieChart pieChart = (PieChart) vboxPieChart.getChildren().get(0);

        VBox vboxBarChart = (VBox) tabPane.getTabs().get(2).getContent();
        BarChart barChart = (BarChart) vboxBarChart.getChildren().get(0);

        new LineChartView(lineChart);
        new PieChartView(pieChart);
        new BarChartView(barChart);

    }

    /**
     * @param o
     * @param arg
     *
     * metodo update ereditato da Observer
     * @see java.util.Observer
     * @see java.util.Observable
     */
    public void update(Observable o, Object arg) {

    }
}
