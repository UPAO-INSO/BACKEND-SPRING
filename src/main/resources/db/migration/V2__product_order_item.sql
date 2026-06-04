CREATE TYPE product_order_item_status AS ENUM ('PENDING', 'PREPARING', 'SERVED');

CREATE TABLE IF NOT EXISTS product_order_item
(
    id                BIGINT                                  PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    product_order_id  BIGINT                                  NOT NULL,
    quantity          INT                                     NOT NULL,
    prepared_quantity INT                                     NOT NULL DEFAULT 0,
    served_quantity   INT                                     NOT NULL DEFAULT 0,
    status            product_order_item_status               NOT NULL DEFAULT 'PENDING'::product_order_item_status,

    CONSTRAINT fk_poi_product_order FOREIGN KEY (product_order_id) REFERENCES product_order (id)
);

CREATE INDEX idx_poi_product_order_id ON product_order_item(product_order_id);

ALTER TABLE product_order
    ADD COLUMN status product_order_status NOT NULL DEFAULT 'PENDING'::product_order_status;
