package compteur;

public class Compteur implements Runnable {
    long compteur;
    int nbIt;
    Compteur(int nbIterations){
        this.compteur=0;
        nbIt=nbIterations;
    }
    long incr(){
        return this.compteur++;
    }

    @Override
    public void run() {
        for(int i=0; i<nbIt;i++){
            incr();
        }
    }
}
