package controller;

public class EventController {


    public boolean delete (String key){
        DBController dbController = DBController.getInstance();

        dbController.delete(key);


        return false;
    }
}
