package view;

import controller.SearchController;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import org.controlsfx.control.textfield.TextFields;


/**
 * questa classe veien invocata ogni volta che viene premuto il tasto per la ricerca avanzata. Crea
 * un popup con le informazioni per filtrare la ricerca secondo le esigenze dell'utente. La classe è
 * composta esclusivamente dal costruttore.
 *
 * @author ingsw20
 */
class AdvancedSearchView {

  /**
   * Il costruttore si occupa della creazione del popup e dell'invio delle preferenze settate al
   * SearchController che farà i controlli per i risultati.
   *
   * @param searchController controller per la ricerca
   */
  AdvancedSearchView(SearchController searchController) {
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    alert.setTitle("Ricerca Avanzata");
    alert.setHeaderText("Ricerca avanzata");
    Button okButton = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);

    VBox vBox = new VBox();
    GridPane grid = new GridPane();
    grid.setHgap(10);
    grid.setVgap(10);
    grid.setPadding(new Insets(20, 150, 10, 10));

    Label error = new Label();
    error.setTextFill(Paint.valueOf("red"));
    error.setVisible(false);
    error.setText("*La data di fine deve essere successiva alla data di inizio");

    TextField prezzoMaxText = new TextField();
    TextField prezzoMinText = new TextField();
    prezzoMaxText.setPromptText("Max");
    prezzoMaxText.textProperty().addListener((observable, oldValue, newValue) -> textFieldControl(prezzoMaxText, newValue));
    prezzoMinText.setPromptText("Min");
    prezzoMinText.textProperty().addListener((observable, oldValue, newValue) -> textFieldControl(prezzoMinText, newValue));

    DatePicker startDatePicker = new DatePicker();
    startDatePicker.setPromptText("Data inizio");
    DatePicker endDatePicker = new DatePicker();
    endDatePicker.setPromptText("Data fine");

    endDatePicker.valueProperty().addListener((ov, oldValue, newValue) -> {
      if (startDatePicker.getValue() != null && newValue.isBefore(startDatePicker.getValue())) {
        error.setVisible(true);
        okButton.setDisable(true);
      } else {
        okButton.setDisable(false);
        error.setVisible(false);
      }
    });

    startDatePicker.valueProperty().addListener((ov, oldValue, newValue) -> {
      if (endDatePicker.getValue() != null && newValue.isAfter(endDatePicker.getValue())) {
        error.setVisible(true);
        okButton.setDisable(true);
      } else {
        okButton.setDisable(false);
        error.setVisible(false);
      }
    });

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

    vBox.getChildren().addAll(grid, error);
    alert.getDialogPane().setContent(vBox);

    Optional<ButtonType> result = alert.showAndWait();
    if (result.get() == ButtonType.OK) {
      if (startDatePicker.getValue() == null) {
        startDatePicker.setValue(LocalDate.MIN);
      } else {
        startDatePicker.getValue().minusDays(1);
      }

      if (endDatePicker.getValue() == null) {
        endDatePicker.setValue(LocalDate.MAX);
      } else {
        endDatePicker.getValue().plusDays(1);
      }

      error.setVisible(false);
      searchController.advancedSearch(prezzoMinText.getText(),
          prezzoMaxText.getText(),
          startDatePicker.getValue(),
          endDatePicker.getValue(),
          locationName.getText());
    }
  }

  /**
   * metodo che effettua il controllo dei caratteri inseriti
   *
   * @param textField textfield da controllare
   * @param newValue  nuovo valore inserito
   */
  private void textFieldControl(TextField textField, String newValue) {
    try {
      if (!newValue.matches("\\d*\\.?\\d+")) {
        textField.setText(newValue.replaceAll("[^\\d.?\\d+]", ""));
      }
      if (textField.getText().length() > 7) {
        String s = textField.getText().substring(0, 7);
        textField.setText(s);
      }
      textField.getText();
    } catch (NullPointerException ignored) {
    }
  }
}
