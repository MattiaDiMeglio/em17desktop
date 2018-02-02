package model;

import javafx.application.Platform;
import model.EventModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class EventListModel extends Observable{

    private static EventListModel instance = new EventListModel();
    private List<EventModel> listaEventi = new ArrayList<>();

    public static EventListModel getInstance() {
        return instance;
    }

    private EventListModel (){
    };


    public List<EventModel> getListaEventi() {
        return listaEventi;
    }

    public void setListaEventi(EventModel evento) {
        this.listaEventi.add(evento);
        Platform.runLater(() -> {
            setChanged();
            notifyObservers(evento.getIndex());
        });

    }

    public void deleteList (){
        this.listaEventi.removeAll(listaEventi);
    }
}
