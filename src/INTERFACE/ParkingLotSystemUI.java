package INTERFACE;

import ParkingSpotLotFloor.*;
import entryexit.*;
import fines.*;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import vehicles.*;

/**
 * Member 5 - Main GUI Integration
 * Package: UI
 */
public class ParkingLotSystemUI extends JFrame {

    private static final long serialVersionUID = 1L;

    // --- Core Systems ---
    private ParkingLot parkingLot;
    private EntryExitManager entryExitManager;
    private FineRegistry fineRegistry;
    private FineEngine activeFineScheme;

    // --- GUI Components ---
    // [FIX 1] Removed 'mainTabPane' from here (it's now local in constructor)
    // [FIX 2] Added 'final' to feedbackArea
    private final JTextArea feedbackArea;
    
    private DefaultTableModel spotStatusModel; 
    private JLabel adminOccupancyLbl, adminRevenueLbl, adminFinesLbl;
    private DefaultTableModel activeVehiclesModel; 
    private JTextArea reportingStatsArea; 
    
    // Entry Panel Components
    private JTextField plateEntry;
    private JComboBox<String> typeCombo;
    private JCheckBox cardCheck;
    private JComboBox<String> spotCombo;
    private JButton parkBtn;

    // Exit Panel Components
    private JTextField plateExit;
    private JTextField payInput;
    private JComboBox<String> methodCombo;
    private JLabel totalLabel;
    private JButton payBtn;

    public ParkingLotSystemUI() {
        // 1. Setup Main Window Frame
        setTitle("Parking Lot Management System (CCP6224)");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // 2. Setup Header
        JLabel header = new JLabel("University Parking Management", SwingConstants.CENTER);
        header.setFont(new Font("Segoe UI", Font.BOLD, 24));
        header.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        add(header, BorderLayout.NORTH);

        // 3. Setup Feedback Area
        feedbackArea = new JTextArea(5, 50);
        feedbackArea.setEditable(false);
        feedbackArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scroll = new JScrollPane(feedbackArea);
        scroll.setBorder(BorderFactory.createTitledBorder("System Logs & Receipts"));
        add(scroll, BorderLayout.SOUTH);

        // 4. Initialize Logic
        initializeSystem();

        // 5. Create Tabs
        // [FIX 1] 'mainTabPane' is defined locally here
        JTabbedPane mainTabPane = new JTabbedPane();
        mainTabPane.addTab("Entry / Exit Panel", createEntryExitPanel());
        mainTabPane.addTab("Admin Panel", createAdminPanel());
        mainTabPane.addTab("Reporting Panel", createReportingPanel());
        add(mainTabPane, BorderLayout.CENTER);

        // 6. Initial Data Load
        refreshAllData();
    }

    private void initializeSystem() {
        fineRegistry = new FineRegistry();
        entryExitManager = new EntryExitManager(fineRegistry);
        activeFineScheme = new HourlyFineScheme(); 

        parkingLot = new ParkingLot("University Central");
        parkingLot.initializeFloor(1, 4, 5, "compact"); 
        parkingLot.initializeFloor(1, 2, 5, "handicapped");
        parkingLot.initializeFloor(2, 6, 8, "regular");
        parkingLot.initializeFloor(3, 2, 6, "reserved");

        log("System Initialized. Default Fine Scheme: Hourly.");
    }

    // =================================================================================
    // 1. ENTRY / EXIT PANEL
    // =================================================================================
    private JPanel createEntryExitPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 2, 15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // --- LEFT: Vehicle Entry Interface ---
        JPanel entryPanel = new JPanel(new GridBagLayout());
        entryPanel.setBorder(BorderFactory.createTitledBorder("Vehicle Entry"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        plateEntry = new JTextField(10);
        typeCombo = new JComboBox<>(new String[]{"Car", "Motorcycle", "SUV", "Truck", "Handicapped"});
        cardCheck = new JCheckBox("Handicapped Card Holder?");
        JButton searchBtn = new JButton("Search Available Spots");
        spotCombo = new JComboBox<>();
        parkBtn = new JButton("Park Vehicle & Generate Ticket");
        parkBtn.setEnabled(false);

        gbc.gridx=0; gbc.gridy=0; entryPanel.add(new JLabel("License Plate:"), gbc);
        gbc.gridx=1; entryPanel.add(plateEntry, gbc);
        gbc.gridx=0; gbc.gridy=1; entryPanel.add(new JLabel("Vehicle Type:"), gbc);
        gbc.gridx=1; entryPanel.add(typeCombo, gbc);
        gbc.gridx=0; gbc.gridy=2; gbc.gridwidth=2; entryPanel.add(cardCheck, gbc);
        gbc.gridx=0; gbc.gridy=3; entryPanel.add(searchBtn, gbc);
        gbc.gridx=0; gbc.gridy=4; gbc.gridwidth=1; entryPanel.add(new JLabel("Select Spot:"), gbc);
        gbc.gridx=1; entryPanel.add(spotCombo, gbc);
        gbc.gridx=0; gbc.gridy=5; gbc.gridwidth=2; entryPanel.add(parkBtn, gbc);

        // --- RIGHT: Vehicle Exit Interface ---
        JPanel exitPanel = new JPanel(new GridBagLayout());
        exitPanel.setBorder(BorderFactory.createTitledBorder("Vehicle Exit & Payment"));
        
        plateExit = new JTextField(10);
        JButton calcBtn = new JButton("Calculate Fee & Fines");
        totalLabel = new JLabel("Total Due: RM 0.00");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 16));
        payInput = new JTextField(10);
        methodCombo = new JComboBox<>(new String[]{"Cash", "Credit Card"});
        payBtn = new JButton("Process Payment & Exit");
        payBtn.setEnabled(false);

