package view;

import com.google.api.services.storage.Storage;
import controller.InsertController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InsertTicketTypeView {

    private InsertController insertController;
    private EventModel newEvent;
    private Button next;
    private Button back;
    private VBox form;
    private List<String> sectors = new ArrayList<>();
    private List<String> seats = new ArrayList<>();
    private List<TextField> seatsFieldsList = new ArrayList<>();
    private List<TextField> prizeList = new ArrayList<>();
    private List<CheckBox> reductionCheckList = new ArrayList<>();
    private final Integer[] oldVal = {0};


    public InsertTicketTypeView(InsertController insertController, EventModel newEvent, VBox insertTicketVbox,
                                Button ticketTypeBackButton, Button ticketTypeNextButton, ImageView insertTicketPlaybillImageView) {
        next = ticketTypeNextButton;
        back = ticketTypeBackButton;
        form = insertTicketVbox;
        this.insertController = insertController;
        this.newEvent = newEvent;
        insertTicketPlaybillImageView.setImage(newEvent.getBillboard());
        if (form.getChildren().get(0) instanceof GridPane) {
            form.getChildren().remove(0);
        }


        next.setOnAction(event -> {
            next();
        });

        back.setOnAction(event -> {
            insertController.back();
        });

        init();


    }

    private void init() {
        int i = 2;
        int j = 0;
        sectors = insertController.getSectorName(newEvent.getLocationName(), newEvent.getLocationAddress());
        seats = insertController.getSteatsList(newEvent.getLocationName(), newEvent.getLocationAddress());
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


        for (String string : sectors) {
            Label label = new Label();
            label.setText(sectors.get(j));
            gridPane.add(label, 0, i);
            TextField textField = new TextField();
            textField.setText(seats.get(j));
            textField.setMaxSize(50.0, textField.getHeight());
            seatsFieldsList.add(textField);
            gridPane.add(textField, 1, i);
            TextField textField1 = new TextField();
            textField1.setMaxSize(50.0, textField1.getHeight());
            prizeList.add(textField1);
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
        textField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
                oldVal[0] = Integer.parseInt(textFieldControl(textField, oldValue, newValue));
            }
        });

        textField1.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
                oldVal[0] = Integer.parseInt(textFieldControl(textField1, oldValue, newValue));
            }
        });

        textField.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) {

                focusText(textField, Integer.parseInt(s), Integer.parseInt(textField.getText()), newPropertyValue);

            }
        });

    }

    private String textFieldControl(TextField textField, String oldValue, String newValue) {
        try {
            if (!newValue.matches("\\d*")) {
                textField.setText(oldValue.toString());
            }
            if (textField.getText().length() > 7) {
                String s = textField.getText().substring(0, 7);
                textField.setText(s);
            }
            return textField.getText();
        } catch (NullPointerException e) {
        }
        return oldValue;
    }

    private void focusText(TextField textField, int oldVal, Integer newVal, Boolean newPropertyValue) {
        if (!newPropertyValue) {
            if (newVal > oldVal) {
                textField.setText(String.valueOf(oldVal));
            }
        }
    }


    private void next() {
        try {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Attenzione!");
            alert.setHeaderText("I seguenti settori non avranno riduzioni:");
            List<EventModel.Sectors> sectorsList = new ArrayList<>();
            for (int i = 0; i < sectors.size(); i++) {
                EventModel.Sectors sector = new EventModel().new Sectors();
                sector.setName(sectors.get(i));
                sector.setPrize(Integer.parseInt(prizeList.get(i).getText()));
                sector.setReduction(reductionCheckList.get(i).isSelected());
                sector.setSeats(Integer.parseInt(seatsFieldsList.get(i).getText()));
                sectorsList.add(sector);
                if (!sector.isReduction()) {
                    alert.setContentText(alert.getContentText() + "-" + sector.getName() + "\n");
                }
            }
            if (!alert.getContentText().isEmpty()) {
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    insertController.ticketTypeNext(sectorsList);
                }
            } else {
                alert.setHeaderText("Tutti i settori avranno riduzioni");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    insertController.ticketTypeNext(sectorsList);
                }
            }

        } catch (NullPointerException e) {
            JOptionPane.showMessageDialog(null, "Compilare tutti i campi prima di procedere", "Form Error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Compilare tutti i campi prima di procedere", "Form Error",
                    JOptionPane.ERROR_MESSAGE);
        }

    }

}
