package view.chartsViews;

import controller.chartsController.ChartsController;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import javafx.beans.value.ChangeListener;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.StackedAreaChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import model.EventListModel;
import model.EventModel;
import model.chartsModels.LineChartModel;
import model.chartsModels.MergedModel;
import model.chartsModels.StackedAreaChartModel;
import view.LoadingPopupView;


/**
 * Questa classe rappresenta la view corrispondente al grafico {@link javafx.scene.chart.LineChart
 * LineChart} e al grafico {@link javafx.scene.chart.StackedAreaChart StackedAreaChart}.
 * L'aggiornamento della view è eseguita secondo i canoni del design pattern MVC.
 *
 * @author ingsw20
 */
public class LineChartView implements Observer, ChartInterface {

  /**
   * dati mostrati sul grfico.
   */
  private List<XYChart.Data<String, Number>> datas;

  /**
   * serie contenente i dati da mostrare sul grafico.
   */
  private XYChart.Series<String, Number> series;

  /**
   * variabile per la gestione del grafico.
   */
  private XYChart chart;

  /**
   * il construttore inizializza il grafico per la gestione del LineChart, inoltre registra la view
   * presso il model. In particolare, questo costruttore è utilizzato per la visualizzazione delle
   * statistiche relativo ad un anno scelto.
   *
   * @param lineChart tipo di grafico da gestire
   * @param comboBox combobox per scegliere l'anno
   */
  public LineChartView(LineChart lineChart, ComboBox comboBox) {
    this.chart = lineChart;
    initializeCharts();
    lineChart.setTitle("Vendita biglietti");

    comboBox.valueProperty()
        .addListener((ChangeListener<Integer>) (observable, oldValue, newValue) -> {
          CountDownLatch latch = new CountDownLatch(1);
          ChartsController.getInstance().populateCharts(String.valueOf(newValue), latch);
          new LoadingPopupView(latch);
          lineChart.setTitle("Vendita biglietti " + String.valueOf(newValue));
        });

    LineChartModel.getInstance().addObserver(this);
  }

  /**
   * Costruttore per l'inizializzazione del LineChart relativo ad un singolo evento, inoltre
   * registra la view presso il model.
   *
   * @param lineChart tipo di grafico
   * @param index indice da utilizzare per identificare l'evento desiderato
   */
  public LineChartView(LineChart lineChart, int index) {
    this.chart = lineChart;
    initializeCharts();
    lineChart.setTitle("Vendita biglietti");

    EventListModel.getInstance().getListaEventi().get(index).addObserver(this);
    update(EventListModel.getInstance().getListaEventi().get(index), null);
  }

  /**
   * il construttore inizializza il grafico per la gestione dello StackedAreaChart, inoltre registra
   * la view presso il model. In particolare, questo costruttore è utilizzato per la visualizzazione
   * delle statistiche relativo ad un anno scelto.
   *
   * @param stackedAreaChart tipo di grafico da gestire
   * @param comboBox combobox per scegliere l'anno
   */
  public LineChartView(StackedAreaChart stackedAreaChart, ComboBox comboBox) {
    this.chart = stackedAreaChart;
    initializeCharts();
    stackedAreaChart.setTitle("Vendita biglietti");

    comboBox.valueProperty()
        .addListener((ChangeListener<Integer>) (observable, oldValue, newValue) -> {
          CountDownLatch latch = new CountDownLatch(1);
          ChartsController.getInstance().populateCharts(String.valueOf(newValue), latch);
          new LoadingPopupView(latch);
          stackedAreaChart.setTitle("Vendita biglietti " + String.valueOf(newValue));
        });

    StackedAreaChartModel.getInstance().addObserver(this);
  }

  /**
   * Costruttore per l'inizializzazione dello StackedAreaChart relativo ad un singolo evento,
   * inoltre registra la view presso il model.
   *
   * @param stackedAreaChart tipo di grafico
   * @param index indice da utilizzare per identificare l'evento desiderato
   */
  public LineChartView(StackedAreaChart stackedAreaChart, int index) {
    this.chart = stackedAreaChart;
    initializeCharts();
    stackedAreaChart.setTitle("Guadagni nel tempo");

    EventListModel.getInstance().getListaEventi().get(index).addObserver(this);
    update(EventListModel.getInstance().getListaEventi().get(index), null);
  }

  /**
   * costruttore per il grafico contenente i dati di più eventi.
   *
   * @param lineChart tipo di grafico da gestire
   */
  public LineChartView(LineChart lineChart) {
    this.chart = lineChart;
    initializeCharts();
    MergedModel.getInstance().addObserver(this);
  }

  /**
   * metodo necessario secondo il design pattern MVC, utile per l'aggiornamento del grafico in tempo.
   * reale
   *
   * @param o Model dal quale è stato invocato il metodo
   * @param arg null
   */
  @Override
  public void update(Observable o, Object arg) {
    Integer[] vendite;
    if (o instanceof LineChartModel) {
      LineChartModel lineChartModel = (LineChartModel) o;
      vendite = lineChartModel.getTicketsSold();
    } else if (o instanceof StackedAreaChartModel) {
      StackedAreaChartModel salesModel = (StackedAreaChartModel) o;
      vendite = salesModel.getEaringsFromTicketsSold();
    } else if (o instanceof EventModel) {
      EventModel eventModel = (EventModel) o;
      if (chart instanceof LineChart) {
        vendite = eventModel.getTicketsSoldPerMonth();
      } else {
        vendite = eventModel.getRevenuePerMonth();
      }
    } else {
      MergedModel mergedModel = (MergedModel) o;
      vendite = mergedModel.getTicketsSoldArray();
    }

    for (int i = 0; i < datas.size(); i++) {
      datas.get(i).setYValue(vendite[i]);
    }
  }

  /**
   * metodo per l'inizializzazione del grafico.
   */
  @Override
  public void initializeCharts() {
    chart.getXAxis().setAnimated(false);
    if (chart.getData().isEmpty()) {
      initilizeSeries();
      chart.getData().add(series);
    } else {
      resetSeries(chart);
    }
  }

  /**
   * effettua un reset della variabile {@link #series} per un nuovo popolamento del grafico.
   *
   * @param chart grafico da resettare
   */
  private void resetSeries(XYChart chart) {
    series = (XYChart.Series<String, Number>) chart.getData().get(0);

    datas = new ArrayList<>();
    datas.addAll(series.getData());
  }

  /**
   * inizializza la variabile {@link #series}
   */
  private void initilizeSeries() {
    series = new XYChart.Series<>();
    series.setName("Biglietti venduti");
    datas = new ArrayList<>();

    datas.add(new XYChart.Data<>("Gen", 0));
    datas.add(new XYChart.Data<>("Feb", 0));
    datas.add(new XYChart.Data<>("Mar", 0));
    datas.add(new XYChart.Data<>("Apr", 0));
    datas.add(new XYChart.Data<>("Mag", 0));
    datas.add(new XYChart.Data<>("Giu", 0));
    datas.add(new XYChart.Data<>("Lug", 0));
    datas.add(new XYChart.Data<>("Ago", 0));
    datas.add(new XYChart.Data<>("Set", 0));
    datas.add(new XYChart.Data<>("Ott", 0));
    datas.add(new XYChart.Data<>("Nov", 0));
    datas.add(new XYChart.Data<>("Dic", 0));

    for (XYChart.Data<String, Number> data : datas) {
      series.getData().add(data);
    }
  }
}
