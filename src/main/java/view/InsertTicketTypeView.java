package view;

import com.google.api.services.storage.Storage;
import controller.InsertController;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.EventModel;

import java.util.ArrayList;
import java.util.List;

public class InsertTicketTypeView {

    private InsertController insertController;
    private EventModel newEvent;
    private Button next;
    private Button back;
    private VBox form;
    private List<String> sectors = new ArrayList<>();
    private List<String> seats = new ArrayList<>();

    public InsertTicketTypeView(InsertController insertController, EventModel newEvent, VBox insertTicketVbox,
                                Button ticketTypeBackButton, Button ticketTypeNextButton, ImageView insertTicketPlaybillImageView) {
        next = ticketTypeNextButton;
        back = ticketTypeBackButton;
        form = insertTicketVbox;
        this.insertController=insertController;
        this.newEvent = newEvent;
        insertTicketPlaybillImageView.setImage(newEvent.getBillboard());

        next.setOnAction(event -> {

        });

        back.setOnAction(event -> {
            insertController.back();
        });

        init();


    }

    private void init (){
        int i=2;
        int j=0;
        sectors = insertController.getSectorName(newEvent.getLocationName(), newEvent.getLocationAddress());
        seats = insertController.getSteatsList(newEvent.getLocationName(), newEvent.getLocationAddress());
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20, 150, 10, 10));
        gridPane.add(new Label("Nome Settore"), 0, 0);
        gridPane.add(new Label("Numero Posti"), 1, 0);
        gridPane.add(new Label("Costo intero"), 2, 0);
        gridPane.addRow(1, new Separator());



        for (String string: sectors) {
            Label label = new Label();
            label.setText(sectors.get(j));
            gridPane.add(label,0, i );
            TextField textField = new TextField();
            textField.setText(seats.get(j));
            textField.setMaxSize(50.0, textField.getHeight());
            gridPane.add(textField, 1, i);
            TextField textField1 = new TextField();
            textField1.setMaxSize(50.0, textField1.getHeight());
            gridPane.add(textField1, 2, i);
            CheckBox checkBox = new CheckBox();
            checkBox.setText("Abilita riduzioni");
            gridPane.add(checkBox, 3, i);
            j++;
            i++;
        }

        form.getChildren().add(0, gridPane);
    }

    private void next(){

    }
}
