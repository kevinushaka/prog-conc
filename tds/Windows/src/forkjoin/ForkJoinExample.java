package forkjoin;

import java.util.concurrent.ForkJoinPool;

public class ForkJoinExample {

    void example(){
        System.out.println("Quicksort");
        QuickSortAction quickSort=new QuickSortAction();
        System.out.print("Entree : ");
        System.out.println(quickSort.getValues());
        int processeurs=2;
        ForkJoinPool pool=new ForkJoinPool(processeurs);
        pool.invoke(quickSort);
        System.out.print("Sortie : ");
        System.out.println(quickSort.getValues());
    }

    public static void main(String[] args) {
        ForkJoinExample forkJoinExample=new ForkJoinExample();
        forkJoinExample.example();
    }
}
