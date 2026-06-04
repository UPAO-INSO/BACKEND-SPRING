ALTER TABLE product_order
    ADD COLUMN served_quantity INT NOT NULL DEFAULT 0;

UPDATE product_order po
SET served_quantity = COALESCE(
(SELECT SUM(served_quantity)
 FROM product_order_item
 WHERE product_order_id = po.id), 0
);

UPDATE product_order
SET status = CASE
    WHEN served_quantity = 0 THEN 'PENDING'::product_order_status
    WHEN served_quantity >= quantity THEN 'SERVED'::product_order_status
    ELSE 'PREPARING'::product_order_status
END;

DROP TABLE IF EXISTS product_order_item;
DROP TYPE IF EXISTS product_order_item_status;
