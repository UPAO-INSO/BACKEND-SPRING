-- Primero eliminamos los DEFAULT que referencian tipos enum
ALTER TABLE tables       ALTER COLUMN status       DROP DEFAULT;
ALTER TABLE orders       ALTER COLUMN order_status DROP DEFAULT;
ALTER TABLE product_order ALTER COLUMN status      DROP DEFAULT;
ALTER TABLE vouchers     ALTER COLUMN currency     DROP DEFAULT;

-- Convertimos todas las columnas de tipo enum a VARCHAR
ALTER TABLE tokens         ALTER COLUMN token_type     TYPE VARCHAR(50) USING token_type::text;
ALTER TABLE "user"         ALTER COLUMN role            TYPE VARCHAR(50) USING role::text;
ALTER TABLE tables         ALTER COLUMN status          TYPE VARCHAR(50) USING status::text;
ALTER TABLE customers      ALTER COLUMN document_type   TYPE VARCHAR(50) USING document_type::text;
ALTER TABLE product_order  ALTER COLUMN status          TYPE VARCHAR(50) USING status::text;
ALTER TABLE orders         ALTER COLUMN order_status    TYPE VARCHAR(50) USING order_status::text;
ALTER TABLE payments       ALTER COLUMN payment_type    TYPE VARCHAR(50) USING payment_type::text;
ALTER TABLE vouchers       ALTER COLUMN voucher_type    TYPE VARCHAR(50) USING voucher_type::text;
ALTER TABLE vouchers       ALTER COLUMN currency        TYPE VARCHAR(50) USING currency::text;
ALTER TABLE job            ALTER COLUMN title           TYPE VARCHAR(50) USING title::text;
ALTER TABLE inventory      ALTER COLUMN type            TYPE VARCHAR(50) USING type::text;
ALTER TABLE inventory      ALTER COLUMN unit_of_measure TYPE VARCHAR(50) USING unit_of_measure::text;
ALTER TABLE product_inventory ALTER COLUMN unit_of_measure TYPE VARCHAR(50) USING unit_of_measure::text;

-- Restauramos los DEFAULT como strings simples
ALTER TABLE tables        ALTER COLUMN status       SET DEFAULT 'AVAILABLE';
ALTER TABLE orders        ALTER COLUMN order_status SET DEFAULT 'PENDING';
ALTER TABLE product_order ALTER COLUMN status       SET DEFAULT 'PENDING';
ALTER TABLE vouchers      ALTER COLUMN currency     SET DEFAULT 'PEN';

-- Eliminamos los tipos enum ya que no son necesarios
DROP TYPE IF EXISTS token_type;
DROP TYPE IF EXISTS user_role;
DROP TYPE IF EXISTS table_status;
DROP TYPE IF EXISTS document_type;
DROP TYPE IF EXISTS product_order_status;
DROP TYPE IF EXISTS order_status;
DROP TYPE IF EXISTS payment_type;
DROP TYPE IF EXISTS voucher_type;
DROP TYPE IF EXISTS currency_code;
DROP TYPE IF EXISTS job_title;
DROP TYPE IF EXISTS inventory_type;
DROP TYPE IF EXISTS unit_of_measure;
