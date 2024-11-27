package model;

public class Resource {
    private final String type;
    private double price;

    public Resource(String type, double price) {
        this.type = type;
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public double getPrice() {
        return price;
    }

    public void adjustPrice(double newPrice) {
        this.price = newPrice;
    }
}