package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class LocationModel extends Observable {
    private String locationName;
    private String locationAddress;
    private List<String> sectorList = new ArrayList<>();
    private List<String> seatsList = new ArrayList<>();
    private String locationID;

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getLocationAddress() {
        return locationAddress;
    }

    public void setLocationAddress(String locationAddress) {
        this.locationAddress = locationAddress;
    }

    public List<String> getSectorList() {
        return sectorList;
    }

    public void setSectorList(List<String> sectorList) {
        this.sectorList = sectorList;
    }

    public List<String> getSeatsList() {
        return seatsList;
    }

    public void setSeatsList(List<String> seatsList) {
        this.seatsList = seatsList;
    }

    public void setLocationID(String locationID) {
        this.locationID = locationID;
    }

    public String getLocationID() {
        return locationID;
    }
}
