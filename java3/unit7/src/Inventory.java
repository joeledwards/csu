import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.math.BigInteger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;
import java.util.logging.Level;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;

import noNamespace.*;
import noNamespace.impl.*;

import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlException;

public class Inventory
{
    private static final Logger logger = Logger.getLogger("Inventory");

    private Schema schema = null;
    private InventoryDocument document = null;
    private InventoryT inventory = null;

    public Inventory(Schema schema)
    {
        this.schema = schema;
        document = noNamespace.InventoryDocument.Factory.newInstance();
        inventory = document.addNewInventory();
    }

    public void addBook(String      title,
                        String      author,
                        String      publisher,
                        BigInteger  year,
                        BigInteger  isbn,
                        double      price)
    {
        BookT book = inventory.addNewBook();
        book.setTitle(title);
        book.setAuthor(author);
        book.setPublisher(publisher);
        book.setYear(year);
        book.setIsbn(isbn);
        book.setPrice(price);
    }

    public void marshal()
    {
        System.err.println(document);
    }

    public void unmarshal(File xmlFile)
    {
        try {
            document = InventoryDocument.Factory.parse(xmlFile);
        } catch (IOException ex) {
            logger.severe("Could not read XML file '" +xmlFile+ "': " + ex.toString());
            return;
        } catch (XmlException ex) {
            logger.severe("XMLBeans parse failed: " + ex.toString());
            return;
        }

        inventory = document.getInventory();
        BookT[] books = inventory.getBookArray();
        System.err.printf("There are %d books in this inventory.\n", books.length);
        Arrays.sort(books, new Comparator<BookT>() {
            public int compare(BookT a, BookT b) {
                return a.getTitle().compareTo(b.getTitle());
            }
            public boolean equals(Object obj) {
                return (this == obj);
            }
        });

        for (BookT book: books) {
            System.err.printf("  \"%s\" by %s [%d], %s - %d, $%.2f\n",
                              book.getTitle(),
                              book.getAuthor(),
                              book.getIsbn().longValue(),
                              book.getPublisher(),
                              book.getYear().longValue(),
                              book.getPrice());
        }
    }

    public XmlCursor getCursor()
    {
        return inventory.newCursor();
    }
}
