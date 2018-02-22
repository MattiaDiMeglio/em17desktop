package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.EventModel;
import org.controlsfx.control.NotificationPane;

import view.*;



/**
 * Controller che gestisce l'FXML per le view
 *
 * @author ingSW20
 */
public class ViewSourceController extends Application {

  /**
   * AnchorPane prinicipale, contenente tutte le schemate
   */
  @FXML
  private AnchorPane mainAnchorPane;
  /**
   * bottone di login
   */
  @FXML
  private Button loginButton;
  /**
   * Campo per l'username, nella schermata login
   */
  @FXML
  private TextField userName;
  /**
   * Campo per la password, della schermata login
   */
  @FXML
  private PasswordField password;
  /**
   * Bottone per andare alla schermata di recupero password da quella di login
   */
  @FXML
  private Button recoveryLabelButton;
  /**
   * Bottone per confermare il recupero password
   */
  @FXML
  private Button recoveryButton;
  /**
   * Campo per la mail, nella schermata di recupero password
   */
  @FXML
  private TextField recoveryEmail;
  /**
   * Bottone indietro della schermata recupero password
   */
  @FXML
  private Button recoveryBackButton;
  /**
   * Hbox in cui si inseriranno le foto per lo slideshow nella dashboard
   */
  @FXML
  private HBox dashSlide;
  /**
   * tab pane della schermata di dashboard
   */
  @FXML
  private TabPane dashBoardTabPane;
  /**
   * bottone sinistro dello slideshow nell aschermata dashboard
   */
  @FXML
  private Button dashBoardSlideShowLeftButton;
  /**
   * bottone sinistro dello slideshow nell aschermata dashboard
   */
  @FXML
  private Button dashBoardSlideShowRightButton;
  /**
   * Bottone per l'inserimento evento dalla schermata dashboard
   */
  @FXML
  private Button dashBoardInsertButton;
  /**
   * toolbar presente nella dashboard
   */
  @FXML
  private ToolBar searchToolBar;
  /**
   * TabPane della schermata eventList
   */
  @FXML
  private TabPane eventListTabPane;
  /**
   * Vbox in eventListView contenente i risultati della ricerca
   */
  @FXML
  private VBox eventListViewVBox;
  /**
   * toolbar presente in eventListView
   */
  @FXML
  private ToolBar searchToolBarEventListView;
  /**
   * bottone toInsertView della schermata di visualizzazione evento
   */
  @FXML
  private Button eventoBackButton;
  /**
   * Tab pane della schermata evento
   */
  @FXML
  private TabPane eventoTabPane;
  /**
   * Lbel del titollo nella schermata visualizzazione evento
   */
  @FXML
  private Label eventoTitleLabel;
  /**
   * Text della location nella schermata visualizzazione evento
   */
  @FXML
  private Text eventLocationText;
  /**
   * Text della data di inizio nella schermata visualizzazione evento
   */
  @FXML
  private Text eventoDataInizioText;
  /**
   * Text della data di fine nella schermata visualizzazione evento
   */
  @FXML
  private Text eventoDataFineText;
  /**
   * * TextArea della descrizione nella schermata visualizzazione evento
   */
  @FXML
  private TextArea eventTextArea;
  /**
   * Text del prezzo nella schermata visualizzazione evento
   */
  @FXML
  private Text eventoPrezzoText;
  /**
   * Text del massimo dei visitatori nella schermata visualizzazione evento
   */
  @FXML
  private Text eventoMaxVisitatoriText;
  /**
   * Text del numero di biglietti venduti nella schermata visualizzazione evento
   */
  @FXML
  private Text eventoBigliettiVendutiText;
  /**
   * Imageview della copertina nella schermata visualizzazione evento
   */
  @FXML
  private ImageView eventPlaybillImageView;
  /**
   * bottone delete della schermata visualizzazione evento
   */
  @FXML
  private Button eventoDeleteButton;
  /**
   * Hbox in cui si inseriranno le foto per lo slideshow nella schermata visualizzazione evento
   */
  @FXML
  private HBox eventSlide;
  /**
   * bottone sinistro dello slideshow nell aschermata visualizzazione evento
   */
  @FXML
  private Button eventSlideShowLeftButton;
  /**
   * bottone destro dello slideshow nell aschermata visualizzazione evento
   */
  @FXML
  private Button eventSlideShowRightButton;
  /**
   * TextField del nome nella scheramta di inserimento
   */
  @FXML
  private TextField insertNameTextField;
  /**
   * TextField del nome location della schermata di inserimento
   */
  @FXML
  private TextField insertLocationTextField;
  /**
   * TextArea della descrizione nella schermata di inserimento
   */
  @FXML
  private TextArea insertTextArea;
  /**
   * Hbox che andrà a contenere le immagini inserite nella schermata di inserimento
   */
  @FXML
  private HBox insertSlideshow;
  /**
   * bottone indietro nella schermata di inserimento
   */
  @FXML
  private Button insertCancelButton;
  /**
   * Bottone conferma della schermata di inserimento
   */
  @FXML
  private Button insertConfirmButton;
  /**
   * DatePicker della data di inizio della schermata di inserimento
   */
  @FXML
  private DatePicker insertInizioDataPicker;
  /**
   * DatePicker della data di fine della schermata di inserimento
   */
  @FXML
  private DatePicker insertFineDataPicker;
  /**
   * TextField del massimo di posti della schermata di inserimento
   */
  @FXML
  private TextField insertMaxGuestsTextArea;
  /**
   * Button per l'inserimento della copertaina della schermata di inserimento
   */
  @FXML
  private Button insertPlayBillLabel;
  /**
   * ImageView che andrà a contenere l'immagine selezionata nella schermata di inserimento
   */
  @FXML
  private ImageView insertPlaybillImageView;
  /**
   * Bottone per l'inserimento delle immagini della schermata di inserimento
   */
  @FXML
  private Button insertUploadButton;
  /**
   * Vbox in cui si inseriranno i textField per i dati dei settori, nella schermata ticketType
   */
  @FXML
  private VBox insertTicketVbox;
  /**
   * bottone indietro per la schermata ticketType
   */
  @FXML
  private Button ticketTypeBackButton;
  /**
   * bottone conferma per la schermata ticketType
   */
  @FXML
  private Button ticketTypeNextButton;
  /**
   * ImageView che andrà a contenere l'immagine selezionata nella schermata TicketType
   */
  @FXML
  private ImageView insertTicketPlaybillImageView;
  /**
   * Vbox in cui verranno inseriti i textField in cui si inseriranno i dati relativi alle riduzioni,
   * schermata Reduction
   */
  @FXML
  private VBox InsertReductionVbox;
  /**
   * bottone indietro della schermata di Reduction
   */
  @FXML
  private Button ticketReductionBackButton;
  /**
   * bottone confermadella schermata di Reduction
   */
  @FXML
  private Button ticketReductionNextButton;
  /**
   * ImageView contenente la locandina nella schermata Reduction
   */
  @FXML
  private ImageView insertReductionPlaybillImageView;
  /**
   * Vbox che contiene il center della schermata Recap, da cui verranno presi i vari nodi necessari
   */
  @FXML
  private VBox recapVBox;
  /**
   * ImageView della copertina nella schermata Recap
   */
  @FXML
  private ImageView RecapPlaybillImageView;
  /**
   * bottone per la modifica di un evento
   */
  @FXML
  private Button eventModifyButton;
  /**
   * Schermata Dasboard
   */
  private Node dashBoardBox;
  /**
   * Schermata EventList
   */
  private Node eventListBox;
  /**
   * Schermata Event
   */
  private Node eventBox;
  /**
   * Schermata Login
   */
  private Node loginBox;
  /**
   * Schermata password recovery
   */
  private Node recoveryBox;
  /**
   * Schermata insert
   */
  private Node insertBox;
  /**
   * Schermata TicketTyoe
   */
  private Node insertTicketTypeBox;
  /**
   * Schermata Reduction
   */
  private Node insertReductionBox;
  /**
   * schermata Recap
   */
  private Node insertRecapBox;
  /**
   * variabile per memorizzare la view precedente alla quale si vuole tornare tramite la pressione
   * del tasto indietro
   */
  private Node prevView;
  /**
   * stage per la visualizzazione del programma
   */
  private static Stage stage;

