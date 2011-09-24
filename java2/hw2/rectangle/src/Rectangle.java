
public class Rectangle implements Comparable<Rectangle>
{
    protected int width;
    protected int height;

    public Rectangle(int width, int height)
    {
        this.width  = (width  < 11) ? 11 : (width  > 20) ? 20 : width;
        this.height = (height < 11) ? 11 : (height > 20) ? 20 : height;
    }

    public int area()
    {
        return width * height;
    }

    public int getWidth()
    {
        return width;
    }

    public int getHeight()
    {
        return height;
    }

    public int hashCode()
    {
        return  ((width  >>  8) | ((width  << 24) & 0x000000ff)) ^
                ((height >> 16) | ((width  << 16) & 0x0000ffff)) ^
                ((area() >> 24) | ((area() <<  8) & 0x00ffffff));
    }

    public boolean equals(Object o)
    {
        return compareTo((Rectangle)o) == 0;
    }

    public int compareTo(Rectangle o)
    {
        if (area() < o.area()) {
            return -1;
        }
        if (area() > o.area()) {
            return 1;
        }
        if (width < o.width) {
            return -1;
        }
        if (width > o.width) {
            return 1;
        }
        if (height < o.height) {
            return -1;
        }
        if (height > o.height) {
            return 1;
        }
        return 0;
    }
}

