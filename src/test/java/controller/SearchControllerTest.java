package controller;

import model.EventListModel;
import model.EventModel;
import model.LocationListModel;
import model.LocationModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SearchControllerTest {
    private EventListModel eventListModel = EventListModel.getInstance();
    private LocationListModel locationListModel = LocationListModel.getInstance();

    @BeforeEach
    void setUp() {
        eventListModel.getEventList().clear();
        for (int i = 0; i < 4; i++) {
            EventModel eventModel = new EventModel();
            eventModel.setStartingDate("10/01/2018");
            eventModel.setEndingDate("11/01/2018");
            eventModel.setPrice(1.0);
            eventModel.setStudentReduction(2.0);
            eventModel.setEldersReduction(5.0);
            eventModel.setChildrenReduction(60.5);
            eventModel.setEventKey("eventKey" + i);
            eventModel.setLocationID("locationID" + i);
            eventModel.setEventName("evento" + i);
            eventModel.setLocationName("location" + i);
            eventModel.setLocationAddress("address" + i);
            eventListModel.setEventList(eventModel);
        }

        locationListModel.getLocationList().clear();
        for (int i = 0; i < 4; i++) {
            LocationModel locationModel = new LocationModel();
            locationModel.setLocationID("locationID" + i);
            locationModel.setLocationName("Location" + i);
            locationModel.setLocationAddress("Address" + i);
            locationListModel.setLocationList(locationModel);
        }
    }

    // test per elemento NON trovato nel database
    @Test
    void searchTestElementoNonTrovato() {
        System.out.println("Test per elemento non trovato: evento non trovato");
        boolean b;
        b = new SearchController().search("evento non trovato").size() != 0;
        assertFalse(b);
    }

    // test per elemento trovato nel database
    @Test
    void searchTestElementoTrovato() {
        System.out.println("Test per elemento trovato: cerco evento1");
        assertEquals("evento1", new SearchController().search("evento1").get(0).getEventName());
    }

    // test per il get della lista dei nomi degli eventi
    @Test
    void getEventsNameTest() {
        System.out.println("test per getEventName()");
        boolean b;
        List<String> eventNames = new SearchController().getEventsName();
        b = eventNames.size() == 4;
        for (int i = 0; i < eventNames.size(); i++) {
            assertEquals(eventNames.get(i), "evento" + i);
        }
        assertTrue(b);
    }

    @Test
    void getLocationNamesTest() {
        System.out.println("test per getLocationName()");
        boolean b;
        List<String> locationNames = new SearchController().getLocationNames();
        b = locationNames.size() == 4;
        for (int i = 0; i < locationNames.size(); i++) {
            assertEquals(locationNames.get(i), "Location" + i + " - " + "Address" + i);
        }
        assertTrue(b);
    }

    //test per la ricerca avanzata
    @Test
    void advancedSearchTest() {
        System.out.println("test per la ricerca avanzata");
        List<EventModel> eventModels = advancedSearch("1.0", "60.0",
                LocalDate.parse("10/01/2018", DateTimeFormatter.ofPattern("d/M/yyyy")),
                LocalDate.parse("11/01/2018", DateTimeFormatter.ofPattern("d/M/yyyy")),
                "location0 - address0");

        assertTrue(eventModels.size() == 1);
    }

    private List<EventModel> advancedSearch(String prezzoMinString, String prezzoMaxString, LocalDate startDate,
                                            LocalDate endDate, String locationName) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy");
        List<EventModel> listFound = new ArrayList<>();
        Double prezzoMin = 0.0;
        Double prezzoMax = Double.MAX_VALUE;
        if (!prezzoMinString.equals("")) {
            prezzoMin = Double.valueOf(prezzoMinString);
        }

        if (!prezzoMaxString.equals("")) {
            prezzoMax = Double.valueOf(prezzoMaxString);
        }

        List<EventModel> eventList = eventListModel.getEventList();
        for (EventModel eventModel : eventList) {
            if ((eventModel.getPrice() >= prezzoMin)
                    && (eventModel.getPrice() <= prezzoMax)
                    && LocalDate.parse(eventModel.getStartingDate(), formatter).isAfter(startDate.minusDays(1))
                    && LocalDate.parse(eventModel.getEndingDate(), formatter).isBefore(endDate.plusDays(1))
                    && (locationName.isEmpty()) || (eventModel.getLocationName() + " - " + eventModel
                    .getLocationAddress()).equals(locationName)) {

                listFound.add(eventModel);
            }

        }
        return listFound;
    }
}
