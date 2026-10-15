CREATE TABLE orders (
    id serial PRIMARY KEY,
    user_id int,
    status varchar(50),
    total_amount decimal(10, 2) NOT NULL,
    created_at timestamp DEFAULT NOW(),
    updated_at timestamp DEFAULT NOW(),
    CONSTRAINT fk_orders_on_users
        FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
