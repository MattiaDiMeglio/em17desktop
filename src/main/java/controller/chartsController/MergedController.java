package controller.chartsController;

import model.EventModel;
import model.chartsModels.MergedModel;

import java.util.ArrayList;
import java.util.List;

public class MergedController {
    private List<EventModel> eventModelList;
    private MergedModel mergedModel = MergedModel.getInstance();

    public MergedController(List<EventModel> eventModelList) {
        this.eventModelList = eventModelList;

        merge();
    }

    private void merge() {
        mergedModel.resetModel();
        List<String> eventNames = new ArrayList<>();
        List<Double> soldPerEvent = new ArrayList<>();
        for (EventModel eventModel : eventModelList) {
            // dati per il barchart
            eventNames.add(eventModel.getEventName());
            soldPerEvent.add(eventModel.getTicketSold());

            // dati per il linechart
            mergedModel.setTicketSoldArray(mergeArray(eventModel.getTicketsSoldPerMonth(), mergedModel.getTicketsSoldArray()));

            //dati per il piechart
            mergedModel.setMaxVisitors(mergeMaxVisitors(mergedModel.getMaxVisitors(), eventModel.getMaxVisitors()));
            mergedModel.setTicketsSold(mergeTicketsSold(mergedModel.getTicketsSold(), eventModel.getTicketSold()));
        }
    }

    private Double mergeTicketsSold(Double ticketsSold, Double ticketsSold1) {
        return ticketsSold + ticketsSold1;
    }

    private Double mergeMaxVisitors(Double maxVisitors, Double maxVisitors1) {
        return maxVisitors + maxVisitors1;
    }

    private Integer[] mergeArray(Integer[] firstArray, Integer[] secondArray) {
        Integer[] ris = new Integer[12];
        for (int i = 0; i < ris.length; i++) {
            ris[i] = firstArray[i] + secondArray[i];
        }
        return ris;
    }


}
