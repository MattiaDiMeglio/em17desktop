package controller.chartsController;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import model.chartsModels.LineChartClassModel;
import model.chartsModels.PieChartClassModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class ChartsController {

    public static Query database;

    public ChartsController() {}

    public static void populateCharts(String year) {

        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                System.out.println("sono nel listeners");
                LineChartClassModel.getInstance().initializeArray();
                Integer maxTickets = 0;
                Integer ticketSold = 0;
                Iterable<DataSnapshot> iterable = snapshot.getChildren();
                while (iterable.iterator().hasNext()) {
                    try {
                        DataSnapshot luoghi = iterable.iterator().next();
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
                                    ticketSold = ticketSold + accesses;
                                    LineChartClassModel.getInstance().add(eventEndTime.getMonth(), accesses);
                                }
                            }
                        }

                    } catch (NullPointerException | ParseException | IndexOutOfBoundsException e) {
                        e.printStackTrace();
                    }
                }

                PieChartClassModel.getInstance().setMaxTickets(Double.valueOf(maxTickets));
                PieChartClassModel.getInstance().setTicketsSold(Double.valueOf(ticketSold));
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.out.println(error.getMessage());
            }
        });
    }
}
