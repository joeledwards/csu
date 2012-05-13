import java.util.logging.Logger;
import java.util.logging.Level;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXParseException;

public class XMLParseErrorHandler
implements ErrorHandler
{
    private static final Logger logger = Logger.getLogger("XMLParseErrorHandler");

    public void error(SAXParseException exception)
    {
        logger.severe("Parse error: " + exception.toString());
    }

    public void fatalError(SAXParseException exception)
    {
        logger.severe("Severe parse error: " + exception.toString());
    }

    public void warning(SAXParseException exception)
    {
        logger.warning("Parse warn condition: " + exception.toString());
    }
}

