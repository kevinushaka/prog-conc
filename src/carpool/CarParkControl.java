package carpool;

import java.util.concurrent.Semaphore;

/**
 * Class to be modified
 */

public class CarParkControl {
	protected int spaces;
	protected int capacity;
	Semaphore arrivals;
	Semaphore departure;
	CarParkControl(int n) {
		capacity = spaces = n;
		arrivals=new Semaphore(spaces,true);
		departure=new Semaphore(spaces,true);
	}

	// TO DO: modify arrive
	void arrive() throws InterruptedException {
		// to do
		arrivals.acquire();
		departure.release();
		--spaces;
	}

	// TO DO: modify arrive
	void depart() throws InterruptedException {
		// to do
		departure.acquire();
		arrivals.release();
		++spaces;
	}
}
