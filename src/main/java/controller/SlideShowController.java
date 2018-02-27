package controller;

import java.util.List;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;

import model.EventModel;
import view.SlideShowView;


/**
 * controller per la creazione degli slideshow.
 *
 * @author ingSW20
 */
public class SlideShowController {

  /**
   * variabile per l'utilizzo di {@link InsertController}.
   *
   * @see #setImageList(List)
   */
  private InsertController insertController;
  /**
   * Variabile per l'utilizzo di {@link ViewSourceController}.
   *
   * @see #handler(int)
   */
  private ViewSourceController viewSourceController;

  /**
   * metodo per la creazione dello slideshow all'interno della {@link view.DashBoardView
   * DashBoardView}.
   *
   * @param hBox HBox contenente le immagini
   * @param leftButton bottone per scorrere la lista di immagini verso sinistra
   * @param rightButton bottone per scorrere la lista di immagini verso destra
   * @param viewSourceController variabile per valorizzare {@link #viewSourceController}
   */
  public void createSlide(HBox hBox, Button leftButton,
      Button rightButton, ViewSourceController viewSourceController) {
    new SlideShowView(hBox, leftButton, rightButton, this);
    this.viewSourceController = viewSourceController;
  }

  /**
   * metodo per la creazione dello slideshow all'interno di {@link view.EventView EventView}.
   *
   * @param hBox HBox contenente le immagini
   * @param leftButton bottone per scorrere la lista di immagini verso sinistra
   * @param rightButton bottone per scorrere la lista di immagini verso destra
   * @param eventModel model contenente le informazioni dell'evento da visualizzare
   */
  public void createSlide(HBox hBox, Button leftButton,
      Button rightButton, EventModel eventModel) {
    new SlideShowView(hBox, leftButton, rightButton, this, eventModel);
  }

  /**
   * metodo per la creazione dello slideshow all'interno di
   * {@link view.InsertView InsertView}.
   *
   * @param hbox HBox contenente le immagini
   * @param insertController variabile per la valorizzazione di {@link #insertController}
   * @param leftButton bottone per scorrere la lista di immagini verso sinistra
   * @param rightButton bottone per scorrere la lista di immagini verso destra
   * @param immagini lista con le immagini da impostare nello slideshow
   */
  public void createSlide(InsertController insertController, Button leftButton, HBox hbox,
      Button rightButton, List<Image> immagini) {
    this.insertController = insertController;
    new SlideShowView(hbox, leftButton, rightButton, this, immagini);
  }

  /**
   * metodo per la creazione dello slideshow all'interno di
   * {@link view.InsertRecapView InsertRecapView}.
   *
   * @param hbox HBox contenente le immagini
   * @param insertController variabile per la valorizzazione di {@link #insertController}
   * @param leftButton bottone per scorrere la lista di immagini verso sinistra
   * @param rightButton bottone per scorrere la lista di immagini verso destra
   * @param eventModel model con le immagini da impostare nello slideshow
   */
  public void createSlide(InsertController insertController, Button leftButton, HBox hbox,
                          Button rightButton, EventModel eventModel) {
    this.insertController = insertController;
    new SlideShowView(hbox, leftButton, rightButton, this, eventModel);
  }


  /**
   * metodo per l'handler nella dahasboard, che permette di indirizzare all'evento selezionato.
   *
   * @param index indice dell'evento
   */
  public void handler(int index) {
    viewSourceController.toEventView(index);
  }

  /**
   * Il metodo setta all'interno di {@link InsertController} la imagelist che verrà caricata sul
   * server.
   *
   * @param imageList lista di immagini che verrà caricata sul server
   */
  public void setImageList(List<Image> imageList) {
    insertController.setImagesList(imageList);
  }
}
