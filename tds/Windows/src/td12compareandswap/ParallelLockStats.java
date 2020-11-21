package td12compareandswap;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ParallelLockStats implements Stats {
    // Définition des minimas et extremas globaux
    private int globalMin = Integer.MAX_VALUE;
    private int globalMax = Integer.MIN_VALUE;
 
    private final int nThreads;
    private final List<Callable<String>> callables = new ArrayList<>();
    private final ExecutorService executor;
 
    public ParallelLockStats(int nb_threads) {
	this.executor = Executors.newFixedThreadPool(nb_threads);
	this.nThreads = nb_threads;
    }
 
    public int[] computeMinAndMax(final int[] ary) {
	// On s'assure que le nombre de threads est un multiple de la longueur du tableau
	if ( ary.length % nThreads != 0 ) {
	    throw new IllegalArgumentException( "The array length must " +
						"be divisible by the number of threads." );
	}
 
	// Le nombre d'elements que chaque thread recevra
	final int lengthPerThread = ary.length/nThreads;
 
	// On prépare la liste de Runnable
	for ( int i = 0; i < nThreads; i++ ) {
	    final int start = i*lengthPerThread;
	    final int end = (i+1)*lengthPerThread;
	    // creation et lancement du nouveau thread
	    // la classe "runnable" est définie de maniere anonyme
	   callables.add(new Callable<String>() {
		    public String call() {
			// recherche des extrema locaux sur la partie du tableau
			// affectée à la thread
			int local_min = ary[start];
			int local_max = ary[start];
			for (int j = start+1; j < end; j++ ) {
			    if(ary[j]<local_min)
			    	local_min=ary[j];
			    if(ary[j]>local_max)
			    	local_max=ary[j];
			}
			// propose le min et max comme valeurs candidates pour
			// les extrema globaux
			setExtrema(local_min, local_max);
			
			return "OK";
		    }
	       } );
	}


        try {
	    // On lance l'exécution de toutes les taches
	    executor.invokeAll(callables);
	    // L'exécutor n'accepte plus de nouvelles connection
	    executor.shutdown();
            // On attend la terminaison de toutes les taches
	    executor.awaitTermination(1, TimeUnit.HOURS);
	    // On pourrait alors récupérer les resultats mais ici on s'en moque un peu
        } catch (Exception e) {
            ;
        }

	return new int[]{globalMin, globalMax};
    }
 
    /**
     * Assigne le min global ou le max global si les valeurs passees en parametres
     * sont de meilleurs extrema.
     * @param min Un candidat pour le minimum global
     * @param max Un candidat pour le maximum global
     */

    // A modifier si nécéssaire
    private synchronized void setExtrema(int min, int max) {
	if (min < globalMin) {
	    globalMin = min;
	}
 
	if (max > globalMax) {
	    globalMax = max;
	} 
    }
}
