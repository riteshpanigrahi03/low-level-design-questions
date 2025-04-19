package pricing;

import model.VehicleType;

public interface PricingStrategy {
    double calculatePrice(VehicleType type, long durationInHours);
}