        gbc.gridx=0; gbc.gridy=0; gbc.gridwidth=1; exitPanel.add(new JLabel("License Plate:"), gbc);
        gbc.gridx=1; exitPanel.add(plateExit, gbc);
        gbc.gridx=0; gbc.gridy=1; gbc.gridwidth=2; exitPanel.add(calcBtn, gbc);
        gbc.gridx=0; gbc.gridy=2; exitPanel.add(totalLabel, gbc);
        gbc.gridx=0; gbc.gridy=3; gbc.gridwidth=1; exitPanel.add(new JLabel("Payment Amount (RM):"), gbc);
        gbc.gridx=1; exitPanel.add(payInput, gbc);
        gbc.gridx=0; gbc.gridy=4; exitPanel.add(new JLabel("Method:"), gbc);
        gbc.gridx=1; exitPanel.add(methodCombo, gbc);
        gbc.gridx=0; gbc.gridy=5; gbc.gridwidth=2; exitPanel.add(payBtn, gbc);

        // --- ACTION LISTENERS ---
        searchBtn.addActionListener(e -> {
            Vehicle v = createVehicleFromInput();
            if (v == null) return;
            List<ParkingSpot> spots = entryExitManager.findAvailableSpots(v, parkingLot);
            spotCombo.removeAllItems();
            if(spots.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No valid spots found for " + v.getType());
                parkBtn.setEnabled(false);
            } else {
                for(ParkingSpot s : spots) spotCombo.addItem(s.getSpotID());
                parkBtn.setEnabled(true);
                log("Found " + spots.size() + " spots for " + v.getType());
            }
        });

        parkBtn.addActionListener(e -> {
            try {
                String spotID = (String) spotCombo.getSelectedItem();
                Vehicle v = createVehicleFromInput();
                if (v == null) return;
                ParkingSpot selectedSpot = null;
                for(Floor f : parkingLot.getFloors()) {
                    for(ParkingSpot s : f.getSpots()) {
                        if(s.getSpotID().equals(spotID)) selectedSpot = s;
                    }
                }
                if (selectedSpot != null) {
                    Ticket t = entryExitManager.parkVehicle(v, selectedSpot);
                    feedbackArea.setText(t.displayTicket());
                    log("Vehicle Parked: " + v.getLicensePlate());
                    plateEntry.setText("");
                    parkBtn.setEnabled(false);
                    spotCombo.removeAllItems();
                    refreshAllData();
                }
            } catch (RuntimeException ex) { // [FIX 3] Specific Exception
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        calcBtn.addActionListener(e -> {
            String plate = plateExit.getText().trim();
            ParkingSession session = entryExitManager.getSession(plate);
            if(session == null) {
                JOptionPane.showMessageDialog(this, "Vehicle not found!");
                return;
            }
            int hours = session.getParkingDuration();
            double fineVal = activeFineScheme.calculateFine(hours);
            if(fineVal > 0) {
                Fine fine = new Fine(plate, fineVal, "Overstay ("+hours+" hrs)", hours);
                fineRegistry.addFine(fine);
                log("ALERT: Fine of RM " + fineVal + " applied for overstay.");
            }
            double fee = entryExitManager.calculateParkingFee(session);
            double fines = fineRegistry.getTotalUnpaidAmount(plate);
            double total = fee + fines;
            totalLabel.setText(String.format("Total Due: RM %.2f", total));
            feedbackArea.setText(String.format("BILLING SUMMARY:\nDuration: %d hrs\nFee: RM %.2f\nFines: RM %.2f\nTOTAL: RM %.2f", hours, fee, fines, total));
            payBtn.setEnabled(true);
        });

        payBtn.addActionListener(e -> {
            try {
                String plate = plateExit.getText().trim();
                double paid = Double.parseDouble(payInput.getText());
                String method = (String) methodCombo.getSelectedItem();
                Receipt r = entryExitManager.processExit(plate, method, paid);
                feedbackArea.setText(r.displayReceipt());
                plateExit.setText("");
                payInput.setText("");
                totalLabel.setText("Total Due: RM 0.00");
                payBtn.setEnabled(false);
                refreshAllData();
            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(this, "Invalid money amount.");
            } catch (RuntimeException ex) { // [FIX 3] Specific Exception
                JOptionPane.showMessageDialog(this, "Payment Error: " + ex.getMessage());
            }
        });

        panel.add(entryPanel);
        panel.add(exitPanel);
        return panel;
    }

    private Vehicle createVehicleFromInput() {
        String plate = plateEntry.getText().trim();
        String type = (String) typeCombo.getSelectedItem();
        if(plate.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a license plate.");
            return null;
        }
        return switch(type) {
            case "Motorcycle" -> new Motorcycle(plate);
            case "SUV" -> new SUV(plate);
            case "Truck" -> new Truck(plate);
            case "Handicapped" -> new HandicappedVehicle(plate, cardCheck.isSelected());
            default -> new Car(plate);
        };
    }

    // =================================================================================
    // 2. ADMIN PANEL
    // =================================================================================
    private JPanel createAdminPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 10, 0));
        statsPanel.setBorder(BorderFactory.createTitledBorder("Real-time Dashboard"));
        
