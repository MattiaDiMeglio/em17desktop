package view;

import controller.InsertController;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import model.EventModel;

public class InsertTicketTypeView {

    private Button next;
    private Button back;
    private VBox form;

    public InsertTicketTypeView(InsertController insertController, EventModel newEvent, VBox insertTicketVbox,
                                Button ticketTypeBackButton, Button ticketTypeNextButton) {
        next = ticketTypeNextButton;
        back = ticketTypeBackButton;
        form = insertTicketVbox;

        next.setOnAction(event -> {

        });

        back.setOnAction(event -> {
            insertController.back();
        });


    }

    private void init (){

    }

    private void next(){

    }
}
