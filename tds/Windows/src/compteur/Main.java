package compteur;

import forkjoincorrection.RunnableQuickSort;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Main {

    public static void main(String[] args) {

        int nb_thread = 2;
        int nb_it = 100000;
        long date_debut = 0;
        long temps_total=0;
        ExecutorService executor = Executors.newFixedThreadPool(nb_thread);
        List<Future<Integer>> futuresAtm=new Vector<>();
        date_debut = System.nanoTime();
        for (int i = 0; i < nb_thread; i++) {
            futuresAtm.add(executor.submit(new Compteur_Atomic_Int(nb_it)));
        }
        try{
            System.out.println(futuresAtm.get(0).get());
            temps_total=System.nanoTime()-date_debut;
        }catch (InterruptedException | ExecutionException e){
            System.out.println(e.getMessage());
        }
        System.out.println("Compteur AtomicInteger :\t"+ temps_total/10000 +"ms.");

        executor = Executors.newFixedThreadPool(nb_thread);
         date_debut = 0;
         temps_total=0;
        List<Future<Long>> futuresLong=new Vector<>();
        date_debut = System.nanoTime();
        for (int i = 0; i < nb_thread; i++) {
            futuresLong.add(executor.submit(new Compteur_lock(nb_it)));
        }
        try{
            System.out.println(futuresLong.get(0).get());
            temps_total=System.nanoTime()-date_debut;
        }catch (InterruptedException | ExecutionException e){
            System.out.println(e.getMessage());
        }
        System.out.println("Compteur Lock :\t"+ temps_total/10000 +"ms.");

        date_debut = 0;
        temps_total=0;
        futuresLong=new Vector<>();
        date_debut = System.nanoTime();
        for (int i = 0; i < nb_thread; i++) {
            futuresLong.add(executor.submit(new Compteur_rw(nb_it)));
        }
        try{
            System.out.println(futuresLong.get(0).get());
            temps_total=System.nanoTime()-date_debut;
        }catch (InterruptedException | ExecutionException e){
            System.out.println(e.getMessage());
        }
        System.out.println("Compteur Read and Write Lock :\t"+ temps_total/10000 +"ms.");

        date_debut = 0;
        temps_total=0;
        futuresLong=new Vector<>();
        date_debut = System.nanoTime();
        for (int i = 0; i < nb_thread; i++) {
            futuresLong.add(executor.submit(new Compteur_semaphore(nb_it)));
        }
        try{
            System.out.println(futuresLong.get(0).get());
            temps_total=System.nanoTime()-date_debut;
        }catch (InterruptedException | ExecutionException e){
            System.out.println(e.getMessage());
        }
        System.out.println("Compteur Semaphore :\t"+ temps_total/10000 +"ms.");

        date_debut = 0;
        temps_total=0;
        futuresLong=new Vector<>();
        date_debut = System.nanoTime();
        for (int i = 0; i < nb_thread; i++) {
            futuresLong.add(executor.submit(new Compteur_synchronized(nb_it)));
        }
        try{
            System.out.println(futuresLong.get(0).get());
            temps_total=System.nanoTime()-date_debut;
        }catch (InterruptedException | ExecutionException e){
            System.out.println(e.getMessage());
        }
        System.out.println("Compteur Synchronized :\t"+ temps_total/10000 +"ms.");



        executor.shutdown();


    }
}
