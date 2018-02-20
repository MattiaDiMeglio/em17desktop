package view;

import controller.InsertController;
import controller.SlideShowController;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.EventModel;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class InsertView implements Observer {
    private InsertController insertController;

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
    private SlideShowController slideShowController = new SlideShowController();
    private List<Image> immagini = new ArrayList<>();
    private List<String> texts = new ArrayList<>();
    private EventModel eventModel;
    private AutoCompletionBinding binding;


    public InsertView(InsertController insertController, List<Button> buttonList, List<TextField> texts,
                      TextArea insertTextArea, HBox insertSlideshow, DatePicker insertInizioDataPicker,
                      DatePicker insertFineDataPicker, ImageView insertPlaybillImageView, EventModel eventModel) {
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
        binding = TextFields.bindAutoCompletion(insertLocationLabel, locations);
        initListeners();

        eventModel.addObserver(this);
        this.eventModel = eventModel;
        insertController.update(eventModel);
    }

    public InsertView(InsertController insertController, List<Button> buttonList, List<TextField> texts,
                      TextArea insertTextArea, HBox insertSlideshow, DatePicker insertInizioDataPicker,
                      DatePicker insertFineDataPicker, ImageView insertPlaybillImageView) {
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

        /*insertPlaybillImageView.setImage(new Image("/image/Picture_80px.png"));
        insertInizioDataPicker.setValue(LocalDate.now());
        insertFineDataPicker.setValue(LocalDate.now());*/
        List<String> locations = insertController.getLocations();
        binding = TextFields.bindAutoCompletion(insertLocationLabel, locations);
        initListeners();

        eventModel = new EventModel();
        eventModel.addObserver(this);
        insertController.update(eventModel);
    }

    private void initListeners() {

        insertPlaybillImageView.setOnMouseClicked(event -> playbill());

        insertPlayBillLabel.setOnAction(event -> playbill());

        insertUploadButton.setOnAction(event -> slideshow());

        insertMaxGuestsLabel.textProperty().addListener((ov, oldValue, newValue) -> maxVisitorControl(newValue));

        insertLocationLabel.focusedProperty().addListener((arg0, oldPropertyValue, newPropertyValue) -> focusLocation(newPropertyValue));

        insertMaxGuestsLabel.focusedProperty().addListener((arg0, oldPropertyValue, newPropertyValue) -> focusGuests(newPropertyValue));

        insertFineDataPicker.valueProperty().addListener((ov, oldValue, newValue) -> {
            if (insertInizioDataPicker.getValue() != null && newValue.isBefore(insertInizioDataPicker.getValue())) {
                insertFineDataPicker.setValue(insertInizioDataPicker.getValue());
            }
        });

        insertInizioDataPicker.valueProperty().addListener((ov, oldValue, newValue) -> {
            if (insertFineDataPicker.getValue() != null && newValue.isAfter(insertFineDataPicker.getValue())) {
                insertFineDataPicker.setValue(newValue);
            }
        });

        insertCancelButton.setOnAction(event -> back());


        insertConfirmButton.setOnAction(event -> next());
    }

    private void back() {
        insertController.toDash();
    }

    private void next() {
        try {
            String pattern = "dd/MM/yyy";
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);
            texts.clear();
            texts.add(insertNameLabel.getText());
            texts.add(insertLocationLabel.getText());
            texts.add(insertMaxGuestsLabel.getText());
            texts.add(insertTextArea.getText());
            texts.add(dateFormatter.format(insertInizioDataPicker.getValue()));
            texts.add(dateFormatter.format(insertFineDataPicker.getValue()));
            binding.dispose();
            insertController.toTicketType(eventModel, texts, insertPlaybillImageView.getImage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void playbill() {
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
            insertPlaybillImageView.setImage(new Image(file.toURI().toString()));
        }
    }

    private void slideshow() {
        Stage stage = new Stage();
        List<Image> immaginiUri = new ArrayList<>();

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
                //file.getAbsolutePath().replaceAll(" ", "%20");
                immaginiUri.add(new Image(file.toURI().toString()));
                System.out.println(file.toURI().toString());
            }
            Button left = (Button) insertSlideshow.getChildren().get(0);
            HBox slide = (HBox) insertSlideshow.getChildren().get(1);
            Button right = (Button) insertSlideshow.getChildren().get(2);

            slideShowController.createSlide(insertController, left, slide, right, immaginiUri);
        }
        immagini.addAll(immaginiUri);
        insertController.setImagesList(immagini);
    }

    private void maxVisitorControl(String newValue) {
        try {
            if (!newValue.matches("\\d*")) {
                insertMaxGuestsLabel.setText(oldVal[0].toString());
            }
            if (insertMaxGuestsLabel.getText().length() > 7) {
                String s = insertMaxGuestsLabel.getText().substring(0, 7);
                insertMaxGuestsLabel.setText(s);
            }
        } catch (NullPointerException ignored) {
        }
    }

    private void focusLocation(Boolean newPropertyValue) {
        try {
            if (!newPropertyValue) {
                String[] parts = insertLocationLabel.getText().split("\\-");
                oldVal[0] = Integer.parseInt(insertController.getMaxVisitors(parts[0]));
                insertMaxGuestsLabel.setText(insertController.getMaxVisitors(parts[0]));
            }
        } catch (NumberFormatException ignored) {
        }

    }

    private void focusGuests(Boolean newPropertyValue) {
        if (!newPropertyValue) {
            Integer newVal = Integer.parseInt(insertMaxGuestsLabel.getText());
            if (newVal > oldVal[0]) {
                insertMaxGuestsLabel.setText(String.valueOf(oldVal[0]));
            }
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        EventModel eventModel = (EventModel) o;
        String pattern = "dd/MM/yyy";
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);

        insertNameLabel.setText(eventModel.getEventName());
        if (!eventModel.getLocationName().equals("")) {
            insertLocationLabel.setText(eventModel.getLocationName() + "-" + eventModel.getLocationAddress());
        }
        insertMaxGuestsLabel.setText(String.valueOf(eventModel.getMaxVisitors()));
        insertTextArea.setText(eventModel.getEventDescription());
        if (!eventModel.getStartingDate().equals("")) {
            insertInizioDataPicker.setValue(LocalDate.parse(eventModel.getStartingDate(), dateFormatter));
        } else {
            insertInizioDataPicker.setValue(LocalDate.now());
        }
        if (!eventModel.getEndingDate().equals("")) {
            insertFineDataPicker.setValue(LocalDate.parse(eventModel.getEndingDate(), dateFormatter));
        } else {
            insertFineDataPicker.setValue(LocalDate.now());
        }
        if (eventModel.getBillboard() == null) {
            insertPlaybillImageView.setImage(new Image("/image/Picture_80px.png"));
        } else {
            insertPlaybillImageView.setImage(eventModel.getBillboard());
        }

        Button left = (Button) insertSlideshow.getChildren().get(0);
        HBox slide = (HBox) insertSlideshow.getChildren().get(1);
        slide.getChildren().clear();
        Button right = (Button) insertSlideshow.getChildren().get(2);
        slideShowController.createSlide(insertController, left, slide, right, eventModel.getSlideshow());
    }
}
