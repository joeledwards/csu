import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import joptsimple.OptionParser;
import joptsimple.OptionSet;

/**
 * @author Joel D. Edwards <joeledwards@gmail.com>
 *
 * 
 */
public class Main
{
    public static String programName = "";
    public static OptionParser parser = null;

    public static void main(String argv[])
    {
        int port = 24642;
        int temperature = 50;
        boolean verbose = false;
        String address = null;
        Runnable core = null;

        parser = new OptionParser() {
            {
                accepts("h", "Prints this help message.");
                accepts("p", "Connect to this port as a client. Bind to this port as a server.").withRequiredArg().ofType(Integer.class);
                accepts("s", "Connect to this server as a client.").withRequiredArg().ofType(String.class);
                accepts("t", "Start at this temperature (only affects the server).").withRequiredArg().ofType(Integer.class);
                accepts("v", "Set to verbose mode.");
            }
        };

        programName = "monitor.jar";
        OptionSet options = parser.parse(argv);

        if (options.has("h")) {
            usage();
        }
        if (options.has("p")) {
            port = (Integer)options.valueOf("p");
        }
        if (options.has("t")) {
            temperature = (Integer)options.valueOf("t");
        }
        if (options.has("v")) {
            verbose = true;
        }
        if (options.has("s")) {
            address = (String)options.valueOf("s");
            try {
                core = new Client(address, port, verbose);
            } catch (IOException ex) {
                System.out.println("Failed to create client.");
                System.exit(1);
            }
        }
        else {
            try {
                core = new Server(port, verbose, temperature);
            } catch (ClassNotFoundException ex) {
                System.out.println("Class not found.");
                System.exit(1);
            } catch (IOException ex) {
                System.out.println("Failed to create client.");
                System.exit(1);
            }
        }

        core.run();
    }

    public static void usage()
    {
        System.out.println("Usage: " +programName+ " [Options]\n");
        try {
            parser.printHelpOn(System.out);
        }
        catch (IOException ex) {
            System.out.println("Failed to print usage due to an I/O error. Details: " +ex);
        }
        System.exit(1);
    }

    public static void usage(String message)
    {
        error(message);
        usage();
    }

    public static void error(String message)
    {
        System.err.println("E: " +message);
    }
}

