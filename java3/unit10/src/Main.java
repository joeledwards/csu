import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.Map;
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
import org.w3c.dom.*;
import org.w3c.dom.bootstrap.*;
import org.w3c.dom.ls.*;
import org.apache.xerces.dom.*;

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
    throws Exception
    {
        findConsoleHandler();
        consoleHandler.setLevel(Level.ALL);
        Logger.getLogger("").setLevel(Level.INFO);

        String schemaFile = argv[0];
        String xmlFile = argv[1];

        ArrayList<File> schemas = new ArrayList<File>();
        schemas.add(new File(schemaFile));

        // Validating XML
        XMLValidator validator = new XMLValidator(schemas);
        validator.validate(xmlFile);

		DOM3Builder builder = new DOM3Builder();
        builder.setSchemaFile("books.xsd");
        builder.setXmlFile("books.xml");
		builder.loadDocument();

        DOM3Writer writer = new DOM3Writer();
        writer.setDocument(builder.getDocument());
        writer.setSchemaFile(builder.getSchemaFile());
        writer.setXmlFile(builder.getXmlFile());
        writer.saveDocument();

        DOM3Filter filter = new DOM3Filter();
        filter.setDocument(writer.getDocument());
        filter.setSchemaFile(builder.getSchemaFile());
        filter.setXmlFile(builder.getXmlFile());
        filter.filter();
        

    }

    public static void error(String message)
    {
        System.err.println("E: " +message);
    }
}
