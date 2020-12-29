package compteur;

import java.util.concurrent.Callable;
import java.util.concurrent.locks.ReentrantLock;

public class Compteur_lock implements Callable<Long> {
    static long compteur;
    static ReentrantLock lock;
    int nbIt;
    Compteur_lock(int nbIterations){
        lock=new ReentrantLock();
        this.compteur=0;
        nbIt=nbIterations;
    }
    long incr()
    {
        lock.lock();
       this.compteur++;
       lock.unlock();
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
