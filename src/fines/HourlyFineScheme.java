public class HourlyFineScheme implements FineEngine {
    @Override
    public double calculateFine(double parkedHours) {
        // "RM 20 per hour for overstaying"
        if (parkedHours > 24) {
            double overstayHours = parkedHours - 24;
            double chargableOverstay = Math.ceil(overstayHours); 
            return chargableOverstay * 20.0;
        }
        return 0.0;
    }
}