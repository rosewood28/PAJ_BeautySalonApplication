package domain;

public class HairService extends AbstractSalonService {
    private HairServiceType type;

    public HairService(HairServiceType type, double price) {
        super(type.name(), price);
        this.type = type;
    }

    public HairServiceType getType() {
        return type;
    }

    public void setType(HairServiceType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "HairService{" +
                "name='" + getName() + '\'' +
                ", price=" + getPrice() +
                ", type=" + type +
                '}';
    }
}
