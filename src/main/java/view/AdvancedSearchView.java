package view;

import controller.SearchController;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import model.EventModel;
import org.controlsfx.control.textfield.TextFields;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AdvancedSearchView {

    AdvancedSearchView(SearchController searchController) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Ricerca Avanzata");
        alert.setHeaderText("Ricerca avanzata");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField prezzoMaxText = new TextField();
        TextField prezzoMinText = new TextField();
        prezzoMaxText.setPromptText("Max");
        prezzoMaxText.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                prezzoMaxText.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
        prezzoMinText.setPromptText("Min");
        prezzoMinText.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                prezzoMinText.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        DatePicker startDatePicker = new DatePicker();
        startDatePicker.setPromptText("Data inizio");
        DatePicker endDatePicker = new DatePicker();
        endDatePicker.setPromptText("Data fine");

        TextField locationName = new TextField();
        locationName.setPromptText("Nome location");
        List<String> locationNames = searchController.getLocationNames();
        TextFields.bindAutoCompletion(locationName, locationNames);

        grid.add(new Label("Prezzo: "), 0, 0);
        grid.add(prezzoMinText, 1, 0);
        grid.add(prezzoMaxText, 2, 0);
        grid.add(new Label("Data:"), 0, 1);
        grid.add(startDatePicker, 1, 1);
        grid.add(endDatePicker, 2, 1);
        grid.add(new Label("Nome Location"), 0, 2);
        grid.add(locationName, 1, 2);
        alert.getDialogPane().setContent(grid);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            if (startDatePicker.getValue() == null) {
                startDatePicker.setValue(LocalDate.MIN);
            }else {
                startDatePicker.getValue().minusDays(1);
            }

            if (endDatePicker.getValue() == null) {
                endDatePicker.setValue(LocalDate.MAX);
            }else {
                endDatePicker.getValue().plusDays(1);
            }

            searchController.advancedSearch(prezzoMinText.getText(),
                    prezzoMaxText.getText(),
                    startDatePicker.getValue(),
                    endDatePicker.getValue(),
                    locationName.getText());
        } else {
            System.out.println("no");
        }
    }
}
