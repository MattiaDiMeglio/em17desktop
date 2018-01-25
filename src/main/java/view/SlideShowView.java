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

    private void right() {
        System.out.println("hhhh");
        if (activeList.get(3)<7){
            System.out.println(activeList.get(0) + activeList.get(1) + activeList.get(2) + activeList.get(3));
            activeList.set(0, activeList.get(0)+1);
            activeList.set(1, activeList.get(1)+1);
            activeList.set(2, activeList.get(2)+1);
            activeList.set(3, activeList.get(3)+1);
            hBox.getChildren().remove(3);
            hBox.getChildren().remove(2);
            hBox.getChildren().remove(1);
            hBox.getChildren().remove(0);
            System.out.println(activeList.get(0) + activeList.get(1) + activeList.get(2) + activeList.get(3));
            hBox.getChildren().add(0, buttonList.get(activeList.get(0)));
            hBox.getChildren().add(1, buttonList.get(activeList.get(1)));
            hBox.getChildren().add(2, buttonList.get(activeList.get(2)));
            hBox.getChildren().add(3, buttonList.get(activeList.get(3)));
        }

    }

    private void left(){
        System.out.println("aaaa");
        if (activeList.get(0)>0){
            System.out.println(activeList.get(0) + activeList.get(1) + activeList.get(2) + activeList.get(3));
            activeList.set(0, activeList.get(0)-1);
            activeList.set(1, activeList.get(1)-1);
            activeList.set(2, activeList.get(2)-+1);
            activeList.set(3, activeList.get(3)-1);
            hBox.getChildren().remove(3);
            hBox.getChildren().remove(2);
            hBox.getChildren().remove(1);
            hBox.getChildren().remove(0);
            System.out.println(activeList.get(0) + activeList.get(1) + activeList.get(2) + activeList.get(3));
            hBox.getChildren().add(0, buttonList.get(activeList.get(0)));
            hBox.getChildren().add(1, buttonList.get(activeList.get(1)));
            hBox.getChildren().add(2, buttonList.get(activeList.get(2)));
            hBox.getChildren().add(3, buttonList.get(activeList.get(3)));
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
