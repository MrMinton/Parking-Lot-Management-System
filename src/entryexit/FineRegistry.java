package entryexit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    // Maps license plate to list of fines
    private Map<String, List<Fine>> finesByPlate;

    /**
     * Constructor: Initializes the fine registry
     */
    public FineRegistry() {
        this.finesByPlate = new HashMap<>();
    }

    /**
     * Adds a fine to a vehicle's record
     * 
     * @param fine The fine to add (already created by Member 4's module)
     */
    public void addFine(Fine fine) {
        String plate = fine.getLicensePlate();
        
        // Get or create the fine list for this plate
        finesByPlate.putIfAbsent(plate, new ArrayList<>());
        finesByPlate.get(plate).add(fine);
        
        System.out.println("[FineRegistry] Fine added: " + fine.getFineID() + 
                          " for " + plate + " - RM " + fine.getAmount());
    }

    /**
     * Gets all unpaid fines for a license plate
     * 
     * @param licensePlate The vehicle's license plate
     * @return List of unpaid fines (empty list if none)
     */
    public List<Fine> getUnpaidFines(String licensePlate) {
        List<Fine> allFines = finesByPlate.getOrDefault(licensePlate, new ArrayList<>());
        
        // Filter only unpaid fines
        List<Fine> unpaidFines = new ArrayList<>();
        for (Fine fine : allFines) {
            if (!fine.isPaid()) {
                unpaidFines.add(fine);
            }
        }
        
        return unpaidFines;
    }

    /**
     * Calculates total unpaid fine amount for a license plate
     * 
     * @param licensePlate The vehicle's license plate
     * @return Total unpaid fine amount
     */
    public double getTotalUnpaidAmount(String licensePlate) {
        List<Fine> unpaidFines = getUnpaidFines(licensePlate);
        
        double total = 0.0;
        for (Fine fine : unpaidFines) {
            total += fine.getAmount();
        }
        
        return total;
    }

    /**
     * Marks all unpaid fines for a license plate as paid
     * Called during exit payment processing
     * 
     * @param licensePlate The vehicle's license plate
     */
    public void markFinesAsPaid(String licensePlate) {
        List<Fine> unpaidFines = getUnpaidFines(licensePlate);
        
        for (Fine fine : unpaidFines) {
            fine.markAsPaid();
        }
        
        if (!unpaidFines.isEmpty()) {
            System.out.println("[FineRegistry] Marked " + unpaidFines.size() + 
                              " fine(s) as paid for " + licensePlate);
        }
    }

    /**
     * Gets all fines (paid and unpaid) for a license plate
     * Used for admin reporting
     * 
     * @param licensePlate The vehicle's license plate
     * @return List of all fines
     */
    public List<Fine> getAllFines(String licensePlate) {
        return finesByPlate.getOrDefault(licensePlate, new ArrayList<>());
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
        int count = 0;
        for (List<Fine> fines : finesByPlate.values()) {
            for (Fine fine : fines) {
                if (!fine.isPaid()) {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * Gets total unpaid fine amount across all vehicles
     * Used for admin revenue reporting
     */
    public double getTotalUnpaidFinesAmount() {
        double total = 0.0;
        for (List<Fine> fines : finesByPlate.values()) {
            for (Fine fine : fines) {
                if (!fine.isPaid()) {
                    total += fine.getAmount();
                }
            }
        }
        return total;
    }
}
