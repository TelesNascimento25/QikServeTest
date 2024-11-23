INSERT INTO product (id, name, price) VALUES
                                          ('PWWe3w1SDU', 'Amazing Burger!', 999),
                                          ('Dwt5F7KAhi', 'Amazing Pizza!', 1099),
                                          ('C8GDyLrHJb', 'Amazing Salad!', 499),
                                          ('4MB7UfpTQs', 'Boring Fries!', 199);

INSERT INTO promotion (product_id, type, required_qty, free_qty) VALUES
    ('PWWe3w1SDU', 'BUY_X_GET_Y_FREE', 2, 1);

INSERT INTO promotion (product_id, type, required_qty, price) VALUES
    ('Dwt5F7KAhi', 'QTY_BASED_PRICE_OVERRIDE', 2, 1799);

INSERT INTO promotion (product_id, type, amount) VALUES
    ('C8GDyLrHJb', 'FLAT_PERCENT', 10);


