import org.w3c.dom.*;

public class DOM3Base
{
    protected String    xmlFile    = null;
    protected String    schemaFile = null;
    protected Document  document   = null;

 // XML File
    public void setXmlFile(String xmlFile)
    {
        this.xmlFile = xmlFile;
    }

    public String getXmlFile()
    {
        return this.xmlFile;
    }

 // Schema File
    public void setSchemaFile(String schemaFile)
    {
        this.schemaFile = schemaFile;
    }

    public String getSchemaFile()
    {
        return this.schemaFile;
    }

 // DOM Document
    public void setDocument(Document document)
    {
        this.document = document;
    }

    public Document getDocument()
    {
        return document;
    }

}
