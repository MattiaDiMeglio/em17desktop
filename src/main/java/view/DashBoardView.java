package view;

import controller.DBController;
import controller.ViewSourceController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import model.EventListModel;
import model.EventModel;

import java.io.IOException;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ExecutionException;

/**
 * Classe view per la schermata DashBoard, nonché main dell'applicativo.
 * Implementa Observer, come definito dall'architettura MVC implementata per il progetto
 * Estende Application per poter utilizzare JavaFX
 *
 *@see java.util.Observable
 * @see java.util.Observer
 * @see javafx.application.Application
 *
 * @author ingSW20
 */
public  class  DashBoardView implements Observer {

    GridPane slideshowImmagine;
    EventModel eventModel = new EventModel();


    private class DashBillBoard{
        Button button;
        private int index;
        private String url;
    }

    public DashBoardView(GridPane slideshowImmagine){
        EventListModel eventListModel = EventListModel.getInstance();
        eventListModel.addObserver(this);
        this.slideshowImmagine = slideshowImmagine;
        System.out.println("nono");

    }


    private void slideShow(GridPane slideshowImmagine){
        List<EventModel> lista = EventListModel.getInstance().getListaEventi();
        int i = lista.size() -1;
        if (!lista.isEmpty()) {
            Button button = new Button();
            button.setGraphic(new ImageView(lista.get(i).getLocandina()));
            button.setId("dashImageButton" + i);
            // DashBillBoard billBoard = new DashBillBoard(lista.get(i).getIndex(), button);
            System.out.println(lista.get(i).getLocandina());
            //slideshowImmagine.addColumn(1 , new Button());
        }
    }

    /**
     * metodo update ereditato da Observer
     * @see java.util.Observer
     * @see java.util.Observable
     *
     * @param o
     * @param arg
     *
     */
    public void update(Observable o, Object arg) {
        System.out.println("ajasjaodnjsdif");
        slideShow(slideshowImmagine);
    }


}
