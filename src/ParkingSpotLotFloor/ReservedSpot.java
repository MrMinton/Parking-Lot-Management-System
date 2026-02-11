package ParkingSpotLotFloor;

public class ReservedSpot extends ParkingSpot {
    public ReservedSpot(String spotID) { 
        super(spotID, 10.0); // RM 10/hour
    } 
}