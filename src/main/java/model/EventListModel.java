package model;

import javafx.application.Platform;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

/**
 * classe che contiene la lista di tutti gli eventi contenuti nel database locale
 *
 * @author ingSW20
 */
public class EventListModel extends Observable {

    /**
     * istanza della classe corrente
     */
    private static EventListModel instance = new EventListModel();
    /**
     * lista con gli eventi trovati nel database
     */
    private List<EventModel> listaEventi = new ArrayList<>();

    /**
     * getter per l'istanza corrente
     *
     * @return istanza corrente
     */
    public static EventListModel getInstance() {
        return instance;
    }

    /**
     * costruttore vuoto
     */
    private EventListModel() {
    }

    /**
     * getter per {@link #listaEventi}
     *
     * @return {@link #listaEventi}
     */
    public List<EventModel> getListaEventi() {
        return listaEventi;
    }

    /**
     * setter per {@link #listaEventi}
     *
     * @param evento evento da aggiungere a {@link #listaEventi}
     */
    public void setListaEventi(EventModel evento) {
        this.listaEventi.add(evento);
       /* Platform.runLater(() -> {
            setChanged();
            notifyObservers(evento.getIndex());
        });*/
    }

    public void notifyMyObservers() {
        Platform.runLater(() -> {
            setChanged();
            notifyObservers();
        });
    }
}
