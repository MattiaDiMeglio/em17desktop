package controller;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import model.EventModel;
import view.SlideShowView;

import java.util.List;

public class SlideShowController {
    private InsertController insertController;
    private ViewSourceController viewSourceController;

    public void createSlide(HBox hBox, Button dashBoardSlideShowLeftButton,
                            Button dashBoardSlideShowRightButton, ViewSourceController viewSourceController){
        new SlideShowView(hBox, dashBoardSlideShowLeftButton, dashBoardSlideShowRightButton, this);
        this.viewSourceController = viewSourceController;
    }

    public void createSlide (HBox hBox, Button dashBoardSlideShowLeftButton,
                             Button dashBoardSlideShowRightButton, ViewSourceController viewSourceControl, EventModel eventModel){
        SlideShowView slideShowView = new SlideShowView(hBox, dashBoardSlideShowLeftButton, dashBoardSlideShowRightButton, this, eventModel);
        this.viewSourceController = viewSourceController;
    }

    public void createSlide(InsertController insertController, Button left, HBox hbox, Button right, List<Image> immagini) {
        this.insertController = insertController;
        SlideShowView slideShowView = new SlideShowView( hbox, left, right, this, immagini);
        this.viewSourceController = viewSourceController;
    }


    public void handler(int index) {
        viewSourceController.toEventView(index);
    }

    public void setImageList(List<Image> imageList){
        insertController.setImagesList(imageList);
    }


}
