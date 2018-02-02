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

public class SearchController {

    private List<EventModel> eventList;
    private List<LocationModel> locationList;
    private EventListView eventListView;

    public SearchController(EventListView eventListView) {
        this.eventListView = eventListView;
        eventList = EventListModel.getInstance().getListaEventi();
        locationList = LocationListModel.getInstance().getListaEventi();
    }

    public SearchController() {
        eventList = EventListModel.getInstance().getListaEventi();
        locationList = LocationListModel.getInstance().getListaEventi();
    }

    public List<EventModel> search(String text) {
        List<EventModel> listFound = new ArrayList<>();
        for (EventModel eventModel : eventList) {
            if (eventModel.getEventName().toLowerCase().contains(text.toLowerCase())) {
                listFound.add(eventModel);
            }
        }
        return listFound;
    }

    public List<String> getEventsName() {
        List<String> eventsName = new ArrayList<>();
        for (EventModel eventModel : eventList) {
            eventsName.add(eventModel.getEventName());
        }
        return eventsName;
    }

    public List<String> getLocationNames() {
        List<String> locationNames = new ArrayList<>();
        for (LocationModel locationModel : locationList) {
            locationNames.add(locationModel.getLocationName() + " - " + locationModel.getLocationAddress());
        }
        return locationNames;
    }

    public void advancedSearch(String prezzoMinString, String prezzoMaxString, LocalDate startDate, LocalDate endDate, String locationName) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy");
        List<EventModel> listFound = new ArrayList<>();
        Double prezzoMin = 0.0;
        Double prezzoMax = 999999999999999999999999999999999999999999999999999999999999999999999.0;
        if (!prezzoMinString.equals("")) {
            prezzoMin = Double.valueOf(prezzoMinString);
        }

        if (!prezzoMaxString.equals("")) {
            prezzoMax = Double.valueOf(prezzoMaxString);
        }
        
        for (EventModel eventModel : eventList) {
            if ((eventModel.getPrice() >= prezzoMin) &&
                    (eventModel.getPrice() <= prezzoMax) &&
                    LocalDate.parse(eventModel.getStartingDate(), formatter).isBefore(endDate) &&
                    LocalDate.parse(eventModel.getStartingDate(), formatter).isAfter(startDate) &&
                    (locationName.isEmpty()) || (eventModel.getLocationName() + " - " + eventModel.getLocationAddress()).equals(locationName)) {

                listFound.add(eventModel);
            }
        }
        eventListView.advancedSearch(listFound);
    }
}
