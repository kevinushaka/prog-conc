package balls;

import java.util.concurrent.locks.Lock;

public class MonitorCycleBarrier {

    int nbThreads;
    int nbThreadsMax;

    MonitorCycleBarrier(int parties){
        this.nbThreadsMax=parties;
        this.nbThreads=0;

    }

    public synchronized  void await(){
        synchronized (this) {
            nbThreads++;
        }
        if(nbThreads==nbThreadsMax){
            notifyAll();
            nbThreads=0;
        }else {
            try {
                wait();
            } catch (InterruptedException e) {
            }
        }

    }
}
