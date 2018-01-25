package view;

import controller.SlideShowController;
import controller.ViewSourceController;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;
import model.EventListModel;
import view.chartsViews.LineChartClass;
import view.chartsViews.PieChartClass;


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



    public DashBoardView(HBox dashSlide, PieChart dashBoardGraph2PieChart, ComboBox dashBoardYearComboBox2, LineChart dashBoardGraph1LineChart, ComboBox dashboardYearComboBox1, ViewSourceController viewSourceController){
        this.viewSourceController=viewSourceController;
        eventListModel.addObserver(this);
        new PieChartClass(dashBoardGraph2PieChart, dashBoardYearComboBox2);
        new LineChartClass(dashBoardGraph1LineChart, dashboardYearComboBox1);
        this.dashSlide = dashSlide;
        System.out.println("nono");


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

        slideShowController.createList(dashSlide, eventListModel.getListaEventi().get((int) arg).getLocandina(), (int) arg);
    }


}
