import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

public class DOMReader
{
    private static final Logger logger = Logger.getLogger("DOMReader");

    private DocumentBuilderFactory domFactory = null;
    private DocumentBuilder        builder    = null;

    private Document doc   = null;
    private XPath    xpath = null;

    private HexBinaryAdapter hexbin = null;
    private MessageDigest digest = null;
    private XHash hash = null;

 // constructor(s)
    public DOMReader()
    {
        this.hexbin = new HexBinaryAdapter();

        this.xpath = XPathFactory.newInstance().newXPath();
        this.domFactory = DocumentBuilderFactory.newInstance();
        //domFactory.setNamespaceAware(true);

        try {
            this.builder = domFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            logger.severe("Invalid configuration for SAX parser.\n  Details: " +e);
            throw new RuntimeException("Invalid configuration for SAX parser.");
        }
    }

    public String read(String xmlFile)
    {
        return read(new File(xmlFile));
    }

    private void resetHash()
    {
        hash = new XHash();
    }

 // read and validate the configuration
    public String read(File xmlFile)
    {
        try {
            digest = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Could not create SHA-1 MessageDigest object for DOMReader.");
        }

        try {
            this.doc = builder.parse(xmlFile);
            //logger.info("Configuration file parsed.");
        } catch (SAXException e) {
            logger.severe("Could not assemble DOM from config file '" +xmlFile+ "'.\n Details: " +e);
            throw new RuntimeException("Could not assemble configuration from file.");
        } catch (IOException e) {
            logger.severe("Could not read config file '" +xmlFile+ "'.\n Details:" +e);
            throw new RuntimeException("Could not read configuration file.");
        }

        System.out.printf("Document generated: %s\n", doc.toString());
        System.out.printf("\n");

        return documentReview();
    }

    private String documentReview()
    {
        // TODO: process the document
        processNode((Node)doc.getDocumentElement());

        return getHexDigest();
    }

    public void updateDigest(String data) {
        updateDigest(data.getBytes());
    }

    public void updateDigest(byte[] data) {
        updateDigest(data, 0, data.length);
    }

    public void updateDigest(byte[] data, int offset, int length) {
        if (this.digest != null) {
            this.digest.update(data, offset, length);
        }
        try {
            System.out.printf("=== Updated Digest [%s] ===\n", this.hexbin.marshal(((MessageDigest)this.digest.clone()).digest()));
        }
        catch (CloneNotSupportedException ex) {
            ;
        }
    }

    public void updateHash(String data) {
        updateHash(data.getBytes());
    }

    public void updateHash(byte[] data) {
        updateHash(data, 0, data.length);
    }

    public void updateHash(byte[] data, int offset, int length) {
        if (this.hash != null) {
            this.hash.update(data, offset, length);
        }
        System.out.printf("=== Updated Hash [%s] ===\n", getHexHash());
    }

    public String getHexDigest()
    {
        if (this.digest != null) {
            return this.hexbin.marshal(this.digest.digest());
        }
        return "";
    }

    public String getHexHash()
    {
        if (this.hash != null) {
            return this.hexbin.marshal(this.hash.hash());
        }
        return "";
    }

    private void processNode(Node node)
    {
        switch (node.getNodeType()) {
            case Node.ELEMENT_NODE:
                Element element = (Element)node;
                String qName = element.getTagName();
                String data = node.getTextContent();
                NodeList children = node.getChildNodes();
                NamedNodeMap attributes = node.getAttributes();

                System.out.printf("Element-Start qName='%s'\n", qName);
                updateDigest(qName);

                // Reset the hash so each hash is unique across an Element's Attributes
                resetHash();
                for (int j = 0; j < attributes.getLength(); j++) {
                    processNode(attributes.item(j));
                }
                // Update the digest with the order-ignoring hash for Attributes
                if (hash.hash() != null) {
                    updateDigest(hash.hash(), 0, hash.hash().length);
                }

                // Process all child Nodes
                for (int i = 0; i < children.getLength(); i++) {
                    processNode(children.item(i));
                }

                System.out.printf("Element-End qName='%s'\n", qName);
                updateDigest(qName);
                break;
            case Node.ATTRIBUTE_NODE:
                Attr attribute = (Attr)node;
                System.out.printf("Attribute qName='%s' value='%s'\n", attribute.getName(), attribute.getValue());
                updateHash(attribute.getName());
                updateHash(attribute.getValue());
                break;
            case Node.TEXT_NODE:
                Text text = (Text)node;
                String textData = text.getWholeText().trim();
                if (textData.length() > 0) {
                    System.out.printf("Character data [%s]\n", textData);
                    updateDigest(textData);
                }
                break;
            default:
                String typeText = "UNRECOGNIZED";
                switch (node.getNodeType()) {
                    case Node.ELEMENT_NODE:
                        typeText = "ELEMENT_NODE"; break;
                    case Node.ATTRIBUTE_NODE:
                        typeText = "ATTRIBUTE_NODE"; break;
                    case Node.CDATA_SECTION_NODE:
                        typeText = "CDATA_SECTION_NODE"; break;
                    case Node.COMMENT_NODE:
                        typeText = "COMMENT_NODE"; break;
                    case Node.DOCUMENT_FRAGMENT_NODE:
                        typeText = "DOCUMENT_FRAGMENT_NODE"; break;
                    case Node.DOCUMENT_NODE:
                        typeText = "DOCUMENT_NODE"; break;
                    case Node.DOCUMENT_POSITION_CONTAINED_BY:
                        typeText = "DOCUMENT_POSITION_CONTAINED_BY"; break;
                    /* Duplicate of ELEMENT_NODE
                    case Node.DOCUMENT_POSITION_CONTAINS:
                        typeText = "DOCUMENT_POSITION_CONTAINS"; break;
                    // */
                    /* Duplicate of ELEMENT_NODE
                    case Node.DOCUMENT_POSITION_DISCONNECTED:
                        typeText = "DOCUMENT_POSITION_DISCONNECTED"; break;
                    // */
                    /* Duplicate of ELEMENT_NODE
                    case Node.DOCUMENT_POSITION_FOLLOWING:
                        typeText = "DOCUMENT_POSITION_FOLLOWING"; break;
                    // */
                    case Node.DOCUMENT_POSITION_IMPLEMENTATION_SPECIFIC:
                        typeText = "DOCUMENT_POSITION_IMPLEMENTATION_SPECIFIC"; break;
                    /* Duplicate of ELEMENT_NODE
                    case Node.DOCUMENT_POSITION_PRECEDING:
                        typeText = "DOCUMENT_POSITION_PRECEDING"; break;
                    // */
                    case Node.DOCUMENT_TYPE_NODE:
                        typeText = "DOCUMENT_TYPE_NODE"; break;
                    case Node.ENTITY_NODE:
                        typeText = "ENTITY_NODE"; break;
                    case Node.ENTITY_REFERENCE_NODE:
                        typeText = "ENTITY_REFERENCE_NODE"; break;
                    case Node.NOTATION_NODE:
                        typeText = "NOTATION_NODE"; break;
                    case Node.PROCESSING_INSTRUCTION_NODE:
                        typeText = "PROCESSING_INSTRUCTION_NODE"; break;
                    case Node.TEXT_NODE:
                        typeText = "TEXT_NODE"; break;
                }
                System.out.printf("Element type %d [%s] no instruction\n", node.getNodeType(), typeText);
        }
    }
}
