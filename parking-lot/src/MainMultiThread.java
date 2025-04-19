import floor.ParkingFloor;
import gate.EntryGate;
import gate.ExitGate;
import lot.ParkingLot;
import model.ParkingSpot;
import model.Ticket;
import model.Vehicle;
import model.VehicleType;

import java.util.*;

public class MainMultiThread {
    public static void main(String[] args) {
        ParkingLot parkingLot = ParkingLot.getInstance();

        // Create floors and add parking spots
        ParkingFloor floor1 = new ParkingFloor("Floor-1");
        for (int i = 1; i <= 10; i++) {
            floor1.addSpot(new ParkingSpot("C-" + i, VehicleType.CAR));
            floor1.addSpot(new ParkingSpot("B-" + i, VehicleType.BIKE));
            floor1.addSpot(new ParkingSpot("T-" + i, VehicleType.TRUCK));
        }

        parkingLot.addFloor(floor1);

        // Create multiple entry and exit gates
        List<EntryGate> entryGates = Arrays.asList(
                new EntryGate("E1"),
                new EntryGate("E2"),
                new EntryGate("E3")
        );

        List<ExitGate> exitGates = Arrays.asList(
                new ExitGate("X1"),
                new ExitGate("X2")
        );

        // Vehicles arriving at different gates
        List<Vehicle> vehicles = Arrays.asList(
                new Vehicle("KA01AA1111", VehicleType.CAR),
                new Vehicle("KA02BB2222", VehicleType.BIKE),
                new Vehicle("KA03CC3333", VehicleType.TRUCK),
                new Vehicle("KA04DD4444", VehicleType.CAR),
                new Vehicle("KA05EE5555", VehicleType.BIKE),
                new Vehicle("KA06FF6666", VehicleType.CAR)
        );

        // Map to hold issued tickets to simulate exit later
        List<Ticket> issuedTickets = Collections.synchronizedList(new ArrayList<>());

        // Entry threads
        List<Thread> entryThreads = vehicles.stream().map(vehicle -> new Thread(() -> {
            EntryGate gate = entryGates.get((int)(Math.random() * entryGates.size()));
            Ticket ticket = gate.processVehicleEntry(vehicle);
            if (ticket != null) {
                System.out.println("model.Vehicle " + vehicle.getVehicleNumber() + " entered through " + gate.getGateId() +
                        ", Assigned Spot: " + ticket.getSpot().getId() + ", TicketId: " + ticket.getTicketId());
                issuedTickets.add(ticket);
            } else {
                System.out.println("No spot available for " + vehicle.getVehicleNumber());
            }
        })).toList();

        // Start entry threads
        entryThreads.forEach(Thread::start);
        entryThreads.forEach(t -> {
            try { t.join(); } catch (InterruptedException e) { e.printStackTrace(); }
        });

        System.out.println("\n--- Available Spot Summary After Entry ---");
        parkingLot.getAvailableSpotsSummary().forEach((k, v) -> System.out.println(k + ": " + v));

        // Small delay before exit
        try { Thread.sleep(2000); } catch (InterruptedException e) { e.printStackTrace(); }

        // Exit threads
        List<Thread> exitThreads = issuedTickets.stream().map(ticket -> new Thread(() -> {
            ExitGate gate = exitGates.get((int)(Math.random() * exitGates.size()));
            boolean success = gate.processVehicleExit(ticket.getTicketId());
            if (success) {
                System.out.println("model.Vehicle " + ticket.getVehicle().getVehicleNumber() +
                        " exited through " + gate.getGateId() + ", Freed Spot: " + ticket.getSpot().getId());
            } else {
                System.out.println("Exit failed for model.Ticket: " + ticket.getTicketId());
            }
        })).toList();

        // Start exit threads
        exitThreads.forEach(Thread::start);
        exitThreads.forEach(t -> {
            try { t.join(); } catch (InterruptedException e) { e.printStackTrace(); }
        });

        System.out.println("\n--- Available Spot Summary After Exit ---");
        parkingLot.getAvailableSpotsSummary().forEach((k, v) -> System.out.println(k + ": " + v));
    }
}

