package carpool; /**
 * File not to be modified
 */
import java.awt.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.Semaphore;

/****************************APPLET**************************/
public class CarPark extends Panel {
    public static final long serialVersionUID = 1L;

    final static int Places = 4;

    ThreadPanel arrivals;
    ThreadPanel departures;
    CarParkCanvas carDisplay;

    public void init() {
        // Set up Display
        arrivals = new ThreadPanel("carpool.Arrivals",Color.blue);
        departures = new ThreadPanel("carpool.Departures",Color.yellow);
        carDisplay = new CarParkCanvas("carpool.CarPark",Places,this);
        GridBagLayout gridbag = new GridBagLayout();
        setLayout(gridbag);
        GridBagConstraints gc = new GridBagConstraints();
        gc.anchor = GridBagConstraints.NORTH;
        gridbag.setConstraints(carDisplay, gc);
        gridbag.setConstraints(arrivals, gc);
        gridbag.setConstraints(departures, gc);
        add(arrivals);
        add(carDisplay);
        add(departures);
    }

    public void start() {
    	System.out.println("start in carpool.CarPark");
        CarParkControl c = new DisplayCarPark(carDisplay,Places);
        arrivals.start(new Arrivals(c));
        departures.start(new Departures(c));
    }

    public static void main(String[] args) {
        Frame f = new Frame();
        f.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) {
                System.exit(0);
            }
        });

        CarPark ut = new CarPark();
        f.add(ut);
        f.pack();
        ut.init();
        f.setSize(650, 250);
        f.setVisible(true);
        ut.start();
    }
}

/*******************ARRIVALS************************/
class Arrivals implements Runnable {
    CarParkControl carpark;
    Arrivals(CarParkControl c) {
        carpark = c;
    }

    public void run() {
      try {
        while(true) {
           ThreadPanel.rotate(330);
           carpark.arrive();
           ThreadPanel.rotate(30);
        }
      } catch (InterruptedException e){}
    }
}

/********************DEPARTURES*******************************/
class Departures implements Runnable {
    CarParkControl carpark;

    Departures(CarParkControl c) {
        carpark = c;
    }

    public void run() {
      try {
        while(true) {
            ThreadPanel.rotate(180);
            carpark.depart();
            ThreadPanel.rotate(180);
        }
      } catch (InterruptedException e){}
    }
}

/**************************************************************/
class DisplayCarPark extends CarParkControl {
    CarParkCanvas disp;
    boolean occupied[];

    DisplayCarPark(CarParkCanvas disp,int size) {
        super(size);
        this.disp = disp;
        occupied = new boolean[size];
        for (int i=0; i<size; i++) occupied[i]=false;
    }

    private void display() {
        disp.setvalue(spaces,occupied);
    }

    public void arrive() throws InterruptedException {
        super.arrive();
        synchronized (this) {
	    occupied[place(false)]=true;
	    display();
	    Thread.sleep(400);
	}
    }
    
    public void depart() throws InterruptedException {
        super.depart();
        synchronized (this) {
	    occupied[place(true)]=false;
	    display();
	}
    }

    private int place(boolean v) {
        int start = ((int)(Math.random() * 1000))% capacity;
        for (int i =0; i<capacity; i++) {
            int j = (start + i) % capacity;
            if(occupied[j] == v) return j;
        }
        return 0; //should never happen
    }
}

/**************************************************************/
class CarParkCanvas extends Canvas {
    public static final long serialVersionUID = 1L;

    String title;
    int slots;
    int spaces;
    boolean occupied[];
    Panel applet;
    Image  car;

    Font f1 = new Font("Helvetica",Font.ITALIC+Font.BOLD,24);
    Font f2 = new Font("TimesRoman",Font.BOLD,36);

    CarParkCanvas(String title, int slots, Panel applet) {
        super();
        this.title=title;
        this.slots=slots;
        spaces = slots;
        this.applet = applet;
        this.occupied = new boolean[slots];
        for (int i=0; i<slots; i++)
        	occupied[i] = false;
        setSize(20+50*slots,150);
        setBackground(Color.cyan);
        MediaTracker mt;
        mt = new MediaTracker(this);

        Path imgPath = Paths.get("image/car.gif");
        if (Files.exists(imgPath)) {
        	car = Toolkit.getDefaultToolkit().getImage("image/car.gif");
        } else{
        	System.out.println("Couldn't load car image");
            System.exit(-1);
        }
        mt.addImage(car, 0);
        try {
        	mt.waitForID(0);
        } catch (java.lang.InterruptedException e) {
            System.out.println("Couldn't load car image");
        }
    }

    public void setvalue(int spaces, boolean occupied[]){
        this.spaces = spaces;
        this.occupied = occupied;
        repaint();
    }

    public void paint(Graphics g) {
        update(g);
    }

    Image offscreen;
    Dimension offscreensize;
    Graphics offgraphics;

    public synchronized void update(Graphics g){
        Dimension d = getSize();
	if ((offscreen == null) || (d.width != offscreensize.width)
	                        || (d.height != offscreensize.height)) {
	    offscreen = createImage(d.width, d.height);
	    offscreensize = d;
	    offgraphics = offscreen.getGraphics();
	    offgraphics.setFont(getFont());
	}

	offgraphics.setColor(getBackground());
	offgraphics.fillRect(0, 0, d.width, d.height);

        // Display the title
        offgraphics.setColor(Color.black);
        offgraphics.setFont(f1);
        FontMetrics fm = offgraphics.getFontMetrics();
        int w = fm.stringWidth(title);
        int h = fm.getHeight();
        int x = (getSize().width - w)/2;
        int y = h;
        offgraphics.drawString(title, x, y);
        offgraphics.drawLine(x,y+3,x+w,y+3);
        // carpool.CarPark Places
        y = h+10;
        offgraphics.setColor(Color.white);
        offgraphics.fillRect(10,y,50*slots,100);
        offgraphics.setColor(Color.black);
        for(int i=0; i<slots; i++) {
            offgraphics.drawRect(10+50*i,y,50,100);
        }
        offgraphics.setColor(Color.white);
        for(int i=1; i<slots; i++) {
            offgraphics.drawLine(10+50*i,y+60,10+50*i,y+99);
        }
        //arrival gate
        if (spaces==0)
             offgraphics.setColor(Color.black);
        else
             offgraphics.setColor(Color.white);
        offgraphics.fillRect(8,y+60,5,39);
        //departure gate
        if (spaces==slots)
             offgraphics.setColor(Color.black);
        else
             offgraphics.setColor(Color.white);
        offgraphics.fillRect(8+50*slots,y+60,5,39);
        //Display Cars
        offgraphics.setColor(Color.blue);
        for (int i=0; i<slots; i++) {
          if (occupied[i])
            offgraphics.drawImage(car,15+50*i,y+5,this);
        }
        g.drawImage(offscreen, 0, 0, null);
    }
}
