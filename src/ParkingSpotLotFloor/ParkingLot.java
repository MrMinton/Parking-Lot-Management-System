import java.util.ArrayList;
import java.util.List;

public class ParkingLot {
    private String name;
    private List<Floor> floors;

    public ParkingLot(String name) {
        this.name = name;
        this.floors = new ArrayList<>();
    }

    public void addFloor(Floor floor) {
        floors.add(floor);
    }

    /**
     * Helper logic to initialize a floor with generated Spot IDs
     * Format: F[Floor]-R[Row]-S[Spot] 
     */
    public void initializeFloor(int floorNum, int rows, int spotsPerRow, String type) {
        Floor floor = new Floor(floorNum);
        for (int row = 1; row <= rows; row++) {
            for (int spot = 1; spot <= spotsPerRow; spot++) {
                String id = generateSpotId(floorNum, row, spot);
                floor.addSpot(createSpot(type, id));
            }
        }
        this.addFloor(floor);
    }

    private String generateSpotId(int floorNum, int row, int spot) {
        return String.format("F%d-R%d-S%d", floorNum, row, spot);
    }

    private ParkingSpot createSpot(String type, String id) {
        return switch (type.toLowerCase()) {
            case "compact" -> new CompactSpot(id);
            case "handicapped" -> new HandicappedSpot(id);
            case "reserved" -> new ReservedSpot(id);
            default -> new RegularSpot(id);
        };
    }

    public int getTotalOccupancy() {
        return floors.stream()
                .mapToInt(floor -> (int) floor.getSpots().stream()
                        .filter(ParkingSpot::isOccupied)
                        .count())
                .sum();
    }

    public List<Floor> getFloors() { return floors; }
}