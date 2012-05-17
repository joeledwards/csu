import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import inventory.BookT;
import inventory.InventoryT;
import inventory.ObjectFactory;

public class Inventory {

    private ObjectFactory factory;
    private InventoryT inventory;

    public Inventory() {
        factory = new ObjectFactory();
        inventory = factory.createInventoryT();
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

    public void marshal() {
        try {
            JAXBElement<InventoryT> inv = factory.createInventory( inventory );
            JAXBContext context = JAXBContext.newInstance( "hello" );
            Marshaller marshaller = context.createMarshaller();
            marshaller.marshal( inv, System.out );
        } catch( JAXBException ex ){
            // ...
        }
    }
}
