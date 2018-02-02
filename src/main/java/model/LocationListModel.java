package model;

import javafx.application.Platform;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class LocationListModel extends Observable {

        private static LocationListModel instance = new LocationListModel();
        List<LocationModel> locationList = new ArrayList<>();

        public static LocationListModel getInstance() {
            return instance;
        }

        protected LocationListModel (){
        };


        public List<LocationModel> getListaEventi() {
            return locationList;
        }

        public void setListaEventi(LocationModel location) {
            this.locationList.add(location);
            Platform.runLater(() -> {
                setChanged();
                notifyObservers(locationList.size());
            });

        }

        public void deleteList (){
            this.locationList.removeAll(locationList);
        }
    }


