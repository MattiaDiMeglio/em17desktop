package controller;

import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import model.EventListModel;
import model.EventModel;
import model.LocationListModel;
import model.LocationModel;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Classe controller che si occupa dell'inserimento di un nuovo evento.
 *
 * @author ingsw20
 */
public class InsertController {

  /**
   * {@link EventModel} contenente i dati del nuovo evento da inserire.
   */
  private EventModel newEvent;

  /**
   * instanza di {@link ViewSourceController}.
   */
  private ViewSourceController viewSourceController;

  /**
   * Instanza di {@link DBController}.
   */
  private DBController dbController = DBController.getInstance();

  /**
   * Instanza di {@link EventListModel}.
   */
  private EventListModel eventListModel = EventListModel.getInstance();

  /**
   * instanza di {@link LocationListModel}.
   */
  private LocationListModel locationListModel = LocationListModel.getInstance();

  /**
   * Lista delle immagini che verranno caricate nello storage del server.
   */
  private List<Image> imagesList = new ArrayList<>();

  /**
   * id della vecchia location in caso di modifica evento.
   */
  private String oldLocationID;

  /**
   * costruttore dell'insertController.
   *
   * @param viewSourceController variabile per l'inizializzazione di {@link #viewSourceController}
   */
  InsertController(ViewSourceController viewSourceController) {
    this.viewSourceController = viewSourceController;
  }

  /**
   * metodo chiamato dai listener dei bottoni indietro di tutta la sequenza di schermate. chiama.
   * {@link ViewSourceController#turnBack()}
   */
  public void toInsertView() {
    viewSourceController.toInsertView(newEvent, this);
  }

  /**
   * metodo chiamato dal listener del bottone indietro della prima schermata di inserimento chiama.
   * {@link ViewSourceController#toDash()}
   */
  public void toDash() {
    viewSourceController.toDash();
  }

  /**
   * metodo per quando si torna alla schermata di ticketType.
   */
  public void toTicketType() {
    viewSourceController.toInsetTicketTypeView(this, newEvent);
  }

  /**
   * metodo chiamato dal listener del bottone conferma della prima schermata di isnerimento.
   *
   * @param strings lista contenente i dati con il quale popolare il model
   * @param insertPlaybillImageView immagine di copertina da settare nel model
   */
  public void toTicketType(EventModel eventModel, List<String> strings,
      Image insertPlaybillImageView) {
    newEvent = eventModel;
    try {
      newEvent.setEventName(strings.get(0));//Valorizza il nome dell'evento
      newEvent.setEventDescription(strings.get(3));//Valorizza la descrizione evento
      newEvent.setBillboard(
          insertPlaybillImageView);//Setta l'immagine di copertina momentanea(verrà sostituita dopo l'upload
      newEvent.setStartingDate(strings.get(4));//Valirizza la data d'inizio
      newEvent.setEndingDate(strings.get(5));//Valorizza la data di fine
      newEvent.setSlideshow(imagesList);
      String[] parts = strings.get(1)
          .split("-");//si splitta il valore inserito dall'autocompletamento come location
      if (!(parts[0].equals(newEvent.getLocationName()) && parts[1]
          .equals(newEvent.getLocationAddress()))
          || newEvent.getSectorList().isEmpty()) {
        newEvent.setLocationName(
            parts[0]);//la prima parte dello split va a valorizzare il nome location
        newEvent
            .setLocationAddress(parts[1]);//La seconda parte valorizza l'indirizzo della location
        oldLocationID = newEvent.getLocationID();
        newEvent.setSectorList(createSector(parts[0], parts[1]));
      }
      newEvent
          .setMaxVisitors(Integer.parseInt(strings.get(2)));//Valorizza il massimo dei visitatori

      viewSourceController.toInsetTicketTypeView(this, newEvent);//Cambio di schermata
    } catch (NullPointerException | NumberFormatException | ArrayIndexOutOfBoundsException e) {
      //In caso manchino elementi dell'evento parte un'alert che avverte l'utente
      JOptionPane
          .showMessageDialog(null, "Compilare tutti i campi prima di procedere", "Form Error",
              JOptionPane.ERROR_MESSAGE);
      e.printStackTrace();
    }
  }

  /**
   * metodo chiamato dal listener del bottone conferma della terza schermata di isnerimento.
   */
  public void toInsertRecap(List<Double> reductions) {
    newEvent.setChildrenReduction(reductions.get(0));
    newEvent.setEldersReduction(reductions.get(1));
    newEvent.setStudentReduction(reductions.get(2));
    viewSourceController.toInsertRecapView(this, imagesList, newEvent);//cambio schermata
  }

  /**
   * il metodo crea la lista dei settori.
   *
   * @param name nome della location
   * @param address indirizzo della location
   * @return lista di settori
   */
  private List<EventModel.Sectors> createSector(String name, String address) {
    for (LocationModel location : locationListModel.getLocationList()) {
      //se la location corrente ha nome e indirizzo uguale a quelli passati al metodo,
      // restituisce la lista dei settori
      if ((location.getLocationAddress().equals(address)) && (location.getLocationName()
          .equals(name))) {
        List<EventModel.Sectors> sectorsList = new ArrayList<>();
        List<String> seats = getSteatsList(newEvent.getLocationName(),
            newEvent.getLocationAddress());
        for (String s : location.getSectorList()) {
          EventModel.Sectors sectors = new EventModel().new Sectors();
          sectors.setName(s);
          sectors.setSeats(Integer.parseInt(seats.get(0)));
          sectorsList.add(sectors);
        }
        return sectorsList;
      }
    }
    return null;
  }

