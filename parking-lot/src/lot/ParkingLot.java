package lot;

import display.ParkingObserver;
import display.ParkingSubject;
import floor.ParkingFloor;
import model.ParkingSpot;
import model.Ticket;
import model.Vehicle;
import model.VehicleType;
import pricing.DefaultPricingStrategy;
import pricing.PricingStrategy;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ParkingLot implements ParkingSubject {
    private static ParkingLot parkingLot;
    private List<ParkingFloor> floors;
    private Map<String, Ticket> activeTickets;
    private List<ParkingObserver> observers;

    private PricingStrategy pricingStrategy = new DefaultPricingStrategy();

    public void setPricingStrategy(PricingStrategy pricingStrategy) {
        this.pricingStrategy = pricingStrategy;
    }

    public ParkingLot() {
        this.activeTickets = new ConcurrentHashMap<>();
        this.floors = new ArrayList<>();
        this.observers = new ArrayList<>();
    }

    public static synchronized ParkingLot getInstance() {
        if (parkingLot == null) {
            parkingLot = new ParkingLot();
        }
        return parkingLot;
    }

    public void addFloor(ParkingFloor floor) {
        floors.add(floor);
    }

    public synchronized Ticket assignSpot(Vehicle vehicle) {
        for (ParkingFloor floor : floors) {
            ParkingSpot spot = floor.allocateSpot(vehicle.getVehicleType());
            if (Objects.nonNull(spot)) {
                String ticketId = UUID.randomUUID().toString();
                Ticket ticket = new Ticket(ticketId, vehicle, spot);
                activeTickets.put(ticketId, ticket);
                notifyObservers();
                return ticket;
            }
        }
        return null;
    }

    public synchronized boolean releaseSpot(String ticketId) {
        Ticket ticket = activeTickets.remove(ticketId);
        if (ticket != null) {
            for (ParkingFloor floor : floors) {
                if (floor.getFloorId().equals(ticket.getSpot().getId().split("-")[0])) {
                    floor.freeSpot(ticket.getSpot());
                    notifyObservers();
                    long durationInHours = Duration.between(ticket.getEntryTime(), LocalDateTime.now()).toHours();//will be toHours(), in real application
                    double fee = pricingStrategy.calculatePrice(ticket.getVehicle().getVehicleType(), durationInHours);
                    System.out.println("Total parking fee for ticket " + ticketId + ": ₹" + fee);
                    return true;
                }
            }
        }
        return false;
    }

    public Map<VehicleType, Integer> getAvailableSpotsSummary() {
        Map<VehicleType, Integer> summary = new EnumMap<>(VehicleType.class);

        for (VehicleType type : VehicleType.values()) {
            summary.put(type, 0);
        }

        for (ParkingFloor floor : floors) {
            for (VehicleType type : VehicleType.values()) {
                for (ParkingSpot spot : floor.getSpotsMap().get(type)) {
                    if (!spot.isOccupied()) {
                        summary.put(type, summary.get(type) + 1);
                    }
                }
            }
        }
        return summary;
    }

    // Observer Pattern Methods
    @Override
    public void registerObserver(ParkingObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(ParkingObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        Map<VehicleType, Integer> summary = getAvailableSpotsSummary();
        for (ParkingObserver observer : observers) {
            observer.update(summary);
        }
    }
}
