package view;

import controller.SearchController;
import controller.ViewSourceController;
import controller.chartsController.MergedController;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import model.EventModel;
import org.controlsfx.control.textfield.TextFields;
import view.chartsViews.BarChartView;
import view.chartsViews.LineChartView;
import view.chartsViews.PieChartView;

import java.util.*;

/**
 * Classe View per la schermata Event List
 * <p>
 * Implementa Observer, come definito dall'architettura MVC implementata per il progetto
 */
public class EventListView {

    /**
     * Label mostrata in caso di risultati non trovati
     */
    private Label notFoundLabel;

    /**
     * variabile per la gestione dei dati visualizzati nei chart
     */
    private MergedController mergedController;

    /**
     * VBox contenente i risultati della ricerca
     */
    private VBox foundedElementsVBox;

    /**
     * controller per la ricerca
     */
    private SearchController searchController;

    /**
     * Lista di eventi trovati
     */
    private List<EventModel> foundedEventInSearch;

    /**
     * istanza di viewSourceController utile per il passaggio da una view all'altra
     */
    private ViewSourceController viewSourceController;

    /**
     * lista contenente le checkbox relative agli elementi trovati
     */
    private List<CheckBox> checkBoxList = new ArrayList<>();

    /**
     * checkbox per selezionare tutti gli elementi trovati
     */
    private CheckBox selectAllCheckBox;

    /**
     * hasmap per la memorizzazione degli elementi selezionati
     */
    private HashMap<Integer, HBox> selectedItems = new HashMap<>();

    /**
     * ObservableList per il popolamento della tabella
     */
    private ObservableList<EventTable> data = FXCollections.observableArrayList();

    /**
     * costruttore per inizializzare la classe
     * @param eventListTabPane TabPane contenente i grafici
     * @param foundedEventInSearch elementi trovati
     * @param eventListViewVBox VBox popolata con i risultati della ricerca
     * @param searchToolBarEventListView ToolBar con gli elementi per la ricerca
     * @param viewSourceController istanza di {@link ViewSourceController} per il cambio di view
     */
    public EventListView(TabPane eventListTabPane, List<EventModel> foundedEventInSearch, VBox eventListViewVBox,
                         ToolBar searchToolBarEventListView, ViewSourceController viewSourceController) {
        this.foundedElementsVBox = eventListViewVBox;
        this.foundedEventInSearch = foundedEventInSearch;
        this.viewSourceController = viewSourceController;

        initalizeSearch(searchToolBarEventListView); // inizializzo la barra di ricerca
        initializeCharts(eventListTabPane); // inizializzo i grafici
        initalizeTableView(eventListTabPane); // inizializzo la tabella

        if (foundedEventInSearch.isEmpty()) {
            foundedElementsVBox.getChildren().add(notFoundLabel);
        } else {
            createResult();
        }
    }

