CREATE TABLE users (
    id serial PRIMARY KEY,
    email varchar(100) NOT NULL UNIQUE,
    password_hash varchar(255) NOT NULL,
    role varchar(50) NOT NULL,
    is_active boolean DEFAULT true,
    created_at timestamp DEFAULT NOW()
);
