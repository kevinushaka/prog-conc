package forkjoin;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;

public class QuickSortAction extends RecursiveAction {

    Random rand=new Random();
    int taille = 50;
    int max = taille-1;
    int min = 0;
    int tableau[]=new int[ taille];
    QuickSortAction(){
        for (int i=0; i<taille; i++) {
            tableau[i] = rand.nextInt(2 * max - min + 1) + min;
        }
    }

    QuickSortAction(int[] tab, int min, int max){
        tableau=tab;
        this.max=max;
        this.min=min;
    }
    @Override
    protected void compute() {
        if(min<max){
            List<ForkJoinTask<Void>> subTasks = new ArrayList<>();
            int indicePivot=partition(tableau,min,max);
            QuickSortAction left =new QuickSortAction(tableau,min,indicePivot-1);
            QuickSortAction right =new QuickSortAction(tableau,indicePivot+1,max);
            subTasks.add(left.fork());
            subTasks.add(right.fork());
            for (ForkJoinTask<Void> subTask : subTasks) subTask.join();
        }

    }
    String getValues(){
            StringBuilder res=new StringBuilder();
            for(int i=0; i<taille; i++)
                res.append(tableau[i]+" ");
            return res.toString();
    }
    String getValues(long[]tab,long d,long f){
        StringBuilder res=new StringBuilder();
        for(int i=(int)d; i<f; i++)
            res.append(tab[i]+" ");
        return res.toString();
    }
    private int partition (int [] tableau, int debut, int fin) {
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