    /**
     * inizializza la tabella che verrà visualizzata nel TabPane
     * @param tabPane TabPane che conterrà la tabella
     */
    private void initalizeTableView(TabPane tabPane) {

        TableView<EventTable> table = new TableView<>();
        table.setEditable(true);

        TableColumn<EventTable, String> eventName = new TableColumn<>("Nome evento");
        eventName.setCellValueFactory(new PropertyValueFactory<EventTable, String>("eventName"));
        TableColumn<EventTable, Integer> ticketsSold = new TableColumn<>("Biglietti venduti");
        ticketsSold.setCellValueFactory(new PropertyValueFactory<EventTable, Integer>("ticketsSold"));
        TableColumn<EventTable, Integer> revenues = new TableColumn<>("Ricavi in \u20ac");
        revenues.setCellValueFactory(new PropertyValueFactory<EventTable, Integer>("revenues"));
        TableColumn<EventTable, Integer> maxVisitors = new TableColumn<>("Max spettatori");
        maxVisitors.setCellValueFactory(new PropertyValueFactory<EventTable, Integer>("maxVisitors"));
        TableColumn<EventTable, String> locationName = new TableColumn<>("Nome location");
        locationName.setCellValueFactory(new PropertyValueFactory<EventTable, String>("locationName"));
        TableColumn<EventTable, String> date = new TableColumn<>("Data");
        date.setCellValueFactory(new PropertyValueFactory<EventTable, String>("date"));
        TableColumn<EventTable, String> isActive = new TableColumn<>("In corso");
        isActive.setCellValueFactory(new PropertyValueFactory<EventTable, String>("isActive"));

        table.setItems(data);
        table.getColumns().addAll(eventName, ticketsSold, revenues, maxVisitors, locationName, date, isActive);

        eventName.prefWidthProperty().bind(table.widthProperty().divide(5));
        ticketsSold.prefWidthProperty().bind(table.widthProperty().divide(5));
        revenues.prefWidthProperty().bind(table.widthProperty().divide(7));
        maxVisitors.prefWidthProperty().bind(table.widthProperty().divide(10));
        locationName.prefWidthProperty().bind(table.widthProperty().divide(7));
        date.prefWidthProperty().bind(table.widthProperty().divide(7));

        Tab tableView = new Tab();
        tableView.setText("Dati");
        tableView.setContent(table);

        tabPane.getTabs().add(tableView);
    }

