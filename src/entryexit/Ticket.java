package entryexit;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Ticket class representing a parking ticket issued at entry.
 * Format: T-PLATE-TIMESTAMP
 * 
 * Responsibilities:
 * - Generate unique ticket ID
 * - Store entry information (time, spot, vehicle, rate)
 * - Display ticket details for customer
 * 
 * @author Member 3 - Entry/Exit Module
 */
public class Ticket {
    private String ticketID;
    private String licensePlate;
    private String vehicleType;
    private LocalDateTime entryTime;
    private String assignedSpotID;
    private double hourlyRate;

    /**
     * Constructor: Creates a new parking ticket
     * 
     * @param licensePlate Vehicle's license plate
     * @param vehicleType Type of vehicle (Car, Motorcycle, etc.)
     * @param assignedSpotID The parking spot ID (e.g., "F1-R1-S1")
     * @param hourlyRate The hourly rate for this parking session
     */
    public Ticket(String licensePlate, String vehicleType, String assignedSpotID, double hourlyRate) {
        this.licensePlate = licensePlate;
        this.vehicleType = vehicleType;
        this.assignedSpotID = assignedSpotID;
        this.hourlyRate = hourlyRate;
        this.entryTime = LocalDateTime.now();
        
        // Generate Ticket ID: T-PLATE-TIMESTAMP
        this.ticketID = generateTicketID(licensePlate, entryTime);
    }

    /**
     * Generates a unique ticket ID using the format: T-PLATE-TIMESTAMP
     * Example: T-ABC123-1738400000000
     */
    private String generateTicketID(String plate, LocalDateTime time) {
        long timestamp = System.currentTimeMillis();
        return String.format("T-%s-%d", plate, timestamp);
    }

    /**
     * Displays the ticket information in a formatted manner
     * Used for printing/displaying to the customer
     */
    public String displayTicket() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        StringBuilder sb = new StringBuilder();
        
        sb.append("╔════════════════════════════════════════╗\n");
        sb.append("║       PARKING TICKET - ENTRY          ║\n");
        sb.append("╠════════════════════════════════════════╣\n");
        sb.append(String.format("║ Ticket ID    : %-23s ║\n", ticketID));
        sb.append(String.format("║ License Plate: %-23s ║\n", licensePlate));
        sb.append(String.format("║ Vehicle Type : %-23s ║\n", vehicleType));
        sb.append(String.format("║ Spot Assigned: %-23s ║\n", assignedSpotID));
        sb.append(String.format("║ Entry Time   : %-23s ║\n", entryTime.format(formatter)));
        sb.append(String.format("║ Hourly Rate  : RM %-20.2f ║\n", hourlyRate));
        sb.append("╠════════════════════════════════════════╣\n");
        sb.append("║  Please keep this ticket for exit     ║\n");
        sb.append("╚════════════════════════════════════════╝\n");
        
        return sb.toString();
    }

    // Getters
    public String getTicketID() {
        return ticketID;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public LocalDateTime getEntryTime() {
        return entryTime;
    }

    public String getAssignedSpotID() {
        return assignedSpotID;
    }

    public double getHourlyRate() {
        return hourlyRate;
    }

    @Override
    public String toString() {
        return String.format("Ticket[%s] - Plate: %s, Spot: %s, Entry: %s", 
            ticketID, licensePlate, assignedSpotID, entryTime);
    }
}
