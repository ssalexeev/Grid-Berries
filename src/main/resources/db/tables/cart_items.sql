CREATE TABLE cart_items (
    id serial PRIMARY KEY,
    cart_id int,
    product_id int,
    quantity int DEFAULT 1,
    created_at timestamp DEFAULT NOW(),
    CONSTRAINT fk_cart_items_on_carts
        FOREIGN KEY (cart_id) REFERENCES carts(id) ON DELETE CASCADE,
    CONSTRAINT fk_cart_items_on_products
        FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE
);
