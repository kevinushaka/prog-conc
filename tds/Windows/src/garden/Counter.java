package garden; /**
 * Class to be modified
 */

/**
 * @author riveill
 *
 */
import java.awt.*;
import java.util.concurrent.Semaphore;

public class Counter extends Semaphore{
	int value_=0;
	TextCanvas display_;
	
	Counter(TextCanvas t) {
		super(1);
		display_=t;
		display_.setcolor(Color.cyan);
	}
	
	// TO DO: modify increment
	public void increment() throws InterruptedException {
		int temp = value_;  //read
		Simulate.interrupt();
		++temp;            //add1
		value_=temp;       //write
		display_.setvalue(value_);
	}	

}