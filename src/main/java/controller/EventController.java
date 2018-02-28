package controller;

import java.util.concurrent.CountDownLatch;

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
   * @param latch latch per la sincronizzazione con l'eliminazione
   */
  public void delete(String key, CountDownLatch latch) {
    DBController dbController = DBController.getInstance();
    dbController.delete(key, latch);

  }
}
