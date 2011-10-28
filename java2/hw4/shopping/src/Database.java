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
    protected Connection connection = null;
    protected Statement statement = null;

    public Database()
    {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException ex) {
            System.exit(1);
        }

        try {
            connection = DriverManager.getConnection(
                "jdbc:mysql://localhost/cart?" + 
                "user=cart&password=cart");
        } catch (SQLException ex) {
            System.exit(1);
        }
    }

    public ResultSet getInventory()
        throws SQLException
    {
        return getInventory(null);
    }

    public ResultSet getInventory(String search)
        throws SQLException
    {
        ResultSet results = null;
        String searchString = "";

        if ((search != null) && (search.length() > 0)) {
            searchString = " WHERE name LIKE '%" +search+ "%' ";
        }

        statement = connection.createStatement();
        statement.executeQuery("SELECT id,name,description,price " +
                               "FROM Inventory " +
                                searchString +
                               "ORDER BY name");
        results = statement.getResultSet();

        return results;
    }
}

