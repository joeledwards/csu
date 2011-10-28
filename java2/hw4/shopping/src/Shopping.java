import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Formatter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class Shopping 
    extends HttpServlet
{
    // Information for the database in a raw format.
    public void doGet(HttpServletRequest  request,
                      HttpServletResponse response)
        throws IOException, ServletException
    {
        HttpSession session = request.getSession();
        String search = request.getParameter("search");
        String productId = request.getParameter("productId");
        String empty = request.getParameter("cart");

        response.setContentType("text/html");
        Page page = new Page("Shopping Simulator - Bible eBooks");

        page.append("<h1>Welcome to the Bible eBookstore</h1>");
        page.append("</br>"); 

        page.append("<form action=\"\" method=\"get\">");
        page.append("<input type=\"text\" name=\"search\" value=\"\" />");
        page.append("<input type=\"submit\" value=\"Search\" />");
        page.append("</form>");

        if (empty != null && empty.equals("empty")) {
            Enumeration<String> names = session.getAttributeNames();
            while (names.hasMoreElements()) {
                String name = names.nextElement();
                session.removeAttribute(name);
            }
        }

        try {
            Database database = new Database();
            ResultSet inventory = database.getInventory(search);

            int i = 0;
            page.append("<table>");
            while (inventory.next()) {
                // If an item has been added to the cart, store it in the
                // session information.
                if (productId != null &&
                    productId.length() > 0 &&
                    productId.equals(inventory.getString(1))) {
                    if (session.getAttribute(productId) == null) {
                        session.setAttribute(
                            productId, 
                            new Product(
                                inventory.getString(2),
                                inventory.getString(3),
                                inventory.getDouble(4)));
                    } else {
                        ((Product)session.getAttribute(productId)).quantity++;
                    }
                }

                i++;
                page.append("<tr style=\"background-color: " +(((i % 2) == 0) ? "#FFFFFF" : "#DDDDDD")+ ";\">");
                page.append("<form action=\"\" method=\"get\">");
                page.append("<input type=\"hidden\" name=\"productId\" value=\"" +inventory.getString(1)+ "\" />");
                page.append("<td><input type=\"submit\" value=\"Buy\" /></td>");
                page.append("<td>" +inventory.getString(2)+ "<td>");
                page.append("<td style=\"text-align: right;\">$" +(new Formatter()).format("%.2f", inventory.getDouble(4)).toString()+ "</td>");
                page.append("</form>");
                page.append("</tr>");
            }
            page.append("</table>");
        } catch (SQLException ex) {
            page.append("<h2>Exception Info: " +ex+ "</h2>");
        }

        page.append("</br>");
        page.append("<table>");
        page.append("<tr style=\"background-color: #CCCCCC; text-align: center;\"><td colspan=\"4\">Shopping Cart</td></tr>");
        page.append("<tr style=\"background-color: #CCCCCC;\"><td>Name</td><td>Qty.</td><td>Price</td><td>Total</td></tr>");
        double total = 0.0;
        Enumeration<String> names = session.getAttributeNames();
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            Product product = (Product)session.getAttribute(name);
            double subTotal = product.quantity * product.price;
            total += subTotal;
            page.append("<tr>");
            page.append("<td>" +product.name+ "</td>");
            page.append("<td>" +product.quantity+ "</td>");
            page.append("<td>" +(new Formatter()).format("%.2f", product.price).toString()+ "</td>");
            page.append("<td>" +(new Formatter()).format("%.2f", subTotal).toString()+ "</td>");
            page.append("</tr>");
        }
        page.append("<tr>");
        page.append("<td colspan=\"2\"></td>");
        page.append("<form action=\"\" method=\"get\">");
        page.append("<input type=\"hidden\" name=\"cart\" value=\"empty\" />");
        page.append("<input type=\"submit\" value=\"Empty Cart\" />");
        page.append("</form>");
        page.append("</td>");
        page.append("<td>Total</td>");
        page.append("<td>" +(new Formatter()).format("%.2f", total)+ "</td>");
        page.append("</tr>");
        page.append("</table>");


        // List cart items (how will this be done?) 
        // - store a cookie with the count for each id

        page.write(response.getWriter());
    }
}

