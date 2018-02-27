package controller;

/**
 * questa classe viene invocata dalla view {@link view.EventView} per
 * la comunicazione con il database
 *
 * @author ingsw20
 */
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
