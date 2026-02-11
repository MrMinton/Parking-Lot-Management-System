package vehicles;

import ParkingSpotLotFloor.CompactSpot;
import ParkingSpotLotFloor.RegularSpot;
import ParkingSpotLotFloor.HandicappedSpot;
import ParkingSpotLotFloor.ReservedSpot;
import ParkingSpotLotFloor.ParkingSpot;

public class TestVehicleModule {

    public static void main(String[] args) {
        System.out.println("=== TESTING MEMBER 2 MODULE: VEHICLE & LOGIC ===\n");

        // 1. Setup Vehicles
        Vehicle moto = new Motorcycle("MOTO-01");
        Vehicle car = new Car("CAR-01");
        Vehicle suv = new SUV("SUV-01");
        Vehicle truck = new Truck("TRUCK-99"); // NEW
        Vehicle hcWithCard = new HandicappedVehicle("HC-YES", true);
        Vehicle hcNoCard = new HandicappedVehicle("HC-NO", false);

        // 2. Setup Spots
        ParkingSpot compactSpot = new CompactSpot("C-1");
        ParkingSpot regularSpot = new RegularSpot("R-1");
        ParkingSpot hcSpot = new HandicappedSpot("H-1");
        ParkingSpot reservedSpot = new ReservedSpot("VIP-1");

        // ---------------------------------------------------------
        // TEST A: Validation Logic (Who can park where?)
        // ---------------------------------------------------------
        System.out.println("--- Test A: Parking Validation ---");
        
        // Motorcycle
        testValidation(moto, compactSpot, true);  
        testValidation(moto, regularSpot, false); 

        // Car
        testValidation(car, compactSpot, true);   
        testValidation(car, regularSpot, true);   

        // SUV (Regular Only)
        testValidation(suv, compactSpot, false);  
        testValidation(suv, regularSpot, true);   

        // Truck (Regular Only) - NEW TEST
        testValidation(truck, compactSpot, false); 
        testValidation(truck, regularSpot, true);  

        // Handicapped
        testValidation(hcWithCard, reservedSpot, true); 
        testValidation(hcWithCard, compactSpot, true);  

        System.out.println();

        // ---------------------------------------------------------
        // TEST B: Discount Logic
        // ---------------------------------------------------------
        System.out.println("--- Test B: Hourly Rate Calculation ---");

        testRate(hcWithCard, hcSpot, 0.0);
        testRate(hcWithCard, regularSpot, 2.0);
        testRate(hcNoCard, hcSpot, 2.0);
        testRate(car, regularSpot, 5.0);
        
        // Truck pays standard Regular rate (RM 5.0)
        testRate(truck, regularSpot, 5.0); 
    }

    private static void testValidation(Vehicle v, ParkingSpot s, boolean expected) {
        boolean result = ValidationEngine.validateParking(v, s);
        String status = (result == expected) ? "PASS" : "FAIL";
        System.out.printf("[%s] %-12s trying to park in %-15s -> Result: %s (Expected: %s)%n", 
            status, v.getType(), s.getClass().getSimpleName(), result, expected);
    }

    private static void testRate(Vehicle v, ParkingSpot s, double expectedRate) {
        double rate = ValidationEngine.getEffectiveHourlyRate(v, s);
        boolean pass = Double.compare(rate, expectedRate) == 0;
        String status = pass ? "PASS" : "FAIL";
        System.out.printf("[%s] %-20s in %-15s -> Rate: RM %.2f (Expected: %.2f)%n", 
            status, v.getLicensePlate(), s.getClass().getSimpleName(), rate, expectedRate);
    }
}