package controller;

import model.EventListModel;
import model.EventModel;

import java.util.ArrayList;
import java.util.List;

public class SearchController {

    public List<EventModel> search(String text) {
        List<EventModel> eventList = EventListModel.getInstance().getListaEventi();

        List<EventModel> listFound = new ArrayList<>();
        for (EventModel eventModel : eventList) {
            if (eventModel.getEventName().toLowerCase().contains(text.toLowerCase())){
                listFound.add(eventModel);
            }
        }
        return listFound;
    }
}
