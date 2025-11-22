-- Dummy users
INSERT INTO users (id, name, email, password, role, phone_number, is_active, created_at, updated_at)
VALUES
    ('user-101', 'Rishav Kumar', 'rishav@example.com',
     '$2a$10$abcdefghijklmnopqrstuv', 'DONOR', '9876543210', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

    ('user-102', 'Amit Sharma', 'amit@example.com',
     '$2a$10$abcdefghijklmnopqrstuv', 'RECIPIENT', '9123456780', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

    ('admin-001', 'Admin User', 'admin@example.com',
     '$2a$10$abcdefghijklmnopqrstuv', 'ADMIN', '9000000000', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Dummy donations (note: table name = donations, and column names match Hibernate schema)
INSERT INTO donations (
    donation_id,
    donor_id,
    recipient_id,
    quantity,
    requested,
    status,
    website_url,
    created_at,
    updated_at
) VALUES
      ('DN001',
       '101',
       '102',
       10,
       TRUE,
       'PENDING',
       'https://ngo-example.org',
       CURRENT_TIMESTAMP,
       CURRENT_TIMESTAMP),

      ('DN002',
       '101',
       '102',
       5,
       FALSE,
       'COMPLETED',
       NULL,
       TIMESTAMP '2025-11-20 10:00:00',
       TIMESTAMP '2025-11-20 11:00:00'),

      ('DN003',
       '101',
       NULL,
       8,
       FALSE,
       'ACCEPTED',
       NULL,
       CURRENT_TIMESTAMP,
       CURRENT_TIMESTAMP);

INSERT INTO food_items (
    id,
    name,
    description,
    food_type,
    quantity_available,
    unit_type,
    is_available,
    created_at,
    expires_at,
    image_url
) VALUES
      ('FOOD001',
       'Veg Biryani',
       'Fresh homemade veg biryani. Good for 10 people.',
       'VEG',
       10,
       1,
       TRUE,
       CURRENT_TIMESTAMP,
       TIMESTAMP '2025-12-01 20:00:00',
       NULL),

      ('FOOD002',
       'Chapati Pack',
       'Pack of 20 chapatis with masala.',
       'VEG',
       20,
       1,
       TRUE,
       CURRENT_TIMESTAMP,
       TIMESTAMP '2025-12-01 21:00:00',
       NULL),

      ('FOOD003',
       'Leftover Rice',
       'Hotel leftover rice. Suitable for animal feeding.',
       'WASTE',
       15,
       1,
       TRUE,
       CURRENT_TIMESTAMP,
       TIMESTAMP '2025-11-30 22:00:00',
       NULL);

INSERT INTO food_requests (
    id, description, food_type, quantity_requested, unit_type, require_on
) VALUES
      ('REQ001', 'Need vegetarian meal packets for community center', 'VEG', 10, 0, TIMESTAMP '2025-11-22 18:00:00'),
      ('REQ002', 'Non-veg food required for night shelter', 'NONVEG', 15, 0, TIMESTAMP '2025-11-22 20:30:00'),
      ('REQ003', 'Vegan sandwiches needed for NGO event', 'JAIN', 8, 0, TIMESTAMP '2025-11-23 12:00:00');
