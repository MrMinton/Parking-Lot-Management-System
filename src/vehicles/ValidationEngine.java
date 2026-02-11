package vehicles;

import ParkingSpotLotFloor.ParkingSpot;
import ParkingSpotLotFloor.CompactSpot;
import ParkingSpotLotFloor.RegularSpot;
import ParkingSpotLotFloor.HandicappedSpot;

public class ValidationEngine {

    /**
     * Requirement: Ensure a vehicle only parks in a valid spot.
     * * Rules:
     * - Motorcycle: Compact spots only.
     * - Car: Compact or Regular spots.
     * - SUV AND Truck: Regular spots only.
     * - Handicapped Vehicle: Can park in any spot.
     */
    public static boolean validateParking(Vehicle vehicle, ParkingSpot spot) {
        boolean isCompactSpot = spot instanceof CompactSpot;
        boolean isRegularSpot = spot instanceof RegularSpot;
        
        // 1. Motorcycle - Can park in Compact spots only
        if (vehicle instanceof Motorcycle) {
            return isCompactSpot;
        } 
        
        // 2. Car - Can park in Compact or Regular spots
        else if (vehicle instanceof Car) {
            return isCompactSpot || isRegularSpot;
        } 
        
        // 3. SUV and Truck - Can park in Regular spots only
        else if (vehicle instanceof SUV || vehicle instanceof Truck) {
            return isRegularSpot;
        } 
        
        // 4. Handicapped Vehicle - Can park in any spot
        else if (vehicle instanceof HandicappedVehicle) {
            return true; 
        }
        
        return false; 
    }

    /**
     * Requirement: Handle the logic for Handicapped discounts.
     */
    public static double getEffectiveHourlyRate(Vehicle vehicle, ParkingSpot spot) {
        double standardRate = spot.getHourlyRate();

        if (vehicle instanceof HandicappedVehicle) {
            HandicappedVehicle hv = (HandicappedVehicle) vehicle;
            
            if (hv.hasCard()) {
                if (spot instanceof HandicappedSpot) {
                    return 0.0;
                }
                return 2.0;
            }
        }
        
        return standardRate;
    }
}