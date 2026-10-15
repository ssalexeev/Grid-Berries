CREATE TABLE users (
    id serial PRIMARY KEY,
    email varchar(100) NOT NULL UNIQUE,
    password_hash varchar(255) NOT NULL,
    role varchar(50) NOT NULL,
    is_active boolean DEFAULT true,
    created_at timestamp DEFAULT NOW()
);

CREATE TABLE seller_profiles (
    id serial PRIMARY KEY,
    user_id int,
    store_name varchar(100) NOT NULL,
    store_description text,
    created_at timestamp DEFAULT NOW(),
    CONSTRAINT fk_seller_profiles_on_users
        FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE products (
    id serial PRIMARY KEY,
    seller_id int,
    title varchar(100) NOT NULL,
    description text,
    price decimal(10, 2) NOT NULL,
    stock_quantity int DEFAULT 0,
    image_url varchar(255),
    type varchar(50) NOT NULL,
    created_at timestamp DEFAULT NOW(),
    CONSTRAINT fk_products_on_seller_profiles
        FOREIGN KEY (seller_id) REFERENCES seller_profiles(id) ON DELETE CASCADE
);

CREATE TABLE categories (
    id serial PRIMARY KEY,
    parent_category_id int,
    name varchar(100) NOT NULL UNIQUE,
    description text,
    created_at timestamp DEFAULT NOW(),
    CONSTRAINT fk_categories_on_categories
        FOREIGN KEY (parent_category_id) REFERENCES categories(id) ON DELETE SET NULL
);

CREATE TABLE product_categories (
    product_id int NOT NULL,
    category_id int NOT NULL,
    PRIMARY KEY (product_id, category_id),
    CONSTRAINT fk_product_categories_on_products
        FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE,
    CONSTRAINT fk_product_categories_on_categories
        FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE CASCADE
);

CREATE TABLE carts (
    id serial PRIMARY KEY,
    user_id int,
    total_amount decimal(10, 2) DEFAULT 0.00,
    updated_at timestamp DEFAULT NOW(),
    CONSTRAINT fk_carts_on_users
        FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

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

CREATE TABLE sales_analytics_reports (
    id serial PRIMARY KEY,
    product_id int,
    period_start date NOT NULL,
    period_end date NOT NULL,
    aggregated_metrics jsonb,
    ai_insights_text text,
    created_at timestamp DEFAULT NOW(),
    CONSTRAINT fk_sales_analytics_reports_on_products
        FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE
);

CREATE TABLE user_metric_events (
    id serial PRIMARY KEY,
    user_id int,
    event_type varchar(100) NOT NULL,
    search_query text,
    created_at timestamp DEFAULT NOW(),
    CONSTRAINT fk_user_metric_events_on_users
        FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);


