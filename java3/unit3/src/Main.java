import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.XMLConstants;

import joptsimple.OptionParser;
import joptsimple.OptionSet;

import org.xml.sax.SAXException;

public class Main
{
    private static final Logger logger = Logger.getLogger("Main");

    public static void main(String argv[])
    {
        Schema schema = null;
        String domDigest;
        String saxDigest;

        //*
        StreamSource[] schemas = new StreamSource[] { new StreamSource(new File("Publication.xsd")),
                                                      new StreamSource(new File("Catalog.xsd")) };
        // */
        /*
        StreamSource[] schemas = new StreamSource[] { new StreamSource(new File("Catalog.xsd")) };
        // */
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

        try {
            schema = schemaFactory.newSchema(schemas);
        } catch (SAXException e) {
            logger.severe("Could not read validation files '" +schemas+ "'.\n  Details: " +e);
            throw new RuntimeException("Could not read validation file.");
        }

        String catalog = "catalog.xml";

        XMLValidator validator = new XMLValidator(schema);
        DOMReader dom = new DOMReader(schema);
        SAXReader sax = new SAXReader(schema);

        System.err.printf("=== START VALIDATION ====================\n");
        boolean valid = false;
        validator.validate(catalog);
        valid = true;
        System.err.printf("=== STOP VALIDATION ====================\n");
        System.err.printf("\n");

        // DOM parser and analysis
        System.err.printf("=== START DOM ASSEMBLY ====================\n");
        domDigest = dom.read(catalog);
        System.err.printf("=== STOP DOM ASSEMBLY ====================\n");
        System.err.printf("\n");

        // SAX perser and analysis
        System.err.printf("=== START SAX PARSE ====================\n");
        saxDigest = sax.read(catalog);
        System.err.printf("=== STOP SAX PARSE ====================\n");
        System.err.printf("\n");

        // Document parse mode comparison
        System.err.printf("DOMReader SHA-1 Digest [%s]\n", domDigest);
        System.err.printf("SAXReader SHA-1 Digest [%s]\n", saxDigest);
        System.err.printf("\n");

        System.err.printf("Validator Validation Result: %s\n", valid ? "VALID" : "INVALID");
        System.err.printf("DOMReader Validation Result: %s\n", dom.isValid() ? "VALID" : "INVALID");
        System.err.printf("SAXReader Validation Result: %s\n", sax.isValid() ? "VALID" : "INVALID");
    }

    public static void error(String message)
    {
        System.err.println("E: " +message);
    }
}

