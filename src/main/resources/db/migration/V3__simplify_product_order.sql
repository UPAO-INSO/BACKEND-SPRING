ALTER TABLE product_order
    ADD COLUMN served_quantity INT NOT NULL DEFAULT 0 AFTER quantity;

UPDATE product_order po
    LEFT JOIN (SELECT product_order_id, SUM(served_quantity) as total_served
               FROM product_order_item
               GROUP BY product_order_id) poi ON po.id = poi.product_order_id
SET po.served_quantity = COALESCE(poi.total_served, 0);

UPDATE product_order
SET status = CASE
                 WHEN served_quantity = 0 THEN 'PENDING'
                 WHEN served_quantity >= quantity THEN 'SERVED'
                 ELSE 'PREPARING'
    END;

DROP TABLE IF EXISTS product_order_item;

