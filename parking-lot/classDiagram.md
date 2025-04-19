classDiagram

%% ===== Enums & Models =====
class VehicleType {
<<enum>>
CAR
BIKE
TRUCK
}

class Vehicle {
-vehicleNumber: String
-type: VehicleType
+getVehicleNumber()
+getType()
}

class ParkingSpot {
-id: String
-spotType: VehicleType
-occupied: boolean
+occupy(): boolean
+free(): void
+isOccupied(): boolean
+getSpotType(): VehicleType
+getId(): String
}

class Ticket {
-ticketId: String
-vehicle: Vehicle
-spot: ParkingSpot
-entryTime: LocalDateTime
+getTicketId()
+getVehicle()
+getSpot()
+getEntryTime()
}

%% ===== Parking Structure =====
class ParkingFloor {
-floorId: String
-spotsMap: Map<VehicleType, List<ParkingSpot>>
+addSpot(spot)
+allocateSpot(type): ParkingSpot
+freeSpot(spot)
+getFloorId()
}

class ParkingLot {
-instance: ParkingLot
-floors: List<ParkingFloor>
-activeTickets: Map<String, Ticket>
-pricingStrategy: PricingStrategy
-observers: List<DisplayBoard>
+getInstance()
+addFloor(floor)
+assignSpot(vehicle): Ticket
+releaseSpot(ticketId): boolean
+getAvailableSpotsSummary(): Map<VehicleType, Integer>
+setPricingStrategy(strategy)
+calculatePrice(ticket): double
+registerObserver(observer)
+removeObserver(observer)
+notifyObservers()
}

%% ===== Gates =====
class EntryGate {
-gateId: String
+processVehicleEntry(vehicle): Ticket
}

class ExitGate {
-gateId: String
+processVehicleExit(ticketId): boolean
}

%% ===== Observer Pattern =====
class ParkingSubject {
<<interface>>
+registerObserver(observer)
+removeObserver(observer)
+notifyObservers()
}

class ParkingObserver {
<<interface>>
+update(summary: Map<VehicleType, Integer>)
}

class DisplayBoard {
+update(summary: Map<VehicleType, Integer>)
}

ParkingLot ..|> ParkingSubject : IS-A
DisplayBoard ..|> ParkingObserver : IS-A
ParkingLot --> DisplayBoard : notifies

%% ===== Strategy Pattern =====
class PricingStrategy {
<<interface>>
+calculatePrice(ticket): double
}

class DefaultPricingStrategy {
+calculatePrice(ticket): double
}

class WeekendPricingStrategy {
+calculatePrice(ticket): double
}

WeekendPricingStrategy ..|> PricingStrategy
DefaultPricingStrategy ..|> PricingStrategy
ParkingLot --> PricingStrategy : uses
ParkingLot --> ParkingObserver : HAS-A

%% ===== Associations =====
Vehicle --> VehicleType
Ticket --> Vehicle
Ticket --> ParkingSpot
ParkingFloor --> ParkingSpot
ParkingLot --> ParkingFloor
ParkingLot --> Ticket
EntryGate --> ParkingLot
ExitGate --> ParkingLot
