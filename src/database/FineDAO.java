package database;

import fines.Fine;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FineDAO {

    public void addFine(Fine fine) {
        String sql = "INSERT INTO fines (plate_number, fine_ref_id, amount, reason, status) VALUES (?, ?, ?, ?, 'UNPAID')";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, fine.getLicensePlate());
            stmt.setString(2, fine.getFineID());
            stmt.setDouble(3, fine.getAmount());
            stmt.setString(4, fine.getReason());
            
            stmt.executeUpdate();
            System.out.println("[DB] Fine added for " + fine.getLicensePlate());
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Fine> getUnpaidFines(String plate) {
        List<Fine> fines = new ArrayList<>();
        String sql = "SELECT * FROM fines WHERE plate_number = ? AND status = 'UNPAID'";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, plate);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                String fineID = rs.getString("fine_ref_id");
                Timestamp createdAt = rs.getTimestamp("created_at");
                
                Fine f = new Fine(
                    rs.getString("plate_number"),
                    rs.getDouble("amount"),
                    rs.getString("reason"),
                    0 
                );
                f.setFineID(fineID);
                if (createdAt != null) f.setDateIssued(createdAt.toLocalDateTime());
                
                fines.add(f);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return fines;
    }

    public void markFinesAsPaid(String plate) {
        String sql = "UPDATE fines SET status = 'PAID' WHERE plate_number = ? AND status = 'UNPAID'";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, plate);
            int rows = stmt.executeUpdate();
            System.out.println("[DB] Marked " + rows + " fines as paid for " + plate);
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public double getTotalUnpaidAmount(String plate) {
        String sql = "SELECT SUM(amount) FROM fines WHERE plate_number = ? AND status = 'UNPAID'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, plate);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                return rs.getDouble(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }
}
