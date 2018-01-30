package controller;

import model.EventModel;
import view.InsertView;

import java.util.concurrent.ExecutionException;

public class InsertController
{
    ViewSourceController viewSourceController;
    DBController dbController = DBController.getInstance();

    public InsertController(ViewSourceController viewSourceController) {
        this.viewSourceController=viewSourceController;
    }

    public void back () throws ExecutionException, InterruptedException {
        viewSourceController.toDashBoardView();
    }

    public void next(InsertView insertView) {
        EventModel newEvent =  new EventModel();
        //newEvent.setEventName(insertView.ge);

    }
}
