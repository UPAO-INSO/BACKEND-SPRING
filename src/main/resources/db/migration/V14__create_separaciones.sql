-- =====================================================================
-- V14: Tablas de separaciones y sus ítems
-- Una separación es un aviso de producción diario: aparta platos del menú
-- del día para un cliente o pensionista, sin cobro inmediato.
-- El inventario se descuenta solo cuando el estado pasa a ENTREGADA.
-- =====================================================================

CREATE TABLE IF NOT EXISTS separacion
(
    id              BIGINT          PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    date            DATE            NOT NULL DEFAULT CURRENT_DATE,
    pensionista_id  BIGINT          NULL,
    customer_id     BIGINT          NULL,
    client_name     VARCHAR(255)    NULL,
    status          VARCHAR(20)     NOT NULL DEFAULT 'PENDIENTE',
    total_price     DECIMAL(10, 2)  NOT NULL DEFAULT 0,
    notes           TEXT            NULL,
    created_at      TIMESTAMP(6)    NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at      TIMESTAMP(6)    NOT NULL DEFAULT CURRENT_TIMESTAMP(6),

    CONSTRAINT fk_separacion_pensionista FOREIGN KEY (pensionista_id) REFERENCES pensionista (id),
    CONSTRAINT fk_separacion_customer    FOREIGN KEY (customer_id)    REFERENCES customers (id)
);

CREATE INDEX idx_separacion_date           ON separacion(date);
CREATE INDEX idx_separacion_pensionista_id ON separacion(pensionista_id);
CREATE INDEX idx_separacion_status         ON separacion(status);

-- =====================================================================

CREATE TABLE IF NOT EXISTS separacion_item
(
    id             BIGINT          PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    separacion_id  BIGINT          NOT NULL,
    product_id     BIGINT          NOT NULL,
    quantity       INT             NOT NULL,
    unit_price     DECIMAL(10, 2)  NOT NULL DEFAULT 0,

    CONSTRAINT fk_separacion_item_separacion FOREIGN KEY (separacion_id) REFERENCES separacion (id),
    CONSTRAINT fk_separacion_item_product    FOREIGN KEY (product_id)    REFERENCES product (id)
);

CREATE INDEX idx_separacion_item_separacion_id ON separacion_item(separacion_id);
CREATE INDEX idx_separacion_item_product_id    ON separacion_item(product_id);
