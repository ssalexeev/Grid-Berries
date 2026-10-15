CREATE TABLE products (
    id serial PRIMARY KEY,
    seller_id int,
    title varchar(100) NOT NULL,
    description text,
    price decimal(10, 2) NOT NULL,
    stock_quantity int DEFAULT 0,
    image_url varchar(255),
    product_type varchar(50) NOT NULL,
    created_at timestamp DEFAULT NOW(),
    CONSTRAINT fk_products_on_seller_profiles
        FOREIGN KEY (seller_id) REFERENCES seller_profiles(id) ON DELETE CASCADE
);
