package entryexit;

import ParkingSpotLotFloor.Floor;
import ParkingSpotLotFloor.ParkingLot;
import ParkingSpotLotFloor.ParkingSpot;
import fines.Fine;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import vehicles.ValidationEngine;
import vehicles.Vehicle;

/**
 * EntryExitManager - Main controller for vehicle entry and exit operations.
 * 
 * This class acts as a FACADE pattern, providing a simple interface to:
 * - Find available spots (using ValidationEngine)
 * - Park vehicles (create tickets, update spots)
 * - Process exits (calculate fees, handle fines, generate receipts)
 * 
 * Responsibilities:
 * - Manage active parking sessions
 * - Coordinate between Vehicle, Spot, Ticket, and Fine modules
 * - Implement entry/exit business logic
 * 
 * @author Member 3 - Entry/Exit Module
 */
public class EntryExitManager {
    // Active parking sessions: Maps license plate to ParkingSession
    private Map<String, ParkingSession> activeSessions;
    
    // Fine registry (shared with Member 4's module)
    private FineRegistry fineRegistry;

    /**
     * Constructor: Initializes the entry/exit manager
     */
    public EntryExitManager(FineRegistry fineRegistry) {
        this.activeSessions = new HashMap<>();
        this.fineRegistry = fineRegistry;
    }

    // ═══════════════════════════════════════════════════════════════
    //                     ENTRY PROCESS
    // ═══════════════════════════════════════════════════════════════

    /**
     * Step 1: Find available spots suitable for the vehicle
     * 
     * Requirement: "The system shows available spots of suitable types"
     * 
     * @param vehicle The vehicle looking for parking
     * @param parkingLot The parking lot to search
     * @return List of suitable available spots
     */
    public List<ParkingSpot> findAvailableSpots(Vehicle vehicle, ParkingLot parkingLot) {
        List<ParkingSpot> suitableSpots = new ArrayList<>();
        
        // Search all floors
        for (Floor floor : parkingLot.getFloors()) {
            // Get available spots on this floor
            List<ParkingSpot> availableSpots = floor.getAvailableSpots();
            
            // Filter spots using ValidationEngine
            for (ParkingSpot spot : availableSpots) {
                if (ValidationEngine.validateParking(vehicle, spot)) {
                    suitableSpots.add(spot);
                }
            }
        }
        
        return suitableSpots;
    }

    /**
     * Step 2-5: Park the vehicle
     * 
     * Requirements:
     * 2. "User selects a spot"
     * 3. "System marks the spot as occupied"
     * 4. "System records entry time & assigns spot"
     * 5. "System generates and displays a ticket"
     * 
     * @param vehicle The vehicle to park
     * @param spot The selected parking spot
     * @return The generated ticket
     * @throws IllegalStateException if vehicle already parked or spot occupied
     */
    public Ticket parkVehicle(Vehicle vehicle, ParkingSpot spot) {
        String plate = vehicle.getLicensePlate();
        
        // Validation 1: Check if vehicle is already parked
        if (activeSessions.containsKey(plate)) {
            throw new IllegalStateException("Vehicle " + plate + " is already parked!");
        }
        
        // Validation 2: Check if spot is available
        if (spot.isOccupied()) {
            throw new IllegalStateException("Spot " + spot.getSpotID() + " is already occupied!");
        }
        
        // Validation 3: Check if vehicle can park in this spot
        if (!ValidationEngine.validateParking(vehicle, spot)) {
            throw new IllegalStateException("Vehicle " + vehicle.getType() + 
                                          " cannot park in " + spot.getClass().getSimpleName());
        }
        
        // Get the effective hourly rate (handles handicapped discounts)
        double effectiveRate = ValidationEngine.getEffectiveHourlyRate(vehicle, spot);
        
        // Create the ticket (Requirement: T-PLATE-TIMESTAMP format)
        Ticket ticket = new Ticket(plate, vehicle.getType(), spot.getSpotID(), effectiveRate);
        
        // Mark spot as occupied (Requirement 3)
        spot.assignVehicle(plate);
        
        // Create parking session (Requirement 4)
        ParkingSession session = new ParkingSession(vehicle, spot, ticket);
        activeSessions.put(plate, session);
        
        System.out.println("[EntryExitManager] Vehicle parked successfully: " + plate + 
                          " in spot " + spot.getSpotID());
        
        // Return the ticket (Requirement 5)
        return ticket;
    }

    // ═══════════════════════════════════════════════════════════════
    //                     EXIT PROCESS
    // ═══════════════════════════════════════════════════════════════

