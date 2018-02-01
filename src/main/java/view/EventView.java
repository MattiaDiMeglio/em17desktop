package view;

import controller.EventController;
import controller.SlideShowController;
import controller.ViewSourceController;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.StackedAreaChart;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import model.EventListModel;
import model.EventModel;
import view.chartsViews.BarChartView;
import view.chartsViews.LineChartView;
import view.chartsViews.PieChartView;

import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Optional;

/**
 * Classe View per la schermata Evento
 * <p>
 * Implementa Observer, come definito dall'architettura MVC implementata per il progetto
 */
public class EventView implements Observer {
    EventListModel eventListModel = EventListModel.getInstance();
    EventModel eventModel;
    Boolean ret;

    /**
     * Costruttore della view, che va a popolarla passando per l'eventModel corrispondente all'index passatogli
     *
     * @param eventController           controller corrispondente
     * @param eventoDeleteButton        bottone per la cancellazione dell'evento corrente
     * @param eventPlaybillImageView    imageview della locandina dell'evento
     * @param eventoTabPane             tabpane contenuto nella schermata, al cui interno andranno i grafici
     * @param index                     indice dell'evento a cui la schermata riferisce
     * @param texts                     lista di text modificabili, che conterranno i dati dell'evento
     * @param eventoTitleLabel          label col titolo dell'evento
     * @param eventTextArea             textArea con la descrizione dell'evento
     * @param eventSlide                Hbox che conterrà le immagini dello slideshow
     * @param eventSlideShowLeftButton  bottone sinistro dello slideshow
     * @param eventSlideShowRightButton bottone destro dello slideshow
     * @param viewSourceController      viewsourcecontroller, necessario per la creazione dello slideshow
     */
    public EventView(EventController eventController, Button eventoDeleteButton, ImageView eventPlaybillImageView,
                     TabPane eventoTabPane, int index, List<Text> texts, Label eventoTitleLabel,
                     TextArea eventTextArea, HBox eventSlide, Button eventSlideShowLeftButton, Button eventSlideShowRightButton,
                     ViewSourceController viewSourceController) {

        SlideShowController slideShowController = new SlideShowController();//creo slideshowcontroller
        eventModel = eventListModel.getListaEventi().get(index); //ottendo l'evento a cui la schermata riferisce
        eventModel.addObserver(this); //setto la view come observer dell'eventmodel
        initializeCharts(eventoTabPane, index); //inizializzazione dei charts
        Image image = eventModel.getBillboard(); //valirizzo l'image con l'immagine della locandina
        eventPlaybillImageView.setImage(image); //creo l'imageView con l'image di cui sopra
        eventoTitleLabel.setText(eventModel.getEventName()); //setto il titolo nella label
        eventTextArea.setText(eventModel.getEventDescription()); //setto la descrizione della textarea
        texts.get(0).setText(eventModel.getLocationName()); //setto il nome location nel primo elemento della lista di text
        texts.get(1).setText(eventModel.getStartingDate().toString()); //setto la data inizio nella text
        texts.get(2).setText(eventModel.getEndingDate().toString()); //setto la data di fine
        texts.get(3).setText("prezzo"); //setto il prezzo
        texts.get(4).setText(eventModel.getMaxVisitors().toString()); //setto il massimo dei visitatori
        texts.get(5).setText(eventModel.getTicketSold().toString()); //setto il numero di biglietti venduti
        //creo lo slideshow
        slideShowController.createSlide(eventSlide, eventSlideShowLeftButton, eventSlideShowRightButton, viewSourceController, eventModel);
        //creo il listener del bottone per la cancellazione
        eventoDeleteButton.setOnAction(event -> {
            if (eventModel.getTicketSold() == 0) {

                //Popup di avviso per confermare l'eliminazione

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Attenzione");
                alert.setHeaderText("Eliminazione");
                alert.setContentText("Si sta tentando di ELIMINARE l'evento " + eventModel.getEventName() + ", confermare?");

                Optional<ButtonType> result = alert.showAndWait();

                if (result.get() == ButtonType.OK){
                    eventController.delete(eventModel.getEventKey());
                }
            }
        });
    }

    /**
     * Metodo per l'inizializzazione dei charts
     *
     * @param tabPane //tabpane contenente i charts
     * @param index   //indice dell'evento a cui la view riferisce
     */
    private void initializeCharts(TabPane tabPane, int index) {
        //ottengo la Vbox in cui è contenuto il linecharts (il contenuto della prima tab
        VBox eventoVboxLinechart = (VBox) tabPane.getTabs().get(0).getContent();
        //ottengo il linechart dalla Vbox ottenuta sopra
        LineChart lineChart = (LineChart) eventoVboxLinechart.getChildren().get(0);

        //ottengo la Vbox in cui è contenuta la piecharts (il contenuto della seconda tab
        VBox eventoVboxPieChart = (VBox) tabPane.getTabs().get(1).getContent();
        //ottengo il piechart contenuto nella vboc di cui sopra
        PieChart pieChart = (PieChart) eventoVboxPieChart.getChildren().get(0);

        //ottengo la Vboc in cui è contenuto il barchart (contenuto della terza tab
        VBox eventoVboxBarChart = (VBox) tabPane.getTabs().get(2).getContent();
        //ottengo il barchart
        BarChart barChart = (BarChart) eventoVboxBarChart.getChildren().get(0);

        //ottengo la Vbox in cui è contenuto lo stacked linechartt (quarta tab
        VBox eventoVboxStackedAreaChart = (VBox) tabPane.getTabs().get(3).getContent();
        //ottendo lo stacked linechart
        StackedAreaChart stackedAreaChart = (StackedAreaChart) eventoVboxStackedAreaChart.getChildren().get(0);

        //creo le view per i quattro chart
        new BarChartView(barChart, index);
        new PieChartView(pieChart, index);
        new LineChartView(lineChart, index);
        new LineChartView(stackedAreaChart, index);
    }

    @Override
    public void update(Observable o, Object arg) {

    }
}
