package controller;

public class EventController {


  /**
   * Metodo per la cancellazione di un evento.
   *
   * @param key indice dell'evento da cancellare
   */
  public void delete(String key) {
    DBController dbController = DBController.getInstance();

    dbController.delete(key);

  }
}
