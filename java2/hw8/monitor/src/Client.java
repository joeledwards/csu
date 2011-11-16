import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client
    implements Runnable
{
    private int port;
    private InetAddress address;
    
    public Client()
        throws IOException, UnknownHostException
    {
        this(0,"localhost");
    }

    public Client(int port)
        throws IOException, UnknownHostException
    {
        this(port,"localhost");
    }

    public Client(String address)
        throws IOException, UnknownHostException
    {
        this(0,address);
    }

    public Client(int port, String address)
        throws IOException, UnknownHostException
    {
        this.port = port;
        this.address = InetAddress.getByName(address);

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
                temperature = (Integer)inputStream.readObject();
                System.out.printf("Temperature: %d\n", temperature);
            }
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

