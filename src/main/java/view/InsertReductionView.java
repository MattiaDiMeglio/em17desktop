package view;

import controller.InsertController;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.EventModel;

import javax.swing.*;
import java.util.*;

/**
 * Classe view che si occupa della schermata di riduzioni pre i biglietti
 *
 * @author ingsw20
 */
public class InsertReductionView implements Observer {
    private VBox insertReductionVbox; //Vbox dove verranno inseriti gli elementi per le riduzioni
    private InsertController insertController;
    //Creo le tre textField per i tipi di riduzione
    /**
     * textfield per la riduzione per bambini
     */
    private TextField childrenReduction = new TextField("0");
    /**
     * textfield per la riduzione per anziani
     */
    private TextField eldersReduction = new TextField("0");
    /**
     * textfield per la riduzione per studenti
     */
    private TextField studentsReduction = new TextField("0");
    /**
     * imageview per la locandina
     */
    private ImageView insertReductionPlaybillImageView;


    /**
     * Costruttore della classe
     *
     * @param insertController                 istanza di {@link InsertController}
     * @param newEvent                         model dell'evento
     * @param insertReductionVbox              vbox principale
     * @param ticketReductionBackButton        tasto per tornare indietro
     * @param ticketReductionNextButton        tasto per andare avanti
     * @param insertReductionPlaybillImageView imageview per la locandina
     */
    public InsertReductionView(InsertController insertController, EventModel newEvent,
                               VBox insertReductionVbox, Button ticketReductionBackButton,
                               Button ticketReductionNextButton, ImageView insertReductionPlaybillImageView) {

        this.insertReductionVbox = insertReductionVbox; //valorizzazione della Vbox principale
        this.insertController = insertController; //valorizzazione del controller
        this.insertReductionPlaybillImageView = insertReductionPlaybillImageView;
        newEvent.addObserver(this);
        insertController.update(newEvent); // avvio update della schermata

        //listener del bottone conferma
        ticketReductionNextButton.setOnAction(event -> next());

        //listener del bottone indietro
        ticketReductionBackButton.setOnAction(event -> insertController.toTicketType());
    }

