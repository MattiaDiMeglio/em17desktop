package model.chartsModels;

import javafx.application.Platform;

import java.util.HashMap;
import java.util.List;
import java.util.Observable;

public class BarChartModel extends Observable{
    private List<Integer> soldPerLocation;
    private List<String> locationNames;
    private HashMap<Integer,String> locationIdMap;
    private static BarChartModel ourInstance = new BarChartModel();

    public static BarChartModel getInstance() {
        return ourInstance;
    }

    private BarChartModel() {}


    public void setSoldPerLocation(List<Integer> soldPerLocation) {
        this.soldPerLocation = soldPerLocation;
        setChanged();
    }

    public void setLocationNames(List<String> locationNames) {
        this.locationNames = locationNames;
        setChanged();
    }

    public void setLocationIdMap(HashMap<Integer,String> locationIdMap) {
        this.locationIdMap = locationIdMap;
        setChanged();
        Platform.runLater(this::notifyObservers);
    }

    public List<Integer> getSoldPerLocation() {
        return soldPerLocation;
    }

    public List<String> getLocationNames() {
        return locationNames;
    }

    public HashMap<Integer, String> getLocationIdMap() {
        return locationIdMap;
    }
}
