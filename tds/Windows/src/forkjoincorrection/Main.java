package forkjoincorrection;

import java.util.Random;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.ForkJoinPool;


public class Main {

    static int MAX = 10000;
    static int tableau[]=new int[MAX];

    static void initialise_tableau (int max) {
	//System.out.println("Initialise le tableau");
	for (int i=0; i<max; i++) {
	    // tableau[i] = rand.nextInt(2*max) + min;
	    tableau[i] = max-i;
	}
    }	

    static void verifie_tableau (int max) {
        //System.out.println("Controle du tableau trié de taille " + max);
	//for (int i=0; i<max; i++) System.out.println(">"+tableau[i]);
        for (int i=0; i<max; i++)
            if (tableau[i]!=(i+1)) {
                System.out.println("tableau pas trié correctement : "
				   +" cases: "
                                   +(i+1)
                                   +" - "
                                   +tableau[i]);
		
		return;
	    }
    }

    public static void main(String[] args) {
	Random rand=new Random();
	int nombreAleatoire;
	int max;
	int nb_thread_max;
	long date_debut=0;
	int nb_iteration = 5;
	int temps_total;

	if (args.length != 2) {
            System.out.println("Usage : java forkjoincorrection.Main <taille du tableau> <nbre max de thread>");
            System.exit(0);
        }
        max = Integer.parseInt(args[0]);
        nb_thread_max = Integer.parseInt(args[1]);
	if (max > MAX) max = MAX;
	System.out.println("Execution avec un tableau de taille : " + max);
 
        temps_total=0;
	for (int i=0; i<=nb_iteration; i++) {
	        initialise_tableau (max);
	        //System.out.println("forkjoincorrection.QuickSort récursif");
	        QuickSort QS = new QuickSort();
	        if (i!=0)
		    date_debut = System.nanoTime();
	        QS.quicksort(tableau, 0, max-1);
                if (i!=0) {
		    temps_total+=System.nanoTime()-date_debut;
		    verifie_tableau(max);
		}
        }
	System.out.println("Itératif :\t"+(temps_total/nb_iteration));

        for (int j=0; j<=nb_thread_max; j++) {
            int nb_thread = (int)Math.pow(2, j);
	    temps_total=0;
            for (int i=0; i<=nb_iteration; i++) {
	            initialise_tableau (max);
	            // System.out.println("Executor forkjoincorrection.QuickSort avec " + nb_thread + " thread(s)");
	            if (i!=0)
			date_debut = System.nanoTime();
		    List<Future<?>> futures = new Vector<Future<?>>();
		    ExecutorService executor = Executors.newFixedThreadPool(nb_thread);
		    futures.add(executor.submit(new RunnableQuickSort(tableau, futures, executor, 0, max-1)));

		    try {
			while (!futures.isEmpty()) {
			    Future top = futures.remove( 0 );
			    top.get(); // on attend la terminaison de la thread
			}
		        executor.shutdown();
		    } catch (Exception e) {
			e.printStackTrace();
		    }

		    if (i!=0) {
			temps_total+=System.nanoTime()-date_debut;
			verifie_tableau(max);
		    }
            }
	    System.out.println("Executor: " + nb_thread+" thread(s) :\t"+(temps_total/nb_iteration));
	}

        for (int j=0; j<nb_thread_max; j++) {
            int nb_thread = (int)Math.pow(2, j);
            temps_total=0;
            for (int i=0; i<=nb_iteration; i++) {
	            initialise_tableau (max);
	            //System.out.println("ForkJoin forkjoincorrection.QuickSort avec " + nb_thread + " thread(s)");
	            if (i!=0)
			date_debut = System.nanoTime();
	            ForkJoinPool pool = new ForkJoinPool(nb_thread);
	            RecursiveAction fs = new ParallelQuickSort(tableau, 0, max-1);
	            pool.invoke(fs);
                    if (i!=0) {
			temps_total+=System.nanoTime()-date_debut;
			verifie_tableau(max);
		    }
            }
	    System.out.println("ForkJoin: " + nb_thread+" thread(s) :\t"+(temps_total/nb_iteration));
        }
    }
}

