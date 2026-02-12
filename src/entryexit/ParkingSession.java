package entryexit;

import ParkingSpotLotFloor.ParkingSpot;
import java.time.LocalDateTime;
import vehicles.Vehicle;

/**
 * ParkingSession represents an active parking session.
 * Links together: Vehicle, ParkingSpot, and Ticket
 * 
 * Responsibilities:
 * - Track which vehicle is parked where
 * - Store entry/exit times
 * - Provide session information for billing
 * 
 * @author Member 3 - Entry/Exit Module
 */
public class ParkingSession {
    private Vehicle vehicle;
    private ParkingSpot spot;
    private Ticket ticket;
    private LocalDateTime entryTime;
    private LocalDateTime exitTime;

    /**
     * Constructor: Creates a new parking session
     * 
     * @param vehicle The vehicle being parked
     * @param spot The parking spot assigned
     * @param ticket The ticket issued at entry
     */
    public ParkingSession(Vehicle vehicle, ParkingSpot spot, Ticket ticket) {
        this.vehicle = vehicle;
        this.spot = spot;
        this.ticket = ticket;
        this.entryTime = ticket.getEntryTime();
        this.exitTime = null; // Still parked
    }

    /**
     * Marks the session as completed (vehicle exiting)
     */
    public void markExit() {
        this.exitTime = LocalDateTime.now();
    }

    /**
     * Checks if the vehicle is still parked (no exit time set)
     */
    public boolean isActive() {
        return exitTime == null;
    }

    /**
     * Gets the parking duration in hours (ceiling rounding)
     * Requirement: "Rounded up to the nearest hour (Ceiling rounding)"
     */
    public int getParkingDuration() {
        LocalDateTime endTime = (exitTime != null) ? exitTime : LocalDateTime.now();
        
        // Calculate duration in minutes
        long minutes = java.time.Duration.between(entryTime, endTime).toMinutes();
        
        // Convert to hours with ceiling rounding
        // Example: 61 minutes = 2 hours, 120 minutes = 2 hours
        int hours = (int) Math.ceil(minutes / 60.0);
        
        // Minimum 1 hour
        return Math.max(1, hours);
    }

    // Getters
    public Vehicle getVehicle() {
        return vehicle;
    }

    public ParkingSpot getSpot() {
        return spot;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public LocalDateTime getEntryTime() {
        return entryTime;
    }

    public LocalDateTime getExitTime() {
        return exitTime;
    }

    public String getLicensePlate() {
        return vehicle.getLicensePlate();
    }

    @Override
    public String toString() {
        return String.format("Session[%s] - Spot: %s, Entry: %s, Active: %s", 
            vehicle.getLicensePlate(), spot.getSpotID(), entryTime, isActive());
    }
}
