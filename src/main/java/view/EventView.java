package view;

import controller.EventController;
import controller.SlideShowController;
import controller.ViewSourceController;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.StackedAreaChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import model.EventListModel;
import model.EventModel;
import view.chartsViews.BarChartClass;
import view.chartsViews.LineChartClass;
import view.chartsViews.PieChartClass;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Classe View per la schermata Evento
 * <p>
 * Implementa Observer, come definito dall'architettura MVC implementata per il progetto
 */
public class EventView implements Observer {
    EventListModel eventListModel = EventListModel.getInstance();
    EventModel eventModel;
    Boolean ret;

    public EventView(EventController eventController, Button eventoDeleteButton, ImageView eventPlaybillImageView,
                     TabPane eventoTabPane, int index, List<Text> texts, Label eventoTitleLabel,
                     TextArea eventTextArea, HBox eventSlide, Button eventSlideShowLeftButton, Button eventSlideShowRightButton,
                     ViewSourceController viewSourceController) {
        SlideShowController slideShowController = new SlideShowController();
        eventModel = eventListModel.getListaEventi().get(index);
        eventModel.addObserver(this);
        initializeCharts(eventoTabPane, index);
        Image image= new Image(eventModel.getLocandina());
        eventPlaybillImageView.setImage(image);
        eventoTitleLabel.setText(eventModel.getNomeEvento());
        eventTextArea.setText(eventModel.getDescrizione());
        texts.get(0).setText(eventModel.getNomeLocation());
        texts.get(1).setText(eventModel.getDataInizio().toString());
        texts.get(2).setText(eventModel.getDataFine().toString());
        texts.get(3).setText("prezzo");
        texts.get(4).setText(eventModel.getMaxVisitatori().toString());
        texts.get(5).setText(eventModel.getTicketSold().toString());
        slideShowController.createSlide(eventSlide, eventSlideShowLeftButton, eventSlideShowRightButton, viewSourceController);
        eventoDeleteButton.setOnAction(event -> {
            if (eventModel.getTicketSold() == 0){
                //ret = eventoDeleteButton.delete(eventModel.getEventKey());
            }
        });
    }

    private void initializeCharts(TabPane tabPane, int index){
        VBox eventoVboxLinechart = (VBox) tabPane.getTabs().get(0).getContent();
        LineChart lineChart = (LineChart) eventoVboxLinechart.getChildren().get(0);

        VBox eventoVboxPieChart = (VBox) tabPane.getTabs().get(1).getContent();
        PieChart pieChart = (PieChart) eventoVboxPieChart.getChildren().get(0);

        VBox eventoVboxBarChart = (VBox) tabPane.getTabs().get(2).getContent();
        BarChart barChart = (BarChart) eventoVboxBarChart.getChildren().get(0);

        VBox eventoVboxStackedAreaChart = (VBox) tabPane.getTabs().get(3).getContent();
        StackedAreaChart stackedAreaChart = (StackedAreaChart) eventoVboxStackedAreaChart.getChildren().get(0);

        new BarChartClass(barChart, index);
        new PieChartClass(pieChart, index);
        new LineChartClass(lineChart, index);
        new LineChartClass(stackedAreaChart, index);
    }

    @Override
    public void update(Observable o, Object arg) {

    }
}
