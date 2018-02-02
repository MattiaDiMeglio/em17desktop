package controller;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.EventListModel;
import model.EventModel;
import view.InsertView;

import java.util.ArrayList;
import java.util.List;

public class InsertController
{
    private EventModel newEvent =  new EventModel();
    private ViewSourceController viewSourceController;
    private DBController dbController = DBController.getInstance();
    private EventListModel eventListModel = EventListModel.getInstance();

    public InsertController(ViewSourceController viewSourceController) {
        this.viewSourceController=viewSourceController;
    }

    public void back() {
        System.out.println("indietro");
        viewSourceController.turnBack();
    }

    public void next(List<String> strings, List<Image> immagini, ImageView insertPlaybillImageView) {
        System.out.println("avanti");

        newEvent.setEventName(strings.get(0));
        newEvent.setEventDescription(strings.get(3));
        newEvent.setBillboard(insertPlaybillImageView.getImage());
        newEvent.setStartingDate(strings.get(4));
        newEvent.setEndingDate(strings.get(5));
        newEvent.setLocationName(strings.get(1));
        newEvent.setMaxVisitors(Integer.parseInt(strings.get(2)));
        newEvent.setSlideshow(immagini);
        viewSourceController.toInsetTicketTypeView();


    }

    public String maxVisitors ( String location) {
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
