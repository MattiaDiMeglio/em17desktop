package view;

import controller.EventController;
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
import model.EventListModel;
import model.EventModel;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;
import view.chartsViews.BarChartView;
import view.chartsViews.LineChartView;
import view.chartsViews.PieChartView;

import java.util.*;
import java.util.concurrent.CountDownLatch;

/**
 * Classe View per la schermata Event List. Implementa Observer, come definito dall'architettura MVC
 * implementata per il progetto.
 *
 * @author ingSW20
 */
public class EventListView implements Observer {

  /**
   * Label mostrata in caso di risultati non trovati.
   */
  private Label notFoundLabel;

  /**
   * variabile per la gestione dei dati visualizzati nei chart.
   */
  private MergedController mergedController;

  /**
   * VBox contenente i risultati della ricerca.
   */
  private VBox foundElementsVBox;

  /**
   * controller per la ricerca.
   */
  private SearchController searchController;

  /**
   * Lista di eventi trovati.
   */
  private List<EventModel> eventFoundInSearch;

  /**
   * istanza di viewSourceController utile per il passaggio da una view all'altra.
   */
  private ViewSourceController viewSourceController;

  /**
   * lista contenente le checkbox relative agli elementi trovati.
   */
  private List<CheckBox> checkBoxList;

  /**
   * checkbox per selezionare tutti gli elementi trovati.
   */
  private CheckBox selectAllCheckBox;

  /**
   * hasmap per la memorizzazione degli elementi selezionati.
   */
  private HashMap<Integer, HBox> selectedItems;

  /**
   * ObservableList per il popolamento della tabella.
   */
  private ObservableList<EventTable> data;

  /**
   * istanza di {@link EventController}.
   */
  private EventController eventController = new EventController();

  /**
   * textfield per la ricerca.
   */
  private TextField searchTextField;

  private AutoCompletionBinding completionBinding;

  /**
   * lista per i suggerimenti della ricerca.
   */
  private List<String> eventsName = new ArrayList<>();

  /**
   * costruttore per inizializzare la classe.
   *
   * @param eventListTabPane TabPane contenente i grafici
   * @param eventFoundInSearch elementi trovati
   * @param eventListViewVBox VBox popolata con i risultati della ricerca
   * @param searchToolBarEventListView ToolBar con gli elementi per la ricerca
   * @param viewSourceController istanza di {@link ViewSourceController} per il cambio di view
   */
  public EventListView(TabPane eventListTabPane, List<EventModel> eventFoundInSearch,
      VBox eventListViewVBox,
      ToolBar searchToolBarEventListView, ViewSourceController viewSourceController) {
    this.foundElementsVBox = eventListViewVBox;
    this.eventFoundInSearch = eventFoundInSearch;
    this.viewSourceController = viewSourceController;
    checkBoxList = new ArrayList<>();
    selectedItems = new HashMap<>();
    data = FXCollections.observableArrayList();
    Button backButton = (Button) searchToolBarEventListView.getItems().get(0);
    backButton.setOnAction(event -> {
      completionBinding.dispose();
      viewSourceController.toDash();
      //viewSourceController.turnBack();
      backButton.onActionProperty().unbind();
    });
    EventListModel.getInstance().addObserver(this);

    initalizeSearch(searchToolBarEventListView); // inizializzo la barra di ricerca
    initializeCharts(eventListTabPane); // inizializzo i grafici
    initalizeTableView(eventListTabPane); // inizializzo la tabella

    if (eventFoundInSearch.isEmpty()) {
      foundElementsVBox.getChildren().add(notFoundLabel);
    } else {
      createResult();
    }
  }

