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

import javax.xml.bind.annotation.adapters.HexBinaryAdapter;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.XMLConstants;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import javax.xml.xpath.XPathExpressionException;

import org.xml.sax.SAXException;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import joptsimple.OptionParser;
import joptsimple.OptionSet;

import org.xml.sax.SAXException;

import org.basex.api.xmldb.*;
import org.xmldb.api.*;
import org.xmldb.api.base.*;
import org.xmldb.api.modules.*;

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
        System.err.println("The Apache Xindice project has been discontinued.");
        System.err.println("This project uses the open source XML:DB compliant BaseX instead.");
        System.err.println("(http://basex.org   OR   https://github.com/BaseXdb)");
        System.err.println("");
        
        org.xmldb.api.base.Collection collection;
        BXDatabase database = new BXDatabase();
        try {
            collection = database.getCollection("xmldb:basex://localhost:1984/db", "admin", "admin");
            BXCollection bookCollection = new BXCollection("books", false, database);
            System.err.println("- New collection name: " + bookCollection.getName());
        } catch (org.xmldb.api.base.XMLDBException ex) {
            throw new RuntimeException("Error setting up XML:DB connection: " + ex.toString());
        }

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        File xmlFile = new File("books.xml");
        DocumentBuilder builder;
        Document document;
        try {
            builder = factory.newDocumentBuilder();
            document = builder.parse(xmlFile);
            System.err.println("- Document '" +xmlFile.getName()+ "' parsed");
        } catch (javax.xml.parsers.ParserConfigurationException ex) {
            throw new RuntimeException("Parse of '" +xmlFile.getName()+ "' failed: " + ex.toString());
        } catch (SAXException ex) {
            throw new RuntimeException("Parse of '" +xmlFile.getName()+ "' failed: " + ex.toString());
        } catch (IOException ex) {
            throw new RuntimeException("Parse of '" +xmlFile.getName()+ "' failed: " + ex.toString());
        }

        String id;
        XMLResource xmlResource;
        try {
            id = collection.createId();
            xmlResource = (XMLResource)(collection.createResource(id, "XMLResource"));
            xmlResource.setContentAsDOM(document);
            collection.storeResource(xmlResource);
            System.err.println("- DOM document added to 'books' collection");
            System.err.println("");
            System.err.println("- Collection contents:");
            xmlResource = (XMLResource)collection.getResource(id);
            System.err.println(xmlResource.getContent());
        } catch (org.xmldb.api.base.XMLDBException ex) {
            throw new RuntimeException("Error adding resource: " + ex.toString());
        }

        String path = "//book/title";
        XPathQueryService queryService;
        ResourceSet resourceSet;
        ResourceIterator iterator;
        try {
            queryService = (XPathQueryService)collection.getService("XPathQueryService", "1.0");
            resourceSet = queryService.query(path);
            iterator = resourceSet.getIterator();
            System.err.println("");
            System.err.println("- XPath '" +path+ "' results:");
            while (iterator.hasMoreResources()) {
                Resource resource = iterator.nextResource();
                System.err.println(resource.getContent());
            }
        } catch (org.xmldb.api.base.XMLDBException ex) {
            throw new RuntimeException("XPath error: " + ex.toString());
        }

        System.err.println("");
        String update;
        XUpdateQueryService updateService;
        try {
            updateService = (XUpdateQueryService)collection.getService("XUpdateQueryService", "1.0");
            System.err.println("- XUpdateQueryService: " + updateService);

            System.err.println("- Adding new book to the end...");
            update = "insert node <book author=\"Lary Niven\" year=\"1992\"><title></title></book> after /inventory/book[last()]";
            queryService.query(update);

            System.err.println("- Replacing last book's title...");
            update = "let $v := /inventory/book[last()]/title return (replace value of node $v with \"Maven with Swing\")";
            queryService.query(update);

            System.err.println("- Removing first book's title...");
            update = "delete node /inventory/book[1]/title";
            queryService.query(update);

            System.err.println("- Collection after update operations:");
            path = "/inventory";
            resourceSet = queryService.query(path);
            iterator = resourceSet.getIterator();
            System.err.println("");
            System.err.println("- XPath '" +path+ "' results:");
            while (iterator.hasMoreResources()) {
                Resource resource = iterator.nextResource();
                System.err.println(resource.getContent());
            }
        } catch (org.xmldb.api.base.XMLDBException ex) {
            throw new RuntimeException("XUpdate error: " + ex.toString());
        }

    }

    public static void error(String message)
    {
        System.err.println("E: " +message);
    }
}