    /**
     * Metodo che inizializza la schermata
     *
     * @param eventModel model per l'inizializzazione della view
     */
    private void init(EventModel eventModel) {
        //gridpane in cui si inseriranno gli altri elementi
        GridPane gridPane = new GridPane();

        //si settano le dimensioni base della gridpane e si inseriscono i primi due figli
        //che faranno da titoli per le "colonne"
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20, 150, 10, 10));
        gridPane.add(new Label("Riduzioni Applicabili"), 0, 0);
        gridPane.add(new Label("Percentuale di Sconto da Applicare"), 1, 0);

        //separator che divide la prima riga dal resto dell aschermata
        Separator separator = new Separator();
        separator.setValignment(VPos.CENTER);
        GridPane.setConstraints(separator, 0, 1);
        GridPane.setColumnSpan(separator, 7);
        gridPane.getChildren().add(separator);

        //Riga con gli elementi per la riduzione per i Bambini
        Label bambini = new Label("Bambini");
        gridPane.add(bambini, 0, 2);
        childrenReduction.setMaxSize(80.0, childrenReduction.getHeight());
        childrenReduction.setText(String.valueOf(eventModel.getChildrenReduction()));
        HBox hBox1 = new HBox();
        hBox1.getChildren().add(childrenReduction);
        hBox1.getChildren().add(new Label("%"));
        gridPane.add(hBox1, 1, 2);

        //Riga con gli elementi per la riduzione per i Anziani
        Label anziani = new Label("Anziani");
        gridPane.add(anziani, 0, 3);
        eldersReduction.setMaxSize(80.0, eldersReduction.getHeight());
        eldersReduction.setText(String.valueOf(eventModel.getEldersReduction()));
        HBox hBox2 = new HBox();
        hBox2.getChildren().add(eldersReduction);
        hBox2.getChildren().add(new Label("%"));
        gridPane.add(hBox2, 1, 3);

        //Riga con gli elementi per la riduzione per i Studenti
        Label studenti = new Label("Studenti");
        gridPane.add(studenti, 0, 4);
        studentsReduction.setMaxSize(80.0, studentsReduction.getHeight());
        studentsReduction.setText(String.valueOf(eventModel.getStudentReduction()));
        HBox hBox3 = new HBox();
        hBox3.getChildren().add(studentsReduction);
        hBox3.getChildren().add(new Label("%"));
        gridPane.add(hBox3, 1, 4);

        //inizializzazione dei listener per gli elementi creati
        initListeners(childrenReduction, eldersReduction, studentsReduction);

        //si aggiunge la gridpane creata nella Vbox principale
        insertReductionVbox.getChildren().add(0, gridPane);
    }

    /**
     * Metodo che si occupa dell'inizializzazione dei listner per le textField bambini, anziani e studenti
     *
     * @param bambini  textfield contenente la riduzione per i bambini
     * @param anziani  textfield contenente la riduzione per gli anziani
     * @param studenti textfield contenente la riduzione per gli studenti
     */
    private void initListeners(TextField bambini, TextField anziani, TextField studenti) {
        //Listener per il cambio del valore nella textfield
        bambini.textProperty().addListener((ov, oldValue, newValue) -> textFieldControl(bambini, oldValue, newValue));

        //Listener per il cambio del valore nella textfield
        anziani.textProperty().addListener((ov, oldValue, newValue) -> textFieldControl(anziani, oldValue, newValue));

        //Listener per il cambio del valore nella textfield
        studenti.textProperty().addListener((ov, oldValue, newValue) -> textFieldControl(studenti, oldValue, newValue));
    }


    /**
     * Metodo che si occupa dei controlli sulla textfield
     *
     * @param textField textfield da controllare
     * @param oldValue  valore precedente
     * @param newValue  nuovo valore
     */
    private void textFieldControl(TextField textField, String oldValue, String newValue) {
        try {
            //controlla che gli elementi inseriti siano solo numerici e "."
            if (!newValue.matches("\\d*\\.?\\d+")) {
                textField.setText(newValue.replaceAll("[^\\d.?\\d+]", ""));
            }
            //permette di inserire al massimo 7 caratteri
            if (textField.getText().length() > 7) {
                String s = textField.getText().substring(0, 7);
                textField.setText(s);
            }
            //se il valore è maggiore di 100, torna al valore subito precedente
            if (Double.valueOf(textField.getText()) > 100d) {
                textField.setText(oldValue);
            }
            //se il valore è meno di 0 torna al valore subito precedente
            if (Double.valueOf(textField.getText()) < 0d) {
                textField.setText(oldValue);
            }
        } catch (Exception ignored) {
        }
    }

    /**
     * metodo che viene chiamato dal listener del bottone next
     */
    private void next() {
        try {
            //mostra un alert di conferma, che mostra un riepilogo delle percentuali inserite
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Attenzione!");
            alert.setHeaderText("Verranno applicate le seguenti riduzioni:");
            alert.setContentText("Bambini: " + childrenReduction.getText() + "% \n");
            alert.setContentText(alert.getContentText() + "Anziani: " + eldersReduction.getText() + "% \n");
            alert.setContentText(alert.getContentText() + "Studenti: " + studentsReduction.getText() + "% \n");

            List<Double> reductions = new ArrayList<>();
            reductions.add(Double.valueOf(childrenReduction.getText()));
            reductions.add(Double.valueOf(eldersReduction.getText()));
            reductions.add(Double.valueOf(studentsReduction.getText()));

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                //avanza di schermata in caso venga premuto ok
                insertController.toInsertRecap(reductions);
            }
        } catch (NullPointerException | NumberFormatException e) {
            //in caso non tutti gli elementi siano valorizzati mostra un'errore
            JOptionPane.showMessageDialog(null, "Compilare tutti i campi prima di procedere", "Form Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }

    }

    /**
     * metodo per l'aggiornamento della view
     *
     * @param o   model chiamante
     * @param arg null
     */
    @Override
    public void update(Observable o, Object arg) {
        EventModel eventModel = (EventModel) o;
        //Si inserisce l'immagine di locandina presa dal nuovo evento
        insertReductionPlaybillImageView.setImage(eventModel.getBillboard());
        if (insertReductionVbox.getChildren().get(0) instanceof GridPane) {
            insertReductionVbox.getChildren().remove(0);
        }
        //inizializzazione della schermata
        init(eventModel);
    }
}
