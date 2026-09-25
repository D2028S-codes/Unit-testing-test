
// Models a product available for purchase
public class Product {
    private String id;
    private double price;
    private int stock;

    public Product(String id, double price, int stock) {
        this.id = id;
        this.price = price;
        this.stock = stock;
    }

    public String getId() { return id; }
    public double getPrice() { return price; }
    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }
}

