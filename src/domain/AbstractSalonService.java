package domain;

public abstract class AbstractSalonService implements SalonService {
    private String name;
    private double price;

    public AbstractSalonService(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "AbstractSalonService{" +
                "name='" + name + '\'' +
                ", price=" + price +
                '}';
    }
}
