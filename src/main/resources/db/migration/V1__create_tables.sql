CREATE TYPE basket_status AS ENUM ('OPEN', 'CHECKED_OUT', 'CANCELLED', 'DELETED');

CREATE TABLE basket
(
    id         SERIAL PRIMARY KEY,
    status     basket_status  NOT NULL,
    total      DECIMAL(10, 2) NOT NULL,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE TABLE basket_item
(
    id         SERIAL PRIMARY KEY,
    basket_id  INT     NOT NULL REFERENCES basket (id),
    product_id VARCHAR NOT NULL,
    quantity   INT     NOT NULL
);
