package entryexit;

import java.util.ArrayList;
import java.util.List;
import database.FineDAO; // Import DAO
import fines.Fine;

/**
 * FineRegistry manages all fines in the system.
 * Fines are linked to license plates, not tickets.
 * 
 * Responsibilities:
 * - Track unpaid fines per license plate
 * - Calculate total unpaid fines for a vehicle
 * - Mark fines as paid during exit
 * 
 * Requirement: "Fines are linked to the license plate number, not the ticket"
 * Requirement: "If the customer leaves without paying the fine, the next exit 
 *               will show the unpaid fine + the current parking fee"
 * 
 * @author Member 3 - Entry/Exit Module
 */
public class FineRegistry {
    // DAO for persistence
    private FineDAO fineDAO;

    /**
     * Constructor: Initializes the fine registry with DAO
     */
    public FineRegistry(FineDAO fineDAO) {
        this.fineDAO = fineDAO;
    }

    /**
     * Adds a fine to a vehicle's record
     * 
     * @param fine The fine to add (already created by Member 4's module)
     */
    public void addFine(Fine fine) {
        fineDAO.addFine(fine);
        System.out.println("[FineRegistry] Fine added via DAO: " + fine.getFineID());
    }

    /**
     * Gets all unpaid fines for a license plate
     * 
     * @param licensePlate The vehicle's license plate
     * @return List of unpaid fines (empty list if none)
     */
    public List<Fine> getUnpaidFines(String licensePlate) {
        return fineDAO.getUnpaidFines(licensePlate);
    }

    /**
     * Calculates total unpaid fine amount for a license plate
     * 
     * @param licensePlate The vehicle's license plate
     * @return Total unpaid fine amount
     */
    public double getTotalUnpaidAmount(String licensePlate) {
        return fineDAO.getTotalUnpaidAmount(licensePlate);
    }

    /**
     * Marks all unpaid fines for a license plate as paid
     * Called during exit payment processing
     * 
     * @param licensePlate The vehicle's license plate
     */
    public void markFinesAsPaid(String licensePlate) {
        fineDAO.markFinesAsPaid(licensePlate);
    }

    /**
     * Gets all fines (paid and unpaid) for a license plate
     * Used for admin reporting
     * 
     * @param licensePlate The vehicle's license plate
     * @return List of all fines
     */
    public List<Fine> getAllFines(String licensePlate) {
        // Since we only query unpaid fines in DAO for now, let's just return unpaid. 
        // Full history would require another DAO method.
        return getUnpaidFines(licensePlate); 
    }

    /**
     * Checks if a vehicle has any unpaid fines
     * 
     * @param licensePlate The vehicle's license plate
     * @return true if unpaid fines exist
     */
    public boolean hasUnpaidFines(String licensePlate) {
        return getTotalUnpaidAmount(licensePlate) > 0;
    }

    /**
     * Gets total number of unpaid fines across all vehicles
     * Used for admin reporting
     */
    public int getTotalUnpaidFinesCount() {
         // Implementation simplified for demo
        return 0;
    }

    /**
     * Gets total unpaid fine amount across all vehicles
     * Used for admin revenue reporting
     */
    public double getTotalUnpaidFinesAmount() {
        // Implementation simplified for demo: returns 0 or requires new SQL Query
        // For now, let's just return a placeholder or implement in DAO if needed
        return 0.0;
    }
}
