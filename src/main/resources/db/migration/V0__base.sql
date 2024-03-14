CREATE TABLE IF NOT EXISTS delivery (
    id SERIAL PRIMARY KEY,
    uuid varchar(36),
    vehicle_id varchar(10),
    started_at TIMESTAMP,
    finished_at TIMESTAMP,
    status smallint
);

CREATE TABLE IF NOT EXISTS delivery_summary (
    id SERIAL PRIMARY KEY,
    deliveries INTEGER,
    average_minutes_between_delivery_start INTEGER,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);