CREATE TABLE IF NOT EXISTS product_order_item
(
    id                BIGINT                                  NOT NULL AUTO_INCREMENT,
    product_order_id  BIGINT                                  NOT NULL,
    quantity          INT                                     NOT NULL,
    prepared_quantity INT                                     NOT NULL DEFAULT 0,
    served_quantity   INT                                     NOT NULL DEFAULT 0,
    status            ENUM ('PENDING', 'PREPARING', 'SERVED') NOT NULL DEFAULT 'PENDING',
    PRIMARY KEY (id),
    KEY idx_poi_product_order_id (product_order_id),
    CONSTRAINT fk_poi_product_order FOREIGN KEY (product_order_id) REFERENCES product_order (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

ALTER TABLE product_order
    ADD COLUMN status ENUM ('PENDING', 'PREPARING', 'SERVED') NOT NULL DEFAULT 'PENDING';
