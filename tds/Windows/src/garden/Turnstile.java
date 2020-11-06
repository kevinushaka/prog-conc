package garden; /**
 * Class not to be modified
 */

/**
 * @author riveill
 *
 */
import java.awt.*;
import java.util.concurrent.Semaphore;

public final class Turnstile extends Thread {
    TextCanvas display_;
    int count_=0;
    Counter people_;
    int delay_;
    boolean suspended = true;
    Semaphore semaphore;
    Turnstile(TextCanvas t, Counter c, int d,Semaphore semaphore) {
        this.semaphore=semaphore;
        display_ = t;
        display_.setcolor(Color.red);
        people_ = c;
        delay_ = d;
      }

    synchronized void mysuspend() {
        while (suspended)
            try {wait();} catch (InterruptedException e) {}
    }

    public void passivate() {
        if (!suspended) {
            suspended = true;
            display_.setcolor(Color.red);
           }
    }

    public void activate() {
        if (suspended) {
            suspended = false;
            display_.setcolor(Color.green);
            synchronized(this) {notify();}
        }
    }

    public void run() {
          while(true) {
            mysuspend();
            try {Thread.sleep(delay_);} catch(InterruptedException e){}
            count_++;
            display_.setvalue(count_);
              try {
                  semaphore.acquire();
                  people_.increment();
                  semaphore.release();
              } catch (InterruptedException e) {
                  e.printStackTrace();
              }
          }
    }

}
