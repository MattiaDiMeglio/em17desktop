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


    List<SlideShowModel.DashBillBoard> imageList = new ArrayList<>();

    public SlideShowView (SlideShowModel slideShowModel) {
        slideShowModel.addObserver(this);
    }

    public void addToSlideshow (HBox hBox, List<SlideShowModel.DashBillBoard> imageList){
        for (int i=0; i < imageList.size(); i++) {

            hBox.getChildren().add(imageList.get(i));
        }
    }

    @Override
    public void update(Observable o, Object arg) {

    }
}
