package model;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import view.SlideShowView;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class SlideShowModel extends Observable {

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

    public void add ( String url, int index ){
        Button button = new Button();
        button.setGraphic(new ImageView( new Image(url)));
        DashBillBoard element = new DashBillBoard(button, index);
        imageList.add(element);

    }

    public List<DashBillBoard> getImageList() {
        return imageList;
    }
}
