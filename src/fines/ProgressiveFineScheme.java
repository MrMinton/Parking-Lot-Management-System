public class ProgressiveFineScheme implements FineEngine {
    @Override
    public double calculateFine(double parkedHours) {
        if (parkedHours <= 24) return 0.0;

        double fine = 0.0;
        double overstayDuration = parkedHours - 24;

		// 1. First 24 hours of overstay (Hours 25-48 of total parking)
        if (overstayDuration > 0) {
            fine += 50.0;
        }

		// 2. Hours 24-48 of overstay (Hours 49-72 of total parking)	 
        if (overstayDuration > 24) {
            fine += 100.0;
        }

		// 3. Hours 48-72 of overstay (Hours 73-96 of total parking)
        if (overstayDuration > 48) {
            fine += 150.0;
        }

		// 4. Beyond 72 hours of overstay (Hours 97+ of total parking)
        if (overstayDuration > 72) {
            fine += 200.0;
        }

        return fine;
    }
}