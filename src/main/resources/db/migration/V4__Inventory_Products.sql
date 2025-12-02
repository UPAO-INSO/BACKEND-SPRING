SET FOREIGN_KEY_CHECKS = 0;

-- Tabla inventory (para insumos, bebidas y descartables)
CREATE TABLE IF NOT EXISTS inventory
(
    id              BIGINT       NOT NULL AUTO_INCREMENT,
    name            VARCHAR(255) NOT NULL,  -- Nombre del ítem en inventario (insumo, bebida, descartable)
    quantity        DECIMAL(15, 6) NOT NULL,  -- Usa DECIMAL para manejar valores enteros y decimales
    type            ENUM('BEVERAGE', 'INGREDIENT', 'DISPOSABLE') NOT NULL, -- Tipo de objeto de inventario
    unit_of_measure ENUM('MG', 'G', 'KG', 'ML', 'L', 'UNIDAD') NOT NULL, -- Unidad de medida (kg, litro, unidad)
    PRIMARY KEY (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- Tabla product_inventory (relaciona productos con cantidades de inventario, insumos y bebidas)
CREATE TABLE IF NOT EXISTS product_inventory
(
    id              BIGINT       NOT NULL AUTO_INCREMENT,
    quantity        DECIMAL(15, 6) NOT NULL,  -- Usamos DECIMAL para manejar tanto enteros como decimales
    unit_of_measure ENUM('MG', 'G', 'KG', 'ML', 'L', 'UNIDAD') NOT NULL,  -- Unidad de medida (kg, litro, unidad)
    product_id      BIGINT       NOT NULL,  -- Referencia al producto (plato, bebida, etc.)
    inventory_id    BIGINT       NOT NULL,  -- Referencia al inventario (insumo, bebida, etc.)
    PRIMARY KEY (id),
    CONSTRAINT fk_product FOREIGN KEY (product_id) REFERENCES product (id),
    CONSTRAINT fk_inventory FOREIGN KEY (inventory_id) REFERENCES inventory (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

SET FOREIGN_KEY_CHECKS = 1;
