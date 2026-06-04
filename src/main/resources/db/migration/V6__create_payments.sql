CREATE TYPE payment_type AS ENUM ('MOBILE_WALLET', 'CASH');

CREATE TABLE IF NOT EXISTS payments
(
    id              BIGINT                         PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    provider        VARCHAR(50)                    NOT NULL,
    external_id     VARCHAR(100)                   NOT NULL,
    amount          INTEGER                        NOT NULL,
    currency_code   VARCHAR(10)                    NOT NULL,
    description     VARCHAR(500)                   NOT NULL,
    order_id        VARCHAR(36)                    NOT NULL UNIQUE,
    customer_id     BIGINT                         NOT NULL,
    payment_type    payment_type                   NOT NULL,
    state           VARCHAR(50)                    NOT NULL,
    total_fee       DECIMAL(19, 2),
    net_amount      DECIMAL(19, 2),
    qr              VARCHAR(1000)                  NOT NULL,
    url_pe          VARCHAR(1000)                  NOT NULL,
    creation_date   TIMESTAMP                      NOT NULL,
    expiration_date TIMESTAMP                      NOT NULL,
    updated_at      TIMESTAMP                      NOT NULL,
    paid_at         TIMESTAMP                      NULL,
    raw_response    TEXT                           NOT NULL,
    created_at      TIMESTAMP                      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modified_at     TIMESTAMP                      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_payments_order FOREIGN KEY (order_id) REFERENCES orders (id),
    CONSTRAINT fk_payments_customer FOREIGN KEY (customer_id) REFERENCES customers (id) ON DELETE RESTRICT ON UPDATE CASCADE
);

CREATE INDEX idx_payments_order_id ON payments (order_id);
CREATE INDEX idx_payments_external_id ON payments (external_id);
