import java.awt.Desktop;

import java.io.File;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import java.lang.reflect.Method;

import java.net.URI;
import java.net.URISyntaxException;

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

import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
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

        BufferedOutputStream output = null;
        File htmlFile = new File("books.html");
        try {
            output = new BufferedOutputStream(new DataOutputStream(new FileOutputStream(htmlFile)));
        } catch (FileNotFoundException ex) {
            logger.severe("Could not create file '" +htmlFile.getName()+ "'");
            System.exit(1);
        }

        Source xmlSource  = new StreamSource("books.xml");
        Source xsltSource = new StreamSource("convert.xslt");
        Result result     = new StreamResult(output);

        Transformer transformer = null;
        TransformerFactory factory = TransformerFactory.newInstance();
        try {
            transformer = factory.newTransformer(xsltSource);
            logger.info("=== START TRANSFORMATION ====================");
            transformer.transform(xmlSource, result);
            logger.info("HTML file '" +htmlFile.getName()+ "' assembled.");
            logger.info("=== STOP TRANSFORMATION ====================");
            logger.info("Opening assembled file");

            Desktop.getDesktop().browse(new URI(htmlFile.getAbsolutePath()));
        } catch (URISyntaxException ex) {
            logger.severe("URI Syntax Exception: " + ex.toString());
        } catch (IOException ex) {
            logger.severe("I/O Exception: " + ex.toString());
        } catch (TransformerConfigurationException ex) {
            logger.severe("XSLT Transformer Factory Configuration Exception: " + ex.toString());
        } catch (TransformerException ex) {
            logger.severe("XSLT Transformation Exception: " + ex.toString());
        }
    }

    public static void error(String message)
    {
        System.err.println("E: " +message);
    }
}

