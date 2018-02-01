package controller;

import model.EventListModel;
import model.EventModel;

import java.util.ArrayList;
import java.util.List;

public class SearchController {

    private List<EventModel> eventList;

    public SearchController(){
        eventList = EventListModel.getInstance().getListaEventi();
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
}
