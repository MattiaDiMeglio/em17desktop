package view;

import controller.InsertController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.controlsfx.control.textfield.TextFields;

import java.awt.*;
import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class InsertView {
    InsertController insertController;

    @FXML
    private TextField insertNameLabel;
    @FXML
    private TextField insertLocationLabel;
    @FXML
    private TextArea insertTextArea;
    @FXML
    private HBox insertSlideshow;
    @FXML
    private Button insertCancelButton;
    @FXML
    private Button insertConfirmButton;
    @FXML
    private DatePicker insertInizioDataPicker;
    @FXML
    private DatePicker insertFineDataPicker;
    @FXML
    private TextField insertMaxGuestsLabel;

    public InsertView(InsertController insertController, Button insertCancelButton,
                      Button insertConfirmButton, TextArea insertTextArea, TextField insertLocationLabel,
                      TextField insertNameLabel, HBox insertSlideshow, DatePicker insertInizioDataPicker,
                      DatePicker insertFineDataPicker, TextField insertMaxGuestsLabel, Button insertPlayBillLabel, ImageView insertPlaybillImageView) {
        this.insertController = insertController;
        this.insertNameLabel = insertNameLabel;
        this.insertLocationLabel = insertLocationLabel;
        this.insertTextArea = insertTextArea;
        this.insertSlideshow = insertSlideshow;
        this.insertConfirmButton = insertConfirmButton;
        this.insertInizioDataPicker = insertInizioDataPicker;
        this.insertFineDataPicker = insertFineDataPicker;
        this.insertMaxGuestsLabel = insertMaxGuestsLabel;
        final Integer[] oldVal = {0};
        Stage stage = new Stage();
        insertPlaybillImageView.setImage(new Image("/image/Picture_80px.png"));

        insertInizioDataPicker.setValue(LocalDate.now());
        insertFineDataPicker.setValue(LocalDate.now());

        insertPlayBillLabel.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Resource File");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("All Images", "*.*"),
                    new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                    new FileChooser.ExtensionFilter("PNG", "*.png")
            );
            File file = fileChooser.showOpenDialog(stage);
            if (file != null) {
                System.out.println(file.getAbsolutePath());
                insertPlaybillImageView.setImage(new Image(file.toURI().toString()));
            }


        });

        insertMaxGuestsLabel.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
                if (!newValue.matches("\\d*")){
                    insertMaxGuestsLabel.setText(oldVal[0].toString());
                }
                if (insertMaxGuestsLabel.getText().length() > 7) {
                    String s = insertMaxGuestsLabel.getText().substring(0, 7);
                    insertMaxGuestsLabel.setText(s);
                }
            }
        });

        List<String> locations = insertController.getLocations();
        TextFields.bindAutoCompletion(insertLocationLabel, locations);

        insertLocationLabel.focusedProperty().addListener(new ChangeListener<Boolean>()
        {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue)
            {
                if (newPropertyValue)
                {
                }
                else
                {
                    try {
                        insertMaxGuestsLabel.setText(insertController.maxVisitors(insertLocationLabel.getText()));
                        oldVal[0] = Integer.parseInt(insertController.maxVisitors(insertLocationLabel.getText()));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        insertMaxGuestsLabel.focusedProperty().addListener(new ChangeListener<Boolean>()
        {

            Integer newVal = 0;
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue)
            {

                if (newPropertyValue)
                {


                }
                else
                {
                    newVal= Integer.parseInt(insertMaxGuestsLabel.getText());


                    if (newVal> oldVal[0]){
                        insertMaxGuestsLabel.setText( String.valueOf(oldVal[0]) );
                    }

                }
            }
        });

        insertCancelButton.setOnAction(event -> {
            try {
                back();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });


        insertConfirmButton.setOnAction(event -> {
            next();
        });
    }

    private void back() throws ExecutionException, InterruptedException {
        insertController.back();
    }

    private void next() {
        List<String> texts = new ArrayList<>();
        texts.add(insertNameLabel.getText());
        texts.add(insertLocationLabel.getText());
        texts.add(insertMaxGuestsLabel.getText());
        texts.add(insertTextArea.getText());
        texts.add(insertInizioDataPicker.getValue().toString());
        texts.add(insertFineDataPicker.getValue().toString());

        insertController.next(this, texts);
    }

}
