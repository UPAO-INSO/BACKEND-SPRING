-- sql

SET FOREIGN_KEY_CHECKS = 0;

-- Tabla persons
CREATE TABLE IF NOT EXISTS persons
(
    id         BIGINT       NOT NULL AUTO_INCREMENT,
    name       VARCHAR(255) NOT NULL,
    lastname   VARCHAR(255) NOT NULL,
    phone      VARCHAR(255) NOT NULL,
    active     BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    PRIMARY KEY (id),
    UNIQUE KEY uk_persons_phone (phone),
    KEY idx_persons_name (name),
    KEY idx_persons_lastname (lastname)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- Tabla job
CREATE TABLE IF NOT EXISTS job
(
    id    BIGINT                                        NOT NULL AUTO_INCREMENT,
    title ENUM ('CAJERO','COCINERO','GERENTE','MESERO') NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_job_title (title)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- Tabla user
CREATE TABLE IF NOT EXISTS `user`
(
    id         BIGINT                             NOT NULL AUTO_INCREMENT,
    username   VARCHAR(100)                       NOT NULL,
    email      VARCHAR(150)                       NOT NULL,
    password   VARCHAR(150)                       NOT NULL,
    role       ENUM ('ADMIN','SUPER_USER','USER') NOT NULL,
    is_active  BOOLEAN                            NOT NULL DEFAULT TRUE,
    last_login DATETIME(6)                        NULL     DEFAULT NULL,
    created_at DATETIME(6)                        NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at DATETIME(6)                        NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    PRIMARY KEY (id),
    UNIQUE KEY uk_user_username (username),
    UNIQUE KEY uk_user_email (email),
    KEY idx_user_email (email)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- Tabla product_type
CREATE TABLE IF NOT EXISTS product_type
(
    id   BIGINT       NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    PRIMARY KEY (id),
    KEY idx_product_type_name (name)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- Tabla tables
CREATE TABLE IF NOT EXISTS `tables`
(
    id        BIGINT                                   NOT NULL AUTO_INCREMENT,
    number    VARCHAR(255)                             NOT NULL,
    `floor`   INT                                      NOT NULL,
    capacity  INT                                      NOT NULL,
    is_active BOOLEAN                                  NOT NULL DEFAULT TRUE,
    status    ENUM ('AVAILABLE','OCCUPIED','RESERVED') NOT NULL DEFAULT 'AVAILABLE',
    PRIMARY KEY (id),
    KEY idx_tables_number (number),
    KEY idx_tables_floor (`floor`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- Tabla clients (usa el mismo id que persons)
CREATE TABLE IF NOT EXISTS clients
(
    id              BIGINT       NOT NULL,
    document_number VARCHAR(255) NOT NULL,
    email           VARCHAR(255) NOT NULL,
    PRIMARY KEY (id),
    KEY idx_clients_document_number (document_number),
    KEY idx_clients_email (email),
    CONSTRAINT fk_clients_person FOREIGN KEY (id) REFERENCES persons (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- Tabla employees (usa el mismo id que persons)
CREATE TABLE IF NOT EXISTS employees
(
    id         BIGINT      NOT NULL,
    salary     DOUBLE      NOT NULL,
    job_id     BIGINT      NOT NULL,
    user_id    BIGINT      NULL,
    created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    PRIMARY KEY (id),
    KEY idx_employees_job_id (job_id),
    KEY idx_employees_user_id (user_id),
    CONSTRAINT fk_employees_person FOREIGN KEY (id) REFERENCES persons (id),
    CONSTRAINT fk_employees_job FOREIGN KEY (job_id) REFERENCES job (id),
    CONSTRAINT fk_employees_user FOREIGN KEY (user_id) REFERENCES `user` (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- Tabla product
CREATE TABLE IF NOT EXISTS product
(
    id              BIGINT       NOT NULL AUTO_INCREMENT,
    name            VARCHAR(255) NOT NULL,
    description     VARCHAR(255) NOT NULL,
    price           DOUBLE       NOT NULL DEFAULT 0,
    active          BOOLEAN      NOT NULL DEFAULT TRUE,
    available       BOOLEAN      NOT NULL DEFAULT TRUE,
    product_type_id BIGINT       NOT NULL,
    PRIMARY KEY (id),
    KEY idx_product_name (name),
    KEY idx_product_type_id (product_type_id),
    CONSTRAINT fk_product_product_type FOREIGN KEY (product_type_id) REFERENCES product_type (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- Tabla orders
CREATE TABLE IF NOT EXISTS `orders`
(
    id           VARCHAR(36) NOT NULL,
    paid         BOOLEAN      NOT NULL DEFAULT FALSE,
    total_items  INT          NOT NULL DEFAULT 0,
    total_price  DOUBLE       NOT NULL DEFAULT 0,
    created_at   DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at   DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    paid_at      DATETIME(6)  NULL     DEFAULT NULL,
    table_id     BIGINT       NULL,
    comment      VARCHAR(255) NULL,
    order_status VARCHAR(20)  NOT NULL DEFAULT 'PENDING',
    client_id    BIGINT       NULL,
    PRIMARY KEY (id),
    KEY idx_orders_table_id (table_id),
    KEY idx_orders_client_id (client_id),
    CONSTRAINT fk_orders_table FOREIGN KEY (table_id) REFERENCES `tables` (id),
    CONSTRAINT fk_orders_client FOREIGN KEY (client_id) REFERENCES clients (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- Tabla product_order
CREATE TABLE IF NOT EXISTS product_order
(
    id         BIGINT NOT NULL AUTO_INCREMENT,
    quantity   INT    NOT NULL,
    subtotal   DOUBLE NOT NULL,
    unit_price DOUBLE NOT NULL,
    order_id   VARCHAR(36) NOT NULL,
    product_id BIGINT NOT NULL,
    PRIMARY KEY (id),
    KEY idx_po_order_id (order_id),
    KEY idx_po_product_id (product_id),
    CONSTRAINT fk_po_order FOREIGN KEY (order_id) REFERENCES `orders` (id),
    CONSTRAINT fk_po_product FOREIGN KEY (product_id) REFERENCES product (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- Tabla order_employee
CREATE TABLE IF NOT EXISTS order_employee
(
    id            BIGINT NOT NULL AUTO_INCREMENT,
    minutes_spent INT DEFAULT 0,
    employee_id   BIGINT NOT NULL,
    order_id      VARCHAR(36) NOT NULL,
    PRIMARY KEY (id),
    KEY idx_order_employee_order_id (order_id),
    KEY idx_order_employee_employee_id (employee_id),
    CONSTRAINT fk_oe_employee FOREIGN KEY (employee_id) REFERENCES employees (id),
    CONSTRAINT fk_oe_order FOREIGN KEY (order_id) REFERENCES `orders` (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- Tabla tokens
CREATE TABLE IF NOT EXISTS tokens
(
    id         BIGINT                              NOT NULL AUTO_INCREMENT,
    token      VARCHAR(500)                        NOT NULL,
    token_type ENUM ('ACCESS', 'REFRESH','BEARER') NOT NULL,
    revoked    BOOLEAN                             NOT NULL DEFAULT FALSE,
    expired    BOOLEAN                             NOT NULL DEFAULT FALSE,
    user_id    BIGINT                              NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_tokens_token (token),
    KEY idx_tokens_user_id (user_id),
    CONSTRAINT fk_tokens_user FOREIGN KEY (user_id) REFERENCES `user` (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

SET FOREIGN_KEY_CHECKS = 1;
