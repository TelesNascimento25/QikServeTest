INSERT INTO product (id, name, price) VALUES
                                          ('PWWe3w1SDU', 'Amazing Burger!', 999),
                                          ('Dwt5F7KAhi', 'Amazing Pizza!', 1099),
                                          ('C8GDyLrHJb', 'Amazing Salad!', 499),
                                          ('4MB7UfpTQs', 'Boring Fries!', 199);


INSERT INTO promotion (product_id, type, required_qty, free_qty, price, amount) VALUES
    ('PWWe3w1SDU', 'BUY_X_GET_Y_FREE', 2, 1, null , null);

INSERT INTO promotion (product_id, type, required_qty, price, free_qty, amount) VALUES
    ('Dwt5F7KAhi', 'QTY_BASED_PRICE_OVERRIDE', 2, 1799, null, null);

INSERT INTO promotion (product_id, type, amount, required_qty, free_qty, price) VALUES
    ('C8GDyLrHJb', 'FLAT_PERCENT', 10, null, null, null);



