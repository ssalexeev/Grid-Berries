CREATE TABLE categories (
    id serial PRIMARY KEY,
    parent_category_id int,
    name varchar(100) NOT NULL UNIQUE,
    description text,
    created_at timestamp DEFAULT NOW(),
    CONSTRAINT fk_categories_on_categories
        FOREIGN KEY (parent_category_id) REFERENCES categories(id) ON DELETE SET NULL
);