  /**
   * inizializza la tabella che verrà visualizzata nel TabPane.
   *
   * @param tabPane TabPane che conterrà la tabella
   */
  private void initalizeTableView(TabPane tabPane) {

    TableView<EventTable> table;
    if (tabPane.getTabs().size() == 4) {
      table = (TableView<EventTable>) tabPane.getTabs().get(3).getContent();
      table.getColumns().clear();
    } else {
      table = new TableView<>();
      table.setEditable(true);
      Tab tableView = new Tab();
      tableView.setText("Dati");
      tableView.setContent(table);
      tabPane.getTabs().add(tableView);
    }

    TableColumn<EventTable, String> eventName = new TableColumn<>("Nome evento");
    eventName.setCellValueFactory(new PropertyValueFactory<>("eventName"));
    TableColumn<EventTable, Integer> ticketsSold = new TableColumn<>("Biglietti venduti");
    ticketsSold.setCellValueFactory(new PropertyValueFactory<>("ticketsSold"));
    TableColumn<EventTable, Integer> revenues = new TableColumn<>("Ricavi in \u20ac");
    revenues.setCellValueFactory(new PropertyValueFactory<>("revenues"));
    TableColumn<EventTable, Integer> maxVisitors = new TableColumn<>("Max spettatori");
    maxVisitors.setCellValueFactory(new PropertyValueFactory<>("maxVisitors"));
    TableColumn<EventTable, String> locationName = new TableColumn<>("Nome location");
    locationName.setCellValueFactory(new PropertyValueFactory<>("locationName"));
    TableColumn<EventTable, String> date = new TableColumn<>("Data");
    date.setCellValueFactory(new PropertyValueFactory<>("date"));
    TableColumn<EventTable, String> isActive = new TableColumn<>("In corso");
    isActive.setCellValueFactory(new PropertyValueFactory<>("isActive"));

    table.setItems(data);
    table.getColumns()
        .addAll(eventName, ticketsSold, revenues, maxVisitors, locationName, date, isActive);

    eventName.prefWidthProperty().bind(table.widthProperty().divide(5));
    ticketsSold.prefWidthProperty().bind(table.widthProperty().divide(5));
    revenues.prefWidthProperty().bind(table.widthProperty().divide(7));
    maxVisitors.prefWidthProperty().bind(table.widthProperty().divide(10));
    locationName.prefWidthProperty().bind(table.widthProperty().divide(7));
    date.prefWidthProperty().bind(table.widthProperty().divide(7));
  }

  /**
   * inizializza  la barra di ricerca.
   *
   * @param toolBar ToolBar che conterrà la barra di ricerca
   */
  private void initalizeSearch(ToolBar toolBar) {
    notFoundLabel = new Label();
    notFoundLabel.setText("non è stato trovato nessun elemento");
    notFoundLabel.fontProperty().setValue(new Font(20));
    searchController = new SearchController();

    searchTextField = (TextField) toolBar.getItems().get(2);
    eventsName.addAll(searchController.getEventsName());
    completionBinding = TextFields.bindAutoCompletion(searchTextField, eventsName);

    Button button = (Button) toolBar.getItems().get(3);
    button.setOnAction(event -> search(searchTextField.getText()));

    Button advancedSearchButton = (Button) toolBar.getItems().get(4);
    advancedSearchButton.setOnAction(event -> new AdvancedSearchView(searchController, this));

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
    foundElementsVBox.getChildren().clear();
    foundElementsVBox.paddingProperty().setValue(new Insets(5.0, 5.0, 2.0, 12.0));
    foundElementsVBox.getChildren().add(selectAllCheckBox);
  }

  /**
   * effettua una ricerca con la stringa passata come parametro.
   *
   * @param string stringa da cercare
   */
  private void search(String string) {
    resetSearch();
    eventFoundInSearch.addAll(searchController.search(string));
    if (eventFoundInSearch.isEmpty()) {
      resetSearch();
      foundElementsVBox.getChildren().add(notFoundLabel);
      populateWithSelectedItems();
    } else {
      createResult();
    }
  }

  /**
   * lista con i risultati trivati dalla ricerca avanzata.
   *
   * @param foundedEventInSearch lita con i risultati
   */
  public void advancedSearch(List<EventModel> foundedEventInSearch) {
    resetSearch();
    this.eventFoundInSearch = foundedEventInSearch;
    createResult();
  }

  /**
   * reinizializza la lista e la vbox contenete i dati trovati precedentemente.
   */
  private void resetSearch() {
    checkBoxList.clear();
    eventFoundInSearch.clear();
    foundElementsVBox.getChildren().clear();
    foundElementsVBox.paddingProperty().setValue(new Insets(5.0, 5.0, 2.0, 12.0));
    foundElementsVBox.getChildren().add(selectAllCheckBox);
  }

