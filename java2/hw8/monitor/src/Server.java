import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class Server
    implements Runnable
{
    private int port;
    private int temperature;
    
    public Server()
        throws IOException, ClassNotFoundException
    {
        this(0);
    }

    public Server(int port)
        throws IOException, ClassNotFoundException
    {
        this.port = port;
    }

    public void run()
    {
        ServerSocket socketServer = null;
        Socket socket = null;
        ObjectOutputStream outputStream = null;
        Random random = new Random();
        try {
            socketServer = new ServerSocket(port);
        }
        catch (IOException ex) {
            System.out.printf("Server failed to bind to port.\n");
            System.exit(1);
        }
        port = socketServer.getLocalPort();

        System.out.printf("Server listening on port %d...\n", port);

        try {
            socket = socketServer.accept();
            outputStream = new ObjectOutputStream(socket.getOutputStream());
        }
        catch (IOException ex) {
            System.out.printf("Server failed to accept connection.\n");
            System.exit(1);
        }

        try {
            while (true) {
                temperature += random.nextInt(2) - 1;
                outputStream.writeObject(temperature);
                Thread.sleep(1000 * random.nextInt(1000));
            }
        }
        catch (InterruptedException ex) {
            System.out.printf("Connection interrupted %s\n",
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
