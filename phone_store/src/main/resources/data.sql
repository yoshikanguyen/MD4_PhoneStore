INSERT INTO admin (username, password) VALUES
                                           ('admin_1', 'pass_1'),
                                           ('admin_2', 'pass_2'),
                                           ('admin_3', 'pass_3'),
                                           ('admin_4', 'pass_4'),
                                           ('admin_5', 'pass_5');

INSERT INTO customer (name, phone, email, address, is_deleted) VALUES
                                                                   ('Abby', '0901122334', 'abby.nguyen@gmail.com', 'Hà Nội', 0),
                                                                   ('Boby', '0912233445', 'bobby.tran@gmail.com', 'TP.HCM', 0),
                                                                   ('Cathy', '0923344556', 'cathy.le@gmail.com', 'Đà Nẵng', 0),
                                                                   ('Danny', '0934455667', 'danny.pham@gmail.com', 'Huế', 0),
                                                                   ('Elly', '0945566778', 'elly.hoang@gmail.com', 'Cần Thơ', 0);

INSERT INTO product (name, brand, price, stock, image, is_deleted) VALUES
                                                                       ('iPhone 15 Pro Max', 'Apple', 29990000.00, 20, 'iphone15promax.jpg', 0),
                                                                       ('Samsung Galaxy S24 Ultra', 'Samsung', 26990000.00, 15, 's24ultra.jpg', 0),
                                                                       ('Xiaomi 14 Pro', 'Xiaomi', 18990000.00, 30, 'xiaomi14pro.jpg', 0),
                                                                       ('Oppo Find X7 Ultra', 'Oppo', 22990000.00, 10, 'oppofindx7.jpg', 0),
                                                                       ('iPhone 14', 'Apple', 19990000.00, 25, 'iphone14.jpg', 0);

INSERT INTO invoice (customer_id, total_amount, status) VALUES
                                                            (1, 29990000.00, 'COMPLETED'),
                                                            (2, 26990000.00, 'CONFIRMED'),
                                                            (3, 18990000.00, 'PENDING'),
                                                            (4, 22990000.00, 'SHIPING'),
                                                            (5, 19990000.00, 'CANCELED');

INSERT INTO invoice_details (invoice_id, product_id, quantity, unit_price) VALUES
                                                                               (1, 1, 1, 29990000.00),
                                                                               (2, 2, 1, 26990000.00),
                                                                               (3, 3, 1, 18990000.00),
                                                                               (4, 4, 1, 22990000.00),
                                                                               (5, 5, 1, 19990000.00);