  /**
   * Main dell'applicazione, richiama il metono launch che fa partire la schermata di javafx
   *
   * @param args argomenti del main
   */
  public static void main(String[] args) {
    launch(args);
  }

  /**
   * Metodo start ereditato da Application. Seleziona lo stage da far partire, carica l'FXML tramite
   * FXMLLoader. Carica il root e se questo non è vuoto, setta la nuova scena. Selezionata la scena,
   * setta Resizable a falso, in modo che la dimensione schermata non possa essere modificata,
   * mostra e centra la schermata. Ne setta il titolo e la imposta a pieno schermo
   *
   * @param primaryStage stage primario dell'applicativo
   * @see javafx.scene.Parent
   * @see javafx.scene.Scene
   * @see javafx.fxml.FXMLLoader
   */
  @Override
  public void start(Stage primaryStage) {

    //carica il file fxml da cui prendere le info sulla grafica.
    //Il file ViewSource.fxml contiene tutte le scermate
    //in sequenza
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/ViewSource.fxml"));
    Parent root = null;
    try {
      root = loader.load(); //setta la root da cui prendere i dati dell'FXML
    } catch (IOException e) {
      e.printStackTrace();
    }

    //root del fxml, che si basa su una struttura ad albero
    if (root != null) {
      primaryStage.setScene(new Scene(root));
    }

    //schermata non ridimensionabile
    primaryStage.setResizable(true);
    //setta le dimensioni minime della schermata
    primaryStage.setMinHeight(800);
    primaryStage.setMinWidth(800);
    primaryStage.show(); //mostra la schermata
    primaryStage.centerOnScreen();
    primaryStage.setTitle("Em-17");
    primaryStage.setMaximized(true); //setta la schermata come massimizzata
    //Listener che chiude i thread aperti, in caso la finestra venga chiusa
    primaryStage.setOnCloseRequest(event -> {
      ViewSourceController viewSourceController = new ViewSourceController();
      viewSourceController.shutdown();
      Platform.exit();

    });
    stage = primaryStage;
  }

