import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;

public class XMLNamespaceContext
implements NamespaceContext
{
    private Hashtable<String, String> namespaces;
    private Hashtable<String, ArrayList<String>> prefixes;
    private String defaultURI = XMLConstants.NULL_NS_URI;

    public XMLNamespaceContext()
    {
        namespaces = new Hashtable<String, String>();
        prefixes = new Hashtable<String, ArrayList<String>>();

        addPair("xml", XMLConstants.XML_NS_URI);
        addPair("cat", "catalog.schemas.csu");
        addPair("pub", "publication.schemas.csu");
        //addPair("", "config.seedscan.asl");
    }

    private void addPair(String prefix, String namespace)
    {
        namespaces.put(prefix, namespace);
        if (!prefixes.containsKey(namespace)) {
            prefixes.put(namespace, new ArrayList<String>());
        }
        prefixes.get(namespace).add(prefix);
    }

    public String getNamespaceURI(String prefix)
    {
        if (prefix == null) {
            throw new IllegalArgumentException();
        } 
        if (namespaces.containsKey(prefix)) {
            return namespaces.get(prefix);
        }
        return defaultURI;
    }

    public String getPrefix(String uri)
    {
        if (uri == null) {
            throw new IllegalArgumentException();
        } 
        if (prefixes.containsKey(uri)) {
            if (prefixes.get(uri).size() > 0) {
                return prefixes.get(uri).get(0);
            }
        }
        return null;
    }

    public Iterator getPrefixes(String uri)
    {
        if (uri == null) {
            throw new IllegalArgumentException();
        } 
        if (prefixes.containsKey(uri)) {
            return prefixes.get(uri).iterator();
        }
        return null;
    }
}