    /**
     * inizializza  la barra di ricerca
     * @param toolBar ToolBar che conterrà la barra di ricerca
     */
    private void initalizeSearch(ToolBar toolBar) {
        notFoundLabel = new Label();
        notFoundLabel.setText("non è stato trovato nessun elemento");
        notFoundLabel.fontProperty().setValue(new Font(20));
        searchController = new SearchController();

        TextField textField = (TextField) toolBar.getItems().get(2);
        List<String> eventsName = searchController.getEventsName();
        TextFields.bindAutoCompletion(textField, eventsName);

        Button button = (Button) toolBar.getItems().get(3);
        button.setOnAction(event -> {
            resetSearch();
            //mergedController.resetModel();
            foundedEventInSearch.addAll(searchController.search(textField.getText()));
            if (foundedEventInSearch.isEmpty()) {
                resetSearch();
                foundedElementsVBox.getChildren().add(notFoundLabel);
                populateWithSelectedItems();
            } else {
                createResult();
            }
        });
        //bottone settato a defalt, per essere premuto con invio
        button.setDefaultButton(true);
        selectAllCheckBox = new CheckBox();
        selectAllCheckBox.setText("Seleziona tutto");
        selectAllCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                for (CheckBox checkBox : checkBoxList) {
                    checkBox.selectedProperty().setValue(true);
                }
            } else {
                for (CheckBox checkBox : checkBoxList) {
                    checkBox.selectedProperty().setValue(false);
                }
            }
        });
        foundedElementsVBox.getChildren().clear();
        foundedElementsVBox.paddingProperty().setValue(new Insets(5.0, 5.0, 2.0, 12.0));
        foundedElementsVBox.getChildren().add(selectAllCheckBox);
    }

    /**
     * reinizializza la lista e la vbox contenete i dati trovati precedentemente
     */
    private void resetSearch() {
        checkBoxList.clear();
        foundedEventInSearch.clear();
        foundedElementsVBox.getChildren().clear();
        foundedElementsVBox.paddingProperty().setValue(new Insets(5.0, 5.0, 2.0, 12.0));
        foundedElementsVBox.getChildren().add(selectAllCheckBox);
    }

    /**
     * crea la colonna con i risultati
     */
    private void createResult() {
        checkBoxList.clear();
        for (EventModel eventModel : foundedEventInSearch) {
            if (!selectedItems.containsKey(eventModel.getIndex())) {
                HBox hBox = new HBox();
                hBox.setSpacing(20);
                CheckBox checkBox = new CheckBox();
                EventTable eventTable = new EventTable(eventModel.getEventName(),
                        eventModel.getTicketSold(),
                        eventModel.getRevenuePerMonth(),
                        eventModel.getMaxVisitors(),
                        eventModel.getLocationName(),
                        eventModel.getStartingDate(), eventModel.getEndingDate(),
                        eventModel.isActive());

                checkBox.setTranslateY(80);
                checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
                    if (newValue) {
                        mergedController.addEventToList(eventModel);
                        data.add(eventTable);

                        hBox.setStyle("-fx-background-color: darkgrey");
                        selectedItems.put(eventModel.getIndex(), hBox);

                    } else {
                        mergedController.removeEventFromList(eventModel);
                        data.remove(eventTable);
                        hBox.setStyle("-fx-background-color: inherit");
                        selectedItems.remove(eventModel.getIndex());
                    }
                });

                checkBoxList.add(checkBox);
                Button button = new Button();
                ImageView imageView = new ImageView(eventModel.getBillboard());
                imageView.setFitHeight(200);
                imageView.setFitWidth(180);
                imageView.setPickOnBounds(true);
                button.setGraphic(imageView);
                button.setOnAction(event -> {
                    viewSourceController.toEventView(eventModel.getIndex());
                });

                VBox vBoxEvent = new VBox();
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
                vBoxEvent.getChildren().addAll(eventName, locationName, date, modifyAndDelete);
                hBox.getChildren().addAll(checkBox, button, vBoxEvent);
                Separator separator = new Separator();
                separator.setOrientation(Orientation.HORIZONTAL);

                VBox vBox = new VBox();
                vBox.setSpacing(20);
                vBox.getChildren().addAll(hBox, separator);
                foundedElementsVBox.getChildren().add(vBox);
            }
        }
        populateWithSelectedItems();
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

    private void populateWithSelectedItems() {
        for (Object o : selectedItems.entrySet()) {
            Map.Entry entry = (Map.Entry) o;
            HBox tmp = (HBox) entry.getValue();
            checkBoxList.add((CheckBox) tmp.getChildren().get(0));
            foundedElementsVBox.getChildren().add((Node) entry.getValue());
        }
    }

    public class EventTable {
        private SimpleStringProperty eventName;
        private SimpleIntegerProperty ticketsSold;
        private SimpleIntegerProperty revenues;
        private SimpleIntegerProperty maxVisitors;
        private SimpleStringProperty locationName;
        private SimpleStringProperty date;
        private SimpleStringProperty isActive;

        EventTable(String eventName, Integer ticketsSold, Integer[] revenues, Integer maxVisitors, String locationName, String startDate, String endDate, Boolean isActive) {
            this.eventName = new SimpleStringProperty(eventName);
            this.ticketsSold = new SimpleIntegerProperty(ticketsSold);
            Integer revenuesNumber = 0;
            for (Integer revenue : revenues) {
                revenuesNumber = revenuesNumber + revenue;
            }
            this.revenues = new SimpleIntegerProperty(revenuesNumber);
            this.maxVisitors = new SimpleIntegerProperty(maxVisitors);
            this.locationName = new SimpleStringProperty(locationName);
            if (startDate.equals(endDate)) {
                this.date = new SimpleStringProperty(startDate);
            } else {
                this.date = new SimpleStringProperty(startDate + " - " + endDate);
            }
            if (isActive) {
                this.isActive = new SimpleStringProperty("Si");
            } else {
                this.isActive = new SimpleStringProperty("No");
            }
        }

        public String getEventName() {
            return eventName.get();
        }


        public void setEventName(String eventName) {
            this.eventName.set(eventName);
        }

        public Integer getTicketsSold() {
            return ticketsSold.get();
        }


        public void setTicketsSold(Integer ticketsSold) {
            this.ticketsSold.set(ticketsSold);
        }

        public Integer getRevenues() {
            return revenues.get();
        }

        public void setRevenues(Integer revenues) {
            this.revenues.set(revenues);
        }


        public Integer getMaxVisitors() {
            return maxVisitors.get();
        }


        public String getLocationName() {
            return locationName.get();
        }

        public String getDate() {
            return date.get();
        }

        public String getIsActive() {
            return isActive.get();
        }
    }
}
