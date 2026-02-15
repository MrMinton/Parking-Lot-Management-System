package database;

import entryexit.ParkingSession;
import entryexit.Ticket;
import vehicles.Car;
import vehicles.HandicappedVehicle;
import vehicles.Motorcycle;
import vehicles.SUV;
import vehicles.Truck;
import vehicles.Vehicle;
import ParkingSpotLotFloor.ParkingSpot; // Import ParkingSpot

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ParkingDAO {

    public void saveSession(ParkingSession session) {
        String sql = "INSERT INTO parking_sessions (plate_number, vehicle_type, spot_id, ticket_id, hourly_rate, entry_time, status) VALUES (?, ?, ?, ?, ?, ?, 'ACTIVE')";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, session.getLicensePlate());
            stmt.setString(2, session.getVehicle().getType());
            stmt.setString(3, session.getSpot().getSpotID());
            stmt.setString(4, session.getTicket().getTicketID());
            stmt.setDouble(5, session.getTicket().getHourlyRate());
            stmt.setTimestamp(6, Timestamp.valueOf(session.getEntryTime()));
            
            stmt.executeUpdate();
            System.out.println("[DB] Session saved for " + session.getLicensePlate());
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateSessionExit(String plate, double fee) {
        String sql = "UPDATE parking_sessions SET exit_time = NOW(), fee_charged = ?, status = 'COMPLETED' WHERE plate_number = ? AND status = 'ACTIVE'";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setDouble(1, fee);
            stmt.setString(2, plate);
            
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("[DB] Session closed for " + plate);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ParkingSession getActiveSession(String plate, ParkingSpot spot) {
        String sql = "SELECT * FROM parking_sessions WHERE plate_number = ? AND status = 'ACTIVE'";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, plate);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                String type = rs.getString("vehicle_type");
                Timestamp entryTime = rs.getTimestamp("entry_time");
                String ticketID = rs.getString("ticket_id");
                double rate = rs.getDouble("hourly_rate");
                
                Vehicle v = createVehicle(plate, type);
                
                // Reconstruct Ticket
                Ticket t = new Ticket(plate, type, spot.getSpotID(), rate);
                t.setTicketID(ticketID);
                t.setEntryTime(entryTime.toLocalDateTime());
                
                ParkingSession session = new ParkingSession(v, spot, t);
                // ParkingSession constructor infers entryTime from ticket.getEntryTime(), so it should be correct now that ticket is updated.
                
                return session;
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    // Helper to recreate vehicle object
    private Vehicle createVehicle(String plate, String type) {
        return switch (type) {
            case "Motorcycle" -> new Motorcycle(plate);
            case "SUV" -> new SUV(plate);
            case "Truck" -> new Truck(plate);
            case "Handicapped" -> new HandicappedVehicle(plate, true);
            default -> new Car(plate);
        };
    }
    
    public List<ParkingSession> getAllActiveSessions(java.util.Map<String, ParkingSpot> spotMap) {
        List<ParkingSession> sessions = new ArrayList<>();
        String sql = "SELECT * FROM parking_sessions WHERE status = 'ACTIVE'";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                String plate = rs.getString("plate_number");
                String type = rs.getString("vehicle_type");
                String spotId = rs.getString("spot_id");
                Timestamp entryTime = rs.getTimestamp("entry_time");
                String ticketID = rs.getString("ticket_id");
                double rate = rs.getDouble("hourly_rate");
                
                ParkingSpot spot = spotMap.get(spotId);
                if (spot != null) {
                    Vehicle v = createVehicle(plate, type);
                    Ticket t = new Ticket(plate, type, spotId, rate);
                    t.setTicketID(ticketID);
                    t.setEntryTime(entryTime.toLocalDateTime());
                    
                    ParkingSession session = new ParkingSession(v, spot, t);
                    sessions.add(session);
                    
                    // Also mark spot as occupied in standard memory model
                    spot.assignVehicle(plate);
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sessions;
    }
}
