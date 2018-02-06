package controller;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.EventListModel;
import model.EventModel;
import model.LocationListModel;
import model.LocationModel;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class InsertController {
    private EventModel newEvent = new EventModel();
    private ViewSourceController viewSourceController;
    private DBController dbController = DBController.getInstance();
    private EventListModel eventListModel = EventListModel.getInstance();
    private LocationListModel locationListModel = LocationListModel.getInstance();

    public InsertController(ViewSourceController viewSourceController) {
        this.viewSourceController = viewSourceController;
    }

    public void back() {
        viewSourceController.turnBack();
    }

    public void toDash() {
        viewSourceController.toDash();
    }

    public void next(List<String> strings, List<Image> immagini, ImageView insertPlaybillImageView) {
        try {
            newEvent.setEventName(strings.get(0));
            newEvent.setEventDescription(strings.get(3));
            newEvent.setBillboard(insertPlaybillImageView.getImage());
            newEvent.setStartingDate(strings.get(4));
            newEvent.setEndingDate(strings.get(5));
            String[] parts = strings.get(1).split("\\-");
            newEvent.setLocationName(parts[0]);
            newEvent.setLocationAddress(parts[1]);
            newEvent.setMaxVisitors(Integer.parseInt(strings.get(2)));
            newEvent.setSlideshow(immagini);

            viewSourceController.toInsetTicketTypeView(this, newEvent);
        } catch (NullPointerException e) {
            JOptionPane.showMessageDialog(null, "Compilare tutti i campi prima di procedere", "Form Error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Compilare tutti i campi prima di procedere", "Form Error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (ArrayIndexOutOfBoundsException e){
            JOptionPane.showMessageDialog(null, "Compilare tutti i campi prima di procedere", "Form Error",
                    JOptionPane.ERROR_MESSAGE);
        }

    }

    public List<String> getSectorName(String name, String address) {
        for (LocationModel location : locationListModel.getLocationList()) {

            if ((location.getLocationAddress().equals(address)) && (location.getLocationName().equals(name))) {
                return location.getSectorList();
            }

        }
        return null;
    }

    public List<String> getSteatsList(String name, String address) {
        for (LocationModel location : locationListModel.getLocationList()) {

            if ((location.getLocationAddress().equals(address)) && (location.getLocationName().equals(name))) {
                return location.getSeatsList();
            }

        }
        return null;
    }

    public String maxVisitors(String location) {
        int i = 0;
        while (i < eventListModel.getListaEventi().size() - 1) {
            if (location.toLowerCase().equals(eventListModel.getListaEventi().get(i).getLocationName().toLowerCase())) {
                return eventListModel.getListaEventi().get(i).getMaxVisitors().toString();
            }
            i++;
        }
        return null;
    }

    public List<String> getLocations() {
        List<String> locations = new ArrayList<>();
        for (LocationModel locationModel : locationListModel.getLocationList()) {
            locations.add(locationModel.getLocationName() + "-" + locationModel.getLocationAddress());
        }
        return locations;
    }

    public void ticketTypeNext(List<EventModel.Sectors> sectorsList) {
        newEvent.setSectorList(sectorsList);
        double price = newEvent.getSectorList().get(0).getPrize();
        for (int i=1; i< newEvent.getSectorList().size(); i++){
            if(newEvent.getSectorList().get(i).getPrize()<price){
                price = newEvent.getSectorList().get(i).getPrize();
            }
        }
        newEvent.setPrice(price);
        viewSourceController.toInsertReductionView(this, newEvent);
    }

    public void ticketReductionNext(){
        viewSourceController.toInsertRecap(this, newEvent);
    }

    public void insert(){

    }
}
