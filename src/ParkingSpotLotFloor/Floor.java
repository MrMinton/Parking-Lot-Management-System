package ParkingSpotLotFloor;

import java.util.ArrayList;
import java.util.List;

public class Floor {
    private int floorNumber;
    private List<ParkingSpot> spots;

    public Floor(int floorNumber) {
        this.floorNumber = floorNumber;
        this.spots = new ArrayList<>();
    }

    // Requirement: Add spots and maintain a list
    public void addSpot(ParkingSpot spot) {
        spots.add(spot);
    }

    // Requirement: Filter available spots for the Entry/Exit panel [cite: 159]
    public List<ParkingSpot> getAvailableSpots() {
        return spots.stream().filter(s -> !s.isOccupied()).toList();
    }

    public int getFloorNumber() { return floorNumber; }
    public List<ParkingSpot> getSpots() { return spots; }
}