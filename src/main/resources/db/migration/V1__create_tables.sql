CREATE TABLE product (
                         id VARCHAR(255) PRIMARY KEY,
                         name VARCHAR(255) NOT NULL,
                         price INT NOT NULL
);

CREATE TABLE promotion (
                           id SERIAL PRIMARY KEY,
                           product_id VARCHAR(255) NOT NULL REFERENCES product(id),
                           type VARCHAR(255) NOT NULL,
                           required_qty INT,
                           free_qty INT,
                           price INT,
                           amount INT
);

CREATE TABLE basket (
                        id SERIAL PRIMARY KEY
);

CREATE TABLE basket_item (
                             id SERIAL PRIMARY KEY,
                             basket_id INT NOT NULL REFERENCES basket(id),
                             product_id VARCHAR(255) NOT NULL REFERENCES product(id),
                             quantity INT NOT NULL
);
