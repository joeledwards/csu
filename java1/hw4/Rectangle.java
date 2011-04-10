import java.awt.Color;
import java.awt.Graphics;
import java.util.UUID;
        
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
    public final long ID = lastID++;

    private int width = DEFAULT_WIDTH;
    private int length = DEFAULT_LENGTH;
    private Color color = DEFAULT_COLOR;

    public Rectangle() {;}

    public Rectangle(int width) throws DimensionOutOfRangeException {
        setWidth(width);
        setLength(width);
    }

    public Rectangle(int width, int length) throws DimensionOutOfRangeException {
        setWidth(width);
        setLength(length);
    }

    public Rectangle(int width, int length, Color color) throws DimensionOutOfRangeException {
        setWidth(width);
        setLength(length);
        this.color = color;
    }

    public void setWidth(int width)
        throws DimensionOutOfRangeException
    {
        if (width < MIN_WIDTH) {
            throw new DimensionOutOfRangeException(String.format("Width is less than the minimum allowed (width=%d  MIN_WIDTH=%d)", width, MIN_WIDTH));
        }
        if (width > MAX_WIDTH) {
            throw new DimensionOutOfRangeException(String.format("Width is greater than the maximum allowed (width=%d  MAX_WIDTH=%d)", width, MAX_WIDTH));
        }
        this.width = width;
    }

    public void setLength(int length)
        throws DimensionOutOfRangeException
    {
        if (length < MIN_LENGTH) {
            throw new DimensionOutOfRangeException(String.format("Length is less than the minimum allowed (length=%d  MIN_LENGTH=%d)", length, MIN_LENGTH));
        }
        if (length > MAX_LENGTH) {
            throw new DimensionOutOfRangeException(String.format("Length is greater than the maximum allowed (length=%d  MIN_LENGTH=%d)", length, MAX_LENGTH));
        }
        this.length = length;
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

