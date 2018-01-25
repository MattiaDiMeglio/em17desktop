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


import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class SlideShowView implements Observer {

    List<Button> buttonList = new ArrayList<>();
    EventListModel eventListModel = EventListModel.getInstance();
    SlideShowController slideShowController;

    public SlideShowView(HBox hBox, SlideShowController slideShowController) {
        eventListModel.addObserver(this);
        this.slideShowController = slideShowController;
        if (hBox != null) {
            hBox.setAlignment(Pos.CENTER);
            hBox.setSpacing(20);


            for (int i = 0; i < 8; i++) {
                Button button = new Button();
                /*button.setMaxHeight(160);
                button.setMaxWidth(160);
                button.setMinHeight(160);
                button.setMinWidth(160);*/
                buttonList.add(button);

                if (i<4){
                    hBox.getChildren().add( button);
                }

            }


        }
    }


    @Override
    public void update(Observable o, Object arg) {
        int index = (int) arg;
        if (index<= buttonList.size()) {
            Image image = new Image(eventListModel.getListaEventi().get(index).getLocandina());
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(300.0);
            imageView.setFitHeight(280.0);
            buttonList.get(index).setGraphic(imageView);
            buttonList.get(index).setOnAction(event -> {
                slideShowController.handler(index);
            });
        }
    }
}
