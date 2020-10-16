package lecteurredacteur;
import carpool.StringCanvas;
import java.awt.Color;
/**
 * File not to be modified
 */
/********************************************************/
class DisplayReadWrite implements ReadWrite {
	StringCanvas display_;
    ReadWrite lock_;
    private int readers = 0;
    private boolean writing = false;

    DisplayReadWrite(StringCanvas t, ReadWrite lock) {
        display_=t;
        lock_=lock;
        setdisplay();
    }

    private void setdisplay(){
    	boolean bool;
    
    	bool = (writing & readers==0) | (!writing);
    	display_.setString("readers= " + String.valueOf(readers)
    	               +"  writing= " + (new Boolean(writing)).toString()
                       +"  check=" + (new Boolean(bool)).toString());

    	if (bool) {
    		display_.setcolor(Color.green);
    	} else {
    		display_.setcolor(Color.red);
    	}
    }

    public void acquireRead() throws InterruptedException{
    	lock_.acquireRead();
    	++readers;
    	setdisplay();
    }

    public void releaseRead() {
    	try {
			lock_.releaseRead();
		}catch (InterruptedException e){}

    	--readers;
    	setdisplay();
    }

    public void acquireWrite() throws InterruptedException {
    	lock_.acquireWrite();
    	writing = true;
    	setdisplay();
    }

    public void releaseWrite() {
    	lock_.releaseWrite();
    	writing = false;
    	setdisplay();
    }
 }

