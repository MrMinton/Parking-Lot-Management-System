// Base class for all parking spots
public abstract class ParkingSpot {
    private String spotID;
    private boolean isOccupied;
    private double hourlyRate;
    private String currentVehiclePlate; // Tracks the plate of the vehicle currently in the spot

    public ParkingSpot(String spotID, double hourlyRate) {
        this.spotID = spotID;
        this.hourlyRate = hourlyRate;
        this.isOccupied = false;
        this.currentVehiclePlate = null;
    }

    public void assignVehicle(String licensePlate) {
        this.currentVehiclePlate = licensePlate;
        this.isOccupied = true;
    }

    public void removeVehicle() {
        this.currentVehiclePlate = null;
        this.isOccupied = false;
    }

    // Getters
    public String getSpotID() { return spotID; }
    public boolean isOccupied() { return isOccupied; }
    public double getHourlyRate() { return hourlyRate; }
    public String getCurrentVehiclePlate() { return currentVehiclePlate; }
}

// Concrete implementations with specific rates as per requirements
class CompactSpot extends ParkingSpot {
    public CompactSpot(String spotID) { super(spotID, 2.0); } // RM 2/hour [cite: 132]
}

class RegularSpot extends ParkingSpot {
    public RegularSpot(String spotID) { super(spotID, 5.0); } // RM 5/hour [cite: 132]
}

class HandicappedSpot extends ParkingSpot {
    public HandicappedSpot(String spotID) { super(spotID, 2.0); } // RM 2/hour [cite: 133]
}

class ReservedSpot extends ParkingSpot {
    public ReservedSpot(String spotID) { super(spotID, 10.0); } // RM 10/hour [cite: 134]
}