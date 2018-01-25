package view;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import model.SlideShowModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class SlideShowView implements Observer {



    public SlideShowView () {
        //slideShowModel.addObserver(this);
    }

    public void addToSlideshow ( ){

            //hBox.getChildren().add(imageList.get(i));
        }

    @Override
    public void update(Observable o, Object arg) {

    }
}
