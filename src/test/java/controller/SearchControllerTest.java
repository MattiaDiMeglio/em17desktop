package controller;

import model.EventListModel;
import model.EventModel;
import model.LocationListModel;
import model.LocationModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SearchControllerTest {
    private EventListModel eventListModel = EventListModel.getInstance();
    private LocationListModel locationListModel = LocationListModel.getInstance();

    // implementazione parziale di componenti da cui il test dipende
    @BeforeEach
    void testStub() {
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
    void searchTest1() {
        boolean b;
        b = new SearchController().search("evento non trovato").isEmpty();
        assertTrue(b);
    }

    // test per elemento trovato nel database
    @Test
    void searchTest2() {
        boolean b;
        b = new SearchController().search("evento1").size() > 0;
        assertTrue(b);
    }

    // test per la stringa vuota
    @Test
    void searchTest3() {
        boolean b;
        b = new SearchController().search("").size() == 4;
        assertTrue(b);
    }

    // test per la dimensione della lista tornata da getEventsName
    @Test
    void getEventsNameTest1() {
        boolean b;
        List<String> eventNames = new SearchController().getEventsName();
        b = eventNames.size() == 4;
        assertTrue(b);
    }

    // test per la stampa della lista tornata da getEventsName
    @Test
    void getEventsNameTest2() {
        List<String> eventNames = new SearchController().getEventsName();
        for (int i = 0; i < eventNames.size(); i++) {
            assertEquals(eventNames.get(i), "evento" + i);
        }
    }

    // test per la dimensione della lista tornata da getLocationName()
    @Test
    void getLocationNamesTest1() {
        boolean b;
        List<String> locationNames = new SearchController().getLocationNames();
        b = locationNames.size() == 4;
        assertTrue(b);
    }

    // test per la stampa della lista tornata da getLocationName ()
    @Test
    void getLocationNamesTest2() {
        List<String> locationNames = new SearchController().getLocationNames();
        for (int i = 0; i < locationNames.size(); i++) {
            assertEquals(locationNames.get(i), "Location" + i + " - " + "Address" + i);
        }
    }

     //test per la ricerca avanzata
    @Test
    void advancedSearchTest1() {
        List<EventModel> eventModels = new SearchController().advancedSearch("1.0", "60.0",
                LocalDate.parse("10/01/2018", DateTimeFormatter.ofPattern("d/M/yyyy")),
                LocalDate.parse("11/01/2018", DateTimeFormatter.ofPattern("d/M/yyyy")),
                "location0 - address0");

        assertTrue(eventModels.size() > 0);
    }

    //test per la ricerca avanzata
    @Test
    void advancedSearchTest2() {
        List<EventModel> eventModels = new SearchController().advancedSearch("", "",
                LocalDate.MIN,
                LocalDate.MAX,
                "");

        assertTrue(eventModels.size() > 0);
    }

    //test per la ricerca avanzata
    @Test
    void advancedSearchTest3() {
        List<EventModel> eventModels = new SearchController().advancedSearch("-1.0", "-5.0",
                LocalDate.MIN,
                LocalDate.MAX,
                "");

        assertTrue(eventModels.size() > 0);
    }
}
