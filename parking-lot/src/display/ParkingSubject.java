package display;

public interface ParkingSubject {
    void registerObserver(ParkingObserver observer);
    void removeObserver(ParkingObserver observer);
    void notifyObservers();
}
