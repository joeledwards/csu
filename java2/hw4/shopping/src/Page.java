import java.io.PrintWriter;

public class Page
{
    protected String head = "<html>\n" +
    "<head>\n" +
    "</head>\n" +
    "<body>";

    protected String body = "";
    private boolean written = false;

    protected String tail = "</body>\n" +
    "</html>";

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

