package view;

import controller.SearchController;
import controller.ViewSourceController;
import controller.chartsController.MergedController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
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
    private SearchController searchController;
    private List<EventModel> foundedEventInSearch;
    private ViewSourceController viewSourceController;

    public EventListView(TabPane eventListTabPane, List<EventModel> foundedEventInSearch, VBox eventListViewVBox,
                         ToolBar searchToolBarEventListView, ViewSourceController viewSourceController) {
        this.foundedElementsVBox = eventListViewVBox;
        this.foundedEventInSearch = foundedEventInSearch;
        this.viewSourceController = viewSourceController;
        initalizeSearch(searchToolBarEventListView);
        initializeCharts(eventListTabPane); // inizializzo i grafici
        createHbox();

    }

    private void initalizeSearch(ToolBar toolBar) {
        searchController = new SearchController();
        TextField textField = (TextField) toolBar.getItems().get(0);
        Button button = (Button) toolBar.getItems().get(1);
        button.setOnAction(event -> {
            resetSearch();
            mergedController.resetModel();
            foundedEventInSearch.addAll(searchController.search(textField.getText()));
            if (foundedEventInSearch.isEmpty()){
                System.out.println("non è stato trovato niente");
            }else {
                createHbox();
            }
        });
    }

    private void resetSearch(){
        foundedEventInSearch.clear();

        foundedElementsVBox.getChildren().clear();
    }

    private void createHbox() {
        for (EventModel eventModel : foundedEventInSearch) {
            HBox hBox = new HBox();
            hBox.setSpacing(20);
            CheckBox checkBox = new CheckBox();
            checkBox.setTranslateY(80);
            checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue) {
                    mergedController.addEventToList(eventModel);
                    hBox.setStyle("-fx-background-color: darkgrey");
                }else {
                    mergedController.removeEventFromList(eventModel);
                    hBox.setStyle("-fx-background-color: inherit");
                }
            });
            Button button = new Button();
            ImageView imageView = new ImageView(eventModel.getBillboard());
            imageView.setFitHeight(200);
            imageView.setFitWidth(180);
            imageView.setPickOnBounds(true);
            button.setGraphic(imageView);
            button.setOnAction(event -> {
                viewSourceController.toEventView(eventModel.getIndex());
            });

            VBox vBox = new VBox();
            Label eventName = new Label();
            eventName.translateYProperty().setValue(20);
            eventName.setTextFill(Paint.valueOf("blue"));
            eventName.underlineProperty().setValue(true);
            eventName.fontProperty().setValue(new Font(20));
            eventName.setText(eventModel.getEventName());
            eventName.setOnMouseClicked(event -> {
                viewSourceController.toEventView(eventModel.getIndex());
            });
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
