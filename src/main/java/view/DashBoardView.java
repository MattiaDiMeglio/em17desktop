package view;

import controller.SlideShowController;
import controller.ViewSourceController;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.StackedAreaChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TabPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.EventListModel;
import view.chartsViews.BarChartClass;
import view.chartsViews.LineChartClass;
import view.chartsViews.PieChartClass;


import javax.sound.sampled.Line;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;

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

    ViewSourceController viewSourceController;
    EventListModel eventListModel = EventListModel.getInstance();
    SlideShowController slideShowController = new SlideShowController();
    HBox dashSlide;
    int i = 0;


    public DashBoardView(HBox dashSlide, Button dashBoardSlideShowLeftButton, Button dashBoardSlideShowRightButton, TabPane dashBoardTabPane, ViewSourceController viewSourceController) {
        this.viewSourceController = viewSourceController;
        initalizeCharts(dashBoardTabPane);
        eventListModel.addObserver(this);

        this.dashSlide = dashSlide;
        slideShowController.createSlide(dashSlide, dashBoardSlideShowLeftButton, dashBoardSlideShowRightButton, viewSourceController);
        System.out.println("nono");
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

        new PieChartClass(pieChart, comboBox2);
        new LineChartClass(lineChart, comboBox1);
        new BarChartClass(barChart,comboBox3);
        new LineChartClass(stackedAreaChart, comboBox4);

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
        //slideShow();

        //slideShowController.createList(dashSlide, eventListModel.getListaEventi().get((int) arg).getLocandina(), (int) arg);
    }


}