        adminOccupancyLbl = new JLabel("Occupancy: 0%");
        adminOccupancyLbl.setFont(new Font("Arial", Font.BOLD, 18));
        adminOccupancyLbl.setHorizontalAlignment(SwingConstants.CENTER);
        
        adminRevenueLbl = new JLabel("Total Revenue: RM 0.00");
        adminRevenueLbl.setFont(new Font("Arial", Font.BOLD, 18));
        adminRevenueLbl.setForeground(new Color(0, 100, 0)); 
        adminRevenueLbl.setHorizontalAlignment(SwingConstants.CENTER);
        
        adminFinesLbl = new JLabel("Unpaid Fines: RM 0.00");
        adminFinesLbl.setFont(new Font("Arial", Font.BOLD, 18));
        adminFinesLbl.setForeground(Color.RED);
        adminFinesLbl.setHorizontalAlignment(SwingConstants.CENTER);

        statsPanel.add(adminOccupancyLbl);
        statsPanel.add(adminRevenueLbl);
        statsPanel.add(adminFinesLbl);
        
        String[] cols = {"Floor", "Spot ID", "Type", "Hourly Rate", "Status", "Occupied By"};
        spotStatusModel = new DefaultTableModel(cols, 0);
        JTable spotTable = new JTable(spotStatusModel);
        JScrollPane tableScroll = new JScrollPane(spotTable);
        tableScroll.setBorder(BorderFactory.createTitledBorder("Floor & Spot Status"));

        JPanel finePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        finePanel.setBorder(BorderFactory.createTitledBorder("Configuration: Fine Calculation Scheme"));
        
        JRadioButton rbFixed = new JRadioButton("Option A: Fixed (RM 50)");
        JRadioButton rbProg = new JRadioButton("Option B: Progressive");
        JRadioButton rbHourly = new JRadioButton("Option C: Hourly (RM 20/hr)", true);
        
        ButtonGroup bg = new ButtonGroup();
        bg.add(rbFixed); bg.add(rbProg); bg.add(rbHourly);
        
        rbFixed.addActionListener(e -> { activeFineScheme = new FixedFineScheme(); log("Admin: Switched to Fixed Fine Scheme."); });
        rbProg.addActionListener(e -> { activeFineScheme = new ProgressiveFineScheme(); log("Admin: Switched to Progressive Fine Scheme."); });
        rbHourly.addActionListener(e -> { activeFineScheme = new HourlyFineScheme(); log("Admin: Switched to Hourly Fine Scheme."); });

        finePanel.add(rbFixed);
        finePanel.add(rbProg);
        finePanel.add(rbHourly);

        panel.add(statsPanel, BorderLayout.NORTH);
        panel.add(tableScroll, BorderLayout.CENTER);
        panel.add(finePanel, BorderLayout.SOUTH);

