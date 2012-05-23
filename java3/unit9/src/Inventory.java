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

import inventory.BookT;
import inventory.InventoryT;
import inventory.ObjectFactory;

public class Inventory
{
    private static final Logger logger = Logger.getLogger("Inventory");

    private ObjectFactory factory = null;
    private InventoryT inventory = null;
    private Schema schema = null;

    public Inventory(Schema schema)
    {
        factory = new ObjectFactory();
        inventory = factory.createInventoryT();
        this.schema = schema;
    }

    public InventoryT getInventory()
    {
        return inventory;
    }

    public void addBook(String      title,
                        String      author,
                        String      publisher,
                        BigInteger  year,
                        BigInteger  isbn,
                        double      price)
    {
        BookT book = factory.createBookT();
        book.setTitle(title);
        book.setAuthor(author);
        book.setPublisher(publisher);
        book.setYear(year);
        book.setIsbn(isbn);
        book.setPrice(price);
        inventory.getBook().add(book);
    }

    public List<BookT> getBooks()
    {
        return inventory.getBook();
    }

    public void unmarshal(File file)
    {
        try {
            /*
            BufferedReader reader = new BufferedReader(new InputStreamReader(new DataInputStream(new FileInputStream(file))));
            System.err.println("Reading file '" +file+ "'");
            String line;
            while ((line = reader.readLine()) != null) {
                System.err.println(line);
            }
            */
            unmarshal(new BufferedInputStream(new DataInputStream(new FileInputStream(file))));
        } catch (FileNotFoundException ex) {
            logger.severe("Could not locate file '"+file+"' for unmarshalling");
        } catch (IOException ex) {
            logger.severe("Could not read file '"+file+"' for unmarshalling");
        }
    }

    public void unmarshal(InputStream stream)
    {
        try {
            JAXBContext context = JAXBContext.newInstance( "inventory" );
            //System.err.println(context.toString());
            Unmarshaller unmarshaller = context.createUnmarshaller();
            unmarshaller.setSchema(schema);
            JAXBElement<InventoryT> inv = (JAXBElement<InventoryT>)unmarshaller.unmarshal( stream );
            inventory = inv.getValue();
            System.err.printf("There are %d books in the list.\n", inventory.getBook().size());
            ArrayList<BookT> books = new ArrayList(inventory.getBook());
            Collections.sort(books, new Comparator<BookT>() {
                public int compare(BookT a, BookT b) {
                    return a.getTitle().compareTo(b.getTitle());
                }
                public boolean equals(Object obj) {
                    return (this == obj);
                }
            });
            for (BookT book: inventory.getBook()) {
                System.err.printf("  \"%s\" by %s [%d], %s - %d, $%.2f\n",
                                  book.getTitle(),
                                  book.getAuthor(),
                                  book.getIsbn().longValue(),
                                  book.getPublisher(),
                                  book.getYear().longValue(),
                                  book.getPrice());
            }
        } catch ( JAXBException ex) {
            logger.severe("JAXB Un-marshal Failed: " + ex.toString());
        }
    }

    public void marshal()
    {
        try {
            JAXBElement<InventoryT> inv = factory.createInventory( inventory );
            JAXBContext context = JAXBContext.newInstance( "inventory" );
            //System.err.println(context.toString());
            Marshaller marshaller = context.createMarshaller();
            marshaller.marshal( inv, System.err );
        } catch( JAXBException ex ){
            logger.severe("JAXB Marshal Failed: " + ex.toString());
        }
    }
}
