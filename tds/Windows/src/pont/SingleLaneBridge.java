package pont; /**
 * File not to be modified
 */
import java.awt.*;
import java.awt.event.*;

/****************************APPLET**************************/
public class SingleLaneBridge extends Panel {
	public static final long serialVersionUID = 1L;

    BridgeCanvas display;
    Button restart;
    Button freeze;
    Button onecar;
    Button twocar;
    Button threecar;
    Checkbox fair;
    Checkbox safe;
    boolean fixed = false;
    int maxCar = 1;

    Thread red[];
    Thread blue[];

    public void init() {
        setLayout(new BorderLayout());
        
        display = new BridgeCanvas(this);
        add("Center",display);
        
        restart = new Button("Restart");
        restart.addActionListener(new ActionListener() {
           public void actionPerformed(ActionEvent e) {
             display.thaw();
           }
        });
		
        freeze = new Button("Freeze");
        freeze.addActionListener(new ActionListener() {
           public void actionPerformed(ActionEvent e) {
             display.freeze();
           }
        });
		
        onecar = new Button("One Car");
        onecar.addActionListener(new ActionListener() {
           public void actionPerformed(ActionEvent e) {
             stop();
             maxCar = 1;
             start();        
           }
        });
		
        twocar = new Button("Two Cars");
        twocar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
              stop();
              maxCar = 2;
              start();        
            }
         });
		
        threecar = new Button("Three Cars");
        threecar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
              stop();
              maxCar = 3;
              start();        
            }
         });
		
        safe = new Checkbox("Safe",null,false);
		safe.setBackground(Color.lightGray);
		safe.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
              stop();
              start();        
            }
         });
	
        fair = new Checkbox("Fair",null,false);
		fair.setBackground(Color.lightGray);
        fair.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
              stop();
              start();        
            }
         });
		
        Panel p1 = new Panel();
        p1.setLayout(new FlowLayout());
        p1.add(freeze);
        p1.add(restart);
        
        Panel p2 = new Panel();
        p2.setLayout(new FlowLayout());
        p2.add(onecar);
        p2.add(twocar);
        p2.add(threecar);
        
        Panel p3 = new Panel();
        p3.setLayout(new FlowLayout());
        p3.add(safe);
        p3.add(fair);
        
        add("North",p1);
        setBackground(Color.lightGray);
        
        Panel p4 = new Panel();
        p4.setLayout(new BorderLayout());
        p4.add("North", p2);
        p4.add("South", p3);        
        add("South",p4);
        setBackground(Color.lightGray);
        

   }

    public void start() {
        red = new Thread[maxCar];
        blue = new Thread[maxCar];
        display.init(maxCar);
        Bridge b;
        if (fair.getState() && safe.getState())
            b = new FairBridge();
        else if ( safe.getState())
            b = new SafeBridge();
        else
            b = new Bridge();
        for (int i = 0; i<maxCar; i++) {
            red[i] = new Thread(new RedCar(b,display,i));
            blue[i] = new Thread(new BlueCar(b,display,i));
        }
        for (int i = 0; i<maxCar; i++) {
            red[i].start();
            blue[i].start();
        }
    }

    public void stop() {
        for (int i = 0; i<maxCar; i++) {
            red[i].interrupt();
            blue[i].interrupt();
        }
    }
    
    public static void main(String[] args) {
        Frame f = new Frame();
        f.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) {
                System.exit(0);
            }
        });

        SingleLaneBridge ut = new SingleLaneBridge();
        f.add(ut);
        f.pack();
        ut.init();
        f.setSize(550, 350);
        f.setVisible(true);
        ut.start();
    }

 }
