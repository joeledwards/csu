
public class Rectangle implements Comparable<Rectangle>
{
    public static final int min = 11;
    public static final int max = 20;

    protected int width;
    protected int height;

    public Rectangle(int width, int height)
    {
        this.width  = (width<min)? min: (width>max)? max: width;
        this.height = (height<min)? min: (height>max)? max: height;
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
        return  (area() & 0xffffffff) ^
                (((width  <<  8) & 0xffffff00) |
                 ((width  >> 24) & 0x000000ff)) ^
                (((height << 16) & 0xffff0000) |
                 ((width  >> 16) & 0x0000ffff));
    }

    public boolean equals(Object o)
    {
        return compareTo((Rectangle)o) == 0;
    }

    public int compareTo(Rectangle o)
    {
        if (o == null) {
            return 1;
        }
        if (area() < o.area()) {
            return -1;
        }
        if (area() > o.area()) {
            return 1;
        }
        if (height < o.height) {
            return -1;
        }
        if (height > o.height) {
            return 1;
        }
        if (width < o.width) {
            return -1;
        }
        if (width > o.width) {
            return 1;
        }
        return 0;
    }
}

