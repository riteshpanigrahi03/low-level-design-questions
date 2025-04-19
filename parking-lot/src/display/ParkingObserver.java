package display;

import model.VehicleType;

import java.util.Map;

public interface ParkingObserver {
    void update(Map<VehicleType, Integer> availableSpots);
}
