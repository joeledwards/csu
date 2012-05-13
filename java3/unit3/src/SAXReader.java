import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.XMLConstants;

import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Document;

public class SAXReader
{
    private static final Logger logger = Logger.getLogger("SAXReader");

    private SAXParserFactory saxFactory = null;
    private SAXParser saxParser = null;
    private SAXHandler saxHandler = null;

 // constructor(s)
    public SAXReader(Schema schema)
    {
        saxFactory = SAXParserFactory.newInstance();
        saxFactory.setNamespaceAware(true);
        if (schema != null) {
            saxFactory.setValidating(true);
            saxFactory.setSchema(schema);
        }
        try {
            saxParser = saxFactory.newSAXParser();
            try {
                XMLReader xmlReader = saxParser.getXMLReader();
                xmlReader.setFeature("http://apache.org/xml/features/validation/schema", true);
            } catch (SAXNotRecognizedException ex) {
                logger.severe("Error setting XMLReader property: " + ex.toString());
            } catch (SAXException ex) {
                logger.severe("Error getting XMLReader: " + ex.toString());
            }
        }
        catch (ParserConfigurationException ex) {
            logger.severe("Failed to initialized the SAX parser: ParserConfigurationException");
        }
        catch (SAXException ex) {
            logger.severe("Failed to initialized the SAX parser: SAXException");
        }
        saxHandler = new SAXHandler();
    }

    public String read(String xmlFile)
    {
        return read(new File(xmlFile));
    }

 // read and validate the configuration
    public String read(File xmlFile)
    {
        try {
            saxParser.parse(xmlFile, this.saxHandler);
            //logger.info("Configuration file parsed.");
        } catch (SAXException e) {
            logger.severe("Could not assemble DOM from config file '" +xmlFile+ "'.\n Details: " +e);
            throw new RuntimeException("Could not assemble configuration from file.");
        } catch (IOException e) {
            logger.severe("Could not read config file '" +xmlFile+ "'.\n Details:" +e);
            throw new RuntimeException("Could not read configuration file.");
        }

        return saxHandler.getHexDigest();
    }
}
