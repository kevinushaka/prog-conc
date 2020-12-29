package compteur;

import java.util.concurrent.Callable;

public class Compteur_synchronized implements Callable<Long> {
    static long compteur;
    int nbIt;
    Compteur_synchronized(int nbIterations){
        nbIt=nbIterations;
        this.compteur=0;
    }
    synchronized long incr(){
        return this.compteur++;
    }
    @Override
    public Long call() {
        for(int i=0; i<nbIt;i++){
            incr();
        }
        return compteur;
    }
}
