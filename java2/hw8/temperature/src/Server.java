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
    private boolean verbose;
    
    public Server()
        throws IOException, ClassNotFoundException
    {
        this(0, false, 50);
    }

    public Server(int port)
        throws IOException, ClassNotFoundException
    {
        this(port, false, 50);
    }

    public Server(int port, boolean verbose)
        throws IOException, ClassNotFoundException
    {
        this(port, verbose, 50);
    }

    public Server(int port, boolean verbose, int temperature)
        throws IOException, ClassNotFoundException
    {
        this.port = port;
        this.verbose = verbose;
        this.temperature = temperature;
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

        while (true) {
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
                int sleep;
                while (true) {
                    temperature += random.nextInt(3) - 1;
                    if (verbose) {
                        System.out.printf("  Sending Temperature: %d\n", temperature);
                    }
                    outputStream.writeObject(temperature);
                    sleep = 1000 + random.nextInt(4000);
                    if (verbose) {
                        System.out.printf("  (sleeping %d milliseconds)\n", sleep);
                    }
                    Thread.sleep(sleep);
                }
            }
            catch (InterruptedException ex) {
                System.out.printf("Connection interrupted %s\n",
                                    ex.toString());
                System.exit(1);
            }
            catch (java.net.SocketException ex) {
                System.out.printf("Client disonnected\n");
            }
            catch (IOException ex) {
                System.out.printf("Connection terminated by exception %s\n",
                                    ex.toString());
                System.exit(1);
            }
        }
    }
}
