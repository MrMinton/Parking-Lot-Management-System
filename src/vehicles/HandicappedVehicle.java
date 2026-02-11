package vehicles;

public class HandicappedVehicle extends Vehicle {
    private boolean hasHandicappedCard;

    public HandicappedVehicle(String licensePlate, boolean hasHandicappedCard) {
        super(licensePlate, "Handicapped Vehicle");
        this.hasHandicappedCard = hasHandicappedCard;
    }

    public boolean hasCard() {
        return hasHandicappedCard;
    }
}