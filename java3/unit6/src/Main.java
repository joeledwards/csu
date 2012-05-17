import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.XMLConstants;
import javax.xml.xpath.XPathExpressionException;

import joptsimple.OptionParser;
import joptsimple.OptionSet;

import org.xml.sax.SAXException;

public class Main
{
    private static final Logger logger = Logger.getLogger("Main");
    private static Handler consoleHandler;

    public static void findConsoleHandler()
    {
     // Locate the global logger's ConsoleHandler if it exists
        Logger globalLogger = Logger.getLogger("");
        for (Handler handler: globalLogger.getHandlers()) {
            if (handler instanceof ConsoleHandler) {
                consoleHandler = handler;
                break;
            }
        }

     // Ensure the global logger has an attached ConsoleHandler
     // creating one for it if necessary
        if (consoleHandler == null) {
            consoleHandler = new ConsoleHandler();
            globalLogger.addHandler(consoleHandler);
        }
    }

    public static void main(String argv[])
    {
        findConsoleHandler();
        consoleHandler.setLevel(Level.ALL);
        Logger.getLogger("").setLevel(Level.ALL);

        String books = "books.xml";

        logger.info("=== Begin = JAXB XML Generation ===");

        Inventory inv = new Inventory();
        inv.addBook("Java Knows Best",
                    "Joel Edwards",
                    "Buzuli Publishing",
                    BigInteger.valueOf(2012L),
                    BigInteger.valueOf(23449903L),
                    0.0);

        inv.addBook("JAXB Methods",
                    "Joel Edwards",
                    "Buzuli Publishing",
                    BigInteger.valueOf(2013L),
                    BigInteger.valueOf(23460249L),
                    0.0);
        inv.marshal();

        logger.info("=== End = JAXB XML Generation ===");

    }

    public static void error(String message)
    {
        System.err.println("E: " +message);
    }
}

