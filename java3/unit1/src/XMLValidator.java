import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;

import javax.xml.bind.annotation.adapters.HexBinaryAdapter;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import javax.xml.XMLConstants;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.xml.sax.SAXException;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Document;

public class XMLValidator
{
    private static final Logger logger = Logger.getLogger("XMLValidator");

    private DocumentBuilderFactory  domFactory      = null;
    private SchemaFactory           schemaFactory   = null;
    private DocumentBuilder         builder         = null;

    private Schema    schema    = null;
    private Validator validator = null;
    private Document  doc       = null;
    private XPath     xpath     = null;

    private boolean validate = false;
    private boolean ready    = false;

 // constructor(s)
    public XMLValidator(StreamSource[] schemas)
    {
        xpath = XPathFactory.newInstance().newXPath();
        xpath.setNamespaceContext(new XMLNamespaceContext());
        domFactory = DocumentBuilderFactory.newInstance();
        domFactory.setNamespaceAware(true);

        if (schemas != null) {
            schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            try {
                schema = schemaFactory.newSchema(schemas);
            } catch (SAXException e) {
                logger.severe("Could not read validation files '" +schemas+ "'.\n  Details: " +e);
                throw new RuntimeException("Could not read validation file.");
            }
            validate = true;
        }

        try {
            builder = domFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            logger.severe("Invalid configuration for SAX parser.\n  Details: " +e);
            throw new RuntimeException("Invalid configuration for SAX parser.");
        }
    }

    public void validate(String xmlFile)
    {
        validate(new File(xmlFile));
    }

 // read and validate the xml file
    public void validate(File xmlFile)
    {
        if (validate) {
            Validator validator = schema.newValidator();
            try {
                validator.validate(new StreamSource(xmlFile));
                logger.info("XML file '" +xmlFile+ "' passed validation.");
            } catch (SAXException e) {
                logger.severe("XML file did not pass validation.\n Details: " +e);
                throw new RuntimeException("XML file failed validation.");
            } catch (IOException e) {
                logger.severe("Failed to read XML file '" +xmlFile+ "'.\n Details: " +e);
                throw new RuntimeException("Could not read XML file.");
            }
        }

        try {
            doc = builder.parse(xmlFile);
            logger.info("DOM assembled from XML file '" +xmlFile+ "'.");
        } catch (SAXException e) {
            logger.severe("Could not assemble DOM from XML file '" +xmlFile+ "'.\n Details: " +e);
            throw new RuntimeException("Could not assemble DOM from XML file.");
        } catch (IOException e) {
            logger.severe("Could not read XML file '" +xmlFile+ "'.\n Details:" +e);
            throw new RuntimeException("Could not read XML file.");
        }
    }
}
