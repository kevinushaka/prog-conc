package forkjoincorrection;

import java.util.concurrent.RecursiveAction;
import java.util.concurrent.ForkJoinTask;
import java.util.ArrayList;
import java.util.List;


public class ParallelQuickSort extends RecursiveAction {
    private int [] tableau;
    private int debut_a_trier;
    private int fin_a_trier;

    public ParallelQuickSort(int [] tableau, int debut, int fin) {
	//for (int i=debut; i<=fin; i++) System.out.println(tableau[i]);
	this.tableau = tableau;
        this.debut_a_trier = debut;
	this.fin_a_trier = fin;
    }

    public void compute() {
	if (debut_a_trier < fin_a_trier) {
	    List<ForkJoinTask<Void>> subTasks = new ArrayList<>();
	    int indicePivot = this.Partition(tableau, debut_a_trier, fin_a_trier);
	    //System.out.println("creation 2 threads");
	    subTasks.add(new ParallelQuickSort(tableau, debut_a_trier, indicePivot-1).fork());
	    subTasks.add(new ParallelQuickSort(tableau, indicePivot+1, fin_a_trier).fork());
	    for (ForkJoinTask<Void> subTask : subTasks) subTask.join();
	    //System.out.println("2 threads terminées");
            // invokeAll(
	    //           new forkjoincorrection.ParallelQuickSort(tableau, debut_a_trier, indicePivot-1).fork(),
	    //           new forkjoincorrection.ParallelQuickSort(tableau, indicePivot+1, fin_a_trier).fork()
            //          );
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
