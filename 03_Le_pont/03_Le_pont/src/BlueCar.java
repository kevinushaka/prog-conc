/**
 * File not to be modified
 */
public class BlueCar implements Runnable {

    BridgeCanvas display;
    Bridge control;
    int id;

    BlueCar(Bridge control, BridgeCanvas d, int id) {
        display = d;
        this.id = id;
        this.control = control;
    }

    @Override
    public void run() {
      try {
        while (true) {
            while (!display.moveBlue(id)){}  // n'est pas dans le carrefour
            control.blueEnter();
            while (display.moveBlue(id)){}   // circuler dans le carrefour
            control.blueExit();
         }
      } catch (InterruptedException e){}
    }

}
