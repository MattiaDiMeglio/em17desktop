package view;

import controller.InsertController;
import controller.SlideShowController;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import model.EventModel;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Classe view che si occupa della schermata di recap dei dati da inserire
 */
public class InsertRecapView {

    /**
     * primo figlio della VBox riepilogoTextBox,
     * contiene i nodi per l'inserimento del nome dell'evento e della location
     *
     * @see HBox
     * @see VBox
     * @see Text
     * @see Label
     */
    private HBox recap1Hbox;
    /**
     * secondo figlio della VBox riepilogoTextBox,
     * contiene i nodi per l'inserimento delle date
     *
     * @see DatePicker
     */
    private HBox recap2Hbox;
    /**
     * terzo figlio della VBox riepilogoTextBox,
     * contiene il nodo per l'inserimento della descrizione dell'evento
     *
     * @see TextArea
     */
    private VBox textAreaVbox;
    /**
     * quarto figlio della VBox riepilogoTextBox,
     * contiene i nodi per l'inserimento del prezzo e del massimo dei visitatori
     * @see TextField
     * @see Label
     */
    private HBox recap3Hbox;
    /**
     * quinto figlio della VBox riepilogoTextBox,
     * contiene i nodi che formano lo slideshow
     *
     * @see HBox
     * @see ImageView
     * @see Button
     * @see SlideShowController
     * @see SlideShowView
     */
    private HBox recapSlideshow;
    /**
     * sesto figlio della VBox riepilogoTextBox,
     * contiene i bottoni di conferma e indietro
     *
     * @see Button
     *
     */
    private HBox recapButton;
    /**
     * ImageView contenente la copertina dell'evento
     *
     * @see ImageView
     */
    private ImageView recapPlaybillImageView;
    /**
     * Istanza della classe SlideShowController,
     * che crea lo slideshow
     *
     * @see SlideShowController
     */
    private SlideShowController slideShowController = new SlideShowController();
    /**
     * Oggetto EventModel creato per contenere i dati da inserire nel db
     *
     * @see EventModel
     */
    private EventModel newEvent;
    /**
     * Instanza dell'InsertController
     */
    private InsertController insertController;

    private List<Image> imageList;

    /**
     * Sottoclasse necessaria per il popolamento della prima delle tue tabelle della schermata
     */
    public static class tab1 {
        /**
         * Nome del settore
         */
        private final SimpleStringProperty sectors;
        /**
         * Massimo dei visitatori per settore
         */
        private final SimpleStringProperty maxVisitors;
        /**
         * Booleano che rappresenta se si applicano le riduzioni al settore
         */
        private final SimpleStringProperty reductions;


        /**
         * @param sectors
         * @param maxVisitors
         * @param reductions
         *
         * Costruttore della sottoclasse per il popolamento della prima tabella nella schermata
         */
        tab1(String sectors, String maxVisitors, String reductions) {
            this.sectors = new SimpleStringProperty(sectors);
            this.maxVisitors = new SimpleStringProperty(maxVisitors);
            this.reductions = new SimpleStringProperty(reductions);
        }

        /**
         * getter per sectors
         *
         * @return
         */
        public String getSectors() {
            return sectors.get();
        }

        /**
         * getter per MaxVisitors
         *
         * @return
         */
        public String getMaxVisitors() {
            return maxVisitors.get();
        }

        /**
         * getter per reductions
         *
         * @return
         */
        public String getReductions() {
            return reductions.get();
        }

    }

    /**
     * Sottoclasse necessaria per il popolamento della seconda delle tue tabelle della schermata
     */
    public static class tab2{
        private final SimpleStringProperty reduction;
        private final SimpleStringProperty percent;

        tab2(String reduction, String percent){
            this.reduction = new SimpleStringProperty(reduction);
            this.percent = new SimpleStringProperty(percent);
        }

        public String getReduction() {
            return reduction.get();
        }

