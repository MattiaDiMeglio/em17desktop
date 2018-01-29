package view;

import controller.SlideShowController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import model.EventListModel;
import model.EventModel;


import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * View che si occupa dello slideshow di immagini
 *
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
     *
     * Costruttore usato per la shashboard
     *
     *
     * @param hBox hbox contenente i bottoni
     * @param dashBoardSlideShowLeftButton bottone sinistro dello slideshow
     * @param dashBoardSlideShowRightButton bottone destro dello slideshow
     * @param slideShowController istanza del controller
     */
    public SlideShowView(HBox hBox, Button dashBoardSlideShowLeftButton,
                         Button dashBoardSlideShowRightButton, SlideShowController slideShowController) {
        eventListModel.addObserver(this); //setto come observer della lista di eventi
        this.slideShowController = slideShowController; //valorizzo l'instanza locale di slideshowcontroller
        this.hBox=hBox; //vsetto l'hbox
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
                if (i<4){
                    hBox.getChildren().add(button); //se l'indice è minore di 4 verrà reso visibile
                    activeList.add(i); //e aggiunto alla lista dei bottoni attivi
                }
            }
        }
    }

    /**
     *
     * Costruttore usato per la eventView
     *
     * @param hBox hbox contenente i bottoni
     * @param dashBoardSlideShowLeftButton bottone sinistro dello slideshow
     * @param dashBoardSlideShowRightButton bottone destro dello slideshow
     * @param slideShowController istanza del controller
     * @param eventModel //instanza del model a cui la schermata eventView e quindi lo slideshow fa riferimento
     */
    public SlideShowView(HBox hBox, Button dashBoardSlideShowLeftButton,
                         Button dashBoardSlideShowRightButton, SlideShowController slideShowController, EventModel eventModel) {
        int i=0; // 
        this.eventModel = eventModel;
        eventModel.addObserver(this);
        buttonList = new ArrayList<>();
        activeList = new ArrayList<>();

        this.slideShowController = slideShowController;
        this.hBox=hBox;
        dashBoardSlideShowRightButton.setOnAction(event -> {
            right();
        });
        dashBoardSlideShowLeftButton.setOnAction(event -> {
            left();
        });
        if (hBox != null) {
            while (hBox.getChildren().size() > 0){
                   hBox.getChildren().remove(hBox.getChildren().size() -1);
            }
            hBox.setAlignment(Pos.CENTER);
            hBox.setSpacing(20);
            for (i = 0; i < eventModel.getSlideshow().size(); i++) {
                Button button = new Button();
                buttonList.add(button);
                Image image = new Image(eventModel.getSlideshow().get(i));
                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(300.0);
                imageView.setFitHeight(280.0);
                buttonList.get(i).setGraphic(imageView);
                if (i<4){
                    hBox.getChildren().add(button);
                    activeList.add(i);
                }
            }
        }
    }


    private void right() {
        if (activeList.get(3)< (buttonList.size() -1)){
            activeList.set(0, activeList.get(0)+1);
            activeList.set(1, activeList.get(1)+1);
            activeList.set(2, activeList.get(2)+1);
            activeList.set(3, activeList.get(3)+1);
            hBox.getChildren().remove(3);
            hBox.getChildren().remove(2);
            hBox.getChildren().remove(1);
            hBox.getChildren().remove(0);
            hBox.getChildren().add(0, buttonList.get(activeList.get(0)));
            hBox.getChildren().add(1, buttonList.get(activeList.get(1)));
            hBox.getChildren().add(2, buttonList.get(activeList.get(2)));
            hBox.getChildren().add(3, buttonList.get(activeList.get(3)));
        }

    }

    private void left(){
        if (activeList.get(0)>0){
            activeList.set(0, activeList.get(0)-1);
            activeList.set(1, activeList.get(1)-1);
            activeList.set(2, activeList.get(2)-+1);
            activeList.set(3, activeList.get(3)-1);
            hBox.getChildren().remove(3);
            hBox.getChildren().remove(2);
            hBox.getChildren().remove(1);
            hBox.getChildren().remove(0);
            hBox.getChildren().add(0, buttonList.get(activeList.get(0)));
            hBox.getChildren().add(1, buttonList.get(activeList.get(1)));
            hBox.getChildren().add(2, buttonList.get(activeList.get(2)));
            hBox.getChildren().add(3, buttonList.get(activeList.get(3)));
        }


    }

    @Override
    public void update(Observable o, Object arg) {
        hBox.getChildren().removeAll();

        if (o instanceof EventListModel) {
            int eventIndex = (int) arg;

            if (eventIndex <= buttonList.size()) {
                //TODO mettere il popolamento in un thread separato
                Image image = new Image(eventListModel.getListaEventi().get(eventIndex).getBillboard());
                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(300.0);
                imageView.setFitHeight(280.0);
                buttonList.get(eventIndex).setGraphic(imageView);
                buttonList.get(eventIndex).setOnAction(event -> {
                    slideShowController.handler(eventIndex);
                });
            }
        } else {
            int i=0;
            while (i < eventModel.getSlideshow().size()){
                Image image = new Image(eventModel.getSlideshow().get(i));
                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(300.0);
                imageView.setFitHeight(280.0);
                buttonList.get(i).setGraphic(imageView);
                i++;

            }

        }
    }

}
