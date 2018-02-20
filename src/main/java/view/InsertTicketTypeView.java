package view;

import controller.InsertController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.EventModel;

import javax.swing.*;
import java.util.*;

public class InsertTicketTypeView implements Observer {

    private InsertController insertController;
    private VBox form;
   // private List<String> sectors = new ArrayList<>();
    private List<EventModel.Sectors> sectorsList;
    private List<TextField> seatsFieldsList = new ArrayList<>();
    private List<TextField> priceList = new ArrayList<>();
    private List<CheckBox> reductionCheckList = new ArrayList<>();
    private ImageView insertTicketPlaybillImageView;

    public InsertTicketTypeView(InsertController insertController, EventModel newEvent, VBox insertTicketVbox,
                                Button ticketTypeBackButton, Button ticketTypeNextButton, ImageView insertTicketPlaybillImageView) {
        form = insertTicketVbox;
        this.insertController = insertController;
        this.insertTicketPlaybillImageView = insertTicketPlaybillImageView;
        newEvent.addObserver(this);
        insertController.update(newEvent);

        ticketTypeNextButton.setOnAction(event -> {
            next();
        });

        ticketTypeBackButton.setOnAction(event -> {
            newEvent.deleteObserver(this);
            insertController.toInsertView();
        });
    }

    private void init(EventModel eventModel) {
        int i = 2;
        int j = 0;
       // sectors = insertController.getSectorName(eventModel.getLocationName(), eventModel.getLocationAddress());
        sectorsList = eventModel.getSectorList();
        List<String> seats = insertController.getSteatsList(eventModel.getLocationName(), eventModel.getLocationAddress());
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20, 150, 10, 10));
        gridPane.add(new Label("Nome Settore"), 0, 0);
        gridPane.add(new Label("Numero Posti"), 1, 0);
        gridPane.add(new Label("Costo intero"), 2, 0);
        Separator separator = new Separator();
        separator.setValignment(VPos.CENTER);
        GridPane.setConstraints(separator, 0, 1);
        GridPane.setColumnSpan(separator, 7);
        gridPane.getChildren().add(separator);

        for (EventModel.Sectors sectors : sectorsList) {
            Label label = new Label();
            label.setText(sectors.getName());
            gridPane.add(label, 0, i);
            TextField textField = new TextField();
            textField.setText(seats.get(j));
            textField.setMaxSize(80.0, textField.getHeight());
            seatsFieldsList.add(textField);
            gridPane.add(textField, 1, i);
            TextField textField1 = new TextField();
            textField1.setMaxSize(80.0, textField1.getHeight());
            textField1.setText(String.valueOf(sectors.getPrice()));

            priceList.add(textField1);
            Label label1 = new Label("\u20ac");
            HBox hBox = new HBox();
            hBox.getChildren().add(textField1);
            hBox.getChildren().add(label1);
            hBox.setAlignment(Pos.CENTER);
            gridPane.add(hBox, 2, i);
            CheckBox checkBox = new CheckBox();
            checkBox.setText("Abilita riduzioni");
            reductionCheckList.add(checkBox);
            gridPane.add(checkBox, 3, i);
            initListeners(textField, seats.get(j), textField1);
            j++;
            i++;
        }
        form.getChildren().add(0, gridPane);
    }

    private void initListeners(TextField textField, String s, TextField textField1) {
        textField.textProperty().addListener((ov, oldValue, newValue) -> textFieldControl(textField, newValue));

        textField1.textProperty().addListener((ov, oldValue, newValue) -> textFieldControl(textField1, newValue));

        textField.focusedProperty().addListener((arg0, oldPropertyValue, newPropertyValue) ->
                focusText(textField, Integer.parseInt(s), Integer.parseInt(textField.getText()), newPropertyValue));

        textField1.focusedProperty().addListener((arg0, oldPropertyValue, newPropertyValue) ->
                focusText1(textField1, Integer.parseInt(textField.getText()), newPropertyValue));

    }


    private void textFieldControl(TextField textField, String newValue) {
        try {
            if (!newValue.matches("\\d*\\.?\\d+")) {
                textField.setText(newValue.replaceAll("[^\\d\\.?\\d+]", ""));
            }
            if (textField.getText().length() > 7) {
                String s = textField.getText().substring(0, 7);
                textField.setText(s);
            }
            textField.getText();
        } catch (NullPointerException ignored) {
        }
    }

    private void focusText(TextField textField, int oldVal, Integer newVal, Boolean newPropertyValue) {
        if (!newPropertyValue) {
            if (newVal > oldVal) {
                textField.setText(String.valueOf(oldVal));
            }
            if (newVal.equals("")) {
                textField.setText("0");
            }
        } else {
            if (newVal.equals("0")) {
                textField.setText("");
            }
        }

    }

    private void focusText1(TextField textField1, Integer newVal, Boolean newPropertyValue) {
        if (newPropertyValue) {
            if (newVal.equals("0")) {
                textField1.setText("");
            }
        }
    }


    private void next() {
        try {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Attenzione!");
            alert.setHeaderText("I seguenti settori non avranno riduzioni:");
            //List<EventModel.Sectors> sectorsList = new ArrayList<>();
            for (int i = 0; i < sectorsList.size(); i++) {
               /* EventModel.Sectors sector = new EventModel().new Sectors();
                sector.setName(sectors.get(i));
                sector.setPrice(Integer.parseInt(priceList.get(i).getText()));
                sector.setReduction(reductionCheckList.get(i).isSelected());
                sector.setSeats(Integer.parseInt(seatsFieldsList.get(i).getText()));
                sectorsList.add(sector);*/
                if (!reductionCheckList.get(i).isSelected()) {
                    alert.setContentText(alert.getContentText() + "-" + sectorsList.get(i).getName() + "\n");
                }
            }
            if (!alert.getContentText().isEmpty()) {
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    //insertController.toInsertReduction(sectorsList);
                    insertController.toInsertReduction(priceList, reductionCheckList, seatsFieldsList);
                }
            } else {
                alert.setHeaderText("Tutti i settori avranno riduzioni");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    //insertController.toInsertReduction(sectorsList);
                    insertController.toInsertReduction(priceList, reductionCheckList, seatsFieldsList);
                }
            }

        } catch (NullPointerException | NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Compilare tutti i campi prima di procedere", "Form Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }

    }

    @Override
    public void update(Observable o, Object arg) {
        EventModel eventModel = (EventModel) o;
        insertTicketPlaybillImageView.setImage(eventModel.getBillboard());
        if (form.getChildren().get(0) instanceof GridPane) {
            form.getChildren().remove(0);
        }
        init(eventModel);
    }
}
