package view;

import controller.SlideShowController;
import controller.ViewSourceController;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TabPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.EventListModel;
import view.chartsViews.LineChartClass;
import view.chartsViews.PieChartClass;


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
 *@see java.util.Observable
 * @see java.util.Observer
 * @see javafx.application.Application
 *
 * @author ingSW20
 */
public  class  DashBoardView implements Observer {

    ViewSourceController viewSourceController;
    EventListModel eventListModel = EventListModel.getInstance();
    SlideShowController slideShowController = new SlideShowController();
    HBox dashSlide;
    int i = 0;



    public DashBoardView(HBox dashSlide, TabPane dashBoardTabPane, ViewSourceController viewSourceController){
        this.viewSourceController=viewSourceController;
        initalizeCharts(dashBoardTabPane);
        eventListModel.addObserver(this);

        this.dashSlide = dashSlide;
        slideShowController.createSlide(dashSlide, viewSourceController);
        System.out.println("nono");
    }

    private void initalizeCharts(TabPane tabPane){
        VBox dashBoardVboxLinechart = (VBox) tabPane.getTabs().get(0).getContent();
        ComboBox comboBox1 = (ComboBox) dashBoardVboxLinechart.getChildren().get(0);
        LineChart lineChart = (LineChart) dashBoardVboxLinechart.getChildren().get(1);


        VBox dashBoardVboxPieChart = (VBox) tabPane.getTabs().get(1).getContent();
        ComboBox comboBox2 = (ComboBox) dashBoardVboxPieChart.getChildren().get(0);
        PieChart pieChart = (PieChart) dashBoardVboxPieChart.getChildren().get(1);


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

        new PieChartClass(pieChart, comboBox2);
        new LineChartClass(lineChart, comboBox1);

    }




    /**
     * metodo update ereditato da Observer
     * @see java.util.Observer
     * @see java.util.Observable
     *
     * @param o
     * @param arg
     *
     */
    public void update(Observable o, Object arg) {
        //slideShow();

        //slideShowController.createList(dashSlide, eventListModel.getListaEventi().get((int) arg).getLocandina(), (int) arg);
    }


}
