CREATE TABLE user_metric_events (
    id serial PRIMARY KEY,
    user_id int,
    event_type varchar(100) NOT NULL,
    search_query text,
    created_at timestamp DEFAULT NOW(),
    CONSTRAINT fk_user_metric_events_on_users
        FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
