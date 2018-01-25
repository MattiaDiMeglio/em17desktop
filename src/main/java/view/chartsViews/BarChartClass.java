package view.chartsViews;

import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import model.EventListModel;
import model.EventModel;

import java.util.*;

public class BarChartClass implements Observer, ChartInterface {

    private BarChart barChart;
    private List<XYChart.Data<String, Number>> datas;
    private XYChart.Series<String, Number> series;
    private int index;

    public BarChartClass(BarChart barChart, int index) {
        this.barChart = barChart;
        initializeCharts();
        this.index = index;
        EventListModel.getInstance().getListaEventi().get(index).addObserver(this);
        update(EventListModel.getInstance().getListaEventi().get(index), null);
    }

    @Override
    public void update(Observable o, Object arg) {

        EventModel eventModel = (EventModel) o;
        System.out.println(eventModel.getIndex());
        HashMap settori = eventModel.getListaVenditaPerSettori();
        List<String> nomeSettori = eventModel.getListaSettoriName();
        System.out.println(settori.toString());

        if (barChart.getData().isEmpty()) {
            System.out.println("primo avvio");
            datas = new ArrayList<>();

            for (int i = 0; i < settori.size(); i++) {
                System.out.println(nomeSettori.get(i) + " " + settori.get(nomeSettori.get(i)));
                datas.add(new XYChart.Data(nomeSettori.get(i), settori.get(nomeSettori.get(i))));
            }
            for (XYChart.Data<String, Number> data : datas) {
                series.getData().add(data);
            }

            barChart.getData().add(series);
        } else {
            //if (datas.size() != nomeSettori.size()) {
                System.out.println("pulisco i dati");
                series.getData().clear();
                datas.clear();

                for (int i = 0; i < settori.size(); i++) {
                    System.out.println(nomeSettori.get(i) + " " + settori.get(nomeSettori.get(i)));
                    datas.add(new XYChart.Data(nomeSettori.get(i), settori.get(nomeSettori.get(i))));
                }

               /* for (int i = 0; i < datas.size(); i++) {
                    System.out.println("aggiorno i dati");
                    datas.get(i).setXValue(nomeSettori.get(i));
                    datas.get(i).setYValue((Number) settori.get(nomeSettori.get(i)));
                }*/
                for (XYChart.Data<String, Number> data : datas) {
                    series.getData().add(data);
                }
            //barChart.getData().add(series);
           // }else {
               /* for (int i = 0; i < datas.size(); i++) {
                    System.out.println("aggiorno i dati");
                    System.out.println(nomeSettori.get(i) + " " + settori.get(nomeSettori.get(i)));
                    datas.get(i).setXValue(nomeSettori.get(i));
                    datas.get(i).setYValue((Number) settori.get(nomeSettori.get(i)));
                }*/
            //}
        }
    }

    @Override
    public void initializeCharts() {
        barChart.setTitle("Vendita per Settore");
        if (barChart.getData().isEmpty()) {
            series = new XYChart.Series<>();
            series.setName("Biglietti venduti per settore");


          /*  List nomeSettori = eventModel.getListaSettoriName();
            datas = new ArrayList<>();

            for (int i = 0; i<settori.size();i++) {
                datas.add(new XYChart.Data(nomeSettori.get(i), settori.get(nomeSettori.get(i))));
            }
            for (XYChart.Data<String, Number> data : datas) {
                series.getData().add(data);
            }

            barChart.getData().add(series);*/

        } else {
            series = (XYChart.Series<String, Number>) barChart.getData().get(0);
            System.out.println("dim series: " + series.getData().size());
            datas = new ArrayList<>();
            for (int i =0;i<series.getData().size();i++) {
                datas.add(series.getData().get(i));
            }
            System.out.println("dim datas: " + datas.size());
        }
    }
}
