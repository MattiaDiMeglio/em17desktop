package view;

import controller.InsertController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class InsertView {

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

        this.insertNameLabel = insertNameLabel;
        this.insertLocationLabel = insertLocationLabel;
        this.insertTextArea = insertTextArea;
        this.insertSlideshow = insertSlideshow;
        this.insertCancelButton = insertCancelButton;
        this.insertConfirmButton = insertConfirmButton;
        this.insertInizioDataPicker = insertInizioDataPicker;
        this.insertFineDataPicker = insertFineDataPicker;
        this.insertMaxGuestsLabel = insertMaxGuestsLabel;

        insertCancelButton.setOnAction(event -> {});
        insertConfirmButton.setOnAction(event -> {});
    }


}
