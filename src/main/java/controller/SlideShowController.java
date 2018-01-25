package controller;

import javafx.beans.Observable;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import model.SlideShowModel;
import view.SlideShowView;

import java.util.ArrayList;
import java.util.List;
import java.util.Observer;

public class SlideShowController implements Observer{

    SlideShowModel slideShowModel = new SlideShowModel();
    SlideShowView slideShowView = new SlideShowView(slideShowModel);
    List<SlideShowModel.DashBillBoard> imageList = new ArrayList<>();

    public void createList( String locandina, int arg){
        SlideShowModel slideShowModel = new SlideShowModel();
        SlideShowView slideShowView = new SlideShowView(slideShowModel);
        List<SlideShowModel.DashBillBoard> imageList = new ArrayList<>();

    }

    public void addToList(HBox dashSlide, String locandina, int index){
        slideShowModel.add(locandina, index);
        slideShowView.addToSlideshow(dashSlide, imageList);
    }


    @Override
    public void update(java.util.Observable o, Object arg) {

    }
}
