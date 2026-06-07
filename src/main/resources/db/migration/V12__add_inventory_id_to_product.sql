-- =====================================================================
-- V12: Vincular directamente product → inventory para bebidas/descartables
-- =====================================================================
-- PROBLEMA: Las bebidas y descartables se almacenaban en AMBAS tablas
-- (product y inventory) con el mismo nombre, y el frontend los enlazaba
-- por coincidencia de nombre (frágil y redundante).
--
-- SOLUCIÓN: Agregar columna inventory_id a product para crear un FK
-- directo, eliminando la dependencia del nombre como clave de búsqueda.
-- =====================================================================

-- 1. Agregar columna inventory_id a product (nullable)
ALTER TABLE product ADD COLUMN inventory_id BIGINT REFERENCES inventory(id);

-- 2. Poblar inventory_id para todos los productos de tipo BEBIDAS y DESCARTABLES
--    usando la relación existente en product_inventory
UPDATE product p
SET inventory_id = (
    SELECT pi.inventory_id
    FROM product_inventory pi
    JOIN inventory i ON i.id = pi.inventory_id
    WHERE pi.product_id = p.id
      AND i.type IN ('BEVERAGE', 'DISPOSABLE')
    ORDER BY pi.id
    LIMIT 1
)
WHERE p.product_type_id IN (
    SELECT id FROM product_type WHERE name IN ('BEBIDAS', 'DESCARTABLES')
);
