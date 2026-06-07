-- =====================================================================
-- V15: Tabla de consumos de pensionistas
-- Registra cada entrega al pensionista, descontando un crédito del plan.
-- =====================================================================

CREATE TABLE IF NOT EXISTS pensionista_consumo
(
    id               BIGINT          PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    pensionista_id   BIGINT          NOT NULL,
    separacion_id    BIGINT          NULL,
    date             DATE            NOT NULL DEFAULT CURRENT_DATE,
    price_applied    DECIMAL(10, 2)  NOT NULL,
    created_at       TIMESTAMP(6)    NOT NULL DEFAULT CURRENT_TIMESTAMP(6),

    CONSTRAINT fk_pc_pensionista  FOREIGN KEY (pensionista_id) REFERENCES pensionista (id),
    CONSTRAINT fk_pc_separacion   FOREIGN KEY (separacion_id)  REFERENCES separacion (id)
);

CREATE INDEX idx_pc_pensionista_id  ON pensionista_consumo(pensionista_id);
CREATE INDEX idx_pc_date            ON pensionista_consumo(date);
