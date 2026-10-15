CREATE TABLE order_items (
    id serial PRIMARY KEY,
    order_id int,
    product_id int,
    quantity int DEFAULT 1,
    unit_price decimal(10, 2) NOT NULL,
    created_at timestamp DEFAULT NOW(),
    CONSTRAINT fk_order_items_on_orders
        FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    CONSTRAINT fk_order_items_on_products
        FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE
);
