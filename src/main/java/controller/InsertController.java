package controller;

import javafx.scene.image.Image;
import model.EventListModel;
import model.EventModel;
import model.LocationListModel;
import model.LocationModel;
import view.LoadingPopupView;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Classe controller che si occupa dell'inserimento di un nuovo evento
 */
public class InsertController {
    /**
     * {@link EventModel} contenente i dati del nuovo evento da inserire
     */
    private EventModel newEvent = new EventModel();
    /**
     * instanza di {@link ViewSourceController}
     */
    private ViewSourceController viewSourceController;
    /**
     * Instanza di {@link DBController}
     */
    private DBController dbController = DBController.getInstance();
    /**
     * Instanza di {@link EventListModel}
     */
    private EventListModel eventListModel = EventListModel.getInstance();
    /**
     * instanza di {@link LocationListModel}
     */
    private LocationListModel locationListModel = LocationListModel.getInstance();
    /**
     * Lista delle immagini che verranno caricate nello storage del server
     */
    private List<Image> imagesList = new ArrayList<>();
    /**
     * copertina dell'evento
     */
    private Image playbill;


    /**
     * costruttore dell'insertController
     *
     * @param viewSourceController
     */
    InsertController(ViewSourceController viewSourceController) {
        this.viewSourceController = viewSourceController;
    }

    /**
     * metodo chiamato dai listener dei bottoni indietro di tutta la sequenza
     * di schermate. chiama {@link ViewSourceController#turnBack()}
     */
    public void back() {
        viewSourceController.turnBack();
    }

    /**
     * metodo chiamato dal listener del bottone indietro della prima schermata di inserimento
     * chiama {@link ViewSourceController#toDash()}
     *
     */
    public void toDash() {
        viewSourceController.toDash();
    }

    public void insertNext(List<String> strings, Image insertPlaybillImageView) {
        try {
            newEvent.setEventName(strings.get(0));
            newEvent.setEventDescription(strings.get(3));
            newEvent.setBillboard(insertPlaybillImageView);
            playbill = insertPlaybillImageView;
            newEvent.setStartingDate(strings.get(4));
            newEvent.setEndingDate(strings.get(5));
            String[] parts = strings.get(1).split("\\-");
            newEvent.setLocationName(parts[0]);
            newEvent.setLocationAddress(parts[1]);
            newEvent.setMaxVisitors(Integer.parseInt(strings.get(2)));


            viewSourceController.toInsetTicketTypeView(this, newEvent);
        } catch (NullPointerException | NumberFormatException | ArrayIndexOutOfBoundsException e) {
            JOptionPane.showMessageDialog(null, "Compilare tutti i campi prima di procedere", "Form Error",
                    JOptionPane.ERROR_MESSAGE);
        }

    }

    public void ticketTypeNext(List<EventModel.Sectors> sectorsList) {
        newEvent.setSectorList(sectorsList);
        double price = newEvent.getSectorList().get(0).getPrize();
        for (int i = 1; i < newEvent.getSectorList().size(); i++) {
            if (newEvent.getSectorList().get(i).getPrize() < price) {
                price = newEvent.getSectorList().get(i).getPrize();
            }
        }
        newEvent.setPrice(price);
        viewSourceController.toInsertReductionView(this, newEvent);
    }

    public void ticketReductionNext() {
        viewSourceController.toInsertRecap(this, imagesList, newEvent);
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

    public void insert(List<Image> image) throws IOException {
        StorageController sg = new StorageController();
        CountDownLatch latch = new CountDownLatch(1);

        try {
            List<Image> imageList = sg.upload(image, playbill, latch);

            newEvent.setBillboard(imageList.get(0));
            imageList.remove(0);
            newEvent.setSlideshow(imageList);
            new LoadingPopupView(latch);
            dbController.insert(newEvent);
            viewSourceController.toDash();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void setImagesList(List<Image> imagesList) {
        this.imagesList.clear();
        this.imagesList.addAll(imagesList);
    }
}
