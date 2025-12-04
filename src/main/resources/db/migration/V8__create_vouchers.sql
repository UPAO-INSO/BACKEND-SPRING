CREATE TABLE IF NOT EXISTS `vouchers`
(
    id             BIGINT                      NOT NULL AUTO_INCREMENT PRIMARY KEY,
    series         VARCHAR(100)                NOT NULL,
    number         VARCHAR(100)                NOT NULL,
    voucher_type   ENUM ('INVOICE', 'RECEIPT') NOT NULL,
    issued_at      TIMESTAMP                   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    currency       ENUM ('PEN')                NOT NULL DEFAULT 'PEN',
    igv_percentage DECIMAL(5, 2)               NOT NULL DEFAULT 18.00,
    total_gravada  DECIMAL(19, 2)              NOT NULL,
    total_igv      DECIMAL(19, 2)              NOT NULL,
    total          DECIMAL(19, 2)              NOT NULL,
    observations   VARCHAR(500)                NULL,
    pdf_url        VARCHAR(1000)               NOT NULL,
    xml_url        VARCHAR(1000)               NOT NULL,
    qr_code_string VARCHAR(1000)               NOT NULL,
    bar_code       VARCHAR(1000)               NOT NULL,
    payment_id     BIGINT                      NULL UNIQUE,

    CONSTRAINT fk_vouchers_payment FOREIGN KEY (payment_id) REFERENCES payments (id)
        ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;