import org.apache.xerces.dom.*;
import org.w3c.dom.*;
import org.w3c.dom.bootstrap.*;
import org.w3c.dom.ls.*;

public class DOM3Builder
     extends DOM3Base
{
	public void loadDocument()
    {
		try {
			System.setProperty(DOMImplementationRegistry.PROPERTY, "org.apache.xerces.dom.DOMImplementationSourceImpl");

			DOMImplementationRegistry registry = DOMImplementationRegistry.newInstance();
		    DOMImplementation domImpl = registry.getDOMImplementation("LS 3.0");
			DOMImplementationLS implLS = (DOMImplementationLS) domImpl;

			LSParser parser = implLS.createLSParser( DOMImplementationLS.MODE_SYNCHRONOUS, "http://www.w3.org/2001/XMLSchema");
			DOMConfiguration config = parser.getDomConfig();

			DOMErrorHandlerImpl errorHandler = new DOMErrorHandlerImpl();
			config.setParameter("error-handler", errorHandler);
			config.setParameter("validate", Boolean.TRUE);
			config.setParameter("schema-type", "http://www.w3.org/2001/XMLSchema");
			config.setParameter("validate-if-schema", Boolean.TRUE);
			config.setParameter("schema-location", schemaFile);

			document = parser.parseURI(xmlFile);
			System.err.println("XML document '"+xmlFile+"' loaded");

            LSSerializer serializer = implLS.createLSSerializer();
            LSOutput output = implLS.createLSOutput();

            System.err.println("Outputting XML Document");
            output.setByteStream(System.err);
            output.setEncoding("UTF-8");

            serializer.write(document, output);
            System.err.println("");
            System.err.println("");

		} catch (DOMException ex) {
			System.err.println("DOMException: " + ex.getMessage());
		} catch (ClassNotFoundException ex) {
			System.err.println("ClassNotFoundException: " + ex.getMessage());
		} catch (InstantiationException ex) {
			System.err.println("InstantiationException: " + ex.getMessage());
		} catch (IllegalAccessException ex) {
			System.err.println("IllegalAccessException: " + ex.getMessage());
		}
	}

	private class DOMErrorHandlerImpl
       implements DOMErrorHandler
    {
		public boolean handleError(DOMError error)
        {
			System.err.println("Error Message:" + error.getMessage());

			if (error.getSeverity() == DOMError.SEVERITY_WARNING) {
				return true;
			} else {
				return false;
			}
		}
	}
}
