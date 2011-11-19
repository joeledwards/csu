
public class TempPoint
    implements Comparable
{
    public static final long serialVersionUID = 1L;
    public int temperature;
    public long timestamp;

    public TempPoint(int temperature, long timestamp)
    {
        this.temperature = temperature;
        this.timestamp = timestamp;
    }

    public int compareTo(Object obj)
    {
        TempPoint point = ((TempPoint)obj);
        if (timestamp > point.timestamp) {
            return 1;
        }
        if (timestamp < point.timestamp) {
            return -1;
        }
        return 0;
    }
}

