/**
 * Class to be modified
 */

/**
 * @author riveill
 *
 */
import java.awt.*;

public class Counter {
	int value_=0;
	TextCanvas display_;
	
	Counter(TextCanvas t) {
		display_=t;
		display_.setcolor(Color.cyan);
	}
	
	// TO DO: modify increment
	public synchronized void increment() {
		int temp = value_;  //read
		
		Simulate.interrupt();
		++temp;            //add1
		value_=temp;       //write
		display_.setvalue(value_);
	}	

}