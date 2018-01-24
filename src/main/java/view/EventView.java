package view;

import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import view.chartsViews.LineChartClass;
import view.chartsViews.PieChartClass;

import java.util.Observable;
import java.util.Observer;

/**
 *  Classe View per la schermata Evento
 *
 * Implementa Observer, come definito dall'architettura MVC implementata per il progetto
 */
public class EventView implements Observer {
    public EventView(LineChart eventoGrafico1LineChart, PieChart eventGraph2PieChart) {
        //new LineChartClass(eventoGrafico1LineChart);
        new PieChartClass(eventGraph2PieChart);
    }

    @Override
    public void update(Observable o, Object arg) {

    }
}
