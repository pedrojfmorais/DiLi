package pt.isec.gps.dili.model.data.book;

public class BookPrice {
    private final String book;
    private final double price;

    public BookPrice(String book, double price) {
        this.book = book;
        this.price = price;
    }

    public String getBook() {
        return book;
    }

    public double getPrice() {
        return price;
    }
}
