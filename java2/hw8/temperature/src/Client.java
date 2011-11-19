import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.ArrayList;

public class Client
    implements Runnable
{
    private InetAddress address;
    private int port;
    private boolean verbose;
    private ArrayList<TempPoint> temperatureQueue;
    
    public Client(String address, int port)
        throws IOException, UnknownHostException
    {
        this(address, port, false);
    }

    public Client(String address, int port, boolean verbose)
        throws IOException, UnknownHostException
    {
        this.address = InetAddress.getByName(address);
        this.port = port;
        this.verbose = verbose;
        temperatureQueue = new ArrayList<TempPoint>();
    }

    public void run()
    {
        Socket socket = null;
        ObjectInputStream inputStream = null;
        try {
            socket = new Socket(address, port);
            inputStream = new ObjectInputStream(socket.getInputStream());
        }
        catch (IOException ex) {
            System.out.printf("Failed to establish connection to %s:%d\n",
                                address.toString(), port);
            System.exit(1);
        }

        System.out.printf("Connection established to %s:%d\n",
                            address.toString(), port);

        Integer temperature;
        try {
            while (true) {
                // Add new temperature data point
                temperature = (Integer)inputStream.readObject();
                TempPoint newPoint = new TempPoint(temperature, (new GregorianCalendar()).getTimeInMillis());
                temperatureQueue.add(newPoint);

                // Remove old data points (older than 60 seconds)
                while (true) {
                    TempPoint point;
                    try {
                        point = temperatureQueue.get(0);
                    } catch (NoSuchElementException ex) {
                        break;
                    }
                    if ((newPoint.timestamp - point.timestamp) < 60000) {
                        break;
                    }
                    temperatureQueue.remove(0);
                }
                if (verbose) {
                    System.out.printf("%d data points in queue.\n", temperatureQueue.size());
                }

                long firstTime = 0;
                long lastTime = newPoint.timestamp - 60000;
                long averageTemp = 0;
                long milliseconds = 0;
                long total = 0;
                long diff = 0;
                for (TempPoint point: temperatureQueue) {
                    if (firstTime == 0) {
                        firstTime = point.timestamp;
                    }
                    diff = point.timestamp - lastTime;
                    if (diff > 6000) {
                        diff = 6000;
                    }
                    total += diff * point.temperature;
                    milliseconds += diff;
                    lastTime = point.timestamp;
                    if (verbose) {
                        System.out.printf("  timestamp: %d\n", point.timestamp);
                        System.out.printf("    temperature: %d\n", point.temperature);
                        System.out.printf("    time-diff:   %d\n", diff);
                        System.out.printf("    weighted:    %d\n", diff * point.temperature);
                    }
                }
                averageTemp = total / milliseconds;
                System.out.printf("Average Temperature: %d\n", averageTemp);
                if (verbose) {
                    System.out.printf("  Weighted Total:   %d\n", total);
                    System.out.printf("  Data Point Range: %d ms\n", lastTime - firstTime);
                    System.out.printf("  Total Time:       %d ms\n\n", milliseconds);
                }
            }
        }
        catch (EOFException ex) {
            System.out.printf("Connection closed by server\n");
        }
        catch (ClassNotFoundException ex) {
            System.out.printf("Connection terminated by exception %s\n",
                                ex.toString());
            System.exit(1);
        }
        catch (IOException ex) {
            System.out.printf("Connection terminated by exception %s\n",
                                ex.toString());
            System.exit(1);
        }

    }
}

