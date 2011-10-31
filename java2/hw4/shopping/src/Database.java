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
        throws ClassNotFoundException,
               SQLException
    {
        // Load JDBC database drivers
        Class.forName("com.mysql.jdbc.Driver");
        Class.forName("org.sqlite.JDBC");

        connection = DriverManager.getConnection(
            "jdbc:mysql://localhost/cart?" + 
            "user=cart&password=cart");
        connection.setAutoCommit(false);
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
            /*
            PreparedStatement statement = connection.prepareStatment(
                                             "SELECT id,name,description,price " +
                                             "FROM Inventory " +
                                             " WHERE name LIKE ? " +
                                             "ORDER BY name");
            statement.setString(1, "%"+search+"%");
            results = statement.executeQuery();
            */

            Statement statement = connection.createStatement();
            statement.executeQuery(
                "SELECT id,name,description,price " +
                "FROM Inventory " +
                " WHERE name LIKE '%" +search+ "%' " +
                "ORDER BY name");
            results = statement.getResultSet();
        } else {
            Statement statement = connection.createStatement();
            statement.executeQuery(
                "SELECT id,name,description,price " +
                "FROM Inventory " +
                "ORDER BY name");
            results = statement.getResultSet();
        }

        return results;
    }
}

