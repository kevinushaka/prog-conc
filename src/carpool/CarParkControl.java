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
	}

	// TO DO: modify arrive
	void arrive() throws InterruptedException {
		// to do
		--spaces;
	}

	// TO DO: modify arrive
	void depart() throws InterruptedException {
		// to do
		++spaces;
	}
}
