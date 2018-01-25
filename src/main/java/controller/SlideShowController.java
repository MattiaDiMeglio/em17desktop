package controller;

import javafx.beans.Observable;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import model.EventListModel;
import view.SlideShowView;

import java.util.ArrayList;
import java.util.List;
import java.util.Observer;

public class SlideShowController {


    EventListModel eventListModel = EventListModel.getInstance();
    ViewSourceController viewSourceController;

   //SlideShowView slideShowView = new SlideShowView();

    public void createSlide(HBox hBox, Button dashBoardSlideShowLeftButton, Button dashBoardSlideShowRightButton, ViewSourceController viewSourceController){
        SlideShowView slideShowView = new SlideShowView(hBox, dashBoardSlideShowLeftButton, dashBoardSlideShowRightButton, this);
        this.viewSourceController = viewSourceController;

    }

    public void handler(int index) {

        viewSourceController.toEventView(index);


    }
}
