package controller;


import model.EventModel;

import java.util.ArrayList;
import java.util.List;

public class EventController {

    private static EventController instance = new EventController();
    List<EventModel> listaEventi = new ArrayList<>();

    public static EventController getInstance() {
        return instance;
    }

    protected EventController (){
    };


    public List<EventModel> getListaEventi() {
        return listaEventi;
    }

    public void setListaEventi(EventModel evento) {
        this.listaEventi.add(evento);
    }

    public void deleteList (){
        this.listaEventi.removeAll(listaEventi);
    }
}
