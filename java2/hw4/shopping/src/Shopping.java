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
        // Start a new HTTP session which we will use to store shopping cart state
        HttpSession session = request.getSession();

        // populate the parameters associated with the request
        String search = request.getParameter("search");
        String productId = request.getParameter("productId");
        String empty = request.getParameter("cart");

        response.setContentType("text/html");
        Page page = new Page("Shopping Simulator - Bible eBooks");

        // Welcome banner
        page.append("<h1>Welcome to the Bible eBookstore</h1>");
        page.append("</br>"); 

        // The search form
        page.append("<form action=\"\" method=\"get\">");
        page.append("<input type=\"text\" name=\"search\" value=\"\" />");
        page.append("<input type=\"submit\" value=\"Search\" />");
        page.append("</form>");

        // If the 'cart' parameter is set to 'empty', we flush all of the
        // sessions attributes, which empties the cart.
        if (empty != null && empty.equals("empty")) {
            Enumeration<String> names = session.getAttributeNames();
            while (names.hasMoreElements()) {
                String name = names.nextElement();
                session.removeAttribute(name);
            }
        }

        try {
            Database database = new Database();
            // Retrieve the inventory from the database
            ResultSet inventory = database.getInventory(search);

            // Generate a table listing the inventory of items
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
                // Alternate row shading
                page.append("<tr style=\"background-color: " +(((i % 2) == 0) ? "#FFFFFF" : "#DDDDDD")+ ";\">");
                // Create a form for each item so it can be added to the shopping cart
                page.append("<form action=\"\" method=\"get\">");
                // The product's ID number is stored in a hidden input field within the form
                page.append("<input type=\"hidden\" name=\"productId\" value=\"" +inventory.getString(1)+ "\" />");
                page.append("<td><input type=\"submit\" value=\"Buy\" /></td>");
                // Display the item description
                page.append("<td>" +inventory.getString(2)+ "<td>");
                // Display the item price
                page.append("<td style=\"text-align: right;\">$" +(new Formatter()).format("%.2f", inventory.getDouble(4)).toString()+ "</td>");
                page.append("</form>");
                page.append("</tr>");
            }
            page.append("</table>");
        } catch (ClassNotFoundException ex) {
            page.append("<h2>Exception Loading Database Module: " +ex+ "</h2>");
        } catch (SQLException ex) {
            page.append("<h2>Exception Querying Database: " +ex+ "</h2>");
        }

        // Generate the shopping cart table
        page.append("</br>");
        page.append("<table>");
        page.append("<tr style=\"background-color: #CCCCCC; text-align: center;\"><td colspan=\"4\">Shopping Cart</td></tr>");
        page.append("<tr style=\"background-color: #CCCCCC;\"><td>Name</td><td>Qty.</td><td>Price</td><td>Total</td></tr>");
        double total = 0.0;
        // List all of item in the cart.
        Enumeration<String> names = session.getAttributeNames();
        while (names.hasMoreElements()) {
            // Report how many of each item is in the cart,
            // and the total cost for each item.
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
        // Report the total for the entire cart
        page.append("<td>" +(new Formatter()).format("%.2f", total)+ "</td>");
        page.append("</tr>");
        page.append("</table>");

        // Write the webpage in a single operation.
        page.write(response.getWriter());
    }
}

