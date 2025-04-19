package pricing;

import model.VehicleType;

import java.util.EnumMap;
import java.util.Map;

public class DefaultPricingStrategy implements PricingStrategy {
    private final Map<VehicleType, Double> hourlyRates = new EnumMap<>(VehicleType.class);

    public DefaultPricingStrategy() {
        hourlyRates.put(VehicleType.CAR, 20.0);
        hourlyRates.put(VehicleType.BIKE, 10.0);
        hourlyRates.put(VehicleType.TRUCK, 30.0);
    }

    @Override
    public double calculatePrice(VehicleType type, long durationInHours) {
        return hourlyRates.getOrDefault(type, 20.0) * Math.max(durationInHours, 1);
    }
}
