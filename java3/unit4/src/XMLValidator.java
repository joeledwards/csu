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

    private Validator validator = null;

    private boolean ready = false;
    private boolean valid = false;

 // constructor(s)
    public XMLValidator(Schema schema)
    {
        validator = schema.newValidator();
    }

    public void validate(String xmlFile)
    {
        validate(new File(xmlFile));
    }

 // read and validate the xml file
    public void validate(File xmlFile)
    {
        try {
            validator.validate(new StreamSource(xmlFile));
            logger.info("XML file '" +xmlFile+ "' passed validation.");
            valid = true;
        } catch (SAXException e) {
            logger.severe("XML file did not pass validation.\n Details: " +e);
            throw new RuntimeException("XML file failed validation.");
        } catch (IOException e) {
            logger.severe("Failed to read XML file '" +xmlFile+ "'.\n Details: " +e);
            throw new RuntimeException("Could not read XML file.");
        }
    }

    public boolean isValid() {
        return this.valid;
    }
}
