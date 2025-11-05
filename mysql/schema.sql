-- MySQL schema for Women Safety App
CREATE DATABASE IF NOT EXISTS womensafety_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE womensafety_db;

-- Users, Emergency Contacts, OTPs, Locations
CREATE TABLE IF NOT EXISTS users (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(255),
  email VARCHAR(255) UNIQUE,
  phone VARCHAR(50),
  password VARCHAR(255),
  verified BOOLEAN DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS emergency_contact (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  user_id BIGINT,
  name VARCHAR(255),
  phone VARCHAR(50),
  email VARCHAR(255),
  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS otp (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  destination VARCHAR(255),
  code VARCHAR(10),
  expires_at DATETIME
);

CREATE TABLE IF NOT EXISTS location (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  user_id BIGINT,
  latitude DOUBLE,
  longitude DOUBLE,
  ts DATETIME,
  sharing_enabled BOOLEAN DEFAULT TRUE,
  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
