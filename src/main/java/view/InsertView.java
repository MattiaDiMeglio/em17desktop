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

/**
 * Classe utilizzata per completare il primo step della modifica o dell'inserimento di un evento.
 *
 * @author ingsw20
 */
public class InsertView implements Observer {
    /**
     * istanza di {@link InsertController}.
     */
    private InsertController insertController;

    /**
     * textfield per il nome dell'evento.
     */
    private TextField insertNameLabel;
    /**
     * textfield per il nome della location.
     */
    private TextField insertLocationLabel;
    /**
     * textfield per la descrizione dell'evento.
     */
    private TextArea insertTextArea;
    /**
     * hbox contenente le immagini dell'evento.
     */
    private HBox insertSlideshow;
    /**
     * tasto per annullare l'inserimento.
     */
    private Button insertCancelButton;
    /**
     * tasto per andare allo step successivo.
     */
    private Button insertConfirmButton;
    /**
     * DatePicker per la data di inizio dell'evento.
     */
    private DatePicker insertInizioDataPicker;
    /**
     * DatePicker per la data di fine dell'evento.
     */
    private DatePicker insertFineDataPicker;
    /**
     * textfield per il numero massimo di spettatori.
     */
    private TextField insertMaxGuestsLabel;
    /**
     * imageview con la copertina.
     */
    private ImageView insertPlaybillImageView;
    /**
     * bottone per l'inserimento della locandina.
     */
    private Button insertPlayBillLabel;
    /**
     * bottone per il caricamento delle foto.
     */
    private Button insertUploadButton;
    /**
     * valore precedentemente memorizzato per le textfield.
     */
    private final Integer[] oldVal = {0};
    /**
     * istanza di {@link SlideShowController}.
     */
    private SlideShowController slideShowController = new SlideShowController();
    /**
     * lista con le immagini dell'evento.
     */
    private List<Image> immagini = new ArrayList<>();
    /**
     * lista che rccoglierà tutte le informazioni per poi passarle al controller.
     */
    private List<String> texts = new ArrayList<>();
    /**
     * model dell'evento.
     */
    private EventModel eventModel;
    /**
     * variabile per l'autocompletamento del campo dedicato alla location.
     */
    private AutoCompletionBinding binding;

    /**
     * costruttore per la modifica di evento.
     * Viene anche chiamato nell'inserimento di un nuovo evento alla pressione del tasto indietro in {@link InsertTicketTypeView}.
     *
     * @param insertController        istanza di {@link InsertController}
     * @param buttonList              lista con i bottoni della view
     * @param texts                   lista con le textfield della view
     * @param insertTextArea          TextArea per la descrizione dell'evento
     * @param insertSlideshow         HBox per le immagini dell'evento
     * @param insertInizioDataPicker  DatePicker per la data di inizio dell'evento
     * @param insertFineDataPicker    DatePicker per la data di fine dell'evento
     * @param insertPlaybillImageView ImageView per l'immagine di locandina
     * @param eventModel              model dell'evento
     */
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

    /**
     * costruttore per la modifica di evento.
     * Viene anche chiamato nell'inserimento di un nuovo evento alla pressione del tasto indietro in {@link InsertTicketTypeView}.
     *
     * @param insertController        istanza di {@link InsertController}
     * @param buttonList              lista con i bottoni della view
     * @param texts                   lista con le textfield della view
     * @param insertTextArea          TextArea per la descrizione dell'evento
     * @param insertSlideshow         HBox per le immagini dell'evento
     * @param insertInizioDataPicker  DatePicker per la data di inizio dell'evento
     * @param insertFineDataPicker    DatePicker per la data di fine dell'evento
     * @param insertPlaybillImageView ImageView per l'immagine di locandina
     */
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


        List<String> locations = insertController.getLocations();
        binding = TextFields.bindAutoCompletion(insertLocationLabel, locations);
        initListeners();

        eventModel = new EventModel();
        eventModel.addObserver(this);
        insertController.update(eventModel);
    }

    /**
     * metodo per l'inizializzazione dei listener.
     */
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

    /**
     * metodo per tornare indietro.
     */
    private void back() {
        insertController.toDash();
    }

    /**
     * metodo per andare allo step successivo.
     */
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

    /**
     * metodo per il caricamento della locandina
     */
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

    /**
     * metodo per la selezione delle immagini
     */
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

    /**
     * metodo per il controllo dell'input nella textfield per il massimo numero di spettatori
     *
     * @param newValue nuovo valore inserito
     */
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

    /**
     * metodo chiamato quando si inizia a scrivere nella textfield della location o quando si sposta il focus su un altro oggetto
     *
     * @param newPropertyValue flag per identificare il tipo di focus
     */
    private void focusLocation(Boolean newPropertyValue) {
        try {
            if (!newPropertyValue) {
                String[] parts = insertLocationLabel.getText().split("-");
                oldVal[0] = Integer.parseInt(insertController.getMaxVisitors(parts[0]));
                insertMaxGuestsLabel.setText(insertController.getMaxVisitors(parts[0]));
            }
        } catch (NumberFormatException ignored) {
        }
    }

    /**
     * metodo chiamato quando si inizia a scrivere nella textfield per il numero di spettatori o quando si sposta il focus su un altro oggetto
     *
     * @param newPropertyValue flag per identificare il tipo di focus
     */
    private void focusGuests(Boolean newPropertyValue) {
        if (!newPropertyValue) {
            Integer newVal = Integer.parseInt(insertMaxGuestsLabel.getText());
            if (newVal > oldVal[0]) {
                insertMaxGuestsLabel.setText(String.valueOf(oldVal[0]));
            }
        }
    }

    /**
     * metodo per il popolamento della view
     *
     * @param o   model dal quale prelevare i dati
     * @param arg null
     */
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
