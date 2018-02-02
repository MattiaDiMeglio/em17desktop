package controller;

import model.EventListModel;
import model.EventModel;
import model.LocationListModel;
import model.LocationModel;

import java.util.ArrayList;
import java.util.List;

public class SearchController {

    private List<EventModel> eventList;
    private List<LocationModel> locationList;

    public SearchController(){
        eventList = EventListModel.getInstance().getListaEventi();
        locationList = LocationListModel.getInstance().getListaEventi();
    }

    public List<EventModel> search(String text) {
        List<EventModel> listFound = new ArrayList<>();
        for (EventModel eventModel : eventList) {
            if (eventModel.getEventName().toLowerCase().contains(text.toLowerCase())){
                listFound.add(eventModel);
            }
        }
        return listFound;
    }

    public List<String> getEventsName(){
        List<String> eventsName = new ArrayList<>();
        for (EventModel eventModel : eventList){
            eventsName.add(eventModel.getEventName());
        }
        return eventsName;
    }

    public List<String> getLocationNames(){
        List<String> locationNames = new ArrayList<>();
        for (LocationModel locationModel : locationList){
            locationNames.add(locationModel.getLocationName());
        }
        return locationNames;
    }
}
