CREATE TYPE inventory_type AS ENUM('BEVERAGE', 'INGREDIENT', 'DISPOSABLE');
CREATE TYPE unit_of_measure AS ENUM('MG', 'G', 'KG', 'ML', 'L', 'UNIDAD');

-- Tabla inventory (para insumos, bebidas y descartables)
CREATE TABLE IF NOT EXISTS inventory
(
    id              BIGINT       PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    name            VARCHAR(255) NOT NULL,  -- Nombre del ítem en inventario (insumo, bebida, descartable)
    quantity        DECIMAL(15, 6) NOT NULL,  -- Usa DECIMAL para manejar valores enteros y decimales
    type            inventory_type NOT NULL, -- Tipo de objeto de inventario
    unit_of_measure unit_of_measure NOT NULL -- Unidad de medida (kg, litro, unidad)
);

-- Tabla product_inventory (relaciona productos con cantidades de inventario, insumos y bebidas)
CREATE TABLE IF NOT EXISTS product_inventory
(
    id              BIGINT       PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    quantity        DECIMAL(15, 6) NOT NULL,  -- Usamos DECIMAL para manejar tanto enteros como decimales
    unit_of_measure unit_of_measure NOT NULL,  -- Unidad de medida (kg, litro, unidad)
    product_id      BIGINT       NOT NULL,  -- Referencia al producto (plato, bebida, etc.)
    inventory_id    BIGINT       NOT NULL,  -- Referencia al inventario (insumo, bebida, etc.)

    CONSTRAINT fk_product FOREIGN KEY (product_id) REFERENCES product (id),
    CONSTRAINT fk_inventory FOREIGN KEY (inventory_id) REFERENCES inventory (id)
);
