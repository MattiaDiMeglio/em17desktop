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
     */
    public void toDash() {
        viewSourceController.toDash();
    }

    /**
     * metodo chiamato dal listener del bottone conferma della prima schermata di isnerimento
     *
     * @param strings
     * @param insertPlaybillImageView
     */
    public void insertNext(List<String> strings, Image insertPlaybillImageView) {
        try {
            newEvent.setEventName(strings.get(0));//Valorizza il nome dell'evento
            newEvent.setEventDescription(strings.get(3));//Valorizza la descrizione evento
            newEvent.setBillboard(insertPlaybillImageView);//Setta l'immagine di copertina momentanea(verrà sostituita dopo l'upload
            newEvent.setStartingDate(strings.get(4));//Valirizza la data d'inizio
            newEvent.setEndingDate(strings.get(5));//Valorizza la data di fine
            String[] parts = strings.get(1).split("\\-");//si splitta il valore inserito dall'autocompletamento come location
            newEvent.setLocationName(parts[0]);//la prima parte dello split va a valorizzare il nome location
            newEvent.setLocationAddress(parts[1]);//La seconda parte valorizza l'indirizzo della location
            newEvent.setMaxVisitors(Integer.parseInt(strings.get(2)));//Valorizza il massimo dei visitatori

            viewSourceController.toInsetTicketTypeView(this, newEvent);//Cambio di schermata
        } catch (NullPointerException | NumberFormatException | ArrayIndexOutOfBoundsException e) {
            //In caso manchino elementi dell'evento parte un'alert che avverte l'utente
            JOptionPane.showMessageDialog(null, "Compilare tutti i campi prima di procedere", "Form Error",
                    JOptionPane.ERROR_MESSAGE);
        }

    }

    /**
     * metodo chiamato dal listener del bottone conferma della seconda schermata di isnerimento
     *
     * @param sectorsList
     */
    public void ticketTypeNext(List<EventModel.Sectors> sectorsList) {
        newEvent.setSectorList(sectorsList);//Valorizza la lista dei settori
        //Inserisce il primo dei prezzi per settore in una variabile di supporto
        double price = newEvent.getSectorList().get(0).getPrize();
        for (int i = 1; i < newEvent.getSectorList().size(); i++) {
            //e poi controlla che quello inserito sia il prezzo minore,
            // nel caso non lo sia sostituisce il valore in price
            if (newEvent.getSectorList().get(i).getPrize() < price) {
                price = newEvent.getSectorList().get(i).getPrize();
            }
        }
        //setta il minore dei prezzi per settore come prezzo dell'evento
        newEvent.setPrice(price);
        viewSourceController.toInsertReductionView(this, newEvent);//cambio di schermata
    }

    /**
     * metodo chiamato dal listener del bottone conferma della terza schermata di isnerimento
     */
    public void ticketReductionNext() {
        viewSourceController.toInsertRecapView(this, imagesList, newEvent);//cambio schermata
    }


    /**
     * metodo che serve per ottenere i nomi dei settori collegati alla location selezionata
     *
     * @param name
     * @param address
     * @return
     */
    public List<String> getSectorName(String name, String address) {
        for (LocationModel location : locationListModel.getLocationList()) {
            //se la location corrente ha nome e indirizzo uguale a quelli passati al metodo, restituisce la lista dei settori
            if ((location.getLocationAddress().equals(address)) && (location.getLocationName().equals(name))) {
                return location.getSectorList();
            }

        }
        return null;
    }

    /**
     * metodo che serve per ottenere il numero di posti dei settori collegati alla location selezionata
     *
     * @param name
     * @param address
     * @return
     */
    public List<String> getSteatsList(String name, String address) {
        for (LocationModel location : locationListModel.getLocationList()) {

            //se la location corrente ha nome e indirizzo uguale a quelli passati al metodo, restituisce la lista dei posti per settore
            if ((location.getLocationAddress().equals(address)) && (location.getLocationName().equals(name))) {
                return location.getSeatsList();
            }

        }
        return null;
    }

    /**
     * Metodo che serve per ottenere il numero massimo di posti per la location
     *
     * @param location
     * @return
     */
    public String getMaxVisitors(String location) {
        int i = 0;
        while (i < eventListModel.getListaEventi().size() - 1) {
            //se la location corrisponde a quella dell'eventlistmodel ritorna il massimo di visitatori
            if (location.toLowerCase().equals(eventListModel.getListaEventi().get(i).getLocationName().toLowerCase())) {
                return eventListModel.getListaEventi().get(i).getMaxVisitors().toString();
            }
            i++;
        }
        return null;
    }

    /**
     * Metodo che serve per ottenere la lista dei nomi delle location con i relativi indirizzi
     *
     * @return
     */
    public List<String> getLocations() {
        List<String> locations = new ArrayList<>();//crea una lista di stringhe
        for (LocationModel locationModel : locationListModel.getLocationList()) {
            //per ogni location inserisce nome e location nella lista locations
            locations.add(locationModel.getLocationName() + "-" + locationModel.getLocationAddress());
        }
        return locations;
    }

    /**
     * Metodo per il settaggio dell'imageList necessaria per la creazione degli slideshow
     * e per l'upload
     *
     * @param imagesList
     */
    public void setImagesList(List<Image> imagesList) {
        this.imagesList.clear();//pulisce la lista
        this.imagesList.addAll(imagesList);//setta la nuova lista
    }

    /**
     * @param image
     * @throws IOException
     */
    public void insert(List<Image> image) throws IOException {
        StorageController sg = new StorageController();//instanzia lo storageController
        CountDownLatch latch = new CountDownLatch(1);//crea un latch per il thread di upload

        try {
            //crea la lista con i link delle immagini caricate
            List<Image> imageList = sg.upload(image, newEvent.getBillboard(), latch);
            //la prima immagine della lista è la copertina
            newEvent.setBillboard(imageList.get(0));
            imageList.remove(0);//si rimuove la copertina dalla lista di immagini
            newEvent.setSlideshow(imageList);//si setta la lista rimanente come lista di immagini
            new LoadingPopupView(latch); //si crea il popup di caricamento
            dbController.insert(newEvent); //finito il thread di upload si inserisce l'evento nel db
            viewSourceController.toDash();//cambio schermata
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
