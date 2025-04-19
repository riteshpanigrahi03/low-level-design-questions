package gate;

import lot.ParkingLot;
import model.Ticket;
import model.Vehicle;

public class EntryGate {
    private final String gateId;

    public EntryGate(String gateId) {
        this.gateId = gateId;
    }

    public Ticket processVehicleEntry(Vehicle vehicle) {
        return ParkingLot.getInstance().assignSpot(vehicle);
    }

    public String getGateId() {
        return gateId;
    }
}
