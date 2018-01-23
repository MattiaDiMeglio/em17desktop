package controller;


import model.EventModel;

import java.util.List;

public class EventController {

    EventController instance = new EventController();
    List<EventModel> listaEventi;

    public EventController getInstance() {
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
