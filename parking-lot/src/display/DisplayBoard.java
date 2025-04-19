package display;

import model.VehicleType;

import java.util.Map;

public class DisplayBoard implements ParkingObserver {
    private final String boardId;

    public DisplayBoard(String boardId) {
        this.boardId = boardId;
    }

    @Override
    public void update(Map<VehicleType, Integer> availableSpots) {
        System.out.println("\n[Display Board: " + boardId + "] Available Spots:");
        for (Map.Entry<VehicleType, Integer> entry : availableSpots.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }
}
