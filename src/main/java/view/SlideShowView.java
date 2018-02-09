package view;

import controller.SlideShowController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import model.EventListModel;
import model.EventModel;


import java.util.*;
import java.util.concurrent.CountDownLatch;

/**
 * View che si occupa dello slideshow di immagini
 * implementa observer come secondo architettura MVC
 */
public class SlideShowView implements Observer {

    /**
     * lista dei bottoni con immagini da caricare nello slideshow
     */
    private List<Button> buttonList;
    /**
     * instanza di eventListModel
     */
    private EventListModel eventListModel = EventListModel.getInstance();
    /**
     * istanza di eventModel
     */
    private EventModel eventModel;
    /**
     * instanza di slideShowController
     */
    private SlideShowController slideShowController;
    /**
     * Hbox che conterrà i bottoni con le immagini
     */
    private HBox hBox;
    /**
     * lista dei bottoni visibili nella schermata corrente
     */
    private List<Integer> activeList;

    /**
     * Costruttore usato per la shashboard
     *
     * @param hBox                          hbox contenente i bottoni
     * @param dashBoardSlideShowLeftButton  bottone sinistro dello slideshow
     * @param dashBoardSlideShowRightButton bottone destro dello slideshow
     * @param slideShowController           istanza del controller
     */
    public SlideShowView(HBox hBox, Button dashBoardSlideShowLeftButton,
                         Button dashBoardSlideShowRightButton, SlideShowController slideShowController) {
        eventListModel.addObserver(this); //setto come observer della lista di eventi
        this.slideShowController = slideShowController; //valorizzo l'instanza locale di slideshowcontroller
        this.hBox = hBox; //vsetto l'hbox
        buttonList = new ArrayList<>(); //Instanzio le liste di bottoni
        activeList = new ArrayList<>();
        //listener per il bottone destro
        dashBoardSlideShowRightButton.setOnAction(event -> {
            right();
        });
        //listener per il bottone sinistro
        dashBoardSlideShowLeftButton.setOnAction(event -> {
            left();
        });
        //creazione dei bottoni nella lista
        if (hBox != null) {
            hBox.setAlignment(Pos.CENTER);//setto l'allineamento adell'Hbox a center
            hBox.setSpacing(20); //setto lo spacing
            //creo otto bottoni diversi, numero scelto arbitrariamente
            for (int i = 0; i < 8; i++) {
                Button button = new Button(); //instanzio il nuovo bottone
                buttonList.add(button); //lo aggiungo alla lista dei bottoni
                if (i < 4) {
                    hBox.getChildren().add(button); //se l'indice è minore di 4 verrà reso visibile
                    activeList.add(i); //e aggiunto alla lista dei bottoni attivi
                }
            }
        }
    }

    /**
     * Costruttore usato per la eventView
     *
     * @param hBox                      hbox contenente i bottoni
     * @param eventSlideShowLeftButton  bottone sinistro dello slideshow
     * @param eventSlideShowRightButton bottone destro dello slideshow
     * @param slideShowController       istanza del controller
     * @param eventModel                instanza del model a cui la schermata eventView e quindi lo slideshow fa riferimento
     */
    public SlideShowView(HBox hBox, Button eventSlideShowLeftButton,
                         Button eventSlideShowRightButton, SlideShowController slideShowController, EventModel eventModel) {
        int i = 0;
        this.eventModel = eventModel; //valorizzazione del model
        this.slideShowController = slideShowController; //si valorizza il controller
        this.hBox = hBox; //si valorizza l'Hbox
        buttonList = new ArrayList<>(); //si crea la lista di bottoni
        activeList = new ArrayList<>(); //si crea la lista dei bottoni attivi
        eventModel.addObserver(this); //si setta la view come observer

        //listener per il bottone right dello slideshow
        eventSlideShowRightButton.setOnAction(event -> {
            right();
        });

        //listener per il bottone left dello slideshow
        eventSlideShowLeftButton.setOnAction(event -> {
            left();
        });


        if (hBox != null) {
            //elimina gli elementi nell'hbox
            while (hBox.getChildren().size() > 0) {
                hBox.getChildren().remove(hBox.getChildren().size() - 1);
            }
            //setta l'allineamento e lo spacing
            hBox.setAlignment(Pos.CENTER);
            hBox.setSpacing(20);
            //si creano i bottoni con le immagini, li si mette nella lista
            //e se l'indice è minore di 4 li si isnreice nella lista dei bottoni attivi
            //e nell'Hbox
            for (i = 0; i < eventModel.getSlideshow().size(); i++) {
                Button button = new Button();
                buttonList.add(button);
                Image image = eventModel.getSlideshow().get(i);
                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(300.0);
                imageView.setFitHeight(280.0);
                buttonList.get(i).setGraphic(imageView);
                if (i < 4) {
                    hBox.getChildren().add(button);
                    activeList.add(i);
                }
            }
        }
    }

