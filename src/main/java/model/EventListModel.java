package model;

import model.EventModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class EventListModel extends Observable{

    private static EventListModel instance = new EventListModel();
    List<EventModel> listaEventi = new ArrayList<>();

    public static EventListModel getInstance() {
        return instance;
    }

    protected EventListModel (){
    };


    public List<EventModel> getListaEventi() {
        return listaEventi;
    }

    public void setListaEventi(EventModel evento) {
        this.listaEventi.add(evento);
        setChanged();
        notifyObservers(listaEventi.size());
    }

    public void deleteList (){
        this.listaEventi.removeAll(listaEventi);
    }
}
