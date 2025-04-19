package model;

public class ParkingSpot {
    private String id;
    private VehicleType spotType;
    private volatile boolean isOccupied;

    public ParkingSpot(String id, VehicleType spotType) {
        this.id = id;
        this.spotType = spotType;
        this.isOccupied = false;
    }

    public String getId() {
        return id;
    }

    public VehicleType getSpotType() {
        return spotType;
    }

    public boolean isOccupied() {
        return isOccupied;
    }

    public synchronized boolean occupy() {
        if (isOccupied) return false;
        isOccupied = true;
        return true;
    }

    public synchronized void free() {
        isOccupied = false;
    }
}
