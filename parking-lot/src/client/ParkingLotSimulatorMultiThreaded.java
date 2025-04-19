package client;

import floor.ParkingFloor;
import gate.EntryGate;
import gate.ExitGate;
import lot.ParkingLot;
import model.ParkingSpot;
import model.Ticket;
import model.Vehicle;
import model.VehicleType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ParkingLotSimulatorMultiThreaded {
    public static void main(String[] args) throws InterruptedException {
        ParkingLot parkingLot = ParkingLot.getInstance();

        // Create and add floors
        ParkingFloor floor1 = new ParkingFloor("F1");
        ParkingFloor floor2 = new ParkingFloor("F2");

        // Add spots to floors
        for (int i = 0; i < 5; i++) {
            floor1.addSpot(new ParkingSpot("F1-CAR-" + i, VehicleType.CAR));
            floor1.addSpot(new ParkingSpot("F1-BIKE-" + i, VehicleType.BIKE));
            floor2.addSpot(new ParkingSpot("F2-CAR-" + i, VehicleType.CAR));
            floor2.addSpot(new ParkingSpot("F2-BIKE-" + i, VehicleType.BIKE));
        }

        parkingLot.addFloor(floor1);
        parkingLot.addFloor(floor2);

        // Create entry and exit gates
        EntryGate entryGate1 = new EntryGate("G1");
        EntryGate entryGate2 = new EntryGate("G2");
        ExitGate exitGate1 = new ExitGate("X1");
        ExitGate exitGate2 = new ExitGate("X2");

        List<Vehicle> vehicles = List.of(
                new Vehicle("KA01AB1234", VehicleType.CAR),
                new Vehicle("KA02BC2345", VehicleType.BIKE),
                new Vehicle("KA03CD3456", VehicleType.CAR),
                new Vehicle("KA04DE4567", VehicleType.BIKE),
                new Vehicle("KA05EF5678", VehicleType.CAR),
                new Vehicle("KA06FG6789", VehicleType.BIKE),
                new Vehicle("KA07GH7890", VehicleType.CAR),
                new Vehicle("KA08HI8901", VehicleType.BIKE),
                new Vehicle("KA08HI2402", VehicleType.TRUCK)
        );

        List<Ticket> issuedTickets = Collections.synchronizedList(new ArrayList<>());

        Thread entryThread1 = new Thread(() -> {
            for (int i = 0; i < vehicles.size() / 2; i++) {
                Vehicle vehicle = vehicles.get(i);
                Ticket ticket = entryGate1.processVehicleEntry(vehicle);
                if (ticket != null) {
                    System.out.println("[ENTRY G1] model.Vehicle " + vehicle.getVehicleNumber() + " assigned to Spot: " + ticket.getSpot().getId());
                    issuedTickets.add(ticket);
                } else {
                    System.out.println("[ENTRY G1] No spot available for model.Vehicle: " + vehicle.getVehicleNumber() + " model.VehicleType: " + vehicle.getVehicleType());
                }
            }
        });

        Thread entryThread2 = new Thread(() -> {
            for (int i = vehicles.size() / 2; i < vehicles.size(); i++) {
                Vehicle vehicle = vehicles.get(i);
                Ticket ticket = entryGate2.processVehicleEntry(vehicle);
                if (ticket != null) {
                    System.out.println("[ENTRY G2] model.Vehicle " + vehicle.getVehicleNumber() + " assigned to Spot: " + ticket.getSpot().getId());
                    issuedTickets.add(ticket);
                } else {
                    System.out.println("[ENTRY G1] No spot available for model.Vehicle: " + vehicle.getVehicleNumber() + " model.VehicleType: " + vehicle.getVehicleType());
                }
            }
        });

        entryThread1.start();
        entryThread2.start();
        entryThread1.join();
        entryThread2.join();

        System.out.println("\nRemaining Available Spots After Entries:");
        parkingLot.getAvailableSpotsSummary().forEach((type, count) ->
                System.out.println(type + ": " + count));

        Thread.sleep(5000);
        System.out.println("\n----- Exit Phase -----\n");

        Thread exitThread1 = new Thread(() -> {
            for (int i = 0; i < issuedTickets.size() / 2; i++) {
                Ticket ticket = issuedTickets.get(i);
                boolean success = exitGate1.processVehicleExit(ticket.getTicketId());
                System.out.println("[EXIT X1] model.Ticket: " + ticket.getTicketId() + " exit " + (success ? "successful" : "failed"));
            }
        });

        Thread exitThread2 = new Thread(() -> {
            for (int i = issuedTickets.size() / 2; i < issuedTickets.size(); i++) {
                Ticket ticket = issuedTickets.get(i);
                boolean success = exitGate2.processVehicleExit(ticket.getTicketId());
                System.out.println("[EXIT X2] model.Ticket: " + ticket.getTicketId() + " exit " + (success ? "successful" : "failed"));
            }
        });

        exitThread1.start();
        exitThread2.start();
        exitThread1.join();
        exitThread2.join();

        System.out.println("\nRemaining Available Spots:");
        parkingLot.getAvailableSpotsSummary().forEach((type, count) ->
                System.out.println(type + ": " + count));
    }
}
