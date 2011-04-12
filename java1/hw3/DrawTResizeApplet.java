import java.applet.Applet;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ComponentListener;
import java.awt.event.ComponentEvent;
import java.awt.Graphics;
import javax.swing.JApplet;
import javax.swing.JFrame;

public class DrawTResizeApplet
    extends JApplet
{
    public static final long serialVersionUID = 1L;

    private int w_div = 6; // Divide width by this value to determine width of stem of the T
    private int h_div = 8; // Divide height by this value to determine height of cross of the T

    public static void main(String[] args) {
        JFrame frame = new JFrame("Draw T");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(300, 400));
        frame.setMinimumSize(new Dimension(30,40));

        JApplet applet = new DrawTResizeApplet(); 
        applet.init();
        applet.start();
        frame.add("Center", applet);
        frame.pack();
        frame.setVisible(true);
    }

    public void init() {
        // Here we handle the re-sizing of the Applet, and ignore all other
        // events returned by the ComponentListener
        this.addComponentListener(new ComponentListener() {
            public void componentHidden(ComponentEvent e) { ; }
            public void componentMoved(ComponentEvent e) { ; }
            public void componentResized(ComponentEvent e) {
                ((DrawTResizeApplet)e.getComponent()).repaint();
            }
            public void componentShown(ComponentEvent e) { ; }
        });
    }

    public void start() {
        this.repaint(); // Draw the initial T prior to any resize events
    }

    public void paint(Graphics g) {
        this.setBackground(Color.WHITE);
        g.setColor(Color.BLACK);
        int width  = this.getWidth();
        int height = this.getHeight();

        // We use these ratios to determine whether the T's dimension should
        // be calculated with respect to width or height.
        double targetRatio = (double)(this.w_div) / (double)(this.h_div);
        double dimRatio = (double)(width) / (double)(height);

        int top_x; // x-coordinate of upper left corner of the T's cross
        int top_y; // y-coordinate of upper left corner of the T's cross
        int top_w; // width of T's cross
        int top_h; // height (thickness) of T's cross
        int cnt_x; // x-coordinate of the upper left corner of the T's stem
        int cnt_y; // y-coordinate of the upper left corner of the T's stem
        int cnt_w; // width (thickness) of the T's stem
        int cnt_h; // height of the T's stem

        // Calculate dimensions with respect to height
        if (dimRatio > targetRatio) {
            top_y = min_one(height / h_div);
            top_h = min_one(top_y);
            cnt_y = min_one(top_y);
            cnt_h = min_one(height - ((top_y) * 2));

            cnt_w = min_one(top_h);
            cnt_x = min_one(width / 2 - (cnt_w / 2));
            top_w = min_one(cnt_h * w_div / h_div);
            top_x = min_one(width / 2 - (top_w / 2));
        } 
        // Calculate dimensions with respect to width
        else {
            top_x = min_one(width  / w_div);
            top_w = min_one(width - ((top_x) * 2));
            cnt_x = min_one((width / 2) - ((top_x) / 2));
            cnt_w = min_one(top_x);

            cnt_h = min_one(top_w * h_div / w_div);
            cnt_y = min_one(height / 2 - (cnt_h / 2));
            top_h = min_one(cnt_w);
            top_y = min_one(cnt_y);
        }

        g.clearRect(0, 0, width, height); // Remove previous T image
        g.fillRect(top_x, top_y, top_w, top_h); // Draw T's cross
        g.fillRect(cnt_x, cnt_y, cnt_w, cnt_h); // Draw T's stem
    }

    private static int min_one(int value) {
        return (value < 1) ? 1 : value;
    }
}
