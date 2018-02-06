package view;

import controller.InsertController;
import controller.SlideShowController;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import model.EventModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InsertRecapView {

    private HBox recap1Hbox;
    private HBox recap2Hbox;
    private VBox textAreaVbox;
    private HBox recap3Hbox;
    private HBox recapSlideshow;
    private HBox recapButton;
    private ImageView recapPlaybillImageView;
    private SlideShowController slideShowController = new SlideShowController();
    private EventModel newEvent;
    private InsertController insertController;


    public InsertRecapView(InsertController insertController, EventModel newEvent, VBox riepilogoTextBox, ImageView recapPlaybillImageView){
        recap1Hbox = (HBox) riepilogoTextBox.getChildren().get(0);
        recap2Hbox = (HBox) riepilogoTextBox.getChildren().get(1);
        textAreaVbox = (VBox) riepilogoTextBox.getChildren().get(2);
        recap3Hbox = (HBox) riepilogoTextBox.getChildren().get(3);
        recapSlideshow = (HBox) riepilogoTextBox.getChildren().get(4);
        recapButton = (HBox) riepilogoTextBox.getChildren().get(6);
        this.recapPlaybillImageView = recapPlaybillImageView;
        this.newEvent = newEvent;
        this.insertController = insertController;

        init(newEvent);
    }

    private void init(EventModel newEvent) {
        recapPlaybillImageView.setImage(newEvent.getBillboard());

        Label eventName = (Label) recap1Hbox.getChildren().get(0);
        Text locationName = (Text) recap1Hbox.getChildren().get(2);
        eventName.setText(newEvent.getEventName());
        locationName.setText(newEvent.getLocationName());

        Text startDate = (Text) recap2Hbox.getChildren().get(1);
        Text endDate = (Text) recap2Hbox.getChildren().get(3);
        startDate.setText(newEvent.getStartingDate());
        endDate.setText(newEvent.getEndingDate());

        TextArea eventDescription = (TextArea) textAreaVbox.getChildren().get(0);
        eventDescription.setDisable(true);
        eventDescription.setText(newEvent.getEventDescription());

        Text price = (Text) recap3Hbox.getChildren().get(1);
        price.setText(price.getText() + "€");
        Text maxVisitors = (Text) recap3Hbox.getChildren().get(3);
        price.setText(newEvent.getPrice().toString());
        maxVisitors.setText(newEvent.getMaxVisitors().toString());

        Button left = (Button) recapSlideshow.getChildren().get(0);
        HBox slide = (HBox) recapSlideshow.getChildren().get(1);
        Button right = (Button) recapSlideshow.getChildren().get(2);
        initSlide(left, right, slide);


        Button back = (Button) recapButton.getChildren().get(0);
        Button next = (Button) recapButton.getChildren().get(2);
        initListener(back, next);


    }

    private void initListener(Button back, Button next) {
        next.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Attenzione!");
            alert.setHeaderText("Verrà inserito nel database l'evento con i dati precedentemente visionati");
            alert.setContentText("Confermare?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                insertController.insert();
            }

        });

        back.setOnAction(event -> {
            insertController.back();
        });
    }

    private void initSlide(Button left, Button right, HBox slide) {
        slideShowController.createSlide(left, slide, right, newEvent.getSlideshow());
    }


}
