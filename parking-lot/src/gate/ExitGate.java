package gate;

import lot.ParkingLot;

public class ExitGate {
    private final String gateId;

    public ExitGate(String gateId) {
        this.gateId = gateId;
    }

    public boolean processVehicleExit(String ticketId) {
        return ParkingLot.getInstance().releaseSpot(ticketId);
    }

    public String getGateId() {
        return gateId;
    }
}
