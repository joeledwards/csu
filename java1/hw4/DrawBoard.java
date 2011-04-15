import java.applet.Applet;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ComponentListener;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import javax.swing.JApplet;
import javax.swing.JFrame;

public class DrawBoard
    extends JApplet
{
    public static final long serialVersionUID = 1L;
    private Rectangle[][] squares = new Rectangle[8][8];

    private Color light  = Color.WHITE;
    private Color dark   = Color.BLACK;
    private Color border = Color.ORANGE;

    public void init() {
        // Here we handle the re-sizing of the Applet, and ignore all other
        // events returned by the ComponentListener
        this.addComponentListener(new ComponentListener() {
            public void componentHidden(ComponentEvent e) { ; }
            public void componentMoved(ComponentEvent e) { ; }
            public void componentResized(ComponentEvent e) {
                ((DrawBoard)e.getComponent()).repaint();
            }
            public void componentShown(ComponentEvent e) { ; }
        });

        Color color;
        int count = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                count++;
                if ((count % 2) == 0) {
                    color = dark;
                } else {
                    color = light;
                }
                squares[i][j] = new Rectangle(10,10,color);
            }
            count++;
        }

        setBackground(border);
    }

    public void start() {
        repaint();
    }

    public void paint(Graphics g) {
        int width   = getWidth();
        int height  = getHeight();
        int smaller = (width < height) ? width : height;
        int size    = smaller / 10; // size of squares

        BufferedImage frameBuffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics gf = frameBuffer.getGraphics();

        // Draw Board Items
        Rectangle square;
        gf.setColor(border);
        gf.fillRect(0, 0, width, height);
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                square = squares[x][y];
                square.setWidth(size);
                square.setLength(size);
                square.drawAt(gf, size+(x*size), size+(y*size));
            }
        }

        g.drawImage(frameBuffer, 0, 0, border, new ImageObserver(){
            public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {return true;}
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Draw R");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(400, 400));
        frame.setMinimumSize(new Dimension(200,200));

        JApplet applet = new DrawBoard(); 
        applet.init();
        applet.start();
        frame.add("Center", applet);
        frame.pack();
        frame.setVisible(true);
    }
}
