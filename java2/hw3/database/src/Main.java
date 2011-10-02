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

public class Main
{
    // Information for the database in a raw format.
    public static final String contacts[] = {
        "Rev.,Joel,David,Edwards,,555.728.2556,Mobile,555.873.2912,Work,555.823.0449,Home",
        ",Robert,,Paulson,,555.471.2208,Work,555.143.3444,Mobile",
        ",Charles,Daniel,Xavier,Ph.D,555.226.2818,Work,555.853.2266,Mobile"
    };

    public static void main(String argv[])
    {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet results = null;
        ResultSet ids = null;
        String value = null;

        // Load the MySQL JDBC driver
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            System.out.printf("Could not load MySQL driver.", ex);
            System.exit(1);
        }

        // Print a list of the available drivers
        System.out.printf("Available Drivers:\n");
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        Driver d = null;
        while (drivers.hasMoreElements()) {
            d = drivers.nextElement();
            System.out.printf("  %s\n", d.toString());
        }
        System.out.print("\n");

        try {
            // Establish a connection to the database
            System.out.print("1.a) Connecting to the database...");
            conn = DriverManager.getConnection(
                   "jdbc:mysql://localhost/test?" + 
                   "user=test&password=test");
            System.out.print("Done.\n");

            // Get rid of the tables from the last run
            // but only if they exist
            System.out.print("1.b) Removing any test tables...");
            stmt = conn.prepareStatement(
                "DROP TABLE IF EXISTS Contact, Phone;");
            stmt.execute();
            System.out.print("Done.\n");


            // Create the fist table
            System.out.print("2)   Creating the first table (Contact)...");
            stmt = conn.prepareStatement(
                "CREATE TABLE IF NOT EXISTS Contact (" + 
                    "id INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY," +
                    "first VARCHAR(64) NOT NULL," +
                    "last VARCHAR(64) NOT NULL," +
                    "middle VARCHAR(64)," +
                    "prefix VARCHAR(32)," +
                    "suffix VARCHAR(32)," +
                    "UNIQUE (first, last, middle)" +
                ");");
            stmt.execute();
            System.out.print("Done.\n");


            // Popuate the first table with data
            System.out.print("3)   Populating the first table (Contact)...");
            HashMap<Integer,HashMap<String,String>> numbers;
            numbers = new HashMap<Integer,HashMap<String,String>>();

            // Generate a prepared statement which can be re-used
            // for each insert operation.
            stmt = conn.prepareStatement(
                "INSERT " + 
                "INTO Contact(prefix, first, middle, last, suffix) " +
                "VALUES (?, ?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS);

            // Loop through the raw data, extract the person's name
            // and their phone numbers
            for (String info: contacts) {
                // Ensure that we have sufficient data to 
                // populate the person's name
                String[] parts = info.split(",");
                if (parts.length < 5) {
                    continue;
                }

                // Track the number of values processed for this contact
                int processed = 0;
                // Add individual column values to the prepared statement
                for (int i = 0; i < 5; i++) {
                    value = parts[i].trim();
                    stmt.setString(i+1, value);
                }
                stmt.executeUpdate();

                // Extract the id for the inserted row
                ids = stmt.getGeneratedKeys();
                ids.next();
                int id = ids.getInt(1);
                processed = 5;
                String number;
                String desc;

                // Pair the stored ID with the remaining values 
                // from this contact.
                HashMap<String,String> phones;
                phones = new HashMap<String,String>();
                while ((processed + 2) <= parts.length) {
                    number = parts[processed];
                    desc = parts[processed+1];
                    phones.put(number, desc);
                    processed += 2;

                }
                numbers.put(id, phones);
            }
            System.out.print("Done.\n");


            // Dump the contents of the fist table to verify
            // that it was correctly created and populated
            System.out.printf("4)   Checking the table's contents:\n");
            stmt = conn.prepareStatement("SELECT * FROM Contact;");
            results = stmt.executeQuery();
            int row = 0;
            while (results.next()) {
                System.out.print("      >");
                for (int i = 1; i < 20; i++) {
                    // For a simple inplementation such as this we can
                    // simply check up to 20 columns, and try to extract
                    // the data in first string then integer format,
                    // stopping once neither works.
                    try {
                        value = results.getString(i);
                        if (value.length() > 0) {
                            System.out.printf(" %s", value);
                        }
                    }
                    catch (SQLException ex) {
                        try {
                            System.out.printf(" %d", results.getInt(i));
                        }
                        catch (SQLException ex2) {
                            continue;
                        }
                    }
                }
                System.out.print("\n");
            }


            // Update the first table with an additional
            // person's information
            System.out.print("5)   Updating the first table (Contact)...");
            stmt = conn.prepareStatement(
                "INSERT IGNORE " +
                "INTO Contact(prefix, first, middle, last, suffix) " +
                "VALUES (?, ?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, "");
            stmt.setString(2, "Lind");
            stmt.setString(3, "");
            stmt.setString(4, "Gee");
            stmt.setString(5, "Ph.D");
            stmt.executeUpdate();
            System.out.print("Done.\n");


            // Dump the contents of the fist table to verify
            // that it was correctly updated
            System.out.printf("6)   Checking the table's contents:\n");
            stmt = conn.prepareStatement("SELECT * FROM Contact;");
            results = stmt.executeQuery();
            row = 0;
            while (results.next()) {
                System.out.print("      >");
                for (int i = 0; i < 20; i++) {
                    try {
                        value = results.getString(i);
                        if (value.length() > 0) {
                            System.out.printf(" %s", value);
                        }
                    }
                    catch (SQLException ex) {
                        try {
                            System.out.printf(" %d", results.getInt(i));
                        }
                        catch (SQLException ex2) {
                            continue;
                        }
                    }
                }
                System.out.print("\n");
            }


            // Create the second table
            System.out.print("7.a) Creating the second table (Phone)...");
            stmt = conn.prepareStatement(
                "CREATE TABLE IF NOT EXISTS Phone (" + 
                    "number VARCHAR(64) NOT NULL PRIMARY KEY," +
                    "description VARCHAR(64) NOT NULL," +
                    "contact_id INTEGER NOT NULL" +
                ");");
            stmt.execute();
            System.out.print("Done.\n");

            // Generate a prepared statement which can be re-used
            // for each insert operation
            stmt = conn.prepareStatement(
                "INSERT IGNORE " +
                "INTO Phone(number, description, contact_id) " +
                "VALUES (?, ?, ?)");

            // Insert values into the second table
            System.out.print("7.b) Populating the second table (Phone)...");
            Set<Integer> keys = numbers.keySet();
            for (Integer id: keys) {
                HashMap<String,String> numList = numbers.get(id);
                Set<String> types = numList.keySet();
                // Add individual column values to the prepared statement
                for (String number: types) {
                    stmt.setString(1, number);
                    stmt.setString(2, numList.get(number));
                    stmt.setInt(3, id);
                    stmt.executeUpdate();
                }
            }
            System.out.print("Done.\n");

            // Dump a join of both tables to confirm that all data
            // was added, and they were correctly tied together.
            System.out.print("8)   Verifying all contact info (join):\n");
            stmt = conn.prepareStatement(
                "SELECT * " +
                "FROM Contact " +
                "INNER JOIN Phone " +
                "  ON Contact.id = Phone.contact_id;"
            );
            results = stmt.executeQuery();
            row = 0;
            while (results.next()) {
                System.out.print("      >");
                row = results.getRow();
                for (int i = 0; i < 20; i++) {
                    try {
                        value = results.getString(i);
                        if (value.length() > 0) {
                            System.out.printf(" %s", value);
                        }
                    }
                    catch (SQLException ex) {
                        try {
                            System.out.printf(" %d", results.getInt(i));
                        }
                        catch (SQLException ex2) {
                            continue;
                        }
                    }
                }
                System.out.print("\n");
            }

            conn.close();
        } catch (SQLException ex) {
            System.out.printf("Caught SQLException: %s\n", ex);
            System.out.printf("\n");
            System.exit(1);
        }

    }
}
