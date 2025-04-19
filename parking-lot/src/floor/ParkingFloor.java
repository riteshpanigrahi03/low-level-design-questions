package floor;

import model.ParkingSpot;
import model.VehicleType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ParkingFloor {
    private String floorId;
    private Map<VehicleType, List<ParkingSpot>> spotsMap;

    public ParkingFloor(String floorId) {
        this.floorId = floorId;
        this.spotsMap = new ConcurrentHashMap<>();
        for (VehicleType type : VehicleType.values()) {
            spotsMap.put(type, Collections.synchronizedList(new ArrayList<>()));
        }
    }

    public ParkingSpot allocateSpot(VehicleType vehicleType) {
        for (ParkingSpot spot : spotsMap.get(vehicleType)) {
            if (!spot.isOccupied() && spot.occupy()) {
                return spot;
            }
        }
        return null;
    }

    public void freeSpot(ParkingSpot spot) {
        spot.free();
    }

    public String getFloorId() {
        return floorId;
    }

    public void addSpot(ParkingSpot spot) {
        spotsMap.get(spot.getSpotType()).add(spot);
    }

    public Map<VehicleType, List<ParkingSpot>> getSpotsMap() {
        return spotsMap;
    }
}
