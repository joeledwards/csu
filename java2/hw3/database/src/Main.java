/*
1) connect to a database
2) generate a table
3) populate the table
4) select and display all rows
5) update the table
6) select and verify all updated rows
7) generate second table & add data
8) use join across tables
*/
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
    public static final String contacts[] = {
        "Rev.,Joel,David,Edwards,,555.918.2106,Mobile,555.853.2582,Work,555.823.0449,Home",
        ",Paul,,Paulson,,555.471.2208,Work,555.143.3444,Mobile",
        ",Charles,Robert,Hutt,Ph.D,555.226.2818,Work,555.853.2266,Mobile"
    };

    public static void main(String argv[])
    {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet results = null;
        ResultSet ids = null;
        String value = null;

        // This will load the MySQL driver, each DB has its own driver
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            System.out.printf("Could not load MySQL driver.", ex);
            System.exit(1);
        }

        System.out.printf("Available Drivers:\n");
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        Driver d = null;
        while (drivers.hasMoreElements()) {
            d = drivers.nextElement();
            System.out.printf("  %s\n", d.toString());
        }
        System.out.print("\n");

        try {
            System.out.print("1.a) Connecting to the database...");
            conn = DriverManager.getConnection(
                   "jdbc:mysql://localhost/test?" + 
                   "user=test&password=test");
            System.out.print("Done.\n");


            System.out.print("1.b) Removing any test tables...");
            stmt = conn.prepareStatement(
                "DROP TABLE IF EXISTS Contact, Phone;");
            stmt.execute();
            System.out.print("Done.\n");


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


            System.out.print("3)   Populating the first table (Contact)...");
            HashMap<Integer,HashMap<String,String>> numbers;
            numbers = new HashMap<Integer,HashMap<String,String>>();
            for (String info: contacts) {
                String[] parts = info.split(",");
                if (parts.length < 5) {
                    continue;
                }

                int processed = 0;
                stmt = conn.prepareStatement(
                    "INSERT " + 
                    "INTO Contact(prefix, first, middle, last, suffix) " +
                    "VALUES (?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
                for (int i = 0; i < 5; i++) {
                    value = parts[i].trim();
                    stmt.setString(i+1, value);
                }
                stmt.executeUpdate();
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


            System.out.printf("4)   Checking the table's contents:\n");
            stmt = conn.prepareStatement("SELECT * FROM Contact;");
            results = stmt.executeQuery();
            int row = 0;
            while (results.next()) {
                System.out.print("      >");
                for (int i = 1; i < 20; i++) {
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


            System.out.print("7.a) Creating the second table (Phone)...");
            stmt = conn.prepareStatement(
                "CREATE TABLE IF NOT EXISTS Phone (" + 
                    "number VARCHAR(64) NOT NULL PRIMARY KEY," +
                    "description VARCHAR(64) NOT NULL," +
                    "contact_id INTEGER NOT NULL" +
                ");");
            stmt.execute();
            System.out.print("Done.\n");

            System.out.print("7.b) Populating the second table (Phone)...");
            Set<Integer> keys = numbers.keySet();
            for (Integer id: keys) {
                HashMap<String,String> numList = numbers.get(id);
                Set<String> types = numList.keySet();
                for (String number: types) {
                    stmt = conn.prepareStatement(
                        "INSERT IGNORE " +
                        "INTO Phone(number, description, contact_id) " +
                        "VALUES (?, ?, ?)");
                    stmt.setString(1, number);
                    stmt.setString(2, numList.get(number));
                    stmt.setInt(3, id);
                    stmt.executeUpdate();
                }
            }
            System.out.print("Done.\n");

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