        return panel;
    }

    // =================================================================================
    // 3. REPORTING PANEL
    // =================================================================================
    private JPanel createReportingPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        String[] cols = {"License Plate", "Spot ID", "Entry Time", "Current Accrued Fee"};
        activeVehiclesModel = new DefaultTableModel(cols, 0);
        JTable vehicleTable = new JTable(activeVehiclesModel);
        JScrollPane vehicleScroll = new JScrollPane(vehicleTable);
        vehicleScroll.setBorder(BorderFactory.createTitledBorder("Vehicles Currently in Lot"));
        vehicleScroll.setPreferredSize(new Dimension(800, 300));

        reportingStatsArea = new JTextArea();
        reportingStatsArea.setEditable(false);
        reportingStatsArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        JScrollPane statsScroll = new JScrollPane(reportingStatsArea);
        statsScroll.setBorder(BorderFactory.createTitledBorder("System Statistics Reports"));

        JButton refreshBtn = new JButton("Refresh All Reports");
        refreshBtn.addActionListener(e -> refreshAllData());

        panel.add(refreshBtn, BorderLayout.NORTH);
        panel.add(vehicleScroll, BorderLayout.CENTER);
        panel.add(statsScroll, BorderLayout.SOUTH);

        return panel;
    }

    private void refreshAllData() {
        activeVehiclesModel.setRowCount(0);
        List<ParkingSession> sessions = entryExitManager.getCurrentlyParkedVehicles();
        for(ParkingSession s : sessions) {
            activeVehiclesModel.addRow(new Object[]{
                s.getLicensePlate(),
                s.getSpot().getSpotID(),
                s.getEntryTime().toString(),
                String.format("RM %.2f", entryExitManager.calculateParkingFee(s))
            });
        }

        spotStatusModel.setRowCount(0);
        int totalSpots = 0;
        int occupiedSpots = 0;

        for(Floor floor : parkingLot.getFloors()) {
            for(ParkingSpot spot : floor.getSpots()) {
                totalSpots++;
                if(spot.isOccupied()) occupiedSpots++;
                
                String type = "Regular";
                if(spot instanceof CompactSpot) type = "Compact";
                else if(spot instanceof HandicappedSpot) type = "Handicapped";
                else if(spot instanceof ReservedSpot) type = "Reserved";

                spotStatusModel.addRow(new Object[]{
                    floor.getFloorNumber(),
                    spot.getSpotID(),
                    type,
                    String.format("RM %.2f", spot.getHourlyRate()),
                    spot.isOccupied() ? "OCCUPIED" : "Available",
                    spot.getCurrentVehiclePlate() == null ? "-" : spot.getCurrentVehiclePlate()
                });
            }
        }

        double occupancyRate = totalSpots > 0 ? ((double)occupiedSpots / totalSpots) * 100 : 0;
        double totalUnpaidFines = fineRegistry.getTotalUnpaidFinesAmount();
        int totalUnpaidCount = fineRegistry.getTotalUnpaidFinesCount();
        double estRevenue = entryExitManager.getEstimatedRevenue(); 

        adminOccupancyLbl.setText(String.format("Occupancy: %.1f%%", occupancyRate));
        adminRevenueLbl.setText(String.format("Active Revenue: RM %.2f", estRevenue));
        adminFinesLbl.setText(String.format("Unpaid Fines: RM %.2f", totalUnpaidFines));

        StringBuilder sb = new StringBuilder();
        sb.append("=== PARKING LOT STATUS REPORT ===\n");
        sb.append(String.format("Total Spots     : %d\n", totalSpots));
        sb.append(String.format("Occupied Spots  : %d\n", occupiedSpots));
        sb.append(String.format("Occupancy Rate  : %.2f%%\n\n", occupancyRate));
        sb.append("=== FINANCIAL REPORT ===\n");
        sb.append(String.format("Estimated Revenue (Active Vehicles): RM %.2f\n", estRevenue));
        sb.append("\n=== FINE REPORT (OUTSTANDING) ===\n");
        sb.append(String.format("Total Unpaid Fines Count  : %d\n", totalUnpaidCount));
        sb.append(String.format("Total Unpaid Fines Amount : RM %.2f\n", totalUnpaidFines));
        
        reportingStatsArea.setText(sb.toString());
    }

    private void log(String msg) {
        if (feedbackArea != null) {
            feedbackArea.append(msg + "\n");
            feedbackArea.setCaretPosition(feedbackArea.getDocument().getLength());
        }
    }

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception ignored) {}
        SwingUtilities.invokeLater(() -> new ParkingLotSystemUI().setVisible(true));
    }
}