package controller;

import model.EventListModel;
import model.EventModel;
import view.InsertView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class InsertController
{
    ViewSourceController viewSourceController;
    DBController dbController = DBController.getInstance();
    EventListModel eventListModel = EventListModel.getInstance();

    public InsertController(ViewSourceController viewSourceController) {
        this.viewSourceController=viewSourceController;
    }

    public void back () throws ExecutionException, InterruptedException {
        System.out.println("indietro");
        viewSourceController.toDashBoardView();
    }

    public void next(InsertView insertView, List<String> texts) {
        System.out.println("avanti");
        EventModel newEvent =  new EventModel();
        viewSourceController.toInsetTicketView();
        //newEvent.setEventName(insertView.ge);

    }

    public String maxVisitors ( String location) throws InterruptedException {
        int i=0;
        while (i < eventListModel.getListaEventi().size()-1){
            if (location.toLowerCase().equals(eventListModel.getListaEventi().get(i).getLocationName().toLowerCase())){
                return eventListModel.getListaEventi().get(i).getMaxVisitors().toString();
            }
            i++;
        }
        return null;
    }

    public List<String> getLocations () {
        System.out.println("entrato");
        List<String> locations = new ArrayList<>();
        int i=0;
        while (i < eventListModel.getListaEventi().size()-1){
            if (!locations.contains(eventListModel.getListaEventi().get(i).getLocationName())){
                locations.add(eventListModel.getListaEventi().get(i).getLocationName());
            }
            i++;
        }

        return locations;
    }
}
