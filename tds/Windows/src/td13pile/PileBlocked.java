package td13pile;
public class PileBlocked implements Pile {
    private Node head = null;

    public synchronized boolean push(int value) {
	head = new Node(head, value);
	return true;
    }

    public synchronized int pop() {
	if (head != null) {
	    Node oldHead = head;
	    head = oldHead.next;
	    return oldHead.value;
	} else
	    return (-1);
    }

    private static class Node {
	Node next = null;
	int value;

	Node(Node next, int value) {
	    this.next = next;
	    this.value = value;
	}
    }
}
