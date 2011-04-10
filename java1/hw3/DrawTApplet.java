import java.applet.Applet;
import java.awt.Color;
import java.awt.Graphics;

public class DrawTApplet
    extends Applet
{
    public static final long serialVersionUID = 1L;

    private static final int NEG = -1;
    private static final int POS = 1;

    private Graphics g = null;

    public void paint(Graphics g) {
        this.g = g;
        this.resize(300, 400);
        this.setBackground(Color.white);

        // Draw line segments
        this.drawPyramid(Color.RED,       25,  50, POS,  50, POS, 249, NEG,  50, POS);
        this.drawPyramid(Color.GREEN,     25, 249, NEG,  50, POS, 249, NEG,  99, NEG);
        this.drawPyramid(Color.ORANGE,    25, 249, NEG,  99, NEG, 174, NEG,  99, NEG);
        this.drawPyramid(Color.BLUE,      25, 174, NEG,  99, NEG, 174, NEG, 349, NEG);
        this.drawPyramid(Color.YELLOW,    25, 174, NEG, 349, NEG, 125, POS, 349, NEG);
        this.drawPyramid(Color.MAGENTA,   25, 125, POS, 349, NEG, 125, POS,  99, NEG);
        this.drawPyramid(Color.DARK_GRAY, 25, 125, POS,  99, NEG,  50, POS,  99, NEG);
        this.drawPyramid(Color.PINK,      25,  50, POS,  99, NEG,  50, POS,  50, POS);
    }

    public void drawLine(Color c, int x1, int y1, int x2, int y2) {
        this.g.setColor(c);
        this.g.drawLine(x1, y1, x2, y2);
    }

    public void drawPyramid(Color c, int depth,
                            int x1, int dx1, 
                            int y1, int dy1,
                            int x2, int dx2,
                            int y2, int dy2) {
       for (int i = 0; i < depth; i++) {
           this.drawLine(c,
                         x1 + (dx1 * i),
                         y1 + (dy1 * i),
                         x2 + (dx2 * i),
                         y2 + (dy2 * i));
       }
    }
}
