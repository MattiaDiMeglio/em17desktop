package view;

import controller.InsertController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import org.controlsfx.control.textfield.TextFields;

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
                      DatePicker insertFineDataPicker, TextField insertMaxGuestsLabel) {
        this.insertController = insertController;
        this.insertNameLabel = insertNameLabel;
        this.insertLocationLabel = insertLocationLabel;
        this.insertTextArea = insertTextArea;
        this.insertSlideshow = insertSlideshow;
        this.insertConfirmButton = insertConfirmButton;
        this.insertInizioDataPicker = insertInizioDataPicker;
        this.insertFineDataPicker = insertFineDataPicker;
        this.insertMaxGuestsLabel = insertMaxGuestsLabel;


        List<String> in = new ArrayList<>();
        in.add("canne");
        in.add("cacca");
        TextFields.bindAutoCompletion(insertLocationLabel, in);
        insertLocationLabel.focusedProperty().addListener(new ChangeListener<Boolean>()
        {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue)
            {
                if (newPropertyValue)
                {
                    System.out.println("Textfield on focus");
                }
                else
                {
                    System.out.println("Textfield out focus");
                    try {
                        insertMaxGuestsLabel.setText(insertController.maxVisitors(insertLocationLabel.getText()));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
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
