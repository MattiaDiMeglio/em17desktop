package controller.chartsController;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import model.chartsModels.BarChartModel;
import model.chartsModels.LineChartModel;
import model.chartsModels.PieChartModel;
import model.chartsModels.StackedAreaChartModel;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * la classe si occupa del prelievo dei dati dal database utili per la rappresentazione delle
 * statistiche mediante grafici. Una volta prelevati, questi dati vengono inviati ai model
 * responsabili dei vari grafici secondo i canoni del pattern MVC
 *
 * @author ingSW20
 */
public class ChartsController {

  /**
   * istanza corrente della classe
   */
  private static ChartsController ourInstance = new ChartsController();

  /**
   * variabile per effettuare la query al database
   */
  private Query database;

  /**
   * getter dell'istanza corrente della classe
   *
   * @return {@link #ourInstance}
   */
  public static ChartsController getInstance() {
    return ourInstance;
  }

  /**
   * costruttore privato vuoto
   */
  private ChartsController() {
  }

  /**
   * il metodo si occupa di scorrere il database al fine di prelevare i dati utili per i grafici per
   * poi scriverli nei rispettivi model
   *
   * @param year anno del quale prelevare le statistiche
   * @param latch latch per la sincronizzazione
   */
  public void populateCharts(String year, CountDownLatch latch) {
    System.out.println("chartsController");
    database.addListenerForSingleValueEvent(new ValueEventListener() {
      @Override
      public void onDataChange(DataSnapshot snapshot) {
        updateChart(year, latch, snapshot);
      }

      @Override
      public void onCancelled(DatabaseError error) {
        System.out.println(error.getMessage());
      }
    });
  }

  /**
   * setter per la variabile {@link #database}
   *
   * @param database {@link #database}
   */
  public void setDatabase(Query database) {
    this.database = database;
  }

  /**
   * metodo che aggiorna i chart
   *
   * @param year anno
   * @param latch latch per la sincronizzazione
   * @param snapshot instantanea del database
   */
  public void updateChart(String year, CountDownLatch latch, DataSnapshot snapshot) {
    try {
      LineChartModel.getInstance().initializeArray();
      StackedAreaChartModel.getInstance().initializeArray();
      Integer maxTickets = 0;
      Integer ticketSold = 0;
      Integer indexForLocation = 0;
      Iterable<DataSnapshot> locationIterable = snapshot
          .getChildren(); // iteratore per scrorrere le location

      /* locationIdMap è un hashmap contenente come key un indice e come valore l'UID identificativo del luogo.
       * locationNames invece contiene semplicemente i nomi delle location.
       * Queste due variabili lavorano in stretto contatto, in quanto l'indice contenuto nella key dell'hashmap
       * corrisponde all'indice per effettuare il get dalla lista locationNames e per prelevare il relativo UID
       */
      List<String> locationNames = new ArrayList<>();
      HashMap<Integer, String> locationIdMap = new HashMap<>();

      Integer soldPerCurrentLocation = 0;
      List<Integer> soldPerLocation = new ArrayList<>();
      while (locationIterable.iterator().hasNext()) {
        try {
          DataSnapshot luoghi = locationIterable.iterator().next();
          locationIdMap.put(indexForLocation, luoghi.getKey());
          locationNames.add(luoghi.child("nome").getValue().toString());

          // iteratore per scorrere i settori delle location
          Iterable<DataSnapshot> settori = luoghi.child("settori").getChildren();

          Integer totTickets = 0;
          while (settori.iterator().hasNext()) {
            totTickets =
                totTickets + Integer.valueOf(settori.iterator().next().getValue().toString());
          }

          // prendo la root degli eventi
          Iterable<DataSnapshot> eventi = luoghi.child("Eventi").getChildren();
          // scorro tutti gli eventi per prenderne il contenuto
          while (eventi.iterator().hasNext()) {
            DataSnapshot dataInizio = eventi.iterator().next();
            String evetDateString = dataInizio.child("data").child("inizio").getValue().toString();
            Date evetDate = new SimpleDateFormat("dd/MM/yyyy").parse(evetDateString);
            LocalDate localDate = evetDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            String ticketEvent = String.valueOf(localDate.getYear());

            // verifico che l'evento è dell'anno desiderato
            if (year.equals(ticketEvent)) {
              maxTickets = maxTickets + totTickets;
            }

            // prendo i biglietti
            Iterable<DataSnapshot> biglietti = dataInizio.child("biglietti").getChildren();

            // scorro tutti i biglietti per prenderne il contenuto
            while (biglietti.iterator().hasNext()) {
              DataSnapshot dataSnapshot = biglietti.iterator().next();
              String eventEndDate = dataSnapshot.child("data vendita").getValue().toString();
              Date eventEndTime = new SimpleDateFormat("dd/MM/yyyy").parse(eventEndDate);
              LocalDate toLocalDate = eventEndTime.toInstant().atZone(ZoneId.systemDefault())
                  .toLocalDate();
              String ticketYear = String.valueOf(toLocalDate.getYear());

              // verifico che i biglietti siano stati venduti nell'anno desiderato
              if (year.equals(ticketYear)) {
                Integer accesses = Integer
                    .valueOf(dataSnapshot.child("accessi").getValue().toString());
                Integer revenue =
                    accesses * Integer.valueOf(dataSnapshot.child("prezzo").getValue().toString());
                ticketSold = ticketSold + accesses;
                soldPerCurrentLocation = soldPerCurrentLocation + accesses;

                // scrivo i dati nei model
                StackedAreaChartModel.getInstance().add(eventEndTime.getMonth(), revenue);
                LineChartModel.getInstance().add(eventEndTime.getMonth(), accesses);
              }
            }
          }
          soldPerLocation.add(soldPerCurrentLocation);
          indexForLocation++;
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
      // scrivo i dati nei model
      BarChartModel.getInstance().setSoldPerLocation(soldPerLocation);
      BarChartModel.getInstance().setLocationNames(locationNames);
      BarChartModel.getInstance().setLocationIdMap(locationIdMap);
      PieChartModel.getInstance().setMaxTickets(Double.valueOf(maxTickets));
      PieChartModel.getInstance().setTicketsSold(Double.valueOf(ticketSold));
      latch.countDown();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
