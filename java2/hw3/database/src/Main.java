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
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;

public class Main
{
    public static final String contacts[] = {
        "Joel,Edwards,David,Rev.,,555.918.2106,mobile,555.853.2582,Work,555.823.0449,Home",
        "Paul,Paulson,,,,555.471.2208,Work,555.143.3444,Mobile",
        "Charles,Hutt,Charles,Ph.D,555.226.2818,Work,555.853.2266,Mobile"
    };

    public static void main(String argv[])
    {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet results = null;
        System.out.printf("Drivers:\n");
        //DriverManager.registerDriver("com.mysql.jdbc.Driver");

        // This will load the MySQL driver, each DB has its own driver
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            System.out.printf("Could not load MySQL driver.", ex);
            System.exit(1);
        }

        Enumeration<Driver> drivers = DriverManager.getDrivers();
        Driver d = null;
        while (drivers.hasMoreElements()) {
            d = drivers.nextElement();
            System.out.printf("  %s\n", d.toString());
        }

        try {
            // 1) Open connection to a MySQL database.
            conn = DriverManager.getConnection(
                   "jdbc:mysql://localhost/test?" + 
                   "user=test&password=test");

            // 1.5) Flush the database for each run.
            stmt = conn.prepareStatement(
                "DROP TABLE IF EXISTS Contact, Phone;");
            stmt.execute();

            // 2) Create the first table.
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

            // 3) Populate the first table.
            HashMap<Integer,ArrayList<String[]>> numbers = new HashMap<Integer,ArrayList<String[]>>();
            for (String info: contacts) {
                String[] parts = info.split(",");
                if (parts.length < 5) {
                    continue;
                }

                int processed = 0;
                stmt = conn.prepareStatement(
                    "INSERT IGNORE " + 
                    "INTO Contact(first, last, middle, prefix, suffix) " +
                    "VALUES (?, ?, ?, ?, ?)");
                for (int i = 0; i < 5; i++) {
                    stmt.setString(i+1, parts[i].trim());
                }
                processed = 5;
                int id = stmt.executeUpdate();

                // Pair the stored ID with the remaining values 
                // from this contact.
                ArrayList<String[]> num = new ArrayList<String[]>();
                while ((processed + 2) <= parts.length) {
                    num.add(new String[] {parts[processed], parts[processed+1]});
                    processed += 2;
                }
                numbers.put(id, num);
            }


            // 4) Select all rows from the first table and display.
            stmt = conn.prepareStatement("SELECT * FROM Contact;");
            results = stmt.executeQuery();

            int row = 0;
            results.first();
            System.out.printf("Fetch size: %d\n", results.getFetchSize());
            while ((row = results.getRow()) > 0) {
                for (int i = 0; i < 20; i++) {
                    try {
                        System.out.printf(" %s", results.getString(i));
                        results.getString(i);
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
                results.next();
            }


            // 5) Update the first table.
            stmt = conn.prepareStatement(
                "INSERT IGNORE " +
                "INTO Contact(first, last, middle, prefix, suffix) " +
                "VALUES (?, ?, ?, ?, ?)");
            stmt.setString(1, "Dilip");
            stmt.setString(2, "Dedhia");
            stmt.setString(3, "");
            stmt.setString(4, "");
            stmt.setString(5, "Ph.D");
            stmt.executeUpdate();


            // 6) Select all rows from the first table and display
            //    in order to verify update was successful.
            stmt = conn.prepareStatement("SELECT * FROM Contact;");
            results = stmt.executeQuery();
            row = 0;
            results.first();
            System.out.printf("Fetch size: %d\n", results.getFetchSize());
            while ((row = results.getRow()) > 0) {
                for (int i = 0; i < 20; i++) {
                    try {
                        System.out.printf(" %s", results.getString(i));
                        results.getString(i);
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
                results.next();
            }


            // 7.a) Create second table.
            stmt = conn.prepareStatement(
                "CREATE TABLE IF NOT EXISTS Phone (" + 
                    "number VARCHAR(64) NOT NULL PRIMARY KEY," +
                    "description VARCHAR(64) NOT NULL," +
                    "contact_id INTEGER NOT NULL" +
                ");");
            stmt.execute();

            // 7.b) Populate second table.
            Set<Integer> keys = numbers.keySet();
            for (Integer id: keys) {
                ArrayList<String[]> numList = numbers.get(id);
                for (String[] num: numList) {
                    stmt = conn.prepareStatement(
                        "INSERT IGNORE " +
                        "INTO Phone(number, description, contact_id) " +
                        "VALUES (?, ?, ?)");
                    stmt.setString(1, num[0]);
                    stmt.setString(2, num[1]);
                    stmt.setInt(3, id);
                    stmt.executeUpdate();
                }
            }

            // 8) Select from both tables using join and 
            //    display results.
            stmt = conn.prepareStatement(
                "SELECT * " +
                "FROM Contact " +
                "INNER JOIN Phone " +
                "  ON Contact.id = Phone.contact_id;"
            );
            results = stmt.executeQuery();
            row = 0;
            results.first();
            System.out.printf("Fetch size: %d\n", results.getFetchSize());
            while ((row = results.getRow()) > 0) {
                for (int i = 0; i < 20; i++) {
                    try {
                        System.out.printf(" %s", results.getString(i));
                        results.getString(i);
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
                results.next();
            }

            conn.close();
        } catch (SQLException ex) {
            System.out.printf("Caught SQLException: %s\n", ex);
            System.out.printf("\n");
            System.exit(1);
        }

    }
}
