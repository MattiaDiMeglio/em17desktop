package controller;

import model.EventModel;
import view.InsertView;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class InsertController
{
    ViewSourceController viewSourceController;
    DBController dbController = DBController.getInstance();

    public InsertController(ViewSourceController viewSourceController) {
        this.viewSourceController=viewSourceController;
    }

    public void back () throws ExecutionException, InterruptedException {
        System.out.println("indietro");
        viewSourceController.toDashBoardView();
    }

    public void next(InsertView insertView, List<String> texts) {
        System.out.println("avanti");
        EventModel newEvent =  new EventModel();
        viewSourceController.toInsetTicketView();
        //newEvent.setEventName(insertView.ge);

    }
}