  /**
   * Metodo per l'inizializzazione del loader di FXML.
   */
  @FXML
  private void initialize() {
    //ottenimento dei box contenenti le varie schermate
    dashBoardBox = mainAnchorPane.getChildren().get(0);
    eventListBox = mainAnchorPane.getChildren().get(1);
    eventBox = mainAnchorPane.getChildren().get(2);
    loginBox = mainAnchorPane.getChildren().get(3);
    recoveryBox = mainAnchorPane.getChildren().get(4);
    insertBox = mainAnchorPane.getChildren().get(5);
    insertTicketTypeBox = mainAnchorPane.getChildren().get(6);
    insertReductionBox = mainAnchorPane.getChildren().get(7);
    insertRecapBox = mainAnchorPane.getChildren().get(8);

    //metodo per passare alla loginview
    toLoginView();
  }

  /**
   * Metodo che si occupa di cambiare le schermate prende in input il node da usare come nuova
   * schermata.
   *
   * @param view view alla quale si vuole passare
   */
  private void changeView(Node view) {
    if (!mainAnchorPane.getChildren().isEmpty()) {
      prevView = mainAnchorPane.getChildren().get(0);
    }
    mainAnchorPane.getChildren().clear();
    mainAnchorPane.getChildren().add(view);
  }

  /**
   * metodo che si occupa di creare la loginView e cambiare la schermata a loginbox.
   */
  public void toLoginView() {
    new LoginView(userName, password, loginButton, recoveryLabelButton, this);
    changeView(loginBox);

  }

