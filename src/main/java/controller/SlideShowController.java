package controller;

import javafx.beans.Observable;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import model.EventListModel;
import model.SlideShowModel;
import view.SlideShowView;

import java.util.ArrayList;
import java.util.List;
import java.util.Observer;

public class SlideShowController implements Observer{
    public class DashBillBoard {
        Button button;
        private int index;
        private String url;

        public DashBillBoard(Button button, int index){
            this.button=button;
            this.index=index;
        }
    }

    List<DashBillBoard> imageList = new ArrayList<>();
    EventListModel eventListModel = EventListModel.getInstance();


    //SlideShowModel slideShowModel = new SlideShowModel();
    SlideShowView slideShowView = new SlideShowView();

    public void createSlide (){

    }

    @Override
    public void update(java.util.Observable o, Object arg) {

    }
}
