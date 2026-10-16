CREATE TABLE seller_profile (
    id serial PRIMARY KEY,
    user_id int,
    store_name varchar(100) NOT NULL,
    store_description text,
    created_at timestamp DEFAULT NOW(),
    CONSTRAINT fk_seller_profiles_on_users
        FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
