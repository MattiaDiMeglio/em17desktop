package controller.chartsController;

import model.EventModel;
import model.chartsModels.MergedModel;

import java.util.ArrayList;
import java.util.List;

/**
 * MergedController si occupa di unire i dati di vari eventi così da inviarli al model una volta uniti
 *
 * @author ingSW20
 */
public class MergedController {
    /**
     * lista degli eventi di cui visualizzare le statistiche mediante i grafici
     */
    private List<EventModel> eventModelList = new ArrayList<>();
    /**
     * model del pattern MVC contenente i dati per aggiornare i grafici
     */
    private MergedModel mergedModel = MergedModel.getInstance();

    /**
     * Il metodo merge() si occupa dell'unione dei dati visualizzati tramite grafici
     */
    private void merge() {
        mergedModel.resetModel();
        List<String> eventNames = new ArrayList<>();
        List<Double> soldPerEvent = new ArrayList<>();
        for (EventModel eventModel : eventModelList) {
            // dati per il barchart
            eventNames.add(eventModel.getEventName());
            soldPerEvent.add(eventModel.getTicketSold());

            // dati per il linechart
            mergedModel.setTicketSoldArray(mergeArray(eventModel.getTicketsSoldPerMonth(), mergedModel.getTicketsSoldArray()));

            //dati per il piechart
            mergedModel.setMaxVisitors(mergeMaxVisitors(mergedModel.getMaxVisitors(), eventModel.getMaxVisitors()));
            mergedModel.setTicketsSold(mergeTicketsSold(mergedModel.getTicketsSold(), eventModel.getTicketSold()));
        }
        mergedModel.setEventNames(eventNames);
        mergedModel.setSoldPerEvent(soldPerEvent);
    }

    /**
     * il metodo unisce i dati dei due paramentri in ingresso sommandoli e ne restituisce il risultato
     *
     * @param ticketsSold  biglietti venduti presenti in {@link #mergedModel}
     * @param ticketsSold1 biglietti da aggiungere a quelli in {@link #mergedModel}
     * @return somma dei due parametri
     */
    private Double mergeTicketsSold(Double ticketsSold, Double ticketsSold1) {
        return ticketsSold + ticketsSold1;
    }

    /**
     * il metodo unisce i dati dei due paramentri in ingresso sommandoli e ne restituisce il risultato
     *
     * @param maxVisitors  numero massimo di visitatori già contenuto in {@link #mergedModel}
     * @param maxVisitors1 numero massimo di visitatori da aggiungere al quello in {@link #mergedModel}
     * @return somma dei due parametri
     */
    private Double mergeMaxVisitors(Double maxVisitors, Double maxVisitors1) {
        return maxVisitors + maxVisitors1;
    }

    /**
     * il metodo unisce i dati dei due paramentri in ingresso sommandoli e ne restituisce il risultato
     *
     * @param firstArray  numero biglietti venduti divisi per mese (array con 12 elementi) già contenuto in {@link #mergedModel}
     * @param secondArray numero biglietti venduti divisi per mese (array con 12 elementi) da aggiungere in {@link #mergedModel}
     * @return somma dei due parametri
     */
    private Integer[] mergeArray(Integer[] firstArray, Integer[] secondArray) {
        Integer[] ris = new Integer[12];
        for (int i = 0; i < ris.length; i++) {
            ris[i] = firstArray[i] + secondArray[i];
        }
        return ris;
    }

    /**
     * aggiunge alla lista {@link #eventModelList} un nuovo evento e ne unisce i dati con quelli già presenti
     *
     * @param eventModel nuovo evento da aggiungere
     */
    public void addEventToList(EventModel eventModel) {
        eventModelList.add(eventModel);
        merge();
    }

    /**
     * rimuove dalla lista {@link #eventModelList} un evento e ne rimuove i dati dai grafici
     *
     * @param eventModel evento da eliminare
     */
    public void removeEventFromList(EventModel eventModel) {
        eventModelList.remove(eventModel);
        merge();
    }


    /**
     * Il metodo richiama un reset per i dati contenuti nel model
     */
    public void resetModel() {
        mergedModel.resetModel();
    }
}
