-- =====================================================================
-- V13: Tabla de pensionistas
-- Plan de 30 días (no necesariamente continuos) con precio diferenciado
-- por almuerzo (ej. S/9 en vez de S/10).
-- =====================================================================

CREATE TABLE IF NOT EXISTS pensionista
(
    id                   BIGINT           PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    name                 VARCHAR(255)     NOT NULL,
    phone                VARCHAR(30)      NULL,
    customer_id          BIGINT           NULL,
    plan_credits         INT              NOT NULL DEFAULT 30,
    plan_price_per_meal  DECIMAL(10, 2)   NOT NULL,
    plan_total_paid      DECIMAL(10, 2)   NOT NULL,
    credits_remaining    INT              NOT NULL,
    start_date           DATE             NOT NULL,
    active               BOOLEAN          NOT NULL DEFAULT TRUE,
    notes                TEXT             NULL,
    created_at           TIMESTAMP(6)     NOT NULL DEFAULT CURRENT_TIMESTAMP(6),

    CONSTRAINT fk_pensionista_customer FOREIGN KEY (customer_id) REFERENCES customers (id)
);

CREATE INDEX idx_pensionista_customer_id ON pensionista(customer_id);
CREATE INDEX idx_pensionista_active ON pensionista(active);
