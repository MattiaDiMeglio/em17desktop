package view;

import controller.chartsController.MergedController;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import model.EventModel;
import view.chartsViews.BarChartView;
import view.chartsViews.LineChartView;
import view.chartsViews.PieChartView;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Classe View per la schermata Event List
 * <p>
 * Implementa Observer, come definito dall'architettura MVC implementata per il progetto
 */
public class EventListView implements Observer {

    /**
     * variabile per la gestione dei dati visualizzati nei chart
     */
    private MergedController mergedController;
    private VBox foundedElementsVBox;

    public EventListView(TabPane eventListTabPane, List<EventModel> foundedEventInSearch, VBox eventListViewVBox) {
        this.foundedElementsVBox = eventListViewVBox;
        initializeCharts(eventListTabPane); // inizializzo i grafici
        createHbox(foundedEventInSearch);


        /*for (EventModel event :foundedEventInSearch){
            System.out.println(event.getEventName());
        }*/
    }

    private void createHbox(List<EventModel> foundedEventInSearch) {
        for (EventModel eventModel : foundedEventInSearch) {
            HBox hBox = new HBox();
            hBox.setSpacing(2);
            CheckBox checkBox = new CheckBox();
            checkBox.setTranslateY(80);
            Button button = new Button();
            ImageView imageView = new ImageView(eventModel.getBillboard());
            imageView.setFitHeight(200);
            imageView.setFitWidth(180);
            imageView.setPickOnBounds(true);
            //imageView.preserveRatioProperty().setValue(true);
            button.setGraphic(imageView);

            VBox vBox = new VBox();
            Label eventName = new Label();
            eventName.translateYProperty().setValue(20);
            eventName.setTextFill(Paint.valueOf("blue"));
            eventName.underlineProperty().setValue(true);
            eventName.fontProperty().setValue(new Font(20));
            eventName.setText(eventModel.getEventName());
            Label locationName = new Label();
            locationName.translateYProperty().setValue(40);
            locationName.setText(eventModel.getLocationName());
            Label date = new Label();
            date.translateYProperty().setValue(40);
            date.setText(eventModel.getStartingDate());

            HBox modifyAndDelete = new HBox();
            modifyAndDelete.setSpacing(20);
            Label modify = new Label();
            modify.setTranslateY(50);
            modify.setTextFill(Paint.valueOf("blue"));
            modify.underlineProperty().setValue(true);
            modify.setText("Modifica");
            Label delete = new Label();
            delete.setTranslateY(50);
            delete.setTextFill(Paint.valueOf("blue"));
            delete.underlineProperty().setValue(true);
            delete.setText("Cancella");

            modifyAndDelete.getChildren().addAll(modify, delete);
            vBox.getChildren().addAll(eventName, locationName, date, modifyAndDelete);
            hBox.getChildren().addAll(checkBox, button, vBox);

            foundedElementsVBox.getChildren().add(hBox);
        }
    }

    /**
     * si occupa dell'inizializzazione dei grafici
     *
     * @param tabPane TabPane contenente i grafici.
     */
    private void initializeCharts(TabPane tabPane) {
        mergedController = new MergedController();
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
     * @param arg metodo update ereditato da Observer
     * @see java.util.Observer
     * @see java.util.Observable
     */
    public void update(Observable o, Object arg) {

    }
}
