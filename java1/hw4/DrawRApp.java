import java.awt.*;
import java.applet.Applet;
import javax.swing.JApplet;
import javax.swing.JFrame;

public class DrawRApp
    extends JApplet
{
    public static final long serialVersionUID = 1L;

    Rectangle r1 = new Rectangle(50,75,Color.blue);
    Rectangle r2 = new Rectangle(60,80,Color.red);

    Rectangle r3 = new Rectangle();
    Rectangle r4 = new Rectangle(190,5,Color.orange);
    Rectangle r5 = new Rectangle(100,11,Color.green);
    Rectangle r6 = new Rectangle(11,93,Color.green);
    Rectangle r7 = new Rectangle(11,98,Color.green);

    public void start() {
        repaint();
    }

    public void paint(Graphics g) {
        r1.drawAt(g,10,10);
        r2.drawAt(g,100,100);
        g.drawLine(100,100,200,200);

        r3.drawAt(g,20, 150);
        r4.drawAt(g,5,190);
        r5.drawAt(g,30,87);
        r6.drawAt(g,62,10);
        r7.drawAt(g,87,82);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Draw R");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(200, 250));
        frame.setMinimumSize(new Dimension(200,200));

        JApplet applet = new DrawRApp(); 
        applet.init();
        applet.start();
        frame.add("Center", applet);
        frame.pack();
        frame.setVisible(true);
    }
}
