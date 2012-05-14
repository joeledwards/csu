import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Hashtable;
import java.util.logging.Logger;
import java.util.logging.Level;

import javax.xml.bind.annotation.adapters.HexBinaryAdapter;

import org.xml.sax.Attributes;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Document;

public class SAXHandler
extends DefaultHandler
{
    private static final Logger logger = Logger.getLogger("SAXHandler");

    private Document dom;
    private String repr;
    private Hashtable<String, String> prefix_map;
    private Hashtable<String, String> uri_map;
    private String prefix;

    private HexBinaryAdapter hexbin = null;

    private MessageDigest digest = null;
    private XHash hash = null;
    private ErrorHandler errorHandler = null;

    public SAXHandler()
    {
        this.dom = dom;
        this.hexbin = new HexBinaryAdapter();
        this.errorHandler = (ErrorHandler)(new XMLParseErrorHandler());
    }

    public ErrorHandler getErrorHandler()
    {
        return this.errorHandler;
    }

    private void resetHash()
    {
        hash = new XHash();
    }

// Warnings and errors
    public void error(SAXParseException exception)
    throws SAXException
    {
        this.errorHandler.error(exception);
    }

    public void fatalError(SAXParseException exception)
    throws SAXException
    {
        this.errorHandler.fatalError(exception);
    }

    public void warning(SAXParseException exception)
    throws SAXException
    {
        this.errorHandler.warning(exception);
    }

// Parse operations
    public void    startDocument()
    {
        this.repr = "";
        this.prefix_map = new Hashtable<String, String>();
        this.uri_map = new Hashtable<String, String>();
        try {
            this.digest = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {;}
        logger.fine("Document-Start\n");
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
            logger.finest("DIGEST [" + this.hexbin.marshal(((MessageDigest)this.digest.clone()).digest()) + "]");
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
        logger.finest("HASH [" +getHexHash()+ "]");
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


    public void    startPrefixMapping(String prefix, String uri)
    {
        if (this.prefix_map.containsKey(uri)) {
            logger.finer("Replacing prefix for uri '" +prefix+ "' (prefix: '" +this.uri_map.get(uri)+ "' -> '" +prefix+ "')");
        }
        if (this.uri_map.containsKey(prefix)) {
            logger.finer("Replacing uri for prefix '" +prefix+ "' (uri: '" +this.uri_map.get(uri)+ "' -> '" +prefix+ "')");
        }
        this.prefix_map.put(uri, prefix);
        this.uri_map.put(prefix, uri);
        this.prefix = prefix;
    }

    public void    startElement(String uri, String localName, String qName, Attributes attributes)
    {
        resetHash();
        String prefix = this.prefix;
        if (this.prefix_map.containsKey(uri)) {
            prefix = this.prefix_map.get(uri);
            if (this.prefix != null) {
                logger.finer("Element specifies URI '" +uri+ "'. Selected prefix is '" +this.prefix_map.get(uri)+ "', URI's prefix is '" +prefix+ "'");
            }
        }
        if (prefix == null) {
            prefix = "";
        }
        logger.fine("Element-Start uri='" +uri+ "' [prefix='" +prefix+ "'] localName='" +localName+ "' qName='" +qName+ "'");
        updateDigest(qName);

        for (int i = 0; i < attributes.getLength(); i++) {
            logger.fine("Attribute qName='"+attributes.getQName(i)+"' value='"+attributes.getValue(i)+"'");
            updateHash(attributes.getQName(i));
            updateHash(attributes.getValue(i));
        }

        if (hash.hash() != null) {
            updateDigest(hash.hash(), 0, hash.hash().length);
        }
    }

    public void    skippedEntity(String name)
    {
        logger.finer("Skipping entity name='"+name+"'\n");
        //updateDigest(name);
    }

    public void    unparsedEntityDecl(String name, String publicId, String systemId, String notationName)
    {
        logger.finer("Un-parsed entity name='"+name+"'"+publicId+"' systemId='"+systemId+"' notationName='"+notationName+"'");
        //updateDigest(name);
        //updateDigest(publicId);
        //updateDigest(systemId);
        //updateDigest(notationName);
    }

    public void    ignorableWhitespace(char[] ch, int start, int length)
    {
        ;
    }

    public void    characters(char[] ch, int start, int length)
    {
        String charData = (new String(ch, start, length)).trim();
        if (charData.length() > 0) { 
            logger.fine("Character data ["+charData+"]");
            updateDigest(charData);
        }
        //updateDigest(ch, start, length);
    }

    public void    endElement(String uri, String localName, String qName)
    {
        String prefix_str = " [no matching prefix]";
        if (this.prefix_map.containsKey(uri)) {
            prefix_str = " [prefix = '" + this.prefix_map.get(uri) + "']";
        }
        logger.fine("Element-End uri='"+uri+"'"+prefix_str+" localName='"+localName+"' qName='"+qName+"'");
        updateDigest(qName);
    }

    public void    endPrefixMapping(String prefix)
    {
        String uri;
        if (this.uri_map.containsKey(prefix)) {
            uri = this.uri_map.get(prefix);
            logger.finer("Removing prefix '"+prefix+"' [uri = "+uri+"]");
            // Removing URI from map
            this.uri_map.remove(prefix);

            // Removing prefix from map
            if (this.prefix_map.containsKey(uri)) {
                this.prefix_map.remove(uri);
            }
        }
        else {
            logger.finer("Prefix '"+prefix+"' does not exist, cannot remove.");
        }

        if (this.prefix == prefix) {
            this.prefix = null;
        }
    }

    public void    endDocument()
    {
        logger.fine("Document-End\n");
    }


    public void    notationDecl(String name, String publicId, String systemId)
    {
        logger.finer("Notation Declaration name='"+name+" publicId='"+publicId+"' systemId='"+systemId+"'");
    }

    public void    processingInstruction(String target, String data)
    {
        logger.finer("Processing Instruction target='"+target+" data='"+data+"'");
    }

    /*
    public InputSource resolveEntity(String publicId, String systemId)
    {
    }
    */

    /*
    public void    setDocumentLocator(Locator locator)
    {
    }
    */
}
