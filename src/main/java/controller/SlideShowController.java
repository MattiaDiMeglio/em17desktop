package controller;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import model.EventModel;
import view.SlideShowView;

import java.util.List;

/**
 * Controller per gli slideshow
 */
public class SlideShowController {
    private InsertController insertController; //instanza dell'insertController
    private ViewSourceController viewSourceController; //instanza del viewSourceController

    /**
     * Metodo per la costruzione dello slideshow, chiamato da dashboard
     *
     * @param hBox                          hbox dove si inseriranno le immagini
     * @param dashBoardSlideShowLeftButton  bottone sinistro della dashboard
     * @param dashBoardSlideShowRightButton bottone destro della dashboard
     * @param viewSourceController          viewSourceController
     */
    public void createSlide(HBox hBox, Button dashBoardSlideShowLeftButton,
                            Button dashBoardSlideShowRightButton, ViewSourceController viewSourceController){

        new SlideShowView(hBox, dashBoardSlideShowLeftButton, dashBoardSlideShowRightButton, this);

        this.viewSourceController = viewSourceController;
    }

    /**
     * @param hBox                      hbox dove si isneriranno le immagini
     * @param eventSlideShowLeftButton  bottone sinistro
     * @param eventSlideShowRightButton bottone destro
     * @param viewSourceController         viewSourceController
     * @param eventModel                eventModel da cui ottenere le immagini
     */
    public void createSlide (HBox hBox, Button eventSlideShowLeftButton,
                             Button eventSlideShowRightButton, ViewSourceController viewSourceController, EventModel eventModel){

        new SlideShowView(hBox, eventSlideShowLeftButton, eventSlideShowRightButton, this, eventModel);

        this.viewSourceController = viewSourceController;
    }

    public void createSlide(InsertController insertController, Button left, HBox hbox, Button right, List<Image> immagini) {

        this.insertController = insertController;

        new SlideShowView( hbox, left, right, this, immagini);
    }


    /**
     * metodo per l'handler nella dhasboard, che permette di indirizzzare all'evento selezionato
     * @param index indice dell'evento
     */
    public void handler(int index) {
        viewSourceController.toEventView(index);
    }

    /**
     * metodo per la modifica della lista immagini
     * @param imageList lista immagini
     */
    public void setImageList(List<Image> imageList){
        insertController.setImagesList(imageList);
    }


}
