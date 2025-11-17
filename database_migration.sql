-- Brisanje stare tabele
DROP TABLE IF EXISTS excel_rows CASCADE;

-- Kreiranje nove tabele sa tačnim nazivima kolona
CREATE TABLE merchant_inventory (
    id SERIAL PRIMARY KEY,
    upload_id UUID NOT NULL DEFAULT gen_random_uuid(),
    merchant_inventory_id VARCHAR(255),
    product_name VARCHAR(500) NOT NULL,
    status VARCHAR(255),
    l1_category VARCHAR(255),
    product_type VARCHAR(255),
    ean VARCHAR(255),
    a_code VARCHAR(255),
    sku VARCHAR(255),
    tags TEXT,
    warehouse VARCHAR(255),
    current_stock INT,
    base_price_with_vat DECIMAL(10, 2),
    new_base_price_with_vat DECIMAL(10, 2),
    vat DECIMAL(5, 2),
    new_vat DECIMAL(5, 2),
    row_number INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Indeksi za brže pretrage
CREATE INDEX idx_upload_id ON merchant_inventory(upload_id);
CREATE INDEX idx_created_at ON merchant_inventory(created_at);
CREATE INDEX idx_product_name ON merchant_inventory(product_name);
CREATE INDEX idx_status ON merchant_inventory(status);
CREATE INDEX idx_l1_category ON merchant_inventory(l1_category);
CREATE INDEX idx_product_type ON merchant_inventory(product_type);
CREATE INDEX idx_ean ON merchant_inventory(ean);
CREATE INDEX idx_sku ON merchant_inventory(sku);
CREATE INDEX idx_warehouse ON merchant_inventory(warehouse);
CREATE INDEX idx_merchant_inventory_id ON merchant_inventory(merchant_inventory_id);

