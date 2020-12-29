package compteur;

import java.util.concurrent.Callable;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Compteur_rw implements Callable<Long> {
    static long compteur;
    static ReadWriteLock lock;
    int nbIt;
    Compteur_rw(int nbIterations){
        lock=new ReentrantReadWriteLock();
        this.compteur=0;
        nbIt=nbIterations;
    }
    long incr()
    {
        lock.writeLock().lock();
       this.compteur++;
       lock.writeLock().unlock();
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
