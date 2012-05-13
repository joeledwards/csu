import java.util.logging.Logger;
import java.util.logging.Level;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class XMLParseErrorHandler
implements ErrorHandler
{
    private static final Logger logger = Logger.getLogger("XMLParseErrorHandler");

    private int warnings = 0;
    private int errors = 0;
    private int fatalErrors = 0;

    public void error(SAXParseException exception)
    {
        errors++;
        //logger.severe("Parse error: " + exception.toString());
    }

    public void fatalError(SAXParseException exception)
    {
        fatalErrors++;
        //logger.severe("Fatal error: " + exception.toString());
    }

    public void warning(SAXParseException exception)
    {
        warnings++;
        //logger.warning("Parse warning: " + exception.toString());
    }

    public int getWarningCount() {
        return warnings;
    }

    public int getErrorCount() {
        return errors;
    }

    public int getFatalErrorCount() {
        return fatalErrors;
    }
}

