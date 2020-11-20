package td12compareandswap;
import java.util.concurrent.*;
import java.util.Random;

public class Main {
    private final static int TOTAL_BENCHMARK_OPERATIONS = 300_000;
    private final static int REPEAT_BENCHMARK = 5;

    private final static int NB_ELEMENTS = 5_000_000;

    public static void main (String[] args){
	int [] tab = new int[NB_ELEMENTS];
	int [] min_and_max = new int[2];
	int [] new_min_max = new int[2];
	Random r = new Random();
	int nb_threads;
	long start, end;
	int exectime;

	// Initialisation du tableau
	min_and_max[0] = Integer.MAX_VALUE;
	min_and_max[1] = Integer.MIN_VALUE;
	for (int i = 0; i < tab.length; ++i) {
	    tab[i] = r.nextInt();
	    if (tab[i] < min_and_max[0]) min_and_max[0] = tab[i];
	    if (min_and_max[1] < tab[i]) min_and_max[1] = tab[i];
	}
	System.out.println("min: "+min_and_max[0]);
	System.out.println("MAX: "+ min_and_max[1]);
      
	System.out.println("----- AVEC CONTENTION -----");
	nb_threads = 10*Runtime.getRuntime().availableProcessors();
	
	exectime = 0;
	for (int i = 0; i < REPEAT_BENCHMARK; i++)  {
	    start = System.nanoTime();
	    ParallelLockStats minmax = new ParallelLockStats(nb_threads);
	    new_min_max = minmax.computeMinAndMax(tab);
	    end = System.nanoTime();
	    exectime += (end-start)/1_000;   
	}

	System.out.println("Avec verrou -- Temps d'exécution en microseconds: " + (exectime)/REPEAT_BENCHMARK );
	if (min_and_max[0]==new_min_max[0] && min_and_max[1]==new_min_max[1])
	    System.out.println("Correct\n");
	else
	    System.out.println("Incorrect\n");

	exectime = 0;
	for (int i = 0; i < REPEAT_BENCHMARK; i++)  {
	    start = System.nanoTime();
	    ParallelAtomicStats atomic_minmax =  new ParallelAtomicStats(nb_threads);
	    new_min_max = atomic_minmax.computeMinAndMax(tab);
	    end = System.nanoTime();
	    exectime += (end-start)/1_000;	    
	}

	System.out.println("Sans verrou -- Temps d'exécution en microseconds: " + (exectime)/REPEAT_BENCHMARK );
	if (min_and_max[0]==new_min_max[0] && min_and_max[1]==new_min_max[1])
	    System.out.println("Correct\n");
	else
	    System.out.println("Incorrect\n");

	System.out.println("----- SANS CONTENTION -----");
	nb_threads = Runtime.getRuntime().availableProcessors() / 2;
	
	exectime = 0;
	for (int i = 0; i < REPEAT_BENCHMARK; i++)  {
	    start = System.nanoTime();
	    ParallelLockStats minmax = new ParallelLockStats(nb_threads);
	    new_min_max = minmax.computeMinAndMax(tab);
	    end = System.nanoTime();
	    exectime += (end-start)/1_000;	    
	}

	System.out.println("Avec verrou -- Temps d'exécution en microseconds: " + (exectime)/REPEAT_BENCHMARK );
	if (min_and_max[0]==new_min_max[0] && min_and_max[1]==new_min_max[1])
	    System.out.println("Correct\n");
	else
	    System.out.println("Incorrect\n");

	exectime = 0;
	for (int i = 0; i < REPEAT_BENCHMARK; i++)  {
	    start = System.nanoTime();
	    ParallelAtomicStats atomic_minmax =  new ParallelAtomicStats(nb_threads);
	    new_min_max = atomic_minmax.computeMinAndMax(tab);
	    end = System.nanoTime();
	    exectime += (end-start)/1_000;	    
	}

	System.out.println("Sans verrou -- Temps d'exécution en microseconds: " + (exectime)/REPEAT_BENCHMARK );
	if (min_and_max[0]==new_min_max[0] && min_and_max[1]==new_min_max[1])
	    System.out.println("Correct\n");
	else
	    System.out.println("Incorrect\n");

    }
}
