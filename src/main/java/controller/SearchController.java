package controller;

import model.EventListModel;
import model.EventModel;
import model.LocationListModel;
import model.LocationModel;
import view.EventListView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * controller per la ricerca.
 *
 * @author ingSW20
 */
public class SearchController {

    /**
     * lista con tutti gli elementi del database.
     */
    private List<EventModel> eventList;
    /**
     * lista con tutte le location del database.
     */
    private List<LocationModel> locationList;
    /**
     * parametro per la comunicazione con la view.
     */
    private EventListView eventListView;


  /**
   * il contruttore inizializza i paramtri {@link #eventList}, {@link #locationList} e {@link
   * #eventListView}.
   *
   * @param eventListView view
   */
  public SearchController(EventListView eventListView) {
    this.eventListView = eventListView;
    eventList = EventListModel.getInstance().getEventList();
    locationList = LocationListModel.getInstance().getLocationList();
  }

  /**
   * costruttore utilizzato in {@link view.DashBoardView DashBoardView}.
   */
  public SearchController() {
    eventList = EventListModel.getInstance().getEventList();
    locationList = LocationListModel.getInstance().getLocationList();
  }

    /**
     * effettua la ricerca della stringa passata come parametro e restituisce una lista di risultati.
     *
     * @param text testo da cercare
     * @return lista di risultati trovati
     */
    public List<EventModel> search(String text) {
        List<EventModel> listFound = new ArrayList<>();
        for (EventModel eventModel : eventList) {
            if (eventModel.getEventName().toLowerCase().contains(text.toLowerCase())) {
                listFound.add(eventModel);
            }
        }
        return listFound;
    }

    /**
     * crea una lista con tutti i nomi degli eventi presenti nel database.
     *
     * @return lista con i nomi degli eventi
     */
    public List<String> getEventsName() {
        List<String> eventsName = new ArrayList<>();
        for (EventModel eventModel : eventList) {
            eventsName.add(eventModel.getEventName());
        }
        return eventsName;
    }

    /**
     * crea una lista con tutti i nomi delle location presenti nel database.
     *
     * @return lista con i nomi delle location
     */
    public List<String> getLocationNames() {
        List<String> locationNames = new ArrayList<>();
        for (LocationModel locationModel : locationList) {
            locationNames
                    .add(locationModel.getLocationName() + " - " + locationModel.getLocationAddress());
        }
        return locationNames;
    }

    /**
     * metodo per la ricerca avanzata.
     *
     * @param prezzoMinString prezzo minimo
     * @param prezzoMaxString prezzo massimo
     * @param startDate       data di inizio evento
     * @param endDate         data di fine evento
     * @param locationName    nome della location
     * @return lista con gli elementi trovati
     */
    public List<EventModel> advancedSearch(String prezzoMinString, String prezzoMaxString, LocalDate startDate,
                               LocalDate endDate, String locationName) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy");
        List<EventModel> listFound = new ArrayList<>();
        Double prezzoMin = 0.0;
        Double prezzoMax = Double.MAX_VALUE;
        if (!prezzoMinString.equals("")) {
            if (Double.valueOf(prezzoMinString)>0){
                prezzoMin = Double.valueOf(prezzoMinString);
            }
        }

        if (!prezzoMaxString.equals("")) {
            if (Double.valueOf(prezzoMaxString)>0) {
                prezzoMax = Double.valueOf(prezzoMaxString);
            }
        }

        for (EventModel eventModel : eventList) {
            if ((eventModel.getPrice() >= prezzoMin)
                    && (eventModel.getPrice() <= prezzoMax)
                    && (startDate.isEqual(LocalDate.MIN) ||
                        LocalDate.parse(eventModel.getStartingDate(), formatter).isAfter(startDate.minusDays(1)))
                    && (endDate.equals(LocalDate.MAX) ||
                        LocalDate.parse(eventModel.getEndingDate(), formatter).isBefore(endDate.plusDays(1)))
                    && (locationName.isEmpty()) || (eventModel.getLocationName() + " - " + eventModel
                    .getLocationAddress()).equals(locationName)) {

                listFound.add(eventModel);
            }
        }
        return listFound;
    }
}
