package client;

import display.DisplayBoard;
import floor.ParkingFloor;
import gate.EntryGate;
import gate.ExitGate;
import lot.ParkingLot;
import model.ParkingSpot;
import model.Ticket;
import model.Vehicle;
import model.VehicleType;

import java.util.Map;
import java.util.Scanner;

public class ParkingLotClient {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Initialize Parking Lot
        ParkingLot lot = ParkingLot.getInstance();
        DisplayBoard displayBoard = new DisplayBoard("Board_1");
        initializeParkingLot(lot);
        lot.registerObserver(displayBoard);

        EntryGate entryGate1 = new EntryGate("Entry-1");
        ExitGate exitGate1 = new ExitGate("Exit-1");

        while (true) {
            System.out.println("\n--- Parking Lot System ---");
            System.out.println("1. Park model.Vehicle");
            System.out.println("2. Remove model.Vehicle");
            System.out.println("3. Show Available Spots");
            System.out.println("4. Exit");
            System.out.print("Select an option: ");
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    System.out.print("Enter model.Vehicle Number: ");
                    String number = scanner.nextLine();
                    System.out.print("Enter model.Vehicle Type (CAR, BIKE, TRUCK): ");
                    VehicleType type = VehicleType.valueOf(scanner.nextLine().toUpperCase());
                    Vehicle vehicle = new Vehicle(number, type);
                    Ticket ticket = entryGate1.processVehicleEntry(vehicle);
                    if (ticket != null) {
                        System.out.println("model.Vehicle Parked. model.Ticket ID: " + ticket.getTicketId());
                        System.out.println("Spot ID: " + ticket.getSpot().getId());
                    } else {
                        System.out.println("No spot available for your vehicle.");
                    }
                    break;
                case 2:
                    System.out.print("Enter model.Ticket ID: ");
                    String ticketId = scanner.nextLine();
                    boolean success = exitGate1.processVehicleExit(ticketId);
                    if (success) {
                        System.out.println("model.Vehicle exited. Thank you!");
                    } else {
                        System.out.println("Invalid model.Ticket ID or spot not found.");
                    }
                    break;
                case 3:
                    Map<VehicleType, Integer> summary = lot.getAvailableSpotsSummary();
                    System.out.println("Available Spots:");
                    for (Map.Entry<VehicleType, Integer> entry : summary.entrySet()) {
                        System.out.println(entry.getKey() + ": " + entry.getValue());
                    }
                    break;
                case 4:
                    System.out.println("Exiting system. Goodbye!");
                    return;
                default:
                    System.out.println("Invalid input. Try again.");
            }
        }
    }

    private static void initializeParkingLot(ParkingLot lot) {
        // Floor 1
        ParkingFloor floor1 = new ParkingFloor("F1");
        for (int i = 1; i <= 5; i++) floor1.addSpot(new ParkingSpot("F1-CAR-" + i, VehicleType.CAR));
        for (int i = 1; i <= 3; i++) floor1.addSpot(new ParkingSpot("F1-BIKE-" + i, VehicleType.BIKE));
        for (int i = 1; i <= 2; i++) floor1.addSpot(new ParkingSpot("F1-TRUCK-" + i, VehicleType.TRUCK));

        // Floor 2
        ParkingFloor floor2 = new ParkingFloor("F2");
        for (int i = 1; i <= 4; i++) floor2.addSpot(new ParkingSpot("F2-CAR-" + i, VehicleType.CAR));
        for (int i = 1; i <= 2; i++) floor2.addSpot(new ParkingSpot("F2-BIKE-" + i, VehicleType.BIKE));
        for (int i = 1; i <= 1; i++) floor2.addSpot(new ParkingSpot("F2-TRUCK-" + i, VehicleType.TRUCK));

        lot.addFloor(floor1);
        lot.addFloor(floor2);
    }
}
