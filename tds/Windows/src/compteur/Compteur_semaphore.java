package compteur;

import java.util.concurrent.Callable;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

public class Compteur_semaphore implements Callable<Long> {
    static long compteur;
    Semaphore semaphore;
    int nbIt;
    Compteur_semaphore(int nbIterations){
        semaphore=new Semaphore(1);
        this.compteur=0;
        nbIt=nbIterations;
    }
    long incr()
    {
       try{ semaphore.acquire();}catch (InterruptedException e){}
       this.compteur++;
        semaphore.release();
       return  this.compteur;
    }
    @Override
    public Long call() {
        for(int i=0; i<nbIt;i++){
            incr();
        }
        return compteur;
    }
}
