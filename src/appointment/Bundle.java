package appointment;

import domain.SalonService;

import java.util.ArrayList;
import java.util.List;

public class Bundle {
    private String bundleName;
    private List<SalonService> salonServices =  new ArrayList<SalonService>();
    private double bundlePrice;

    public Bundle(String bundleName, List<SalonService> salonServices, double bundlePrice) {
        this.bundleName = bundleName;
        this.salonServices = salonServices;
        this.bundlePrice = bundlePrice;
    }

    public String getBundleName() {
        return bundleName;
    }

    public List<SalonService> getSalonServices() {
        return salonServices;
    }

    public double getBundlePrice() {
        return bundlePrice;
    }

    @Override
    public String toString() {
        return "Bundle{" +
                "bundleName='" + bundleName + '\'' +
                ", salonServices=" + salonServices +
                ", bundlePrice=" + bundlePrice +
                '}';
    }
}
