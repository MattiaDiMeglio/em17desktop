package view;

import controller.SearchController;
import controller.SlideShowController;
import controller.ViewSourceController;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.StackedAreaChart;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.EventListModel;
import model.EventModel;
import org.controlsfx.control.textfield.TextFields;
import view.chartsViews.BarChartView;
import view.chartsViews.LineChartView;
import view.chartsViews.PieChartView;


import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

/**
 * Classe view per la schermata DashBoard, nonché main dell'applicativo.
 * Implementa Observer, come definito dall'architettura MVC implementata per il progetto
 * Estende Application per poter utilizzare JavaFX
 *
 * @author ingSW20
 * @see java.util.Observable
 * @see java.util.Observer
 * @see javafx.application.Application
 */
public class DashBoardView implements Observer {

    private ViewSourceController viewSourceController;
    private SearchController searchController;
    private List<EventModel> foundedEventInSearch;

    public DashBoardView(HBox dashSlide, Button dashBoardSlideShowLeftButton, Button dashBoardSlideShowRightButton,
                         TabPane dashBoardTabPane, Button dashBoardInsertButton,
                         ViewSourceController viewSourceController, ToolBar searchToolBar) {
        this.viewSourceController = viewSourceController;
        initalizeCharts(dashBoardTabPane);
        initalizeSearch(searchToolBar);
        EventListModel eventListModel = EventListModel.getInstance();
        eventListModel.addObserver(this);

        while (dashSlide.getChildren().size() > 0) {
            dashSlide.getChildren().remove(dashSlide.getChildren().size() - 1);
        }

        SlideShowController slideShowController = new SlideShowController();
        slideShowController.createSlide(dashSlide, dashBoardSlideShowLeftButton, dashBoardSlideShowRightButton, viewSourceController);
        dashBoardInsertButton.setOnAction(event -> {
            viewSourceController.toInsertView();
        });
    }

    private void initalizeSearch(ToolBar toolBar) {
        searchController = new SearchController();
        TextField textField = (TextField) toolBar.getItems().get(0);
        textField.setOnMouseClicked(event -> {
            List<String> eventsName = searchController.getEventsName();
            TextFields.bindAutoCompletion(textField, eventsName);
        });

        Button button = (Button) toolBar.getItems().get(1);
        button.setOnAction(event -> {
            foundedEventInSearch = searchController.search(textField.getText());
            viewSourceController.toEventListView(foundedEventInSearch);
        });
        //bottone settato a defalt, per essere premuto con invio
        button.setDefaultButton(true);
    }

    private void initalizeCharts(TabPane tabPane) {
        VBox dashBoardVboxLinechart = (VBox) tabPane.getTabs().get(0).getContent();
        ComboBox comboBox1 = (ComboBox) dashBoardVboxLinechart.getChildren().get(0);
        LineChart lineChart = (LineChart) dashBoardVboxLinechart.getChildren().get(1);

        VBox dashBoardVboxPieChart = (VBox) tabPane.getTabs().get(1).getContent();
        ComboBox comboBox2 = (ComboBox) dashBoardVboxPieChart.getChildren().get(0);
        PieChart pieChart = (PieChart) dashBoardVboxPieChart.getChildren().get(1);

        VBox dashBoardVboxBarChart = (VBox) tabPane.getTabs().get(2).getContent();
        ComboBox comboBox3 = (ComboBox) dashBoardVboxBarChart.getChildren().get(0);
        BarChart barChart = (BarChart) dashBoardVboxBarChart.getChildren().get(1);

        VBox dashBoardVboxLineChartSales = (VBox) tabPane.getTabs().get(3).getContent();
        ComboBox comboBox4 = (ComboBox) dashBoardVboxLineChartSales.getChildren().get(0);
        StackedAreaChart stackedAreaChart = (StackedAreaChart) dashBoardVboxLineChartSales.getChildren().get(1);

        Date date = new Date();
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        int year = localDate.getYear();

        int year1 = year - 4;
        int year2 = year - 3;
        int year3 = year - 2;
        int year4 = year - 1;

        comboBox1.getItems().setAll(year, year4, year3, year2, year1);
        comboBox1.getSelectionModel().selectFirst();

        comboBox2.getItems().setAll(year, year4, year3, year2, year1);
        comboBox2.getSelectionModel().selectFirst();

        comboBox3.getItems().setAll(year, year4, year3, year2, year1);
        comboBox3.getSelectionModel().selectFirst();

        comboBox4.getItems().setAll(year, year4, year3, year2, year1);
        comboBox4.getSelectionModel().selectFirst();

        new PieChartView(pieChart, comboBox2);
        new LineChartView(lineChart, comboBox1);
        new BarChartView(barChart, comboBox3);
        new LineChartView(stackedAreaChart, comboBox4);

    }


    /**
     * metodo update ereditato da Observer
     *
     * @param o
     * @param arg
     * @see java.util.Observer
     * @see java.util.Observable
     */
    public void update(Observable o, Object arg) {

    }


}
