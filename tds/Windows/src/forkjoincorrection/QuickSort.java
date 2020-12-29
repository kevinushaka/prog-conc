package forkjoincorrection;

public class QuickSort {
    
    public void quicksort(int [] tableau, int debut, int fin) {
	//System.out.println("tri de l'indice " + debut + " à l'indice " + fin);
	if (debut < fin) {
	    int indicePivot = partition(tableau, debut, fin);
	    quicksort(tableau, debut, indicePivot-1);
	    quicksort(tableau, indicePivot+1, fin);
	}
    }

    public int partition (int [] tableau, int debut, int fin) {
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
	//System.out.println("indice du pivot " + d);
	return d;
    }
}
