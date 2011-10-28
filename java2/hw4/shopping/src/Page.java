import java.io.PrintWriter;

public class Page
{
    protected String head = null;
    protected String body = null;
    protected String tail = null;

    private boolean written = false;

    public Page(String title)
    {
        head =  "<html><head><title>" +title+ "</title></head><body>";
        body = "";
        tail =  "</body></html>";
    }

    public boolean write(PrintWriter out)
    {
        // A page can only be written once.
        if (written) {
            return false;
        }

        out.println(head);
        out.println(body);
        out.println(tail);
        written = true;
        return true;
    }

    public void append(String content) {
        body += content;
    }
}