    /**
     * Step 1-2: Find vehicle by license plate
     * 
     * Requirement: "User enters license plate number"
     * Requirement: "The system finds the vehicle and its entry time"
     * 
     * @param licensePlate The vehicle's license plate
     * @return The parking session, or null if not found
     */
    public ParkingSession findVehicleByPlate(String licensePlate) {
        return activeSessions.get(licensePlate);
    }

    /**
     * Step 3-4: Calculate parking fee
     * 
     * Requirement: "The system calculates parking duration in hours"
     * Requirement: "The system calculates the fee based on the spot type and duration"
     * 
     * @param session The parking session
     * @return The parking fee
     */
    public double calculateParkingFee(ParkingSession session) {
        int hours = session.getParkingDuration(); // Ceiling rounding handled in ParkingSession
        double rate = session.getTicket().getHourlyRate();
        return hours * rate;
    }

    /**
     * Step 5-9: Process exit and payment
     * 
     * Requirements:
     * 5. "System checks if there are unpaid fines"
     * 6. "System shows: Hours parked, Parking fee, Any unpaid fines, Total payment due"
     * 7. "The system accepts payment"
     * 8. "System marks the spot as available"
     * 9. "System generates exit receipt"
     * 
     * @param licensePlate The vehicle's license plate
     * @param paymentMethod "Cash" or "Card"
     * @param amountPaid Amount paid by customer
     * @return The exit receipt
     * @throws IllegalArgumentException if vehicle not found or payment insufficient
     */
    public Receipt processExit(String licensePlate, String paymentMethod, double amountPaid) {
        // Step 1-2: Find the vehicle
        ParkingSession session = findVehicleByPlate(licensePlate);
        if (session == null) {
            throw new IllegalArgumentException("Vehicle " + licensePlate + " not found in parking lot!");
        }
        
        // Mark exit time
        session.markExit();
        
        // Step 3-4: Calculate parking fee
        int durationHours = session.getParkingDuration();
        double parkingFee = calculateParkingFee(session);
        
        // Step 5: Check for unpaid fines
        List<Fine> unpaidFines = fineRegistry.getUnpaidFines(licensePlate);
        double totalFineAmount = fineRegistry.getTotalUnpaidAmount(licensePlate);
        
        // Step 6: Calculate total amount due
        double totalAmount = parkingFee + totalFineAmount;
        
        // Step 7: Validate payment
        if (amountPaid < totalAmount) {
            throw new IllegalArgumentException(
                String.format("Insufficient payment! Total due: RM %.2f, Paid: RM %.2f", 
                             totalAmount, amountPaid));
        }
        
        double remainingBalance = amountPaid - totalAmount; // Change or balance
        
        // Mark fines as paid
        if (totalFineAmount > 0) {
            fineRegistry.markFinesAsPaid(licensePlate);
        }
        
        // Step 8: Mark spot as available
        session.getSpot().removeVehicle();
        
        // Remove from active sessions
        activeSessions.remove(licensePlate);
        
        System.out.println("[EntryExitManager] Vehicle exited successfully: " + licensePlate);
        
        // Step 9: Generate receipt
        Receipt receipt = new Receipt(
            licensePlate,
            session.getVehicle().getType(),
            session.getSpot().getSpotID(),
            session.getEntryTime(),
            session.getExitTime(),
            durationHours,
            session.getTicket().getHourlyRate(),
            parkingFee,
            unpaidFines,
            totalFineAmount,
            totalAmount,
            paymentMethod,
            amountPaid,
            remainingBalance
        );
        
        return receipt;
    }

    // ═══════════════════════════════════════════════════════════════
    //                  UTILITY & REPORTING METHODS
    // ═══════════════════════════════════════════════════════════════

    /**
     * Gets all currently parked vehicles
     * Used for admin reporting
     */
    public List<ParkingSession> getCurrentlyParkedVehicles() {
        return new ArrayList<>(activeSessions.values());
    }

    /**
     * Gets the number of currently parked vehicles
     */
    public int getCurrentOccupancy() {
        return activeSessions.size();
    }

    /**
     * Checks if a vehicle is currently parked
     */
    public boolean isVehicleParked(String licensePlate) {
        return activeSessions.containsKey(licensePlate);
    }

    /**
     * Gets a parking session by license plate (for admin queries)
     */
    public ParkingSession getSession(String licensePlate) {
        return activeSessions.get(licensePlate);
    }

    /**
     * Gets total revenue from all active sessions (estimated)
     * Note: This is an estimate for currently parked vehicles
     */
    public double getEstimatedRevenue() {
        double total = 0.0;
        for (ParkingSession session : activeSessions.values()) {
            total += calculateParkingFee(session);
        }
        return total;
    }
}