        public String getPercent() {
            return percent.get();
        }

    }



    public InsertRecapView(InsertController insertController, EventModel newEvent, List<Image> imagesList, VBox riepilogoTextBox, ImageView recapPlaybillImageView){
        recap1Hbox = (HBox) riepilogoTextBox.getChildren().get(0);
        recap2Hbox = (HBox) riepilogoTextBox.getChildren().get(1);
        textAreaVbox = (VBox) riepilogoTextBox.getChildren().get(2);
        recap3Hbox = (HBox) riepilogoTextBox.getChildren().get(3);
        recapSlideshow = (HBox) riepilogoTextBox.getChildren().get(4);
        recapButton = (HBox) riepilogoTextBox.getChildren().get(6);
        this.recapPlaybillImageView = recapPlaybillImageView;
        this.newEvent = newEvent;
        this.insertController = insertController;
        this.imageList = imagesList;

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

        initTable(textAreaVbox);

    }

    private void initTable(VBox vBox) {
        TableView table = new TableView<>();
        table.setEditable(false);
        TableView table1 = new TableView<>();
        table1.setEditable(false);

        final ObservableList<tab1> data1 = FXCollections.observableArrayList();
        final ObservableList<tab2> data2 = FXCollections.observableArrayList();


        data1.add(new tab1("Totale", String.valueOf(newEvent.getMaxVisitors()), ""));

        for (int i=0; i < newEvent.getSectorList().size(); i++){
            data1.add(new tab1(newEvent.getSectorList().get(i).getName(), String.valueOf(newEvent.getSectorList().get(i).getSeats()),
                    String.valueOf(newEvent.getSectorList().get(i).isReduction())));
        }



        data2.add(new tab2("Anziani", String.valueOf(newEvent.getEldersReduction())));
        data2.add(new tab2("Bambini", String.valueOf(newEvent.getChildrenReduction())));
        data2.add(new tab2("Studenti", String.valueOf(newEvent.getStudentReduction())));




        TableColumn first = new TableColumn("Posti per settore");
        TableColumn second = new TableColumn("Max Visitatori");
        TableColumn third = new TableColumn("Riduzione Attiva");
        first.setCellValueFactory(new PropertyValueFactory<tab1,String>("sectors"));
        second.setCellValueFactory(new PropertyValueFactory<tab1,String>("maxVisitors"));
        third.setCellValueFactory(new PropertyValueFactory<tab1, SlideShowView>("reductions"));
        table.setItems(data1);
        table.getColumns().addAll(first, second, third);
        first.setMinWidth(150);
        second.setMinWidth(120);
        third.setMinWidth(150);

        table.setMaxHeight(178);


        TableColumn first1 = new TableColumn("Riduzioni");
        TableColumn second1 = new TableColumn("Percentuale");
        first1.setCellValueFactory(new PropertyValueFactory<tab2, String>("reduction"));
        second1.setCellValueFactory(new PropertyValueFactory<tab2, String>("percent"));
        table1.setItems(data2);
        table1.getColumns().addAll(first1, second1);
        table1.setMaxWidth(210);

        first1.setMinWidth(100);
        second1.setMinWidth(100);

        table1.setMaxHeight(178);






        HBox hBox = new HBox();
        hBox.setSpacing(80.0);
        hBox.setAlignment(Pos.CENTER);
        hBox.getChildren().add(table);
        hBox.getChildren().add(table1);

        vBox.getChildren().add(hBox);

    }

    private void initListener(Button back, Button next) {
        next.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Attenzione!");
            alert.setHeaderText("Verrà inserito nel database l'evento con i dati precedentemente visionati");
            alert.setContentText("Confermare?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                insertController.insert(imageList);
            }

        });

        back.setOnAction(event -> {
            insertController.back();
        });
    }

    private void initSlide(Button left, Button right, HBox slide) {
        slideShowController.createSlide(insertController, left, slide, right, imageList);
    }


}
