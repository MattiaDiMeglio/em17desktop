package view;

import controller.SearchController;
import controller.SlideShowController;
import controller.ViewSourceController;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.StackedAreaChart;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.EventModel;
import org.controlsfx.control.textfield.TextFields;
import view.chartsViews.BarChartView;
import view.chartsViews.LineChartView;
import view.chartsViews.PieChartView;


/**
 * Classe view per la schermata DashBoard.
 *
 * @author ingSW20
 */
public class DashBoardView {

  /**
   * istanza di {@link ViewSourceController}.
   */
  private ViewSourceController viewSourceController;
  /**
   * istanza di {@link SearchController}.
   */
  private SearchController searchController;
  /**
   * lista degli eventi trovati nella ricerca.
   */
  private List<EventModel> foundedEventInSearch;

  /**
   * lista per i suggerimenti della ricerca.
   */
  private List<String> eventsName;

  /**
   * costruttore per la dashboard dell'applicativo.
   *
   * @param dashSlide slide che contiene le locandine degli eventi
   * @param dashBoardSlideShowLeftButton bottone sinistro per lo scorrimento dello slide
   * @param dashBoardSlideShowRightButton bottone destro per lo scorrimento dello slide
   * @param dashBoardTabPane TabPane contenente i grafici
   * @param dashBoardInsertButton tasto per l'inserimento di un nuovo evento
   * @param viewSourceController istanza di {@link ViewSourceController}
   * @param searchToolBar ToolBar contenente la ricerca
   */
  public DashBoardView(HBox dashSlide, Button dashBoardSlideShowLeftButton,
      Button dashBoardSlideShowRightButton,
      TabPane dashBoardTabPane, Button dashBoardInsertButton,
      ViewSourceController viewSourceController, ToolBar searchToolBar) {
    this.viewSourceController = viewSourceController;
    initalizeCharts(dashBoardTabPane);
    initalizeSearch(searchToolBar);

    while (dashSlide.getChildren().size() > 0) {
      dashSlide.getChildren().remove(dashSlide.getChildren().size() - 1);
    }

    SlideShowController slideShowController = new SlideShowController();
    slideShowController
        .createSlide(dashSlide, dashBoardSlideShowLeftButton, dashBoardSlideShowRightButton,
            viewSourceController);
    dashBoardInsertButton.setOnAction(event -> {
      viewSourceController.toInsertView();
    });
  }

  /**
   * metodo per l'inizializzazione della ricerca.
   *
   * @param toolBar toolbar contenente gli elementi utili alla ricerca
   */
  private void initalizeSearch(ToolBar toolBar) {
    searchController = new SearchController();
    TextField textField = (TextField) toolBar.getItems().get(0);
    textField.setOnMouseClicked(event -> {
      eventsName = searchController.getEventsName();
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

  /**
   * metodo per l'inizializzazione dei grafici.
   *
   * @param tabPane TabPane contenente i grafici
   */
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
    StackedAreaChart stackedAreaChart = (StackedAreaChart) dashBoardVboxLineChartSales.getChildren()
        .get(1);

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
}
