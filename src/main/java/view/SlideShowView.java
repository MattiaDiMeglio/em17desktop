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

public class SlideShowView implements Observer {

    List<Button> buttonList = new ArrayList<>();
    EventListModel eventListModel = EventListModel.getInstance();
    EventModel eventModel;
    SlideShowController slideShowController;
    HBox hBox;
    List<Integer> activeList = new ArrayList<>();
    public SlideShowView(HBox hBox, Button dashBoardSlideShowLeftButton, Button dashBoardSlideShowRightButton, SlideShowController slideShowController) {
        eventListModel.addObserver(this);
        this.slideShowController = slideShowController;
        this.hBox=hBox;
        dashBoardSlideShowRightButton.setOnAction(event -> {
            right();
        });
        dashBoardSlideShowLeftButton.setOnAction(event -> {
            left();
        });
        if (hBox != null) {
            hBox.setAlignment(Pos.CENTER);
            hBox.setSpacing(20);
            for (int i = 0; i < 8; i++) {
                Button button = new Button();
                buttonList.add(button);
                if (i<4){
                    hBox.getChildren().add(button);
                    activeList.add(i);
                }
            }
        }
    }

    public SlideShowView(HBox hBox, Button dashBoardSlideShowLeftButton, Button dashBoardSlideShowRightButton, SlideShowController slideShowController, EventModel eventModel) {
        int i=0;
        this.eventModel = eventModel;
        eventModel.addObserver(this);

        this.slideShowController = slideShowController;
        this.hBox=hBox;
        dashBoardSlideShowRightButton.setOnAction(event -> {
            right();
        });
        dashBoardSlideShowLeftButton.setOnAction(event -> {
            left();
        });
        if (hBox != null) {
            hBox.getChildren().removeAll();
            hBox.setAlignment(Pos.CENTER);
            hBox.setSpacing(20);
            for (i = 0; i < eventModel.getSlideshow().size(); i++) {
                System.out.println(i);
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
        if (activeList.get(3)<7){
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
            int index = (int) arg;

            if (index <= buttonList.size()) {
                Image image = new Image(eventListModel.getListaEventi().get(index).getLocandina());
                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(300.0);
                imageView.setFitHeight(280.0);
                buttonList.get(index).setGraphic(imageView);
                buttonList.get(index).setOnAction(event -> {
                    slideShowController.handler(index);
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
