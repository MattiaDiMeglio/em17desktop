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
        for (int i = 0; i < eventModelList.size(); i++) {
            EventModel eventModel = eventModelList.get(i);
            eventNames.add(eventModel.getNomeEvento());
            mergedModel.setTicketSoldArray(mergeArray(eventModel.getTicketsSoldPerMonth(), mergedModel.getTicketsSoldArray()));

            mergedModel.setMaxTickets(mergeMaxVisitors(eventModel.getMaxVisitatori()));
        }
    }

    private Integer[] mergeArray(Integer[] firstArray, Integer[] secondArray) {
        Integer[] ris = new Integer[12];
        for (int i = 0; i < ris.length; i++) {
            ris[i] = firstArray[i] + secondArray[i];
        }
        return ris;
    }


}