  /**
   * crea la colonna con i risultati.
   */
  private void createResult() {
    checkBoxList.clear();
    for (EventModel eventModel : eventFoundInSearch) {
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
        button.setOnAction(event -> viewSourceController.toEventView(eventModel.getIndex()));

        VBox vBoxEvent = new VBox();
        Label eventName = new Label();
        eventName.translateYProperty().setValue(20);
        eventName.setTextFill(Paint.valueOf("blue"));
        eventName.underlineProperty().setValue(true);
        eventName.fontProperty().setValue(new Font(20));
        eventName.setText(eventModel.getEventName());
        eventName
            .setOnMouseClicked(event -> viewSourceController.toEventView(eventModel.getIndex()));
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

        //Crea il pulsante Cancella

        Label delete = new Label();
        delete.setTranslateY(50);
        delete.setTextFill(Paint.valueOf("blue"));
        delete.underlineProperty().setValue(true);
        delete.setText("Cancella");
        delete.setOnMouseClicked(event -> {
          if (eventModel.getTicketSold() == 0) {

            //Popup di avviso per confermare l'eliminazione

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Attenzione");
            alert.setHeaderText("Eliminazione");
            alert.setContentText(
                "Si sta tentando di ELIMINARE l'evento " + eventModel.getEventName()
                    + ", confermare?");

            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.OK) {
              CountDownLatch latch = new CountDownLatch(1);
              eventController.delete(eventModel.getEventKey(), latch);
              new LoadingPopupView(latch);
            }
          } else {

            //Popup di avviso che mostra perché è impossibile eliminare l'evento

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Informazione");
            alert.setHeaderText("L'evento non può essere eliminato");
            alert.setContentText("L'evento " + eventModel.getEventName()
                + " non può essere eliminato perché ha già venduto qualche biglietto");

            alert.showAndWait();
          }
        });

        modifyAndDelete.getChildren().addAll(modify, delete);
        vBoxEvent.getChildren().addAll(eventName, locationName, date, modifyAndDelete);
        hBox.getChildren().addAll(checkBox, button, vBoxEvent);
        Separator separator = new Separator();
        separator.setOrientation(Orientation.HORIZONTAL);

        VBox vBox = new VBox();
        vBox.setSpacing(20);
        vBox.getChildren().addAll(hBox, separator);
        foundElementsVBox.getChildren().add(vBox);
      }
    }
    populateWithSelectedItems();
  }

  /**
   * si occupa dell'inizializzazione dei grafici.
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
   * popolamento che memorizza i dati selezionati nella precedente ricerca.
   */
  private void populateWithSelectedItems() {
    for (Object o : selectedItems.entrySet()) {
      Map.Entry entry = (Map.Entry) o;
      HBox tmp = (HBox) entry.getValue();
      checkBoxList.add((CheckBox) tmp.getChildren().get(0));
      foundElementsVBox.getChildren().add((Node) entry.getValue());
    }
  }

  /**
   * metodo per l'aggiornamento della view.
   *
   * @param o model al quale fare riferimento
   * @param arg null
   */
  @Override
  public void update(Observable o, Object arg) {
    completionBinding.dispose();

    eventsName.clear();
    eventsName.addAll(searchController.getEventsName());
    completionBinding = TextFields.bindAutoCompletion(searchTextField, eventsName);
    search(searchTextField.getText());
  }

  /**
   * sottoclasse per il popolamento della tabella.
   */
  public class EventTable {

    /**
     * nome dell'evento.
     */
    private SimpleStringProperty eventName;
    /**
     * biglietti venduti.
     */
    private SimpleIntegerProperty ticketsSold;
    /**
     * guadagni.
     */
    private SimpleIntegerProperty revenues;
    /**
     * massimo numero di visitatori.
     */
    private SimpleIntegerProperty maxVisitors;
    /**
     * nome della location.
     */
    private SimpleStringProperty locationName;
    /**
     * data.
     */
    private SimpleStringProperty date;
    /**
     * se l'evento è attivo o meno.
     */
    private SimpleStringProperty isActive;

    /**
     * costruttore per il popolamento della tabella.
     *
     * @param eventName nome dell'evento
     * @param ticketsSold biglietti venduti
     * @param revenues guadagni
     * @param maxVisitors massimo numero di visitatori
     * @param locationName nome della location
     * @param startDate data di inizio evento
     * @param endDate data di fine evento
     * @param isActive se l'evento è attivo o meno
     */
    EventTable(String eventName, Integer ticketsSold, Integer[] revenues, Integer maxVisitors,
        String locationName, String startDate, String endDate, Boolean isActive) {
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

    /**
     * getter per {@link #eventName}.
     *
     * @return {@link #eventName}
     */
    public String getEventName() {
      return eventName.get();
    }

    /**
     * getter per {@link #ticketsSold}.
     *
     * @return {@link #ticketsSold}
     */
    public Integer getTicketsSold() {
      return ticketsSold.get();
    }

    /**
     * getter per {@link #revenues}.
     *
     * @return {@link #revenues}
     */
    public Integer getRevenues() {
      return revenues.get();
    }

    /**
     * getter per {@link #maxVisitors}.
     *
     * @return {@link #maxVisitors}
     */
    public Integer getMaxVisitors() {
      return maxVisitors.get();
    }

    /**
     * getter per {@link #locationName}.
     *
     * @return {@link #locationName}
     */
    public String getLocationName() {
      return locationName.get();
    }

    /**
     * getter per {@link #date}.
     *
     * @return {@link #date}
     */
    public String getDate() {
      return date.get();
    }

    /**
     * getter per {@link #isActive}.
     *
     * @return {@link #isActive}
     */
    public String getIsActive() {
      return isActive.get();
    }
  }
}
