package td13pile;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

public class Main {
    private final static int MAX_THREADS = 4;
    private final static int TOTAL_BENCHMARK_OPERATIONS = 300_000;

    private final static int REPEAT_BENCHMARK = 5;

    private static long nothing(int threads, Pile pile) throws InterruptedException, ExecutionException {
	System.out.println("NB THREAD="+threads);
	ExecutorService executor = Executors.newFixedThreadPool(threads);
	
	System.out.println("Création de la liste des Piles");
	List<PileWriter> writers = new ArrayList<>();
	for (int i=0; i< threads; i++) {
	    writers.add(new PileWriter(TOTAL_BENCHMARK_OPERATIONS/threads, pile, new Random(i)));
	}

	long t1 = System.nanoTime();
	List<Future<Stat>> futures = executor.invokeAll(writers);
	executor.shutdown();
	executor.awaitTermination(1, TimeUnit.HOURS);
	long t2 = System.nanoTime();

	List<Stat> stats = new ArrayList<>();
	for (Future<Stat> future : futures) {
	    stats.add(future.get());
	}

	Stat total = new Stat();
	stats.forEach(total::add);
	System.out.println("TOTAL: " + total);


	System.out.println(threads+" thread(s) " + pile.getClass().getSimpleName() + " TIME: " + (t2 - t1)/1_000_000 + " ms");
	return t2-t1;
    }
    private static long test(int threads, Pile pile) throws InterruptedException, ExecutionException {
	System.out.println("NB THREAD="+threads);
	ExecutorService executor = Executors.newFixedThreadPool(threads);
	
	System.out.println("Création de la liste des Piles");
	List<PileWriter> writers = new ArrayList<>();
	for (int i=0; i< threads; i++) {
	    writers.add(new PileWriter(TOTAL_BENCHMARK_OPERATIONS/threads, pile, new Random(i)));
	}

	long t1 = System.nanoTime();
	List<Future<Stat>> futures = executor.invokeAll(writers);
	executor.shutdown();
	executor.awaitTermination(1, TimeUnit.HOURS);
	long t2 = System.nanoTime();

	List<Stat> stats = new ArrayList<>();
	for (Future<Stat> future : futures) {
	    stats.add(future.get());
	}

	Stat total = new Stat();
	stats.forEach(total::add);
	System.out.println("TOTAL: " + total);


	System.out.println(threads+" thread(s) " + pile.getClass().getSimpleName() + " TIME: " + (t2 - t1)/1_000_000 + " ms");
	return t2-t1;
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException {

	long time1 = 0;
	for (int i=1; i <= MAX_THREADS; i++) {
	    for (int j = 0; j < REPEAT_BENCHMARK; j++) {
		time1 += test(i, new PileBlocked());
	    }
	}


	long time2 = 0;
	for (int i=1; i <= MAX_THREADS; i++) {
	    for (int j = 0; j < REPEAT_BENCHMARK; j++) {
		time2 += test(i, new PileNonBlocked());
	    }
	}

	System.out.println("PileBlocked: "+time1/1_000_000+" ms");
	System.out.println("PileNonBlocked: "+time2/1_000_000+" ms");

    }
}
