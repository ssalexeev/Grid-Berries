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
