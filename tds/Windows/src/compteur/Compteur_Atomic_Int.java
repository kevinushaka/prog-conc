package compteur;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class Compteur_Atomic_Int implements Callable<Integer> {
    static AtomicInteger compteur;
    int nbIt;

    Compteur_Atomic_Int(int nbIterations){
        compteur=new AtomicInteger(0);
        nbIt=nbIterations;

    }
    long incr(){
        return this.compteur.incrementAndGet();
    }


    @Override
    public Integer call() throws Exception {
        for(int i=0; i<nbIt;i++){
            incr();
        }
        return compteur.get();
    }
}
