-- client_schedule demo schema for Appointment Creator
-- Run in MySQL Workbench or: mysql -u root -p < AppointmentCreator/schema.sql
--
-- App connection (see src/utilities/DatabaseConnection.java):
--   host: localhost
--   database: client_schedule
--   user: sqlUser
--   password: Passw0rd!

DROP DATABASE IF EXISTS client_schedule;
CREATE DATABASE client_schedule;
USE client_schedule;

CREATE TABLE countries (
    Country_ID INT NOT NULL AUTO_INCREMENT,
    Country VARCHAR(50) NOT NULL,
    Create_Date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    Created_By VARCHAR(50) NOT NULL DEFAULT 'script',
    Last_Update TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    Last_Updated_By VARCHAR(50) NOT NULL DEFAULT 'script',
    PRIMARY KEY (Country_ID)
);

CREATE TABLE first_level_divisions (
    Division_ID INT NOT NULL AUTO_INCREMENT,
    Division VARCHAR(50) NOT NULL,
    Create_Date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    Created_By VARCHAR(50) NOT NULL DEFAULT 'script',
    Last_Update TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    Last_Updated_By VARCHAR(50) NOT NULL DEFAULT 'script',
    COUNTRY_ID INT NOT NULL,
    PRIMARY KEY (Division_ID),
    CONSTRAINT fk_divisions_country FOREIGN KEY (COUNTRY_ID) REFERENCES countries (Country_ID)
);

CREATE TABLE users (
    User_ID INT NOT NULL AUTO_INCREMENT,
    User_Name VARCHAR(50) NOT NULL,
    Password VARCHAR(50) NOT NULL,
    Create_Date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    Created_By VARCHAR(50) NOT NULL DEFAULT 'script',
    Last_Update TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    Last_Updated_By VARCHAR(50) NOT NULL DEFAULT 'script',
    PRIMARY KEY (User_ID)
);

CREATE TABLE contacts (
    Contact_ID INT NOT NULL AUTO_INCREMENT,
    Contact_Name VARCHAR(50) NOT NULL,
    Email VARCHAR(50) NOT NULL,
    PRIMARY KEY (Contact_ID)
);

CREATE TABLE customers (
    Customer_ID INT NOT NULL AUTO_INCREMENT,
    Customer_Name VARCHAR(50) NOT NULL,
    Address VARCHAR(100) NOT NULL,
    Postal_Code VARCHAR(50) NOT NULL,
    Phone VARCHAR(50) NOT NULL,
    Create_Date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    Created_By VARCHAR(50) NOT NULL DEFAULT 'script',
    Last_Update TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    Last_Updated_By VARCHAR(50) NOT NULL DEFAULT 'script',
    Division_ID INT NOT NULL,
    PRIMARY KEY (Customer_ID),
    CONSTRAINT fk_customers_division FOREIGN KEY (Division_ID) REFERENCES first_level_divisions (Division_ID)
);

CREATE TABLE appointments (
    Appointment_ID INT NOT NULL AUTO_INCREMENT,
    Title VARCHAR(50) NOT NULL,
    Description VARCHAR(50) NOT NULL,
    Location VARCHAR(50) NOT NULL,
    Type VARCHAR(50) NOT NULL,
    Start DATETIME NOT NULL,
    End DATETIME NOT NULL,
    Create_Date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    Created_By VARCHAR(50) NOT NULL DEFAULT 'script',
    Last_Update TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    Last_Updated_By VARCHAR(50) NOT NULL DEFAULT 'script',
    Customer_ID INT NOT NULL,
    User_ID INT NOT NULL,
    Contact_ID INT NOT NULL,
    PRIMARY KEY (Appointment_ID),
    CONSTRAINT fk_appointments_customer FOREIGN KEY (Customer_ID) REFERENCES customers (Customer_ID),
    CONSTRAINT fk_appointments_user FOREIGN KEY (User_ID) REFERENCES users (User_ID),
    CONSTRAINT fk_appointments_contact FOREIGN KEY (Contact_ID) REFERENCES contacts (Contact_ID)
);

INSERT INTO countries (Country_ID, Country) VALUES
    (1, 'U.S'),
    (2, 'UK'),
    (3, 'Canada');

INSERT INTO first_level_divisions (Division_ID, Division, COUNTRY_ID) VALUES
    (1, 'Alabama', 1), (2, 'Arizona', 1), (3, 'Arkansas', 1), (4, 'California', 1),
    (5, 'Colorado', 1), (6, 'Connecticut', 1), (7, 'Delaware', 1), (8, 'District of Columbia', 1),
    (9, 'Florida', 1), (10, 'Georgia', 1), (11, 'Idaho', 1), (12, 'Illinois', 1),
    (13, 'Indiana', 1), (14, 'Iowa', 1), (15, 'Kansas', 1), (16, 'Kentucky', 1),
    (17, 'Louisiana', 1), (18, 'Maine', 1), (19, 'Maryland', 1), (20, 'Massachusetts', 1),
    (21, 'Michigan', 1), (22, 'Minnesota', 1), (23, 'Mississippi', 1), (24, 'Missouri', 1),
    (25, 'Montana', 1), (26, 'Nebraska', 1), (27, 'Nevada', 1), (28, 'New Hampshire', 1),
    (29, 'New Jersey', 1), (30, 'New Mexico', 1), (31, 'New York', 1), (32, 'North Carolina', 1),
    (33, 'North Dakota', 1), (34, 'Ohio', 1), (35, 'Oklahoma', 1), (36, 'Oregon', 1),
    (37, 'Pennsylvania', 1), (38, 'Rhode Island', 1), (39, 'South Carolina', 1), (40, 'South Dakota', 1),
    (41, 'Tennessee', 1), (42, 'Texas', 1), (43, 'Utah', 1), (44, 'Vermont', 1),
    (45, 'Virginia', 1), (46, 'Washington', 1), (47, 'West Virginia', 1), (48, 'Wisconsin', 1),
    (49, 'Wyoming', 1), (52, 'Hawaii', 1), (54, 'Alaska', 1),
    (60, 'Northwest Territories', 3), (61, 'Alberta', 3), (62, 'British Columbia', 3),
    (63, 'Manitoba', 3), (64, 'New Brunswick', 3), (65, 'Nova Scotia', 3),
    (66, 'Prince Edward Island', 3), (67, 'Ontario', 3), (68, 'Québec', 3),
    (69, 'Saskatchewan', 3), (70, 'Nunavut', 3), (71, 'Yukon', 3), (72, 'Newfoundland and Labrador', 3),
    (101, 'England', 2), (102, 'Wales', 2), (103, 'Scotland', 2), (104, 'Northern Ireland', 2);

INSERT INTO users (User_Name, Password) VALUES
    ('test', 'test'),
    ('admin', 'admin');

INSERT INTO contacts (Contact_Name, Email) VALUES
    ('Anika Costa', 'anika@company.com'),
    ('Daniel Garcia', 'daniel@company.com'),
    ('Li Lee', 'li@company.com');

INSERT INTO customers (Customer_Name, Address, Postal_Code, Phone, Division_ID) VALUES
    ('Daddy Warbucks', '57th Street, NYC', '10022', '212-555-1212', 31),
    ('Lady Macbeth', 'Castle Street', 'G1 1AA', '44-131-555-1212', 103),
    ('John Doe', '123 Main St', '90210', '310-555-1212', 4);

CREATE USER IF NOT EXISTS 'sqlUser'@'localhost' IDENTIFIED BY 'Passw0rd!';
GRANT ALL PRIVILEGES ON client_schedule.* TO 'sqlUser'@'localhost';
FLUSH PRIVILEGES;