  /**
   * il metodo procura l'id della location scelta.
   *
   * @param name nome location
   * @param address indirizzo location
   * @return id della location
   */
  private String getLocationID(String name, String address) {
    for (LocationModel location : locationListModel.getLocationList()) {
      //se la location corrente ha nome e indirizzo uguale a quelli passati al metodo,
      // restituisce la lista dei settori
      if ((location.getLocationAddress().equals(address)) && (location.getLocationName()
          .equals(name))) {
        return location.getLocationID();
      }
    }
    return null;
  }

  /**
   * metodo che serve per ottenere il numero di posti dei settori collegati alla location
   * selezionata.
   *
   * @param name nome della location
   * @param address indirizzo della location
   * @return numero di posti dei settori collegati alla location selezionata
   */
  public List<String> getSteatsList(String name, String address) {
    for (LocationModel location : locationListModel.getLocationList()) {
      //se la location corrente ha nome e indirizzo uguale a quelli passati al metodo,
      // restituisce la lista dei posti per settore
      if ((location.getLocationAddress().equals(address)) && (location.getLocationName()
          .equals(name))) {
        return location.getSeatsList();
      }
    }
    return null;
  }

  /**
   * Metodo che serve per ottenere il numero massimo di posti per la location.
   *
   * @param location nome della location
   * @return numero massimo di posti per la location
   */
  public String getMaxVisitors(String location) {
    int i = 0;
    while (i < eventListModel.getListaEventi().size() - 1) {
      //se la location corrisponde a quella dell'eventlistmodel ritorna il massimo di visitatori
      if (location.toLowerCase()
          .equals(eventListModel.getListaEventi().get(i).getLocationName().toLowerCase())) {
        return eventListModel.getListaEventi().get(i).getMaxVisitors().toString();
      }
      i++;
    }
    return null;
  }

  /**
   * Metodo che serve per ottenere la lista dei nomi delle location con i relativi indirizzi.
   *
   * @return lista dei nomi delle location con i relativi indirizzi
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
   * Metodo per il settaggio dell'imageList necessaria per la creazione degli slideshow e per
   * l'upload.
   *
   * @param imagesList imagelist aggiornata
   */
  public void setImagesList(List<Image> imagesList) {
    this.imagesList.clear();//pulisce la lista
    this.imagesList.addAll(imagesList);//setta la nuova lista
  }

  /**
   * metodo che avvia l'inserimento nel database.
   *
   * @param image lista con le immagini da inserire
   */
  public void insert(List<Image> image) {
    StorageController sg = new StorageController();//instanzia lo storageController
    CountDownLatch latchUpload = new CountDownLatch(1);//crea un latch per il thread di upload
    CountDownLatch latchInsert = new CountDownLatch(
        1);// latch per l'inserimento dell'evento nel database

    try {
      if (newEvent.getLocationID().equals("")) {
        dbController.insert(newEvent, latchUpload,
            latchInsert); //finito il thread di upload si inserisce l'evento nel db
      } else {
        newEvent.setLocationID(
            getLocationID(newEvent.getLocationName(), newEvent.getLocationAddress()));
        latchUpload.countDown();
        if (newEvent.getLocationID().equals(oldLocationID)) {
          dbController.updateChild(newEvent, latchInsert);
        } else {
          dbController.updateChild(newEvent, oldLocationID, latchInsert);
        }
      }
      //crea la lista con i link delle immagini caricate
      List<Image> imageList = sg
          .upload(newEvent, image, newEvent.getBillboard(), latchUpload, latchInsert);
      //la prima immagine della lista è la copertina
      newEvent.setBillboard(imageList.get(0));
      imageList.remove(0);//si rimuove la copertina dalla lista di immagini
      newEvent.setSlideshow(imageList);//si setta la lista rimanente come lista di immagini
      //new LoadingPopupView(latch); //si crea il popup di caricamento
      viewSourceController.toDash();//cambio schermata
    } catch (InterruptedException e) {
      e.printStackTrace(); // todo mettere un alert per riavviare l'inserimento
    }
  }

  /**
   * metodo invocato dalle view per aggiornarsi.
   *
   * @param eventModel model dal quale prelevare i dati
   */
  public void update(EventModel eventModel) {
    eventModel.notifyMyObservers();
  }

  /**
   * metodo per quando si torna alla schermata di insertReduction.
   */
  public void toInsertReduction() {
    viewSourceController.toInsertReductionView(this, newEvent);
  }

  /**
   * metodo per andare alla schermata di ticketType.
   *
   * @param priceList lista con i TextField contenente i prezzi
   * @param reductionCheckList lista con i checkbox per la verifica
   * @param seatsFieldsList lista di TextField contenente il numero di posti per settore
   */
  public void toInsertReduction(List<TextField> priceList, List<CheckBox> reductionCheckList,
      List<TextField> seatsFieldsList) {
    double price = Double.parseDouble(priceList.get(0).getText());
    for (int i = 0; i < newEvent.getSectorList().size(); i++) {
      newEvent.getSectorList().get(i).setPrice(Double.valueOf(priceList.get(i).getText()));
      newEvent.getSectorList().get(i).setReduction(reductionCheckList.get(i).isSelected());
      newEvent.getSectorList().get(i).setSeats(Integer.parseInt(seatsFieldsList.get(i).getText()));

      if (newEvent.getSectorList().get(i).getPrice() < price) {
        price = newEvent.getSectorList().get(i).getPrice();
      }
    }

    //setta il minore dei prezzi per settore come prezzo dell'evento
    newEvent.setPrice(price);
    viewSourceController.toInsertReductionView(this, newEvent);//cambio di schermata
  }
}
