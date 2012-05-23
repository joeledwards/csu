import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.XMLConstants;
import javax.xml.xpath.XPathExpressionException;

import joptsimple.OptionParser;
import joptsimple.OptionSet;

import org.xml.sax.SAXException;

import inventory.BookT;
import inventory.InventoryT;

public class Main
{
    private static final Logger logger = Logger.getLogger("Main");
    private static Handler consoleHandler;

    public static void findConsoleHandler()
    {
     // Locate the global logger's ConsoleHandler if it exists
        Logger globalLogger = Logger.getLogger("");
        for (Handler handler: globalLogger.getHandlers()) {
            if (handler instanceof ConsoleHandler) {
                consoleHandler = handler;
                break;
            }
        }

     // Ensure the global logger has an attached ConsoleHandler
     // creating one for it if necessary
        if (consoleHandler == null) {
            consoleHandler = new ConsoleHandler();
            globalLogger.addHandler(consoleHandler);
        }
    }

    public static void main(String argv[])
    throws Exception
    {
        findConsoleHandler();
        consoleHandler.setLevel(Level.ALL);
        Logger.getLogger("").setLevel(Level.INFO);

        String schemaFile = argv[0];
        String xmlFile = argv[1];

        ArrayList<File> schemas = new ArrayList<File>();
        schemas.add(new File(schemaFile));

        // Validating XML
        XMLValidator validator = new XMLValidator(schemas);
        validator.validate(xmlFile);

        // Loading database driver
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException ex) {
            String message = "Could not load database driver : " + ex.toString();
            logger.severe(message);
            throw new RuntimeException(message);
        }

        Inventory inv;
        InventoryT inventory;
        Connection connection;
        Statement  statement;
        Map<String, Class<?>> typeMap;
        ResultSet result;
        PreparedStatement prepared;
        try {
            // 1) Establishing database connection
            connection = DriverManager.getConnection("jdbc:sqlite:books.db");
            System.err.println("Database open.");
            statement = connection.createStatement();
            System.err.println("Statement created.");

            // Print database type map
            /*
            typeMap = connection.getTypeMap();
            System.err.println("Type Map:");
            for (Map.Entry<String, Class<?>> entry: typeMap.entrySet()) {
                System.err.printf("  '%s' -> %s\n", entry.getKey(), ((Class)entry.getValue()).getName());
            }
            */

            // Ensuring the database schema is in place
            statement.executeUpdate("create table if not exists books (isbn, title, publisher, year, author, price," +
                                    " unique (isbn, title, publisher, year, author, price));");
            System.err.println("Table created.");

            // Unmarshalling the source XML file
            inv = new Inventory(validator.getSchema());
            inv.unmarshal(new File(xmlFile));
            inventory = inv.getInventory();
            prepared = connection.prepareStatement("insert or replace into books values (?,?,?,?,?,?);");
        
            // Inject parsed data into database
            System.err.println("Inserting [" +inventory.getBook().size()+ "] records...");
            connection.setAutoCommit(false);
            for (BookT book: inventory.getBook()) {
                prepared.setLong(  1, book.getIsbn().longValue());
                prepared.setString(2, book.getTitle());
                prepared.setString(3, book.getPublisher());
                prepared.setLong(  4, book.getYear().longValue());
                prepared.setString(5, book.getAuthor());
                prepared.setDouble(6, book.getPrice());
                prepared.executeUpdate();
                //prepared.addBatch();
            }
            //connection.setAutoCommit(false);
            System.err.println("Books injected into database.");
            //prepared.executeBatch();
            connection.commit();
            connection.setAutoCommit(true);
            System.err.println("Values committed to database.");

            Thread.sleep(1000);

            // Query data from the database
            result = statement.executeQuery("select * from books;");
            System.err.println("Database contents:");
            inv = new Inventory(validator.getSchema());
            while (result.next()) {
                System.err.printf("  book: \"%s\" by %s [%010d], %s %d, $%.2f\n",
                                  result.getString("title"),
                                  result.getString("author"),
                                  result.getLong(  "isbn"),
                                  result.getString("publisher"),
                                  result.getLong(  "year"),
                                  result.getDouble("price"));

                // Add the book to the XML object
                inv.addBook(result.getString("title"),
                            result.getString("author"),
                            result.getString("publisher"),
                            BigInteger.valueOf(result.getLong("year")),
                            BigInteger.valueOf(result.getLong("isbn")),
                            result.getDouble("price"));

            }
            result.close();
            connection.close();

            System.err.println("");
            System.err.println("Re-assembling XML file...");
            inv.marshal();

        } catch (SQLException ex) {
            String message = "Error creating table: " + ex.toString();
            logger.severe(message);
            System.exit(1);
        }

    }

    public static void error(String message)
    {
        System.err.println("E: " +message);
    }
}

