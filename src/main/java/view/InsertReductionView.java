package view;

import controller.InsertController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.EventModel;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InsertReductionView {
    private VBox insertReductionVbox;
    private InsertController insertController;
    private TextField bambiniReduction = new TextField("0");
    private TextField anzianiReduction = new TextField("0");
    private TextField studentiReduction = new TextField("0");




    public InsertReductionView(InsertController insertController, EventModel newEvent,
                               VBox insertReductionVbox, Button ticketReductionBackButton,
                               Button ticketReductionNextButton, ImageView insertReductionPlaybillImageView) {

        this.insertReductionVbox = insertReductionVbox;
        this.insertController = insertController;
        insertReductionPlaybillImageView.setImage(newEvent.getBillboard());

        ticketReductionNextButton.setOnAction(event -> {
            next();
        });

        ticketReductionBackButton.setOnAction(event -> {
            insertController.back();
        });



        init();
    }

    private void init() {
        int i=2;
        int j=0;
        GridPane gridPane = new GridPane();

        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20, 150, 10, 10));
        gridPane.add(new Label("Riduzioni Applicabili"), 0, 0);
        gridPane.add(new Label("Percentuale di Sconto da Applicare"), 1, 0);

        Separator separator = new Separator();
        separator.setValignment(VPos.CENTER);
        GridPane.setConstraints(separator, 0, 1);
        GridPane.setColumnSpan(separator, 7);
        gridPane.getChildren().add(separator);

        Label bambini = new Label("Bambini");
        gridPane.add(bambini, 0, 2);
        bambiniReduction.setMaxSize(80.0, bambiniReduction.getHeight());
        HBox hBox1 = new HBox();
        hBox1.getChildren().add(bambiniReduction);
        hBox1.getChildren().add(new Label("%"));
        gridPane.add(hBox1, 1, 2);

        Label anziani = new Label("Anziani");
        gridPane.add(anziani, 0, 3);
        anzianiReduction.setMaxSize(80.0, anzianiReduction.getHeight());
        HBox hBox2 = new HBox();
        hBox2.getChildren().add(anzianiReduction);
        hBox2.getChildren().add(new Label("%"));
        gridPane.add(hBox2, 1, 3);

        Label studenti = new Label("Studenti");
        gridPane.add(studenti, 0, 4);
        studentiReduction.setMaxSize(80.0, studentiReduction.getHeight());
        HBox hBox3 = new HBox();
        hBox3.getChildren().add(studentiReduction);
        hBox3.getChildren().add(new Label("%"));
        gridPane.add(hBox3, 1, 4);


        initListeners(bambiniReduction, anzianiReduction, studentiReduction);

        insertReductionVbox.getChildren().add(0, gridPane);
    }

    private void initListeners(TextField bambini, TextField anziani, TextField studenti) {
        bambini.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
               textFieldControl(bambini, oldValue, newValue);
            }
        });
        anziani.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
                textFieldControl(anziani, oldValue, newValue);
            }
        });
        studenti.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
                textFieldControl(studenti, oldValue, newValue);
            }
        });
    }


    private void textFieldControl(TextField textField, String oldValue, String newValue) {
        try {
            if (!newValue.matches("\\d*\\.?\\d+")) {
                textField.setText(newValue.replaceAll("[^\\d\\.?\\d+]", ""));
            }
            if (textField.getText().length() > 7) {
                String s = textField.getText().substring(0, 7);
                textField.setText(s);
            }
            if (Integer.parseInt(textField.getText()) > 100){
                textField.setText(oldValue);
            }
            if (Integer.parseInt(textField.getText()) < 0){
                textField.setText(oldValue);
            }
        } catch (NullPointerException e){
        }
    }

    private void next() {
        try {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Attenzione!");
            alert.setHeaderText("Verranno applicate le seguenti riduzioni:");
            alert.setContentText("Bambini: " + bambiniReduction.getText() + "% \n");
            alert.setContentText(alert.getContentText() + "Anziani: " + anzianiReduction.getText() + "% \n");
            alert.setContentText(alert.getContentText() + "Studenti: " + studentiReduction.getText() + "% \n");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                insertController.ticketReductionNext();
            }


        } catch (NullPointerException e) {
            JOptionPane.showMessageDialog(null, "Compilare tutti i campi prima di procedere", "Form Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Compilare tutti i campi prima di procedere", "Form Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }

    }

}
