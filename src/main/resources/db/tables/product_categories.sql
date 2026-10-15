CREATE TABLE product_categories (
    product_id int NOT NULL,
    category_id int NOT NULL,
    PRIMARY KEY (product_id, category_id),
    CONSTRAINT fk_product_categories_on_products
        FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE,
    CONSTRAINT fk_product_categories_on_categories
        FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE CASCADE
);
