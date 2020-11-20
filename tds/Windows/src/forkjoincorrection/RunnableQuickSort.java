package forkjoincorrection;

import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.ExecutorService;

public class RunnableQuickSort implements Runnable {
    private int [] tableau;
    private int debut_a_trier;
    private int fin_a_trier;
    private ExecutorService executor;
    private List<Future<?>> futures;

    public RunnableQuickSort(int [] tableau, List<Future<?>> futures, ExecutorService executor, int debut, int fin) {
	this.executor = executor;
	this.futures = futures;
	this.tableau = tableau;
        this.debut_a_trier = debut;
	this.fin_a_trier = fin;
    }

    public void run() {
	if (debut_a_trier < fin_a_trier) {
	    int indicePivot = this.Partition(tableau, debut_a_trier, fin_a_trier);
	    futures.add(executor.submit(new RunnableQuickSort(tableau, futures, executor, debut_a_trier, indicePivot-1))); // appel asynchrone
	    futures.add(executor.submit(new RunnableQuickSort(tableau, futures, executor, indicePivot+1, fin_a_trier))); // appel asynchrone
	}
    }

    private int Partition (int [] tableau, int debut, int fin) {
	int valeurPivot = tableau[debut];
	int d = debut+1;
	int f = fin;
	while (d < f) {
	    while(d < f && tableau[f] >= valeurPivot) f--;
	    while(d < f && tableau[d] <= valeurPivot) d++;
	    int temp = tableau[d];
	    tableau[d]= tableau[f];
	    tableau[f] = temp;
	}
	if (tableau[d] > valeurPivot) d--;
	tableau[debut] = tableau[d];
	tableau[d] = valeurPivot;
	return d;
    }
}
