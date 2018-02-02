package view;

import controller.InsertController;
import controller.SlideShowController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class InsertView {
    InsertController insertController;

    private TextField insertNameLabel;
    private TextField insertLocationLabel;
    private TextArea insertTextArea;
    private HBox insertSlideshow;
    private Button insertCancelButton;
    private Button insertConfirmButton;
    private DatePicker insertInizioDataPicker;
    private DatePicker insertFineDataPicker;
    private TextField insertMaxGuestsLabel;
    private ImageView insertPlaybillImageView;
    private Button insertPlayBillLabel;
    private Button insertUploadButton;
    private final Integer[] oldVal = {0};
    private Integer newVal = 0;
    private SlideShowController slideShowController = new SlideShowController();
    private List<String> immagini = new ArrayList<>();
    private List<String> texts = new ArrayList<>();


    public InsertView(InsertController insertController, List<Button> buttonList, List<TextField> texts,
                      TextArea insertTextArea, HBox insertSlideshow, DatePicker insertInizioDataPicker,
                      DatePicker insertFineDataPicker, ImageView insertPlaybillImageView){


        this.insertController = insertController;
        this.insertNameLabel = texts.get(0);
        this.insertLocationLabel = texts.get(1);
        this.insertMaxGuestsLabel = texts.get(2);
        this.insertCancelButton = buttonList.get(0);
        this.insertConfirmButton = buttonList.get(1);
        this.insertPlayBillLabel = buttonList.get(2);
        this.insertUploadButton = buttonList.get(3);
        this.insertTextArea = insertTextArea;
        this.insertSlideshow = insertSlideshow;
        this.insertInizioDataPicker = insertInizioDataPicker;
        this.insertFineDataPicker = insertFineDataPicker;
        this.insertPlaybillImageView = insertPlaybillImageView;


        insertPlaybillImageView.setImage(new Image("/image/Picture_80px.png"));
        insertInizioDataPicker.setValue(LocalDate.now());
        insertFineDataPicker.setValue(LocalDate.now());
        List<String> locations = insertController.getLocations();
        TextFields.bindAutoCompletion(insertLocationLabel, locations);

        initListeners();


    }

    private void initListeners() {
        insertPlayBillLabel.setOnAction(event -> {
            playbill();
        });

        insertUploadButton.setOnAction(event -> {
            slideshow();
        });

        insertMaxGuestsLabel.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
                maxVisitorControl(newValue);
            }
        });

        insertLocationLabel.focusedProperty().addListener(new ChangeListener<Boolean>()
        {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue)
            {
                focusLocation(newPropertyValue);
            }
        });

        insertMaxGuestsLabel.focusedProperty().addListener(new ChangeListener<Boolean>()
        {

            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue)
            {
                focusGuests(newPropertyValue);

            }
        });

        insertFineDataPicker.valueProperty().addListener((ov, oldValue, newValue) -> {
            if (newValue.isBefore(insertInizioDataPicker.getValue())){
                insertFineDataPicker.setValue(insertInizioDataPicker.getValue());
            }
        });

        insertInizioDataPicker.valueProperty().addListener((ov, oldValue, newValue) -> {
            if (newValue.isAfter(insertFineDataPicker.getValue())){
                insertFineDataPicker.setValue(newValue);
            }
        });

        insertCancelButton.setOnAction(event -> {
                back();
        });


        insertConfirmButton.setOnAction(event -> {
            next();
        });
    }

    private void back() {
        insertController.back();
    }

    private void next() {
        texts.add(insertNameLabel.getText());
        texts.add(insertLocationLabel.getText());
        texts.add(insertMaxGuestsLabel.getText());
        texts.add(insertTextArea.getText());
        texts.add(insertInizioDataPicker.getValue().toString());
        texts.add(insertFineDataPicker.getValue().toString());

        insertController.next(this, texts);
    }

    private void playbill(){
        Stage stage = new Stage();

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
    }

    private void slideshow() {
        Stage stage = new Stage();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Images", "*.*"),
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("PNG", "*.png")
        );
        List<File> list = fileChooser.showOpenMultipleDialog(stage);
        if (list != null) {
            for (File file : list) {
                    immagini.add(file.toURI().toString());
            }
            Button left = (Button)insertSlideshow.getChildren().get(0);
            HBox slide = (HBox)insertSlideshow.getChildren().get(1);
            Button right= (Button) insertSlideshow.getChildren().get(2);
            slideShowController.createSlide(left, slide, right, immagini);
        }
    }

    private void maxVisitorControl(String newValue){
        if (!newValue.matches("\\d*")){
            insertMaxGuestsLabel.setText(oldVal[0].toString());
        }
        if (insertMaxGuestsLabel.getText().length() > 7) {
            String s = insertMaxGuestsLabel.getText().substring(0, 7);
            insertMaxGuestsLabel.setText(s);
        }
    }

    private void focusLocation (Boolean newPropertyValue){
        if (!newPropertyValue)
        {
            insertMaxGuestsLabel.setText(insertController.maxVisitors(insertLocationLabel.getText()));
            oldVal[0] = Integer.parseInt(insertController.maxVisitors(insertLocationLabel.getText()));
        }

    }

    private void focusGuests(Boolean newPropertyValue) {
        if (!newPropertyValue)
        {
            newVal= Integer.parseInt(insertMaxGuestsLabel.getText());
            if (newVal> oldVal[0]){
                insertMaxGuestsLabel.setText( String.valueOf(oldVal[0]) );
            }
        }
    }


}
