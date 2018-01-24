package view;

import controller.DBController;
import controller.ViewSourceController;
import controller.chartsController.ChartsController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import model.EventListModel;
import model.EventModel;
import view.chartsViews.LineChartClass;
import view.chartsViews.PieChartClass;

import java.io.IOException;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ExecutionException;

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
    GridPane slideshowImmagine;
    EventListModel eventListModel = EventListModel.getInstance();
    int i = 0;


    private class DashBillBoard{
        Button button;
        private int index;
        private String url;
    }

    public DashBoardView(GridPane slideshowImmagine, PieChart dashBoardGraph2PieChart, ComboBox dashBoardYearComboBox2, LineChart dashBoardGraph1LineChart, ComboBox dashboardYearComboBox1, ViewSourceController viewSourceController){
        this.viewSourceController=viewSourceController;
        eventListModel.addObserver(this);
        this.slideshowImmagine = slideshowImmagine;
        new PieChartClass(dashBoardGraph2PieChart, dashBoardYearComboBox2);
        new LineChartClass(dashBoardGraph1LineChart, dashboardYearComboBox1);

        System.out.println("nono");


    }


    private void slideShow(){

        DashBillBoard dashBillBoard = new DashBillBoard();
        dashBillBoard.button = new Button();
        dashBillBoard.button.setGraphic(new ImageView(eventListModel.getListaEventi().get(i).getLocandina()));
        viewSourceController.createSlide(i, dashBillBoard.button);
        i++;
        System.out.println("creato");

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
        slideShow();
    }


}
