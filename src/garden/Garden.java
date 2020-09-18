/**
 * Class not to be modified
 */

/**
 * @author riveill
 *
 */
import java.awt.*;
import java.awt.event.*;

public class Garden extends Panel implements ActionListener {
	public static final long serialVersionUID = 1L;

    Button start_;
    Button stop_;
    Turnstile west_;
    Turnstile east_;

    public void init() {
        // Set up Buttons
        this.setFont(new Font("Helvetica",Font.BOLD,24));
        Panel p1 = new Panel();
        start_ = new Button("Start");
        start_.addActionListener(this);
        p1.add(start_);
        stop_ = new Button("Stop");
        stop_.addActionListener(this);
        p1.add(stop_);

        // Set up Display
        Panel p2 = new Panel();
        TextCanvas gardenD = new TextCanvas("Garden");
        TextCanvas westD = new TextCanvas("West");
        TextCanvas eastD = new TextCanvas("East");
        gardenD.setSize(150,100);
        westD.setSize(100,100);
        eastD.setSize(100,100);
        p2.add(westD);
        p2.add(gardenD);
        p2.add(eastD);

        // Arrange display
        setLayout(new BorderLayout());
        add("Center",p2);
        add("South",p1);

        // Create Thread
        Counter people_ = new Counter(gardenD);
        west_= new Turnstile(westD,people_,500);
        east_= new Turnstile(eastD,people_,500);
        west_.start();
        east_.start();
    }
    public void actionPerformed(ActionEvent event) {
        Object source = event.getSource();

        if(source==start_) {
            east_.activate();
            west_.activate();
        } else if (source==stop_) {
            east_.passivate();
            west_.passivate();
        }
    }

    public static void main(String[] args) {
        Frame f = new Frame();
        f.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) {
                System.exit(0);
            }
        });

        Garden ut = new Garden();
        f.add(ut);
        f.pack();
        ut.init();
        f.setSize(400, 200);
        f.setVisible(true);
    }

}
