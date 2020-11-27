package td13pile;
import java.util.concurrent.atomic.*;
import java.util.concurrent.locks.LockSupport;

public class PileNonBlocked implements Pile {
    private AtomicReference<Node> head = new AtomicReference<Node>();

    public boolean push(int value) {

        boolean rightValue = false;
        do {
            Node exceptedHead = head.get();
            if(exceptedHead==null ||exceptedHead.value != value){
                Node newnode =new Node(value);
                if(head.compareAndExchange(exceptedHead,newnode)==exceptedHead){
                    newnode.next=exceptedHead;
                    rightValue=true;
                }
            }else{
                rightValue = true;
            }
        } while (!rightValue);
        return true;
    }

    public int pop() {
        boolean rightValue = false;
        do {
            Node exceptedHead = head.get();
            if(exceptedHead==null){
                return (-1);}
            if (exceptedHead.value != head.get().value) {
                rightValue = true;
            } else {
                if(head.compareAndExchange(exceptedHead, head.get().next)==exceptedHead){
                    rightValue=true;
                }
            }
        } while (!rightValue);
        if(head.get()==null){
            return (-1);}
        return head.get().value;
    }

    private static class Node {
	Node next = null;
	int value;

	Node(int value) {
	    this.value = value;
	}
    }
}
