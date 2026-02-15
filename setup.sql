-- Database Setup Script for Parking Lot Management System
-- Run this in phpMyAdmin or MySQL Workbench

CREATE DATABASE IF NOT EXISTS parking_db;
USE parking_db;

-- Table to store Parking Sessions (Active and History)
CREATE TABLE IF NOT EXISTS parking_sessions (
    session_id INT AUTO_INCREMENT PRIMARY KEY,
    plate_number VARCHAR(20) NOT NULL,
    vehicle_type VARCHAR(20) NOT NULL,
    spot_id VARCHAR(20) NOT NULL,
    ticket_id VARCHAR(50) NOT NULL,
    hourly_rate DECIMAL(10, 2) NOT NULL,
    entry_time DATETIME NOT NULL,
    exit_time DATETIME NULL,
    fee_charged DECIMAL(10, 2) DEFAULT 0.00,
    status ENUM('ACTIVE', 'COMPLETED') DEFAULT 'ACTIVE',
    INDEX (plate_number),
    INDEX (status)
);

-- Table to store Fines
CREATE TABLE IF NOT EXISTS fines (
    fine_id INT AUTO_INCREMENT PRIMARY KEY,
    fine_ref_id VARCHAR(50) NOT NULL,
    plate_number VARCHAR(20) NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    reason VARCHAR(255) NOT NULL,
    status ENUM('UNPAID', 'PAID') DEFAULT 'UNPAID',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX (plate_number),
    INDEX (status)
);
