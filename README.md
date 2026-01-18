# Parking Lot Management System 🚗

**Course:** CCP6224 - Object-Oriented Analysis and Design
**Client:** University Parking Lot Management Office
**Assignment:** Final Group Assignment

## 📖 Project Overview
This project is a standalone GUI application developed using **Java Swing**. It simulates a parking lot management system for a multi-level building, handling vehicle entry, exit, payment processing, and fine management according to strict client requirements.

## 🌟 Key Features

### 1. Parking Lot Configuration
* **Multi-Level Structure:** Supports multiple floors with configurable rows and spots.
* **Spot Types & Rates:**
    * **Compact:** RM 2/hour (Motorcycles/Bicycles).
    * **Regular:** RM 5/hour (Cars/SUVs).
    * **Handicapped:** RM 2/hour (Free for card holders).
    * **Reserved:** RM 10/hour (VIPs).

### 2. Vehicle Management
* **Supported Vehicles:** Motorcycles, Cars, SUVs/Trucks, and Handicapped vehicles.
* **Entry Process:** Search for available spots, assign spots, and generate tickets (Format: `T-PLATE-TIMESTAMP`).
* **Exit Process:** Calculate duration (rounded up to the nearest hour), compute fees, check for unpaid fines, and generate receipts.

### 3. Fine Management
* **Policy:** Fines are linked to the license plate number.
* **Triggers:**
    * Overstaying (>24 hours).
    * Parking in a Reserved spot without reservation.
* **Schemes:** Admin can toggle between Fixed (RM 50), Progressive, or Hourly (RM 20/hr) fine calculations.

### 4. Admin Dashboard
* **Real-time Monitoring:** View occupancy rates, current vehicles, and total revenue.
* **Fine Control:** View unpaid fines and select the active fine calculation scheme.

## 🛠️ Tech Stack
* **Language:** Java
* **GUI:** Java Swing (Required)

## 🏗️ Design Pattern
**Pattern Used:** Singleton
---
*This project is for educational purposes only.*
