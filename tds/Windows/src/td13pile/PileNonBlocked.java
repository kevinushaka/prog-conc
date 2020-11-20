package td13pile;
import java.util.concurrent.atomic.*;
import java.util.concurrent.locks.LockSupport;

public class PileNonBlocked implements Pile {
    private AtomicReference<Node> head = new AtomicReference<Node>();

    public boolean push(int value) {
	return true;
    }

    public int pop() {
	return (-1);
    }

    private static class Node {
	Node next = null;
	int value;

	Node(int value) {
	    this.value = value;
	}
    }
}
