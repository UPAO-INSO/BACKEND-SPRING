-- sql

-- Crear TYPES
CREATE TYPE job_title AS ENUM ('CAJERO', 'COCINERO', 'GERENTE', 'MESERO');
CREATE TYPE user_role AS ENUM ('ADMIN', 'SUPER_USER', 'USER');
CREATE TYPE table_status AS ENUM ('AVAILABLE', 'OCCUPIED', 'RESERVED');
CREATE TYPE token_type AS ENUM ('ACCESS', 'REFRESH', 'BEARER');
CREATE TYPE document_type AS ENUM ('DNI', 'RUC');
CREATE TYPE product_order_status AS ENUM ('PENDING', 'PREPARING', 'SERVED');
CREATE TYPE order_status AS ENUM ('PENDING', 'PREPARING','READY', 'PAID', 'CANCELLED', 'COMPLETED');

-- Tabla persons
CREATE TABLE IF NOT EXISTS persons
(
    id         BIGINT       PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    name       VARCHAR(255) NOT NULL,
    lastname   VARCHAR(255) NOT NULL,
    phone      VARCHAR(255) NOT NULL UNIQUE ,
    active     BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at TIMESTAMP(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP(6)
);

CREATE INDEX idx_persons_name ON persons(name);
CREATE INDEX idx_persons_lastname ON persons(lastname);

-- Tabla job
CREATE TABLE IF NOT EXISTS job
(
    id    BIGINT                                  PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    title job_title                               NOT NULL UNIQUE
);

-- Tabla user
CREATE TABLE IF NOT EXISTS "user"
(
    id         BIGINT                             NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    username   VARCHAR(100)                       NOT NULL UNIQUE,
    email      VARCHAR(150)                       NOT NULL UNIQUE,
    password   VARCHAR(150)                       NOT NULL,
    role       user_role                          NOT NULL,
    is_active  BOOLEAN                            NOT NULL DEFAULT TRUE,
    last_login TIMESTAMP(6)                       NULL     DEFAULT NULL,
    created_at TIMESTAMP(6)                       NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at TIMESTAMP(6)                       NOT NULL DEFAULT CURRENT_TIMESTAMP(6)
);

CREATE INDEX idx_user_email ON "user"(email);

-- Tabla product_type
CREATE TABLE IF NOT EXISTS product_type
(
    id   BIGINT       PRIMARY KEY NOT NULL GENERATED ALWAYS AS IDENTITY,
    name VARCHAR(255) NOT NULL
);

CREATE INDEX idx_product_type_name ON product_type(name);

-- Tabla tables
CREATE TABLE IF NOT EXISTS "tables"
(
    id        BIGINT                                   NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    number    VARCHAR(255)                             NOT NULL,
    floor   INT                                        NOT NULL,
    capacity  INT                                      NOT NULL,
    is_active BOOLEAN                                  NOT NULL DEFAULT TRUE,
    status    table_status                             NOT NULL DEFAULT 'AVAILABLE'::table_status
);

CREATE INDEX idx_tables_number ON "tables"(number);
CREATE INDEX idx_tables_floor ON "tables"(floor);

-- Tabla customers (usa el mismo id que persons)
CREATE TABLE IF NOT EXISTS customers
(
    id               BIGINT              PRIMARY KEY,
    document_number  VARCHAR(20)         NOT NULL,
    document_type    document_type       NOT NULL,
    email            VARCHAR(200)        NOT NULL,
    departament      VARCHAR(100)        NOT NULL,
    province         VARCHAR(100)        NOT NULL,
    district         VARCHAR(100)        NOT NULL,
    complete_address VARCHAR(255)        NOT NULL,

    CONSTRAINT fk_customers_person FOREIGN KEY (id) REFERENCES persons (id)
);

CREATE INDEX idx_customers_document_number ON customers(document_number);
CREATE INDEX idx_customers_email ON customers(email);

-- Tabla employees (usa el mismo id que persons)
CREATE TABLE IF NOT EXISTS employees
(
    id         BIGINT               PRIMARY KEY,
    salary     DOUBLE PRECISION     NOT NULL,
    job_id     BIGINT               NOT NULL,
    user_id    BIGINT               NULL,
    created_at TIMESTAMP(6)         NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at TIMESTAMP(6)         NOT NULL DEFAULT CURRENT_TIMESTAMP(6),

    CONSTRAINT fk_employees_person FOREIGN KEY (id) REFERENCES persons (id),
    CONSTRAINT fk_employees_job FOREIGN KEY (job_id) REFERENCES job (id),
    CONSTRAINT fk_employees_user FOREIGN KEY (user_id) REFERENCES "user" (id)
);

CREATE INDEX idx_employees_job_id ON employees(job_id);
CREATE INDEX idx_employees_user_id ON employees(user_id);

-- Tabla product
CREATE TABLE IF NOT EXISTS product
(
    id                  BIGINT                  PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    name                VARCHAR(255)            NOT NULL,
    description         VARCHAR(255)            NOT NULL,
    price               DOUBLE PRECISION        NOT NULL DEFAULT 0,
    active              BOOLEAN                 NOT NULL DEFAULT TRUE,
    available           BOOLEAN                 NOT NULL DEFAULT TRUE,
    product_type_id     BIGINT                  NOT NULL,

    CONSTRAINT fk_product_product_type FOREIGN KEY (product_type_id) REFERENCES product_type (id)
);

CREATE INDEX idx_product_name ON product(name);
CREATE INDEX idx_product_type_id ON product(product_type_id);

-- Tabla orders
CREATE TABLE IF NOT EXISTS "orders"
(
    id           VARCHAR(36)            PRIMARY KEY,
    paid         BOOLEAN                NOT NULL DEFAULT FALSE,
    total_items  INT                    NOT NULL DEFAULT 0,
    total_price  DOUBLE PRECISION       NOT NULL DEFAULT 0,
    created_at   TIMESTAMP(6)           NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at   TIMESTAMP(6)           NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    paid_at      TIMESTAMP(6)           NULL     DEFAULT NULL,
    table_id     BIGINT                 NULL,
    comment      VARCHAR(255)           NULL,
    order_status order_status           NOT NULL DEFAULT 'PENDING'::order_status,

    CONSTRAINT fk_orders_table FOREIGN KEY (table_id) REFERENCES "tables" (id)
);

CREATE INDEX idx_orders_table_id ON orders (table_id);

-- Tabla product_order
CREATE TABLE IF NOT EXISTS product_order
(
    id         BIGINT               PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    quantity   INT                  NOT NULL,
    subtotal   DOUBLE PRECISION     NOT NULL,
    unit_price DOUBLE PRECISION     NOT NULL,
    order_id   VARCHAR(36)          NOT NULL,
    product_id BIGINT               NOT NULL,

    CONSTRAINT fk_po_order FOREIGN KEY (order_id) REFERENCES orders (id),
    CONSTRAINT fk_po_product FOREIGN KEY (product_id) REFERENCES product (id)
);

CREATE INDEX idx_po_order_id ON product_order(order_id);
CREATE INDEX idx_po_product_id ON product_order(product_id);

-- Tabla order_employee
CREATE TABLE IF NOT EXISTS order_employee
(
    id            BIGINT      PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    minutes_spent INT DEFAULT 0,
    employee_id   BIGINT      NOT NULL,
    order_id      VARCHAR(36) NOT NULL,

    CONSTRAINT fk_oe_employee FOREIGN KEY (employee_id) REFERENCES employees (id),
    CONSTRAINT fk_oe_order FOREIGN KEY (order_id) REFERENCES orders (id)
);

CREATE INDEX idx_order_employee_order_id ON order_employee(order_id);
CREATE INDEX idx_order_employee_employee_id ON order_employee(employee_id);

-- Tabla tokens
CREATE TABLE IF NOT EXISTS tokens
(
    id         BIGINT                              PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    token      VARCHAR(500)                        NOT NULL UNIQUE,
    token_type token_type                          NOT NULL,
    revoked    BOOLEAN                             NOT NULL DEFAULT FALSE,
    expired    BOOLEAN                             NOT NULL DEFAULT FALSE,
    user_id    BIGINT                              NOT NULL,

    CONSTRAINT fk_tokens_user FOREIGN KEY (user_id) REFERENCES "user" (id)
);
