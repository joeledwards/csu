import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Hashtable;

import javax.xml.bind.annotation.adapters.HexBinaryAdapter;

import org.xml.sax.Attributes;
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
    private Document dom;
    private String repr;
    private Hashtable<String, String> prefix_map;
    private Hashtable<String, String> uri_map;
    private String prefix;

    private HexBinaryAdapter hexbin = null;

    private MessageDigest digest = null;
    private XHash hash = null;

    public SAXHandler()
    {
        this.dom = dom;
        this.hexbin = new HexBinaryAdapter();
    }

    private void resetHash()
    {
        hash = new XHash();
    }

// Warnings and errors
    public void    warning(SAXParseException e)
    {
        System.out.printf("Warning Condition!\n");
    }

    public void    error(SAXParseException e)
    {
        System.out.printf("Error Condition!!\n");
    }

    public void    fatalError(SAXParseException e)
    {
        System.out.printf("Fatal Condition!!!\n");
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
        System.out.printf("Document-Start\n");
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


    public void    startPrefixMapping(String prefix, String uri)
    {
        if (this.prefix_map.containsKey(uri)) {
            System.out.printf("Replacing prefix for uri '%s' (prefix: '%s' -> '%s')\n",
                              prefix, this.uri_map.get(uri), prefix);
        }
        if (this.uri_map.containsKey(prefix)) {
            System.out.printf("Replacing uri for prefix '%s' (uri: '%s' -> '%s')\n",
                              prefix, this.uri_map.get(uri), prefix);
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
                System.out.printf("Element specifies URI '%s'. Selected prefix is '%s', URI's prefix is '%s'.\n",
                                  uri, this.prefix_map.get(uri), prefix);

            }
        }
        if (prefix == null) {
            prefix = "";
        }
        System.out.printf("Element-Start uri='%s' [prefix='%s'] localName='%s' qName='%s'\n",
                          uri, prefix, localName, qName);
        updateDigest(qName);

        for (int i = 0; i < attributes.getLength(); i++) {
            System.out.printf("Attribute qName='%s' value='%s'\n", attributes.getQName(i), attributes.getValue(i));
            updateHash(attributes.getQName(i));
            updateHash(attributes.getValue(i));
        }

        if (hash.hash() != null) {
            updateDigest(hash.hash(), 0, hash.hash().length);
        }
    }

    public void    skippedEntity(String name)
    {
        System.out.printf("Skipping entity name='%s'\n", name);
        //updateDigest(name);
    }

    public void    unparsedEntityDecl(String name, String publicId, String systemId, String notationName)
    {
        System.out.printf("Un-parsed entity name='%s publicId='%s' systemId='%s' notationName='%s'\n", name, publicId, systemId, notationName);
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
            System.out.printf("Character data [%s]\n", charData);
            updateDigest(charData);
        }
        //updateDigest(ch, start, length);
    }

    public void    endElement(String uri, String localName, String qName)
    {
        String prefix_str = " [no matching prefix]";
        if (this.prefix_map.containsKey(uri)) {
            prefix_str = " [prefix = '" + this.prefix_map + "']";
        }
        System.out.printf("Element-End uri='%s'%s localName='%s' qName='%s'\n",
                          uri, prefix_str, localName, qName);
        updateDigest(qName);
    }

    public void    endPrefixMapping(String prefix)
    {
        String uri;
        if (this.uri_map.containsKey(prefix)) {
            uri = this.uri_map.get(prefix);
            System.out.printf("Removing prefix '%s' [uri = %s]\n", prefix, uri);
            // Removing URI from map
            this.uri_map.remove(prefix);

            // Removing prefix from map
            if (this.prefix_map.containsKey(uri)) {
                this.prefix_map.remove(uri);
            }
        }
        else {
            System.out.printf("Prefix '%s' does not exist, cannot remove.\n", prefix);
        }

        if (this.prefix == prefix) {
            this.prefix = null;
        }
    }

    public void    endDocument()
    {
        System.out.printf("Document-End\n");
    }


    public void    notationDecl(String name, String publicId, String systemId)
    {
        System.out.printf("Notation Declaration name='%s publicId='%s' systemId='%s'\n", name, publicId, systemId);
    }

    public void    processingInstruction(String target, String data)
    {
        System.out.printf("Processing Instruction target='%s data='%s'\n", target, data);
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
