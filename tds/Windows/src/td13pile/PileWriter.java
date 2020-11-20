package td13pile;
import java.util.Random;
import java.util.concurrent.Callable;

public class PileWriter implements Callable<Stat> {

    private final static int MASK = 0xFFFFF00;
    private final static int MULTIPLIER = 256;

    private final Pile pile;

    private final int count;
    private final Random rnd;
    
    private final Stat stat = new Stat();

    public PileWriter(int count, Pile pile, Random rnd) {
	this.count = count;
	this.pile = pile;
	this.rnd = rnd;
    }

    @Override
    public Stat call() {
	long id = Thread.currentThread().getId();
	int a = 0;
	int b = 0;
	for (int i = 0; i < count; i++) {
	    a += rnd.nextInt();
	    if (a / 3 > 1) {
	    	if (pile.push(i)) {
		    stat.added();
		 } else {
		    stat.notAdded();
		 } 	    
	    } else {
		if (pile.pop() >= 0) {
		    stat.deleted();
		} else {
		    stat.notDeleted();
		} 
	    }
	}
	return stat;
    }
}
