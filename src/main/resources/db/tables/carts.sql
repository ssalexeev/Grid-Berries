CREATE TABLE carts (
    id serial PRIMARY KEY,
    user_id int,
    total_amount decimal(10, 2) DEFAULT 0.00,
    updated_at timestamp DEFAULT NOW(),
    CONSTRAINT fk_carts_on_users
        FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
