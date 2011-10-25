import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;

public class Database
{
    protected Connection conn = null;
    protected PreparedStatement stmt = null;

    public Database()
    {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            //System.out.printf("Could not load MySQL driver.", ex);
            System.exit(1);
        }

        try {
            conn = DriverManager.getConnection(
                   "jdbc:mysql://localhost/shopping?" + 
                   "user=shopping&password=shopping");
        } catch (SQLException ex) {
            System.exit(1);
        }
    }

    // - User
    // - Inventory
    // - Item
    // - Cart
    // - CartItem

    public boolean add()
    {
        try {
            stmt = conn.prepareStatement(
                "INSERT " + 
                "INTO CartItems(cart_id, item_id, quantity) " +
                "VALUES (?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS);
        } catch (SQLException ex) {
            return false;
        }
        return true;
    }
}

