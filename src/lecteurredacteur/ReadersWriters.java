//@author: j.n.magee 11/11/96
/**
 * File not to be modified
 */
package lecteurredacteur;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import carpool.StringCanvas;
/********************************************************/
class Reader implements Runnable {

    ReadWrite lock_;

    Reader(ReadWrite lock) {
        lock_ = lock;
    }

    public void run() {
      try {
        while(true)  {
            while(!ThreadPanel.rotate());
            // begin critical section
            lock_.acquireRead();
            while(ThreadPanel.rotate());
            lock_.releaseRead();
        }
      } catch (InterruptedException e){}
    }
}

/********************************************************/
class Writer implements Runnable {

    ReadWrite lock_;

    Writer(ReadWrite lock) {
        lock_ = lock;
    }

    public void run() {
      try {
        while(true)  {
            while(!ThreadPanel.rotate());
            // begin critical section
            lock_.acquireWrite();
            while(ThreadPanel.rotate());
            lock_.releaseWrite();
        }
      } catch (InterruptedException e){}
    }
}

/****************************APPLET**************************/
public class ReadersWriters extends Panel {
	public static final long serialVersionUID = 1L;

    ThreadPanel read1;
    ThreadPanel read2;
    ThreadPanel write1;
    ThreadPanel write2;
        
    Checkbox priority;
    Checkbox fair;
    Checkbox safe;
    Checkbox unsafe;

    String rwClass;
    StringCanvas display;

    public void init() {
        setLayout(new BorderLayout());
        display = new StringCanvas(rwClass,Color.green);
        add("Center", display);
        
        Panel p1 = new Panel();
        p1.add(read1 =new ThreadPanel("Reader 1",Color.blue,true));
        p1.add(read2 =new ThreadPanel("Reader 2",Color.blue,true));
        p1.add(write1=new ThreadPanel("Writer 1",Color.yellow,true));
        p1.add(write2=new ThreadPanel("Writer 2",Color.yellow,true));
        add("South",p1);
		setBackground(Color.lightGray);
		
        Panel p2 = new Panel();
        p2.setLayout(new GridLayout(1, 3));
        CheckboxGroup cbg = new CheckboxGroup();
        
        unsafe = new Checkbox("Unsafe",cbg,true);
        unsafe.setBackground(Color.lightGray);
        unsafe.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
              stop();
              rwClass = "ReadWriteUnsafe";
              start();        
            }
         });
        
        safe = new Checkbox("Safe",cbg,false);
		safe.setBackground(Color.lightGray);
		safe.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
              stop();
              rwClass = "ReadWriteSafe";
              start();        
            }
         });
	
        fair = new Checkbox("Fair",cbg,false);
		fair.setBackground(Color.lightGray);
        fair.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
              stop();
              rwClass = "ReadWriteFair";
              start();        
            }
         });

        priority = new Checkbox("Priority",cbg,false);
        priority.setBackground(Color.lightGray);
        priority.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
              stop();
              rwClass = "ReadWritePriority";
              start();        
            }
         });
        
        p2.add(unsafe);
        p2.add(safe);
        p2.add(fair);
        p2.add(priority);
        add("North", p2);

    }

    public void start() {
        DisplayReadWrite lock;
        if (rwClass=="ReadWriteSafe")
           lock = new DisplayReadWrite(display,new ReadWriteSafe());
        else if (rwClass=="ReadWritePriority")
           lock = new DisplayReadWrite(display,new ReadWritePriority());
        else if (rwClass=="ReadWriteFair")
           lock = new DisplayReadWrite(display,new ReadWriteFair());
        else
            lock = new DisplayReadWrite(display,new ReadWriteUnsafe());
        
        display.setString(rwClass);
        
        read1.start(new Reader(lock));
        read2.start(new Reader(lock));
        write1.start(new Writer(lock));
        write2.start(new Writer(lock));
    }

    public void stop() {
        read1.stop();
        read2.stop();
        write1.stop();
        write2.stop();
     }
    
    public static void main(String[] args) {
        Frame f = new Frame();
        f.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) {
                System.exit(0);
            }
        });

        ReadersWriters ut = new ReadersWriters();
        ut.rwClass = "ReadWriteUnsafe";
        f.add(ut);
        f.pack();
        ut.init();
        f.setSize(730, 400);
        f.setVisible(true);
        ut.start();
    }

}