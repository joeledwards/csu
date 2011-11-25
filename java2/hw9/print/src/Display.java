import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ComponentEvent;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.RenderedImage;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.event.ChangeEvent;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;

public class Display
    extends JPanel
    implements Printable
{
    private Window parent = null;
    private File file = null;
    private boolean fresh = true;
    private boolean saved = true;

    private int barSpace = 2;
    private Random rand = new Random();

    private Color backgroundColor = Color.WHITE;
    private int height;
    private int width;
    private int barWidth = 0;
    private ArrayList<Rectangle> rectangles = new ArrayList<Rectangle>();

    public Display(Window parent)
    {
        super();
        this.parent = parent;

        this.addComponentListener(new ComponentListener() {
            public void componentHidden(ComponentEvent e) { ; }
            public void componentMoved(ComponentEvent e) { ; }
            public void componentResized(ComponentEvent e) {
                ((Display)e.getComponent()).refresh();
            }
            public void componentShown(ComponentEvent e) { ; }
        });
    }

    public void sortRectangles()
    {
        Collections.sort(rectangles);
        repaint();
    }

    public void setDefault()
    {
        width = getWidth();
        height = getHeight();

        int barCount = 5;
        barWidth = (width - ((barCount - 1) * barSpace)) / barCount;

        rectangles.clear();
        rectangles.add(new Rectangle(barWidth,  5 * height / 12, Color.red));
        rectangles.add(new Rectangle(barWidth,  7 * height / 12, Color.green));
        rectangles.add(new Rectangle(barWidth, 12 * height / 12, Color.orange));
        rectangles.add(new Rectangle(barWidth,  6 * height / 12, Color.blue));
        rectangles.add(new Rectangle(barWidth,  4 * height / 12, Color.yellow));
        repaint();
    }

    public void refresh()
    {
        int barCount = parent.getBarCount();
        if (barCount < 1) {
            return;
        }

        width = getWidth();
        height = getHeight();

        Color color = Color.red;
        int barHeight = 0;
        int minHeight = height / 8;
        int maxHeight = height - (height / 32);
        int heightDiff = maxHeight - minHeight;

        barWidth = (width - ((barCount - 1) * barSpace)) / barCount;

        rectangles.clear();
        for (int i = 0; i < barCount; i++) {
            barHeight = minHeight + rand.nextInt(heightDiff);
            switch (i % 6) {
                case 0: color = Color.red;    break;
                case 1: color = Color.green;  break;
                case 2: color = Color.orange; break;
                case 3: color = Color.blue;   break;
                case 4: color = Color.yellow; break;
                case 5: color = Color.black;  break;
            }
            rectangles.add(new Rectangle(barWidth, barHeight, color));
            
        }
        repaint();
    }

    public void paint(Graphics g) {
        generate(g);
    }

    public int print(Graphics g, PageFormat pf, int pageIndex)
    {
        generate(g);
        return Printable.PAGE_EXISTS;
    }

    public void generate(Graphics g)
    {
        int startX = 0;
        int startY = 0;
        g.clearRect(0, 0, width, height);
        Rectangle bg = new Rectangle(width, height, backgroundColor);
        bg.drawAt(g, 0, 0);
        for (Rectangle rect: rectangles) {
            startY = height - rect.getHeight();
            rect.drawAt(g, startX, startY);
            startX += barWidth + barSpace;
        }
    }

    private void setSaved(boolean saved) {
        this.fresh = false;
        this.saved = saved;
    }

    public boolean isFresh()
    {
        return fresh;
    }

    public boolean isSaved()
    {
        return saved;
    }

    public File getFile()
    {
        return file;
    }

    public void saveToFile(File file)
    {
        System.out.println("saveToFile(): " + file);
        boolean write = true;
        if (file.exists()) {
            JOptionPane confirm = new JOptionPane("The file already exists. Do you wish to overwrite it?",
                                                  JOptionPane.QUESTION_MESSAGE,
                                                  JOptionPane.YES_NO_OPTION,
                                                  UIManager.getIcon("OptionPane.questionIcon"));
            JDialog dialog = confirm.createDialog("Overwrite File");
            dialog.setVisible(true);
            Object selectedValue = confirm.getValue();
            if (selectedValue != null) {
                int value = (Integer)selectedValue;
                if (value != JOptionPane.YES_OPTION) {
                    write = false;
                }
            }
        }
        if (write) {
            File last = this.file;
            this.file = file;
            if (store()) {
                setSaved(true);
            } else {
                this.file = last;
            }
        }
    }

    public void save() {
        if (store()) {
            setSaved(true);
        }
    }

    private boolean store() {
        // Save to PNG
        boolean result = true;
        try {
            BufferedImage buffered = new BufferedImage(getWidth(), getHeight(),
                                                       BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics = buffered.createGraphics();
            generate(graphics);
            //RenderedImage rendered = new RenderedImage();
            ImageIO.write(buffered, "png", this.file);
            System.out.printf("Successfully wrote to file '%s'\n", this.file);
            graphics.dispose();
        } catch (Exception e) {
            System.out.printf("Failed to write to file '%s'\n", this.file);
            result = false;
        }
        return result;
    }
}
