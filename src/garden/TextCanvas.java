/**
 * Class not to be modified
 */

/**
 * @author riveill
 *
 */
import java.awt.*;

public class TextCanvas extends Canvas{
    public static final long serialVersionUID = 1L;

    int value_ = 0;
    String title_;

    Font f1 = new Font("Helvetica",Font.BOLD,36);
    Font f2 = new Font("Times",Font.ITALIC+Font.BOLD,24);

    TextCanvas(String title) {
        super();
        title_=title;
    }

    public void setcolor(Color c){
        setBackground(c);
        repaint();
    }

    public void setvalue(int newval){
        value_ = newval;
        repaint();
    }

    public void paint(Graphics g){
        g.setColor(Color.black);

        // Display the title
        g.setFont(f2);
        FontMetrics fm = g.getFontMetrics();
        int w = fm.stringWidth(title_);
        int h = fm.getHeight();
        int x = (getSize().width - w)/2;
        int y = h+10;
        g.drawString(title_, x, y);
        g.drawLine(x,y+3,x+w,y+3);

        // Display the value
        g.setFont(f1);
        fm = g.getFontMetrics();
        String s1 = String.valueOf(value_);
        w = fm.stringWidth(s1);
        h = fm.getHeight();
        x = (getSize().width - w)/2;
        y = (getSize().height+ h)/2;
        g.drawString(s1, x, y);
    }

}
