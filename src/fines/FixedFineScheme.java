public class FixedFineScheme implements FineEngine {
    @Override
    public double calculateFine(double parkedHours) {
        if (parkedHours > 24) {
            return 50.0;
        }
        return 0.0;
    }
}