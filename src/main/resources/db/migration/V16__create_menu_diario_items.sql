-- =====================================================================
-- V16: Tabla de porciones del menú diario
-- Permite rastrear cuántas porciones se prepararon por producto por día
-- y cuántas ya fueron consumidas (pedidos + separaciones).
-- Si no existe un registro para un producto ese día → sin límite.
-- =====================================================================

CREATE TABLE IF NOT EXISTS menu_diario_item
(
    id                  BIGINT       PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    product_id          BIGINT       NOT NULL,
    date                DATE         NOT NULL DEFAULT CURRENT_DATE,
    estimated_portions  INT          NOT NULL,
    used_portions       INT          NOT NULL DEFAULT 0,
    created_at          TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),

    CONSTRAINT fk_mdi_product FOREIGN KEY (product_id) REFERENCES product (id),
    CONSTRAINT uq_mdi_product_date UNIQUE (product_id, date)
);

CREATE INDEX idx_mdi_date ON menu_diario_item(date);
