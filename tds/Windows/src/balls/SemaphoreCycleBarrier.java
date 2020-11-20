package balls;

import java.util.concurrent.Semaphore;

public class SemaphoreCycleBarrier {
    int nbThreadsMax;
    int nbThreads;
     Semaphore mutex;
     Semaphore barrier;
     SemaphoreCycleBarrier(int parties){
         this.nbThreadsMax=parties;
         this.mutex=new Semaphore(1);
         this.barrier=new Semaphore(0);
         this.nbThreads=0;
     }

     public void await(){
         try { mutex.acquire(); }catch (InterruptedException e){ }
         nbThreads++;
         mutex.release();
         if(nbThreads==nbThreadsMax){
             barrier.release();
             nbThreads=0;
         }else {
             try {
                 barrier.acquire();
             } catch (InterruptedException e) {
             }
         }

     }
}
