import org.w3c.dom.*;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.*;
import org.w3c.dom.traversal.*;

import java.io.*;

public class DOM3Filter
     extends DOM3Base
{
    private String skipping = null;

    // Method to filter an input document and an output document.
    public void filter()
    {
        try {
            System.setProperty(DOMImplementationRegistry.PROPERTY, "org.apache.xerces.dom.DOMImplementationSourceImpl");

            DOMImplementationRegistry registry = DOMImplementationRegistry.newInstance();
            DOMImplementation domImpl = registry.getDOMImplementation("XML 3.0");
            DOMImplementationLS impl = (DOMImplementationLS) domImpl;

            LSParser parser = impl.createLSParser(DOMImplementationLS.MODE_SYNCHRONOUS, null);
            LSInput input = impl.createLSInput();

            InputStream inputStream = new FileInputStream(new File(xmlFile));
            input.setByteStream(inputStream);

            InputFilter inputFilter = new InputFilter();
            parser.setFilter(inputFilter);
            document = parser.parse(input);

            LSSerializer domWriter = impl.createLSSerializer();

            OutputFilter outputFilter = new OutputFilter();
            domWriter.setFilter(outputFilter);

            LSOutput lsOutput = impl.createLSOutput();
            lsOutput.setByteStream(System.err);

            System.err.println("\n"+"Filtered Document"+"\n");
            domWriter.write(document, lsOutput);

        } catch (IOException ex) {
            System.err.println("IOException: " + ex.getMessage());
        } catch (ClassNotFoundException ex) {
            System.err.println("ClassNotFoundException: " + ex.getMessage());
        } catch (InstantiationException ex) {
            System.err.println("InstantiationException: " + ex.getMessage());
        } catch (IllegalAccessException ex) {
            System.err.println("IllegalAccessException: " + ex.getMessage());
        }
    }

    //Input filter class
    private class InputFilter
       implements LSParserFilter
    {
        public short acceptNode(Node node)
        {
            return NodeFilter.FILTER_ACCEPT;
        }

        public int getWhatToShow()
        {
            return NodeFilter.SHOW_ELEMENT;
        }

        public short startElement(Element element)
        {
            System.err.println("Element Parsed " + element.getTagName());

            return NodeFilter.FILTER_ACCEPT;
        }
    }

    //Output filter class
    private class OutputFilter
       implements LSSerializerFilter
    {
        public short acceptNode(Node node)
        {
            try {
                Element element = (Element) node;
                if (skipping != null) {
                    if (skipping.equals(element.getTagName())) {
                        skipping = null;
                    }
                    return NodeFilter.FILTER_SKIP;
                }
                else if (element.getTagName().equals("isbn")) {
                    skipping = element.getTagName();
                    return NodeFilter.FILTER_SKIP;
                }
                else if (element.getTagName().equals("price")) {
                    skipping = element.getTagName();
                    return NodeFilter.FILTER_SKIP;
                }

            }
            catch (ClassCastException ex) {
                if (skipping != null) {
                    return NodeFilter.FILTER_SKIP;
                }
            }

            return NodeFilter.FILTER_ACCEPT;
        }

        public int getWhatToShow()
        {
            return NodeFilter.SHOW_ALL;
        }
    }
}
