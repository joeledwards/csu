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

import org.apache.xmlbeans.XmlCursor;
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
        Inventory inventory = null;
        XMLValidator validator = null;

        findConsoleHandler();
        consoleHandler.setLevel(Level.ALL);
        Logger.getLogger("").setLevel(Level.INFO);

        String schemaFile = argv[0];
        String xmlFile = argv[1];

        ArrayList<File> schemas = new ArrayList<File>();
        schemas.add(new File(schemaFile));
        validator = new XMLValidator(schemas);
        validator.validate(xmlFile);


        logger.info("=== Begin = XMLBeans Marshal ===");
        inventory = new Inventory(validator.getSchema());
        inventory.addBook(  "Snow Cash",
                            "Neal Stephenson",
                            "Spectra",
                            BigInteger.valueOf(2000L),
                            BigInteger.valueOf(553380958L),
                            14.95);
        inventory.addBook(  "Burning Tower",
                            "Larry Niven",
                            "Pocket",
                            BigInteger.valueOf(2005L),
                            BigInteger.valueOf(743416910L),
                            5.99);
        inventory.addBook(  "Zodiac",
                            "Neal Stephenson",
                            "Spectra",
                            BigInteger.valueOf(1995L),
                            BigInteger.valueOf(553573862L),
                            7.50);
        inventory.marshal();
        logger.info("=== End = XMLBeans Marshal ===");

        System.err.println("");
        
        logger.info("=== Begin = XMLBeans Unmarshal ===");
        inventory = new Inventory(validator.getSchema());
        inventory.unmarshal(new File(xmlFile));
        logger.info("=== End = XMLBeans Unmarshal ===");

        System.err.println("");
        
        XmlCursor cursor = inventory.getCursor();
        XmlCursor result;
        boolean found;

        logger.info("=== Begin = XQuery First Node ===");
        result = cursor.execQuery("/inventory/book[0]/@author");
        System.err.println("The first author's name is " + result.getTextValue());
        logger.info("=== End = XQuery First Node ===");

        logger.info("=== Begin = XPath Titles ===");
        cursor.selectPath("/inventory/book/title");
        cursor.toStartDoc();
        found = cursor.toFirstChild();
        while (found) {
            System.err.println("\"" + cursor.getTextValue() + "\"");
            found = cursor.toNextSibling();
        }
        logger.info("=== End = XPath Titles ===");

        logger.info("=== Begin = New Book ===");
        logger.info("=== End = New Book ===");
    }

    public static void error(String message)
    {
        System.err.println("E: " +message);
    }
}

