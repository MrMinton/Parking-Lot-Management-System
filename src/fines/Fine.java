import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Fine {
    
	private double amount;
    private String fineID;
	private String reason;
	private LocalDateTime dateIssued;
    private boolean isPaid;
	private String licensePlate;
	private int durationHours;

    public Fine(String licensePlate, double amount, String reason, int durationHours) {
        this.licensePlate = licensePlate;
        this.amount = amount;
        this.reason = reason;
        this.isPaid = false;
        this.dateIssued = LocalDateTime.now();
        this.durationHours = durationHours;
        this.fineID = "F-" + licensePlate + "-" + System.currentTimeMillis();
    }

    public void markAsPaid() {
        this.isPaid = true;
    }
    // Helper to print nice details for the Receipt
    public String getDetails() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return String.format("[%s] %s - RM %.2f (%s)", 
            dateIssued.format(fmt), reason, amount, isPaid ? "PAID" : "UNPAID");
    }

    public String getFineID() { 
		return fineID; 
	}
    public String getLicensePlate() { 
		return licensePlate; 
	}
    public double getAmount() { 
		return amount; 
	}
    public boolean isPaid() { 
		return isPaid; 
	}
    public LocalDateTime getDateIssued() { 
		return dateIssued; 
	}
	public int getDurationHours() {
		return durationHours;
	}
	public String getReason() {
		return reason;
	}

}