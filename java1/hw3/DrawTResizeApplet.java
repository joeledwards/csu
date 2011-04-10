import java.applet.Applet;
import java.awt.Color;
import java.awt.Container;
import java.awt.event.ComponentListener;
import java.awt.event.ComponentEvent;
import java.awt.Graphics;

public class DrawTResizeApplet
    extends Applet
{
    private   Graphics g      = null;
    private   int      w_div  = 6; // Divide width by this value to determine width of stemp of the T
    private   int      h_div  = 8; // Divide height by this value to determine height of cross of the T
    public    boolean  sizing = false;

    public void paint(Graphics g) {
        this.g = g;

        this.setBackground(Color.WHITE);
        this.g.setColor(Color.BLACK);

        // Here we handle the re-sizing of the Applet, and ignore all other
        // events returned by the ComponentListener
        this.addComponentListener( new ComponentListener() {
            public void componentHidden(ComponentEvent e) { ; }
            public void componentMoved(ComponentEvent e) { ; }
            public void componentResized(ComponentEvent e) {
                DrawTResizeApplet a = (DrawTResizeApplet)e.getComponent();
                if (!a.sizing) {
                    a.sizing = true;
                    a.resize(a.getWidth(), a.getHeight());
                    a.redrawT();
                    a.sizing = false;
                }
            }
            public void componentShown(ComponentEvent e) { ; }
        });

        this.redrawT(); // Draw the initial T prior to any resize events
    }

    public void setSize(int width, int height) {
        super.setSize(width, height);
        validate();
    }

    public void redrawT() {
        int width = this.getWidth();
        int height = this.getHeight();

        // We use these ratios to determine whether the T's dimension should
        // be calculated with respect to width or height.
        double targetRatio = (float)(this.w_div) / (float)(this.h_div);
        double dimRatio = (float)(width) / (float)(height);

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
            top_y = height / h_div;
            top_y = (top_y < 1) ? 1 : top_y;
            top_h = top_y;
            top_h = (top_h < 1) ? 1 : top_h;
            cnt_y = top_y;
            cnt_y = (cnt_y < 1) ? 1 : cnt_y;
            cnt_h = height - ((top_y) * 2);
            cnt_h = (cnt_h < 1) ? 1 : cnt_h;

            cnt_w = top_h;
            cnt_w = (cnt_w < 1) ? 1 : cnt_w;
            cnt_x = width / 2 - (cnt_w / 2);
            cnt_x = (cnt_x < 1) ? 1 : cnt_x;
            top_w = cnt_h * w_div / h_div;
            top_w = (top_w < 1) ? 1 : top_w;
            top_x = width / 2 - (top_w / 2);
            top_x = (top_x < 1) ? 1 : top_x;
        } 
        // Calculate dimensions with respect to width
        else {
            top_x = width  / w_div;
            top_x = (top_x < 1) ? 1 : top_x;
            top_w = width - ((top_x) * 2);
            top_w = (top_w < 1) ? 1 : top_w;
            cnt_x = (width / 2) - ((top_x) / 2);
            cnt_x = (cnt_x < 1) ? 1 : cnt_x;
            cnt_w = top_x;
            cnt_w = (cnt_w < 1) ? 1 : cnt_w;

            cnt_h = top_w * h_div / w_div;
            cnt_h = (cnt_h < 1) ? 1 : cnt_h;
            cnt_y = height / 2 - (cnt_h / 2);
            cnt_y = (cnt_y < 1) ? 1 : cnt_y;
            top_h = cnt_w;
            top_h = (top_h < 1) ? 1 : top_h;
            top_y = cnt_y;
            top_y = (top_y < 1) ? 1 : top_y;
        }

        this.g.clearRect(0, 0, width, height); // Remove previous T image
        this.g.fillRect(top_x, top_y, top_w, top_h); // Draw T's cross
        this.g.fillRect(cnt_x, cnt_y, cnt_w, cnt_h); // Draw T's stem
    }
}
