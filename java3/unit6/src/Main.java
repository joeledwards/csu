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

import inventory.BookT;
import inventory.InventoryT;

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
        Logger.getLogger("").setLevel(Level.INFO);

        String schemaFile = argv[0];
        String xmlFile = argv[1];

        ArrayList<File> schemas = new ArrayList<File>();
        schemas.add(new File(schemaFile));

        logger.info("=== Begin = JAXB Unmarshal ===");
        XMLValidator validator = new XMLValidator(schemas);
        validator.validate(xmlFile);
        Inventory inventory = new Inventory(validator.getSchema());
        inventory.unmarshal(new File(xmlFile));
        logger.info("=== End = JAXB Unmarshal ===");

        logger.info("=== Begin = JAXB XML Marshal ===");
        inventory = new Inventory(validator.getSchema());
        inventory.addBook(  "Java Knows Best",
                            "Joel Edwards",
                            "Buzuli Publishing",
                            BigInteger.valueOf(2012L),
                            BigInteger.valueOf(23449903L),
                            0.0);
        inventory.addBook(  "JAXB Methods",
                            "Joel Edwards",
                            "Buzuli Publishing",
                            BigInteger.valueOf(2013L),
                            BigInteger.valueOf(23460249L),
                            0.0);
        inventory.marshal();
        logger.info("=== End = JAXB XML Marshal ===");

    }

    public static void error(String message)
    {
        System.err.println("E: " +message);
    }
}