  /**
   * mostra una barra che notifica l'aggiornamento locale del database.
   */
  public static void showNotificationPane() {
    Platform.runLater(() -> {
      Scene scene = stage.getScene();
      Parent pane = scene.getRoot();
      if (!(pane instanceof NotificationPane)) {
        NotificationPane notificationPane = new NotificationPane(pane);
        notificationPane.getStyleClass().add(NotificationPane.STYLE_CLASS_DARK);
        notificationPane.setShowFromTop(false);
        notificationPane.setText("database aggiornato");
        scene = new Scene(notificationPane, scene.getWidth(), scene.getHeight());
        stage.setScene(scene);
        notificationPane.show();
        new Thread(() -> {
          try {
            Thread.sleep(2000);
            notificationPane.hide();
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }).start();
      } else {
        ((NotificationPane) pane).show();
      }
    });
  }

  /**
   * metodo che si occupa di creare la RecoveryView.
   */
  public void toRecoveryView() {
    new RecoveryView(recoveryEmail, recoveryButton, recoveryBackButton, this);
    changeView(recoveryBox);

  }

  /**
   * metodo che si occupa di creare la dashboardview e cambiare la schermata.
   */
  public void toDashBoardView() {
    new DashBoardView(dashSlide, dashBoardSlideShowLeftButton, dashBoardSlideShowRightButton,
        dashBoardTabPane, dashBoardInsertButton, this, searchToolBar);

    changeView(dashBoardBox);
    DBController.getInstance(); // inizializza il database
  }

  /**
   * Metodo che si occupa di creare la EventView e cambiuare schermata.
   *
   * @param index indice identificativo dell'evento
   */
  public void toEventView(int index) {
    //si crea una lista di Text, delle parti della schermata
    List<Text> texts = new ArrayList<>();
    texts.add(eventLocationText);
    texts.add(eventoDataInizioText);
    texts.add(eventoDataFineText);
    texts.add(eventoPrezzoText);
    texts.add(eventoMaxVisitatoriText);
    texts.add(eventoBigliettiVendutiText);
    EventController eventController = new EventController();

    new EventView(eventController, eventoDeleteButton, eventPlaybillImageView,
        eventoTabPane, index, texts, eventoTitleLabel, eventTextArea, eventSlide,
        eventSlideShowLeftButton,
        eventSlideShowRightButton, eventoBackButton, this, eventModifyButton);
    changeView(eventBox);

  }

  /**
   * metodo che si occupa di creare la EventListView e cambiare la schermata.
   *
   * @param foundedEventInSearch elementi trovati nella ricerca
   */
  public void toEventListView(List<EventModel> foundedEventInSearch) {
    new EventListView(eventListTabPane, foundedEventInSearch, eventListViewVBox,
        searchToolBarEventListView, this);

    changeView(eventListBox);
  }

  /**
   * metodo che si occupa di creare la InsertView e cambiare la schermata.
   */
  public void toInsertView() {

    InsertController insertController = new InsertController(this);
    //si creano due liste, una per i bottoni e una per i textField della schermata
    List<Button> buttonList = new ArrayList<>();
    List<TextField> texts = new ArrayList<>();

    //si inseriscono bottoni e textFields nelle rispettive liste
    buttonList.add(insertCancelButton);
    buttonList.add(insertConfirmButton);
    buttonList.add(insertPlayBillLabel);
    buttonList.add(insertUploadButton);

    texts.add(insertNameTextField);
    texts.add(insertLocationTextField);
    texts.add(insertMaxGuestsTextArea);

    new InsertView(insertController, buttonList, texts, insertTextArea, insertSlideshow,
        insertInizioDataPicker,
        insertFineDataPicker, insertPlaybillImageView);

    changeView(insertBox);
  }

  /**
   * metodo per avviare la schermata per la modifica o l'inserimento (nel caso in cui si prema il
   * tasto indietro)di un evento.
   *
   * @param eventModel model dell'evento
   * @param insertController istanza di {@link InsertController}
   */
  public void toInsertView(EventModel eventModel, InsertController insertController) {
    //si creano due liste, una per i bottoni e una per i textField della schermata
    List<Button> buttonList = new ArrayList<>();
    List<TextField> texts = new ArrayList<>();

    //si inseriscono bottoni e textFields nelle rispettive liste
    buttonList.add(insertCancelButton);
    buttonList.add(insertConfirmButton);
    buttonList.add(insertPlayBillLabel);
    buttonList.add(insertUploadButton);

    texts.add(insertNameTextField);
    texts.add(insertLocationTextField);
    texts.add(insertMaxGuestsTextArea);

    new InsertView(insertController, buttonList, texts, insertTextArea, insertSlideshow,
        insertInizioDataPicker,
        insertFineDataPicker, insertPlaybillImageView, eventModel);

    changeView(insertBox);
  }

  /**
   * Metodo che si occupa di creare la InserTicketTypeView e cambiare la schermata.
   *
   * @param insertController istanza di {@link InsertController}
   * @param newEvent model dell'evento
   */
  public void toInsetTicketTypeView(InsertController insertController, EventModel newEvent) {
    new InsertTicketTypeView(insertController, newEvent, insertTicketVbox, ticketTypeBackButton,
        ticketTypeNextButton, insertTicketPlaybillImageView);

    changeView(insertTicketTypeBox);
  }

  /**
   * Metodo che si occupa di creare la InsertReductionView e cambiare la schermata.
   *
   * @param insertController istanza di {@link InsertController}
   * @param newEvent model dell'evento
   */
  public void toInsertReductionView(InsertController insertController, EventModel newEvent) {
    new InsertReductionView(insertController, newEvent,
        InsertReductionVbox, ticketReductionBackButton, ticketReductionNextButton,
        insertReductionPlaybillImageView);
    changeView(insertReductionBox);
  }

  /**
   * Metodo che si occupa di creare la InsertRecapView e cambiare la schermata.
   *
   * @param insertController istanza di {@link InsertController}
   * @param imagesList lista con le immagini dell'evento
   * @param newEvent model dell'evento
   */
  public void toInsertRecapView(InsertController insertController, List<Image> imagesList,
      EventModel newEvent) {
    new InsertRecapView(insertController, newEvent, imagesList, recapVBox, RecapPlaybillImageView);
    changeView(insertRecapBox);
  }

  /**
   * Metodo per tornare alla schermata precedente.
   */
  public void turnBack() {
    changeView(prevView);
  }

  /**
   * Metodo per andare alla schermata dashboard senza chiarame il DBController.
   */
  public void toDash() {
    changeView(dashBoardBox);
  }

  /**
   * Metodo per la chiusura dei thread aperti.
   */
  private void shutdown() {
    LoginController.getInstance().shutdown();
  }

  /**
   * metodo per la modifica di un evento.
   *
   * @param eventModel evento da modificare
   */
  public void toModifyEvent(EventModel eventModel) {
    toInsertView(eventModel, new InsertController(this));
  }
}
