import org.w3c.dom.*;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.*;

import javax.xml.parsers.*;

public class DOM3Writer
     extends DOM3Base
{
    public void saveDocument()
    {
        try {
            DocumentBuilderFactory factory;
            DocumentBuilder builder;
            Element inventory;

            if (document == null) {
                factory = DocumentBuilderFactory.newInstance();
                builder = factory.newDocumentBuilder();
                document = builder.newDocument();

                inventory = document.createElement("inventory");
                document.appendChild(inventory);
            }
            else {
                inventory = document.getDocumentElement();
            }

            Element book = document.createElement("book");
            book.setAttribute("year", "2012");
            book.setAttribute("author", "Joel Edwards");
            inventory.appendChild(book);

            Element title = document.createElement("title");
            title.appendChild(document.createTextNode("DOM3 for Java"));
            book.appendChild(title);

            Element publisher = document.createElement("publisher");
            publisher.appendChild(document.createTextNode("Buzuli Publishing"));
            book.appendChild(publisher);

            Element isbn = document.createElement("isbn");
            isbn.appendChild(document.createTextNode("0234023966"));
            book.appendChild(isbn);

            Element price = document.createElement("price");
            price.appendChild(document.createTextNode("13.99"));
            book.appendChild(price);

            System.setProperty(DOMImplementationRegistry.PROPERTY, "org.apache.xerces.dom.DOMImplementationSourceImpl");

            DOMImplementationRegistry registry = DOMImplementationRegistry.newInstance();
            DOMImplementation domImpl = registry.getDOMImplementation("LS 3.0");
            DOMImplementationLS implLS = (DOMImplementationLS) domImpl;

            LSSerializer serializer = implLS.createLSSerializer();
            LSOutput output = implLS.createLSOutput();

            System.err.println("Outputting XML Document");
            output.setByteStream(System.err);
            output.setEncoding("UTF-8");

            serializer.write(document, output);
            System.err.println("\n\n"+"Outputting the journal Node"+"\n");

            serializer.write(book, output);
            String nodeString = serializer.writeToString(book);

        } catch (ClassNotFoundException ex) {
			System.err.println("ClassNotFoundException: " + ex.getMessage());
        } catch (IllegalAccessException ex) {
			System.err.println("IllegalAccessException: " + ex.getMessage());
        } catch (InstantiationException ex) {
			System.err.println("InstantiationException: " + ex.getMessage());
        } catch (ParserConfigurationException ex) {
			System.err.println("ParserConfigurationException: " + ex.getMessage());
        }
    }
}
