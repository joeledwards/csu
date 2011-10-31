
// Tracks items within the shopping cart.
public class Product
{
    public String name;
    public String description;
    public double price; // Price of each item
    public int quantity; // Number of items in the cart

    public Product(String name, String description, double price)
    {
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = 1;
    }
}

