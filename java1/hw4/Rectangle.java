import java.awt.Color;
import java.awt.Graphics;
        
public class Rectangle
{
    public static final int MIN_WIDTH = 10;
    public static final int MAX_WIDTH = 100;

    public static final int MIN_LENGTH = 10;
    public static final int MAX_LENGTH = 100;

    public static final int DEFAULT_WIDTH  = 10;
    public static final int DEFAULT_LENGTH = 10;
    public static final Color DEFAULT_COLOR = Color.BLACK;

    private static int lastID = 1;
    public final int ID = lastID++;

    private int width = DEFAULT_WIDTH;
    private int length = DEFAULT_LENGTH;
    private Color color = DEFAULT_COLOR;

    public Rectangle() {;}

    public Rectangle(int width) {
        setWidth(width);
        setLength(width);
    }

    public Rectangle(int width, int length) {
        setWidth(width);
        setLength(length);
    }

    public Rectangle(int width, int length, Color color) {
        setWidth(width);
        setLength(length);
        this.color = color;
    }

    public Rectangle clone() {
        return new Rectangle(this.width, this.length, this.color);
    }

    public void setWidth(int width) {
        if (width < MIN_WIDTH) {
            this.width = MIN_WIDTH;
        } 
        else if (width > MAX_WIDTH) {
            this.width = MAX_WIDTH;
        } 
        else {
            this.width = width;
        }
    }

    public void setLength(int length) {
        if (length < MIN_LENGTH) {
            this.length = MIN_LENGTH;
        } 
        else if (length > MAX_LENGTH) {
            this.length = MAX_LENGTH;
        } 
        else {
            this.length = length;
        }
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void drawAt(Graphics g, int x, int y) {
        Color lastColor = g.getColor();
        g.setColor(color);
        g.fillRect(x, y, width, length);
        g.setColor(lastColor);
    }

    public boolean equals(Rectangle other) {
        if (width != other.width)
            return false;
        if (length != other.length)
            return false;
        if (!(color.equals(other.color)))
            return false;
        return true;
    }

    public int computeArea() {
        return width * length;
    }

    public int getWidth() {
        return width;
    }

    public int getLength() {
        return length;
    }

    public Color getColor() {
        return color;
    }
}

