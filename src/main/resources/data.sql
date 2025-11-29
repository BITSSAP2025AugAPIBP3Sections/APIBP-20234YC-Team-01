-- Dummy users
INSERT INTO users (id, name, email, password, role, phone_number, is_active, created_at, updated_at)
VALUES
    ('user-101', 'Rishav Singh', 'rishav@example.com',
     '$2a$10$abcdefghijklmnopqrstuv', 'DONOR', '9876543210', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

    ('user-102', 'Amit Sharma', 'amit@example.com',
     '$2a$10$abcdefghijklmnopqrstuv', 'RECIPIENT', '9123456780', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

    ('admin-001', 'Admin User', 'admin@example.com',
     '$2a$10$abcdefghijklmnopqrstuv', 'ADMIN', '9000000000', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Dummy donations (Hibernate table: donations)
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
       'user-101',
       'user-102',
       10,
       TRUE,
       'PENDING',
       'https://ngo-example.org',
       CURRENT_TIMESTAMP,
       CURRENT_TIMESTAMP),

      ('DN002',
       'user-101',
       'user-102',
       5,
       FALSE,
       'COMPLETED',
       NULL,
       TIMESTAMP '2025-11-20 10:00:00',
       TIMESTAMP '2025-11-20 11:00:00'),

      ('DN003',
       'user-101',
       NULL,
       8,
       FALSE,
       'ACCEPTED',
       NULL,
       CURRENT_TIMESTAMP,
       CURRENT_TIMESTAMP);

-- Initial food items
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

-- Food requests
INSERT INTO food_requests (id, description, quantity_requested, unit_type, food_type, require_on)
VALUES
    ('REQ001', 'Need 5 veg meals for orphanage dinner', 5, 1, 'VEG', DATEADD('DAY', 1, CURRENT_TIMESTAMP())),
    ('REQ002', 'Looking for packed vegan food for shelter', 8, 1, 'VEG', DATEADD('DAY', 2, CURRENT_TIMESTAMP())),
    ('REQ003', 'Require chicken meal packs for 7 people', 7, 1, 'NONVEG', DATEADD('DAY', 1, CURRENT_TIMESTAMP()));

-- More food items (for browse/filter scenarios)
INSERT INTO food_items (
    id,
    name,
    description,
    food_type,
    quantity_available,
    unit_type,
    is_available,
    expires_at,
    created_at,
    image_url
)
VALUES
    (
        'F001',
        'Veg Biryani Pack',
        'Spiced vegetable biryani packs for distribution',
        'VEG',
        10,
        1, -- SERVINGS (assuming ordinal 1)
        TRUE,
        DATEADD('DAY', 2, CURRENT_TIMESTAMP()),
        CURRENT_TIMESTAMP(),
        NULL
    ),
    (
        'F002',
        'Chapati Meal Box',
        'Chapati with dal and sabzi, individually packed',
        'VEG',
        20,
        1,
        TRUE,
        DATEADD('DAY', 1, CURRENT_TIMESTAMP()),
        CURRENT_TIMESTAMP(),
        NULL
    ),
    (
        'F003',
        'Chicken Curry Meal',
        'Chicken curry with rice meal boxes',
        'NONVEG',
        15,
        1,
        TRUE,
        DATEADD('DAY', 3, CURRENT_TIMESTAMP()),
        CURRENT_TIMESTAMP(),
        NULL
    ),
    (
        'F004',
        'Fruit Salad Pack',
        'Cut fruit salad bowls, ready to serve',
        'VEG',          -- or 'JAIN' if you prefer
        12,
        1,
        TRUE,
        DATEADD('DAY', 2, CURRENT_TIMESTAMP()),
        CURRENT_TIMESTAMP(),
        NULL
    );

-- Address used for pickup
INSERT INTO address (address_id, address, city, state, pincode, latitude, longitude)
VALUES
    ('ADDR_PICK_001', '12/3 MG Road', 'Bengaluru', 'Karnataka', '560001', 12.9716, 77.5946);

-- Donation used by pickup
INSERT INTO donations (
    donation_id,
    donor_id,
    recipient_id,
    quantity,
    requested,
    status,
    website_url,
    pickup_address_id,
    delivery_address_id,
    created_at,
    updated_at
)
VALUES
    ('DN1001', 'user-101', 'user-102', 10, TRUE, 'PENDING',
     'https://example.org', 'ADDR_PICK_001', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Pickup requests for that donation
INSERT INTO pickup_requests (
    id,
    donation_id,
    assigned_to,
    contact_name,
    contact_number,
    pickup_address_id,
    scheduled_time,
    status,
    remarks,
    created_at,
    updated_at
)
VALUES
    ('PU001', 'DN1001', 'DELIVERY_USER_201', 'Rishav Pickup', '9876543210',
     'ADDR_PICK_001', TIMESTAMP '2025-11-30 10:00:00', 'SCHEDULED',
     'Handle with care', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