    /**
     * costruttore usato per la insertView
     *
     * @param hBox                  hbox cetrale
     * @param left                  bottone left
     * @param right                 bottone right
     * @param slideShowController   controllore dello slideshow
     * @param immagini              lista di immagini
     */
    public SlideShowView(HBox hBox, Button left, Button right, SlideShowController slideShowController, List<Image> immagini) {
        int i = 0; //
        buttonList = new ArrayList<>(); //si crea la lista di bottoni
        activeList = new ArrayList<>(); //si crea la lista dei bottoni attivi

        this.slideShowController = slideShowController; //valorizza il controller
        this.hBox = hBox; //valorizza l'Hbox

        //listener per il bottone right
        right.setOnAction(event -> {
            right();
        });

        //listener per il bottone left
        left.setOnAction(event -> {
            left();
        });

        if (hBox != null) {
            //setta l'allineamento e lo spacing
            hBox.setAlignment(Pos.CENTER);
            hBox.setSpacing(20);
            //si creano i bottoni con le immagini, li si mette nella lista
            //e se l'indice è minore di 4 li si isnreice nella lista dei bottoni attivi
            //e nell'Hbox
            for (i = 0; i < immagini.size(); i++) {
                Button button = new Button();
                buttonList.add(button);
                if (i < 4) {
                    hBox.getChildren().add(button);
                    activeList.add(i);
                }
                int finalI = i;
                button.setOnAction(event -> {
                    if (deleteConfirm()) {
                        buttonList.remove(button);
                        activeList.remove(finalI);
                        immagini.remove(finalI);
                        hBox.getChildren().remove(button);
                        slideShowController.setImageList(immagini);
                    }
                });
                //si crea la lista di immagini che verrà caricata nello storage
                ImageView imageView = new ImageView(immagini.get(i));
                imageView.setFitWidth(300.0);
                imageView.setFitHeight(280.0);
                buttonList.get(i).setGraphic(imageView);
            }
        }
    }


    //metodo chiamato dal listener del bottone right
    private void right() {
        //in caso si possa ancora fare slide a destra
        if (activeList.get(3) < (buttonList.size() - 1)) {
            //slide della active list
            activeList.set(0, activeList.get(0) + 1);
            activeList.set(1, activeList.get(1) + 1);
            activeList.set(2, activeList.get(2) + 1);
            activeList.set(3, activeList.get(3) + 1);
            //si rimuovono gli elementi dell'hbox
            hBox.getChildren().remove(3);
            hBox.getChildren().remove(2);
            hBox.getChildren().remove(1);
            hBox.getChildren().remove(0);
            //si inseriscono i nuovi elementi
            hBox.getChildren().add(0, buttonList.get(activeList.get(0)));
            hBox.getChildren().add(1, buttonList.get(activeList.get(1)));
            hBox.getChildren().add(2, buttonList.get(activeList.get(2)));
            hBox.getChildren().add(3, buttonList.get(activeList.get(3)));
        }

    }

    //metodo chiamato dal listener del bottone left
    private void left() {
        //in caso si possa ancora fare slide a sinistra
        if (activeList.get(0) > 0) {
            //slide della active list
            activeList.set(0, activeList.get(0) - 1);
            activeList.set(1, activeList.get(1) - 1);
            activeList.set(2, activeList.get(2) - +1);
            activeList.set(3, activeList.get(3) - 1);
            //si rimuovono gli elementi dell'hbox
            hBox.getChildren().remove(3);
            hBox.getChildren().remove(2);
            hBox.getChildren().remove(1);
            hBox.getChildren().remove(0);
            //si inseriscono i nuovi elementi
            hBox.getChildren().add(0, buttonList.get(activeList.get(0)));
            hBox.getChildren().add(1, buttonList.get(activeList.get(1)));
            hBox.getChildren().add(2, buttonList.get(activeList.get(2)));
            hBox.getChildren().add(3, buttonList.get(activeList.get(3)));
        }


    }


    /**
     * update dell'observer
     *
     * @param o     observable
     * @param arg   argomenti mandati con il notify
     */
    @Override
    public void update(Observable o, Object arg) {
        hBox.getChildren().removeAll(); //pulisce l'hobox
        //se l'update viene da EventListModel
        if (o instanceof EventListModel) {
            int eventIndex = (int) arg;
            //creazione dello slideshow con le copertine degli eventi
            if (eventIndex < buttonList.size()) {
                Image image = eventListModel.getListaEventi().get(eventIndex).getBillboard();
                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(300.0);
                imageView.setFitHeight(280.0);
                buttonList.get(eventIndex).setGraphic(imageView);
                buttonList.get(eventIndex).setOnAction(event -> {
                    slideShowController.handler(eventIndex);
                });
            }
        } else {
            //creazione dello slide con le immagini dell'evento
            int i = 0;
            while (i < eventModel.getSlideshow().size()) {
                Image image = eventModel.getSlideshow().get(i);
                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(300.0);
                imageView.setFitHeight(280.0);
                buttonList.get(i).setGraphic(imageView);
                i++;

            }

        }
    }

    /**
     * metodo che si occupa dell'alert pre-cancellazione
     * @return
     */
    private boolean deleteConfirm() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Eliminazione immagine");
        alert.setHeaderText("Eliminazione Immagine");
        alert.setContentText("Eliminare definitivamente l'immagine?");

        Optional<ButtonType> result = alert.showAndWait();
        return result.get() == ButtonType.OK;
    }

}
