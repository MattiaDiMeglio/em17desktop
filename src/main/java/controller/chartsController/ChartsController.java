package controller.chartsController;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import model.chartsModels.BarChartModel;
import model.chartsModels.LineChartModel;
import model.chartsModels.StackedAreaChartModel;
import model.chartsModels.PieChartModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class ChartsController {

    private static ChartsController ourInstance = new ChartsController();

    public static ChartsController getInstance() {
        return ourInstance;
    }

    private ChartsController() {}

    private Query database;

    public void populateCharts(String year) {

        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                LineChartModel.getInstance().initializeArray();
                StackedAreaChartModel.getInstance().initializeArray();
                Integer maxTickets = 0;
                Integer ticketSold = 0;
                Integer indexForLocation = 0;
                Iterable<DataSnapshot> iterable = snapshot.getChildren();
                List<String> locationNames = new ArrayList<>();
                HashMap<Integer, String> locationIdMap = new HashMap<>();
                Integer soldPerCurrentLocation = 0;
                List<Integer> soldPerLocation = new ArrayList<>();
                while (iterable.iterator().hasNext()) {
                    try {
                        DataSnapshot luoghi = iterable.iterator().next();
                        locationIdMap.put(indexForLocation, luoghi.getKey());
                        locationNames.add(luoghi.child("nome").getValue().toString());

                        Iterable<DataSnapshot> settori = luoghi.child("settori").getChildren();
                        Integer totTickets = 0;
                        while (settori.iterator().hasNext()) {
                            totTickets = totTickets + Integer.valueOf(settori.iterator().next().getValue().toString());
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
                                LocalDate toLocalDate = eventEndTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                                String ticketYear = String.valueOf(toLocalDate.getYear());


                                if (year.equals(ticketYear)) {
                                    Integer accesses = Integer.valueOf(dataSnapshot.child("accessi").getValue().toString());
                                    Integer revenue = accesses * Integer.valueOf(dataSnapshot.child("prezzo").getValue().toString());
                                    ticketSold = ticketSold + accesses;
                                    soldPerCurrentLocation = soldPerCurrentLocation + accesses;
                                    StackedAreaChartModel.getInstance().add(eventEndTime.getMonth(), revenue);
                                    LineChartModel.getInstance().add(eventEndTime.getMonth(), accesses);
                                }
                            }
                        }
                        soldPerLocation.add(soldPerCurrentLocation);
                        indexForLocation++;
                    } catch (NullPointerException | ParseException | IndexOutOfBoundsException e) {
                        e.printStackTrace();
                    }

                }
                BarChartModel.getInstance().setSoldPerLocation(soldPerLocation);
                BarChartModel.getInstance().setLocationNames(locationNames);
                BarChartModel.getInstance().setLocationIdMap(locationIdMap);
                PieChartModel.getInstance().setMaxTickets(Double.valueOf(maxTickets));
                PieChartModel.getInstance().setTicketsSold(Double.valueOf(ticketSold));
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.out.println(error.getMessage());
            }
        });
    }

    public void setDatabase(Query database) {
        this.database = database;
    }
}
