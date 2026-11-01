CREATE SCHEMA phone_store_db;
USE phone_store_db;

CREATE TABLE IF NOT EXISTS admin (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS customer (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    phone VARCHAR(20) NOT NULL UNIQUE,
    email VARCHAR(100) NULL DEFAULT NULL UNIQUE,
    address VARCHAR(255) NULL DEFAULT NULL,
    is_deleted BIT(1) DEFAULT 0
);

CREATE TABLE IF NOT EXISTS product (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    brand VARCHAR(50) NOT NULL,
    price DECIMAL(12,2) NOT NULL,
    stock INT NOT NULL,
    image VARCHAR(255) NOT NULL,
    is_deleted BIT(1) DEFAULT 0
);

CREATE TABLE IF NOT EXISTS invoice (
    id INT AUTO_INCREMENT PRIMARY KEY,
    customer_id INT DEFAULT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    total_amount DECIMAL(12,2) NOT NULL,
    status ENUM('PENDING', 'CONFIRMED', 'SHIPING', 'COMPLETED', 'CANCELED') NOT NULL DEFAULT 'PENDING',
    CONSTRAINT fk_invoice_customer FOREIGN KEY (customer_id) REFERENCES customer(id)
);

CREATE TABLE IF NOT EXISTS invoice_details (
    id INT AUTO_INCREMENT PRIMARY KEY,
    invoice_id INT DEFAULT NULL,
    product_id INT DEFAULT NULL,
    quantity INT NOT NULL,
    unit_price DECIMAL(12,2) NOT NULL,
    CONSTRAINT fk_details_invoice FOREIGN KEY (invoice_id) REFERENCES invoice(id),
    CONSTRAINT fk_details_product FOREIGN KEY (product_id) REFERENCES product(id)
);