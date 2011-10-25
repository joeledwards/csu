import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
//import org.apache.catalina.Context;

public class Shopping 
    extends HttpServlet
{
    // Information for the database in a raw format.
    public void doGet(HttpServletRequest  request,
                      HttpServletResponse response)
        throws IOException, ServletException
    {
        response.setContentType("text/html");
        Page page = new Page();
        page.append("<h1>Hello, shoppers!</h1>");

        page.write(response.getWriter());
    }
}

