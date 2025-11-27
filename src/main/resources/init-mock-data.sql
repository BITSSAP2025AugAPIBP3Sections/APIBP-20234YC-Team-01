-- PostgreSQL Database Setup and Mock Data Script for GreenGrub Application
-- This script creates/updates tables and populates them with mock data

-- ============================================
-- CREATE TABLES
-- ============================================

-- Users Table
CREATE TABLE IF NOT EXISTS users (
    id VARCHAR(255) PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    phone_number VARCHAR(10),
    is_active BOOLEAN NOT NULL DEFAULT true,
    role VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

-- Address Table
CREATE TABLE IF NOT EXISTS address (
    address_id VARCHAR(255) PRIMARY KEY,
    address VARCHAR(150) NOT NULL,
    city VARCHAR(50) NOT NULL,
    state VARCHAR(50) NOT NULL,
    pincode VARCHAR(6) NOT NULL,
    latitude NUMERIC(10, 6),
    longitude NUMERIC(10, 6)
);

-- Food Items Table
CREATE TABLE IF NOT EXISTS food_items (
    id VARCHAR(255) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(500) NOT NULL,
    quantity_available INTEGER NOT NULL,
    unit_type VARCHAR(50) NOT NULL,
    image_url VARCHAR(500),
    is_available BOOLEAN NOT NULL DEFAULT true,
    food_type VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    expires_at TIMESTAMP NOT NULL
);

-- Food Requests Table
CREATE TABLE IF NOT EXISTS food_requests (
    id VARCHAR(255) PRIMARY KEY,
    description VARCHAR(500) NOT NULL,
    quantity_requested INTEGER NOT NULL,
    unit_type VARCHAR(50) NOT NULL,
    food_type VARCHAR(50) NOT NULL,
    require_on TIMESTAMP NOT NULL
);

-- Donations Table
CREATE TABLE IF NOT EXISTS donations (
    donation_id VARCHAR(255) PRIMARY KEY,
    quantity INTEGER NOT NULL,
    requested BOOLEAN NOT NULL,
    donor_id VARCHAR(255) NOT NULL,
    recipient_id VARCHAR(255),
    website_url VARCHAR(500),
    pickup_address_id VARCHAR(255),
    delivery_address_id VARCHAR(255),
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    FOREIGN KEY (pickup_address_id) REFERENCES address(address_id),
    FOREIGN KEY (delivery_address_id) REFERENCES address(address_id)
);

-- Donation Foods (Many-to-Many relationship)
CREATE TABLE IF NOT EXISTS donation_foods (
    donation_id VARCHAR(255) NOT NULL,
    food_id VARCHAR(255) NOT NULL,
    PRIMARY KEY (donation_id, food_id),
    FOREIGN KEY (donation_id) REFERENCES donations(donation_id)
);

-- Pickup Requests Table
CREATE TABLE IF NOT EXISTS pickup_requests (
    id VARCHAR(255) PRIMARY KEY,
    donation_id VARCHAR(255) NOT NULL UNIQUE,
    assigned_to VARCHAR(255),
    contact_name VARCHAR(100) NOT NULL,
    contact_number VARCHAR(10) NOT NULL,
    pickup_address_id VARCHAR(255) NOT NULL,
    scheduled_time TIMESTAMP NOT NULL,
    status VARCHAR(30) NOT NULL,
    remarks VARCHAR(250),
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    FOREIGN KEY (donation_id) REFERENCES donations(donation_id),
    FOREIGN KEY (pickup_address_id) REFERENCES address(address_id)
);

-- Transactions Table
CREATE TABLE IF NOT EXISTS transactions (
    "transactionId" VARCHAR(255) PRIMARY KEY,
    food_id BIGINT,
    food_request_id BIGINT,
    transaction_date TIMESTAMP NOT NULL,
    rating INTEGER CHECK (rating >= 0 AND rating <= 5),
    review VARCHAR(500)
);

-- Issues Table
CREATE TABLE IF NOT EXISTS issues (
    id VARCHAR(255) PRIMARY KEY,
    food_id VARCHAR(255) NOT NULL,
    food_request_id VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    reported_at TIMESTAMP NOT NULL
);

-- ============================================
-- INSERT MOCK DATA
-- ============================================
-- Using INSERT ... ON CONFLICT DO NOTHING to handle existing records

-- Insert Users
INSERT INTO users (id, name, email, password, phone_number, is_active, role, created_at, updated_at) VALUES
('a1b2c3d4-e5f6-7890-abcd-ef1234567890', 'Rajesh Kumar', 'rajesh.kumar@example.com', '$2a$10$xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx', '9876543210', true, 'DONOR', '2024-01-15 10:00:00', '2024-01-15 10:00:00'),
('b2c3d4e5-f6a7-8901-bcde-f12345678901', 'Priya Sharma', 'priya.sharma@example.com', '$2a$10$xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx', '9876543211', true, 'RECIPIENT', '2024-01-16 11:00:00', '2024-01-16 11:00:00'),
('c3d4e5f6-a7b8-9012-cdef-123456789012', 'Amit Patel', 'amit.patel@example.com', '$2a$10$xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx', '9876543212', true, 'DONOR', '2024-01-17 12:00:00', '2024-01-17 12:00:00'),
('d4e5f6a7-b8c9-0123-def1-234567890123', 'Sneha Reddy', 'sneha.reddy@example.com', '$2a$10$xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx', '9876543213', true, 'RECIPIENT', '2024-01-18 13:00:00', '2024-01-18 13:00:00'),
('e5f6a7b8-c9d0-1234-ef12-345678901234', 'Vikram Singh', 'vikram.singh@example.com', '$2a$10$xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx', '9876543214', true, 'ADMIN', '2024-01-19 14:00:00', '2024-01-19 14:00:00'),
('f6a7b8c9-d0e1-2345-f123-456789012345', 'Anita Desai', 'anita.desai@example.com', '$2a$10$xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx', '9876543215', true, 'DONOR', '2024-01-20 15:00:00', '2024-01-20 15:00:00'),
('a7b8c9d0-e1f2-3456-1234-567890123456', 'Rahul Verma', 'rahul.verma@example.com', '$2a$10$xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx', '9876543216', true, 'RECIPIENT', '2024-01-21 16:00:00', '2024-01-21 16:00:00'),
('b8c9d0e1-f2a3-4567-2345-678901234567', 'Kavita Nair', 'kavita.nair@example.com', '$2a$10$xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx', '9876543217', true, 'DONOR', '2024-01-22 17:00:00', '2024-01-22 17:00:00'),
('c9d0e1f2-a3b4-5678-3456-789012345678', 'Suresh Gupta', 'suresh.gupta@example.com', '$2a$10$xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx', '9876543218', true, 'RECIPIENT', '2024-01-23 18:00:00', '2024-01-23 18:00:00'),
('d0e1f2a3-b4c5-6789-4567-890123456789', 'Meera Iyer', 'meera.iyer@example.com', '$2a$10$xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx', '9876543219', true, 'DONOR', '2024-01-24 19:00:00', '2024-01-24 19:00:00')
ON CONFLICT (id) DO NOTHING;

-- Insert Addresses
INSERT INTO address (address_id, address, city, state, pincode, latitude, longitude) VALUES
('addr-001', '123 MG Road', 'Bangalore', 'Karnataka', '560001', 12.971599, 77.594566),
('addr-002', '456 Anna Salai', 'Chennai', 'Tamil Nadu', '600002', 13.082680, 80.270718),
('addr-003', '789 Marine Drive', 'Mumbai', 'Maharashtra', '400001', 18.920430, 72.836220),
('addr-004', '321 Park Street', 'Kolkata', 'West Bengal', '700016', 22.556263, 88.363895),
('addr-005', '654 Connaught Place', 'New Delhi', 'Delhi', '110001', 28.631377, 77.219796),
('addr-006', '147 Brigade Road', 'Bangalore', 'Karnataka', '560025', 12.971980, 77.607849),
('addr-007', '258 Indiranagar', 'Bangalore', 'Karnataka', '560038', 12.971230, 77.641120),
('addr-008', '369 Banjara Hills', 'Hyderabad', 'Telangana', '500034', 17.431460, 78.444260),
('addr-009', '741 Koramangala', 'Bangalore', 'Karnataka', '560095', 12.934533, 77.626579),
('addr-010', '852 Whitefield', 'Bangalore', 'Karnataka', '560066', 12.969910, 77.749900),
('addr-011', '963 HSR Layout', 'Bangalore', 'Karnataka', '560102', 12.909100, 77.641990),
('addr-012', '159 Jayanagar', 'Bangalore', 'Karnataka', '560011', 12.925560, 77.595150)
ON CONFLICT (address_id) DO NOTHING;

-- Insert Food Items
INSERT INTO food_items (id, name, description, quantity_available, unit_type, image_url, is_available, food_type, created_at, expires_at) VALUES
('food-001', 'Fresh Vegetable Biryani', 'Aromatic basmati rice with mixed vegetables, freshly prepared', 50, 'SERVINGS', 'https://example.com/images/veg-biryani.jpg', true, 'VEGETARIAN', '2024-11-25 08:00:00', '2024-11-27 20:00:00'),
('food-002', 'Paneer Butter Masala', 'Rich and creamy cottage cheese curry with butter', 30, 'SERVINGS', 'https://example.com/images/paneer-masala.jpg', true, 'VEGETARIAN', '2024-11-25 09:00:00', '2024-11-27 18:00:00'),
('food-003', 'Chicken Tikka', 'Grilled chicken pieces marinated in spices', 40, 'SERVINGS', 'https://example.com/images/chicken-tikka.jpg', true, 'NON_VEGETARIAN', '2024-11-25 10:00:00', '2024-11-27 16:00:00'),
('food-004', 'Mixed Fruit Salad', 'Fresh seasonal fruits, diced and mixed', 25, 'KILOGRAMS', 'https://example.com/images/fruit-salad.jpg', true, 'JAIN', '2024-11-25 11:00:00', '2024-11-27 14:00:00'),
('food-005', 'Dal Tadka', 'Yellow lentils tempered with spices and ghee', 60, 'SERVINGS', 'https://example.com/images/dal-tadka.jpg', true, 'VEGETARIAN', '2024-11-25 12:00:00', '2024-11-28 12:00:00'),
('food-006', 'Idli Sambar', 'Steamed rice cakes with lentil-based vegetable stew', 100, 'SERVINGS', 'https://example.com/images/idli-sambar.jpg', true, 'VEGETARIAN', '2024-11-26 07:00:00', '2024-11-27 12:00:00'),
('food-007', 'Fish Curry', 'Traditional South Indian fish curry with coconut', 20, 'SERVINGS', 'https://example.com/images/fish-curry.jpg', true, 'NON_VEGETARIAN', '2024-11-26 08:00:00', '2024-11-27 15:00:00'),
('food-008', 'Vegetable Sandwiches', 'Whole wheat sandwiches with fresh vegetables', 80, 'PIECES', 'https://example.com/images/veg-sandwiches.jpg', true, 'JAIN', '2024-11-26 09:00:00', '2024-11-27 19:00:00'),
('food-009', 'Roti with Sabzi', 'Whole wheat flatbread with mixed vegetable curry', 70, 'SERVINGS', 'https://example.com/images/roti-sabzi.jpg', true, 'VEGETARIAN', '2024-11-26 10:00:00', '2024-11-28 10:00:00'),
('food-010', 'Mutton Biryani', 'Spiced rice with tender mutton pieces', 35, 'SERVINGS', 'https://example.com/images/mutton-biryani.jpg', true, 'NON_VEGETARIAN', '2024-11-26 11:00:00', '2024-11-27 21:00:00')
ON CONFLICT (id) DO NOTHING;

-- Insert Food Requests
INSERT INTO food_requests (id, description, quantity_requested, unit_type, food_type, require_on) VALUES
('freq-001', 'Need vegetarian meals for community kitchen', 100, 'SERVINGS', 'VEGETARIAN', '2024-11-28 12:00:00'),
('freq-002', 'Requesting food for orphanage children', 50, 'SERVINGS', 'VEGETARIAN', '2024-11-29 10:00:00'),
('freq-003', 'Need meals for shelter home residents', 75, 'SERVINGS', 'VEGETARIAN', '2024-11-28 18:00:00'),
('freq-004', 'Food required for elderly care center', 40, 'SERVINGS', 'JAIN', '2024-11-30 11:00:00'),
('freq-005', 'Meals needed for disaster relief camp', 200, 'SERVINGS', 'VEGETARIAN', '2024-11-27 15:00:00')
ON CONFLICT (id) DO NOTHING;

-- Insert Donations
INSERT INTO donations (donation_id, quantity, requested, donor_id, recipient_id, website_url, pickup_address_id, delivery_address_id, status, created_at, updated_at) VALUES
('don-001', 50, false, 'a1b2c3d4-e5f6-7890-abcd-ef1234567890', 'b2c3d4e5-f6a7-8901-bcde-f12345678901', 'https://greengrub.org/don-001', 'addr-001', 'addr-002', 'COMPLETED', '2024-11-20 10:00:00', '2024-11-22 15:00:00'),
('don-002', 30, true, 'c3d4e5f6-a7b8-9012-cdef-123456789012', 'd4e5f6a7-b8c9-0123-def1-234567890123', 'https://greengrub.org/don-002', 'addr-003', 'addr-004', 'IN_TRANSIT', '2024-11-21 11:00:00', '2024-11-26 12:00:00'),
('don-003', 40, false, 'f6a7b8c9-d0e1-2345-f123-456789012345', NULL, 'https://greengrub.org/don-003', 'addr-006', NULL, 'PENDING', '2024-11-22 12:00:00', '2024-11-22 12:00:00'),
('don-004', 25, true, 'b8c9d0e1-f2a3-4567-2345-678901234567', 'c9d0e1f2-a3b4-5678-3456-789012345678', 'https://greengrub.org/don-004', 'addr-008', 'addr-009', 'ACCEPTED', '2024-11-23 13:00:00', '2024-11-25 14:00:00'),
('don-005', 60, false, 'd0e1f2a3-b4c5-6789-4567-890123456789', 'a7b8c9d0-e1f2-3456-1234-567890123456', 'https://greengrub.org/don-005', 'addr-010', 'addr-011', 'COMPLETED', '2024-11-24 14:00:00', '2024-11-26 16:00:00'),
('don-006', 100, true, 'a1b2c3d4-e5f6-7890-abcd-ef1234567890', NULL, 'https://greengrub.org/don-006', 'addr-001', NULL, 'PENDING', '2024-11-25 15:00:00', '2024-11-25 15:00:00'),
('don-007', 20, false, 'c3d4e5f6-a7b8-9012-cdef-123456789012', 'b2c3d4e5-f6a7-8901-bcde-f12345678901', 'https://greengrub.org/don-007', 'addr-003', 'addr-007', 'IN_TRANSIT', '2024-11-26 08:00:00', '2024-11-26 18:00:00')
ON CONFLICT (donation_id) DO NOTHING;

-- Insert Donation Foods (linking donations with food items)
INSERT INTO donation_foods (donation_id, food_id) VALUES
('don-001', 'food-001'),
('don-001', 'food-002'),
('don-002', 'food-003'),
('don-003', 'food-004'),
('don-003', 'food-005'),
('don-004', 'food-006'),
('don-005', 'food-007'),
('don-005', 'food-008'),
('don-006', 'food-009'),
('don-007', 'food-010')
ON CONFLICT (donation_id, food_id) DO NOTHING;

-- Insert Pickup Requests
INSERT INTO pickup_requests (id, donation_id, assigned_to, contact_name, contact_number, pickup_address_id, scheduled_time, status, remarks, created_at, updated_at) VALUES
('pickup-001', 'don-001', 'e5f6a7b8-c9d0-1234-ef12-345678901234', 'Rajesh Kumar', '9876543210', 'addr-001', '2024-11-21 14:00:00', 'COMPLETED', 'Pickup successful', '2024-11-20 10:30:00', '2024-11-21 15:00:00'),
('pickup-002', 'don-002', 'e5f6a7b8-c9d0-1234-ef12-345678901234', 'Amit Patel', '9876543212', 'addr-003', '2024-11-27 10:00:00', 'SCHEDULED', 'Scheduled for tomorrow morning', '2024-11-21 11:30:00', '2024-11-26 09:00:00'),
('pickup-003', 'don-003', NULL, 'Anita Desai', '9876543215', 'addr-006', '2024-11-28 11:00:00', 'PENDING', 'Awaiting assignment', '2024-11-22 12:30:00', '2024-11-22 12:30:00'),
('pickup-004', 'don-004', 'e5f6a7b8-c9d0-1234-ef12-345678901234', 'Kavita Nair', '9876543217', 'addr-008', '2024-11-26 09:00:00', 'IN_PROGRESS', 'Driver en route', '2024-11-23 13:30:00', '2024-11-26 08:30:00'),
('pickup-005', 'don-005', 'e5f6a7b8-c9d0-1234-ef12-345678901234', 'Meera Iyer', '9876543219', 'addr-010', '2024-11-25 16:00:00', 'COMPLETED', 'Successfully delivered', '2024-11-24 14:30:00', '2024-11-25 17:00:00')
ON CONFLICT (id) DO NOTHING;

-- Insert Transactions
INSERT INTO transactions ("transactionId", food_id, food_request_id, transaction_date, rating, review) VALUES
('txn-001', 1, NULL, '2024-11-22 15:30:00', 5, 'Excellent quality food, very fresh and well-packaged'),
('txn-002', 2, NULL, '2024-11-23 16:00:00', 4, 'Good food, delivered on time'),
('txn-003', NULL, 1, '2024-11-24 12:00:00', 5, 'Very helpful, met our requirements perfectly'),
('txn-004', 3, NULL, '2024-11-25 14:00:00', 4, 'Great initiative, food was tasty'),
('txn-005', 4, NULL, '2024-11-26 16:30:00', 5, 'Outstanding service, will use again')
ON CONFLICT ("transactionId") DO NOTHING;

-- Insert Issues
INSERT INTO issues (id, food_id, food_request_id, description, reported_at) VALUES
('issue-001', 'food-001', 'freq-001', 'Food quantity was less than expected', '2024-11-23 10:00:00'),
('issue-002', 'food-003', 'freq-002', 'Delivery was delayed by 2 hours', '2024-11-24 11:00:00'),
('issue-003', 'food-005', 'freq-003', 'Some containers were not properly sealed', '2024-11-25 12:00:00')
ON CONFLICT (id) DO NOTHING;

-- ============================================
-- VERIFY DATA
-- ============================================
SELECT 'Users' AS table_name, COUNT(*) AS record_count FROM users
UNION ALL
SELECT 'Addresses', COUNT(*) FROM address
UNION ALL
SELECT 'Food Items', COUNT(*) FROM food_items
UNION ALL
SELECT 'Food Requests', COUNT(*) FROM food_requests
UNION ALL
SELECT 'Donations', COUNT(*) FROM donations
UNION ALL
SELECT 'Donation Foods', COUNT(*) FROM donation_foods
UNION ALL
SELECT 'Pickup Requests', COUNT(*) FROM pickup_requests
UNION ALL
SELECT 'Transactions', COUNT(*) FROM transactions
UNION ALL
SELECT 'Issues', COUNT(*) FROM issues;
