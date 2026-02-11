package entryexit;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Receipt class for exit payment processing.
 * 
 * Responsibilities:
 * - Display entry/exit times
 * - Show parking duration and fee breakdown
 * - Display unpaid fines (if any)
 * - Show total amount paid and payment method
 * 
 * Requirement: "Show receipt with: Entry time, exit time, Duration (hours),
 *               Parking fee breakdown, Any fines due, Total amount paid,
 *               Payment method used, Remaining balance (if any)"
 * 
 * @author Member 3 - Entry/Exit Module
 */
public class Receipt {
    private String licensePlate;
    private String vehicleType;
    private String spotID;
    private LocalDateTime entryTime;
    private LocalDateTime exitTime;
    private int durationHours;
    private double hourlyRate;
    private double parkingFee;
    private List<Fine> unpaidFines;
    private double totalFineAmount;
    private double totalAmount;
    private String paymentMethod;
    private double amountPaid;
    private double remainingBalance;

    /**
     * Constructor: Creates an exit receipt
     */
    public Receipt(String licensePlate, String vehicleType, String spotID,
                   LocalDateTime entryTime, LocalDateTime exitTime,
                   int durationHours, double hourlyRate, double parkingFee,
                   List<Fine> unpaidFines, double totalFineAmount,
                   double totalAmount, String paymentMethod,
                   double amountPaid, double remainingBalance) {
        this.licensePlate = licensePlate;
        this.vehicleType = vehicleType;
        this.spotID = spotID;
        this.entryTime = entryTime;
        this.exitTime = exitTime;
        this.durationHours = durationHours;
        this.hourlyRate = hourlyRate;
        this.parkingFee = parkingFee;
        this.unpaidFines = unpaidFines;
        this.totalFineAmount = totalFineAmount;
        this.totalAmount = totalAmount;
        this.paymentMethod = paymentMethod;
        this.amountPaid = amountPaid;
        this.remainingBalance = remainingBalance;
    }

    /**
     * Displays the receipt in a formatted manner
     */
    public String displayReceipt() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        StringBuilder sb = new StringBuilder();
        
        sb.append("╔════════════════════════════════════════════════════╗\n");
        sb.append("║          PARKING EXIT RECEIPT                     ║\n");
        sb.append("╠════════════════════════════════════════════════════╣\n");
        sb.append(String.format("║ License Plate : %-33s ║\n", licensePlate));
        sb.append(String.format("║ Vehicle Type  : %-33s ║\n", vehicleType));
        sb.append(String.format("║ Parking Spot  : %-33s ║\n", spotID));
        sb.append("╠════════════════════════════════════════════════════╣\n");
        sb.append(String.format("║ Entry Time    : %-33s ║\n", entryTime.format(formatter)));
        sb.append(String.format("║ Exit Time     : %-33s ║\n", exitTime.format(formatter)));
        sb.append(String.format("║ Duration      : %-33s ║\n", durationHours + " hour(s)"));
        sb.append("╠════════════════════════════════════════════════════╣\n");
        sb.append("║                 FEE BREAKDOWN                     ║\n");
        sb.append("╠════════════════════════════════════════════════════╣\n");
        sb.append(String.format("║ Hourly Rate   : RM %-30.2f ║\n", hourlyRate));
        sb.append(String.format("║ Parking Fee   : %d hrs × RM %.2f = RM %-14.2f ║\n", 
                                durationHours, hourlyRate, parkingFee));
        
        // Display unpaid fines if any
        if (!unpaidFines.isEmpty()) {
            sb.append("╠════════════════════════════════════════════════════╣\n");
            sb.append("║              UNPAID FINES                         ║\n");
            sb.append("╠════════════════════════════════════════════════════╣\n");
            
            for (Fine fine : unpaidFines) {
                String fineDetails = String.format("%s - RM %.2f", fine.getReason(), fine.getAmount());
                sb.append(String.format("║ • %-47s ║\n", fineDetails));
            }
            
            sb.append(String.format("║ Total Fines   : RM %-30.2f ║\n", totalFineAmount));
        }
        
        sb.append("╠════════════════════════════════════════════════════╣\n");
        sb.append(String.format("║ TOTAL AMOUNT  : RM %-30.2f ║\n", totalAmount));
        sb.append("╠════════════════════════════════════════════════════╣\n");
        sb.append(String.format("║ Payment Method: %-33s ║\n", paymentMethod));
        sb.append(String.format("║ Amount Paid   : RM %-30.2f ║\n", amountPaid));
        
        if (remainingBalance > 0) {
            sb.append(String.format("║ Change        : RM %-30.2f ║\n", remainingBalance));
        } else if (remainingBalance < 0) {
            sb.append(String.format("║ Balance Due   : RM %-30.2f ║\n", Math.abs(remainingBalance)));
        }
        
        sb.append("╠════════════════════════════════════════════════════╣\n");
        sb.append("║          Thank you for parking with us!           ║\n");
        sb.append("╚════════════════════════════════════════════════════╝\n");
        
        return sb.toString();
    }

    // Getters
    public String getLicensePlate() {
        return licensePlate;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public double getParkingFee() {
        return parkingFee;
    }

    public double getTotalFineAmount() {
        return totalFineAmount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    @Override
    public String toString() {
        return String.format("Receipt[%s] - Total: RM %.2f, Parking: RM %.2f, Fines: RM %.2f", 
            licensePlate, totalAmount, parkingFee, totalFineAmount);
    }
}
