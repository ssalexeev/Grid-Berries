
-- 1. Insert Users
INSERT INTO users (email, password_hash, role, is_active) VALUES
('john.doe@example.com', '$2a$12$eImiTXuWVxfM37uY4JANjO.Gk4A4L4dE2f1Gz6h1.g98j4X5Y6Z7a', 'buyer', true),
('jane.smith@example.com', '$2a$12$eImiTXuWVxfM37uY4JANjO.Gk4A4L4dE2f1Gz6h1.g98j4X5Y6Z7b', 'seller', true),
('tech.vendor@example.com', '$2a$12$eImiTXuWVxfM37uY4JANjO.Gk4A4L4dE2f1Gz6h1.g98j4X5Y6Z7c', 'seller', true),
('admin@marketplace.com', '$2a$12$eImiTXuWVxfM37uY4JANjO.Gk4A4L4dE2f1Gz6h1.g98j4X5Y6Z7d', 'admin', true),
('inactive.user@example.com', '$2a$12$eImiTXuWVxfM37uY4JANjO.Gk4A4L4dE2f1Gz6h1.g98j4X5Y6Z7e', 'buyer', false);

-- 2. Insert Seller Profiles (Linked to User IDs 2 and 3)
INSERT INTO seller_profiles (user_id, store_name, store_description) VALUES
(2, 'Jane Boutique', 'Trendy apparel and handcrafted accessories.'),
(3, 'Tech Zone Store', 'High-end electronics, gadgets, and PC components.');

-- 3. Insert Products (Linked to Seller Profile IDs 1 and 2)
INSERT INTO products (seller_id, title, description, price, stock_quantity, image_url, type) VALUES
(1, 'Handcrafted Leather Wallet', 'Genuine full-grain leather bi-fold wallet.', 49.99, 150, 'https://img.example.com/wallet.jpg', 'physical'),
(1, 'Summer Floral Dress', 'Lightweight summer dress with floral pattern.', 79.50, 45, 'https://img.example.com/dress.jpg', 'physical'),
(2, 'Wireless Noise-Canceling Headphones', 'Over-ear Bluetooth headphones with active noise cancellation.', 199.99, 80, 'https://img.example.com/headphones.jpg', 'physical'),
(2, 'Mechanical Gaming Keyboard', 'RGB backlit mechanical keyboard with tactile switches.', 119.00, 30, 'https://img.example.com/keyboard.jpg', 'physical'),
(2, 'Python Mastery E-Book', 'Comprehensive digital guide to modern Python backend development.', 29.99, 9999, 'https://img.example.com/ebook.jpg', 'digital');

-- 4. Insert Categories (Parent & Child Categories)
INSERT INTO categories (parent_category_id, name, description) VALUES
(NULL, 'Electronics', 'Gadgets, hardware, and audio equipment'),
(NULL, 'Fashion & Apparel', 'Clothing, shoes, and wearable items'),
(NULL, 'Digital Goods', 'E-books, software, and digital downloads'),
(1, 'Audio Devices', 'Headphones, speakers, and microphones'),
(1, 'Computer Peripherals', 'Keyboards, mice, and monitors'),
(2, 'Women Clothing', 'Dresses, tops, and skirts');

-- 5. Insert Product-Category Junction Data
INSERT INTO product_categories (product_id, category_id) VALUES
(1, 2), -- Wallet -> Fashion & Apparel
(2, 2), -- Dress -> Fashion & Apparel
(2, 6), -- Dress -> Women Clothing
(3, 1), -- Headphones -> Electronics
(3, 4), -- Headphones -> Audio Devices
(4, 1), -- Keyboard -> Electronics
(4, 5), -- Keyboard -> Computer Peripherals
(5, 3); -- E-Book -> Digital Goods

-- 6. Insert Carts (Linked to Buyers: User IDs 1 and 5)
INSERT INTO carts (user_id, total_amount) VALUES
(1, 249.98),
(5, 0.00);

-- 7. Insert Cart Items
INSERT INTO cart_items (cart_id, product_id, quantity) VALUES
(1, 3, 1), -- 1x Headphones ($199.99)
(1, 1, 1); -- 1x Wallet ($49.99)

-- 8. Insert Orders
INSERT INTO orders (user_id, status, total_amount) VALUES
(1, 'DELIVERED', 168.99),
(1, 'PROCESSING', 199.99);

-- 9. Insert Order Items
INSERT INTO order_items (order_id, product_id, quantity, unit_price) VALUES
(1, 1, 1, 49.99),  -- Wallet
(1, 4, 1, 119.00), -- Keyboard
(2, 3, 1, 199.99); -- Headphones

-- 10. Insert Sales Analytics Reports (Uses Postgres JSONB)
INSERT INTO sales_analytics_reports (product_id, period_start, period_end, aggregated_metrics, ai_insights_text) VALUES
(3, '2026-09-01', '2026-09-30', '{"total_units_sold": 45, "gross_revenue": 8999.55, "conversion_rate": 0.042}', 'Headphones sales peaked during late September. Strong customer engagement via search.'),
(1, '2026-09-01', '2026-09-30', '{"total_units_sold": 12, "gross_revenue": 599.88, "conversion_rate": 0.018}', 'Wallet conversion remains steady, but low search traffic indicates a need for SEO optimization.');

-- 11. Insert User Metric Events
INSERT INTO user_metric_events (user_id, event_type, search_query) VALUES
(1, 'SEARCH', 'wireless bluetooth headphones'),
(1, 'VIEW_PRODUCT', NULL),
(1, 'ADD_TO_CART', NULL),
(1, 'CHECKOUT_SUCCESS', NULL),
(5, 'SEARCH', 'summer dresses sale');
