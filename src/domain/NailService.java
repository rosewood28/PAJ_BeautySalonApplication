package domain;

public class NailService extends AbstractSalonService {
    private NailServiceType type;

    public NailService(NailServiceType nailType, double price) {
        super(nailType.name(), price);
    }

    @Override
    public String toString() {
        return "NailService{" +
                "name='" + getName() + '\'' +
                ", price=" + getPrice() +
                '}';
    }
}
