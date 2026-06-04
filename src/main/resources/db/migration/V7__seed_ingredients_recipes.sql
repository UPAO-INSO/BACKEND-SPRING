-- =====================================================
-- V6: Ingredientes (Insumos) y Recetas para Platillos
-- =====================================================
-- NOTA: Los IDs empiezan en 200 para no conflictuar con 
-- los IDs 100-104 de bebidas/descartables en V5
-- =====================================================

-- =====================================================
-- INGREDIENTES BASE (Insumos en inventario)
-- =====================================================

INSERT INTO inventory (name, quantity, type, unit_of_measure)
VALUES
    -- Tubérculos y Vegetales
    ('Papa Blanca', 25.00, 'INGREDIENT', 'KG'),
    ('Papa Amarilla', 15.00, 'INGREDIENT', 'KG'),
    ('Yuca', 10.00, 'INGREDIENT', 'KG'),
    ('Cebolla Roja', 12.00, 'INGREDIENT', 'KG'),
    ('Tomate', 8.00, 'INGREDIENT', 'KG'),
    ('Zanahoria', 6.00, 'INGREDIENT', 'KG'),
    ('Lechuga', 4.00, 'INGREDIENT', 'KG'),
    ('Ají Amarillo', 3.00, 'INGREDIENT', 'KG'),
    ('Ají Limo', 1.50, 'INGREDIENT', 'KG'),
    ('Culantro', 2.00, 'INGREDIENT', 'KG'),
    ('Arveja', 5.00, 'INGREDIENT', 'KG'),
    ('Caigua', 3.00, 'INGREDIENT', 'KG'),
    
    -- Carnes y Proteínas
    ('Pollo', 20.00, 'INGREDIENT', 'KG'),
    ('Gallina', 10.00, 'INGREDIENT', 'KG'),
    ('Carne de Res', 15.00, 'INGREDIENT', 'KG'),
    ('Lomo de Res', 8.00, 'INGREDIENT', 'KG'),
    ('Pescado', 12.00, 'INGREDIENT', 'KG'),
    ('Cerdo', 10.00, 'INGREDIENT', 'KG'),
    ('Cabrito', 6.00, 'INGREDIENT', 'KG'),
    ('Pato', 5.00, 'INGREDIENT', 'KG'),
    ('Sangre de Pollo', 3.00, 'INGREDIENT', 'KG'),
    
    -- Lácteos y Derivados
    ('Leche Evaporada', 24.00, 'INGREDIENT', 'UNIDAD'),
    ('Queso Fresco', 4.00, 'INGREDIENT', 'KG'),
    ('Huevo', 60.00, 'INGREDIENT', 'UNIDAD'),
    
    -- Granos y Carbohidratos
    ('Arroz', 30.00, 'INGREDIENT', 'KG'),
    ('Fideo', 10.00, 'INGREDIENT', 'KG'),
    ('Sémola', 5.00, 'INGREDIENT', 'KG'),
    ('Pan', 50.00, 'INGREDIENT', 'UNIDAD'),
    ('Galleta de Soda', 3.00, 'INGREDIENT', 'KG'),
    
    -- Condimentos y Salsas
    ('Sal', 5.00, 'INGREDIENT', 'KG'),
    ('Aceite Vegetal', 10.00, 'INGREDIENT', 'L'),
    ('Vinagre', 3.00, 'INGREDIENT', 'L'),
    ('Sillao', 2.00, 'INGREDIENT', 'L'),
    ('Mayonesa', 4.00, 'INGREDIENT', 'KG'),
    ('Limón', 5.00, 'INGREDIENT', 'KG'),
    ('Ajo', 2.00, 'INGREDIENT', 'KG'),
    ('Pimienta', 1.00, 'INGREDIENT', 'KG'),
    ('Chicha de Jora', 5.00, 'INGREDIENT', 'L'),
    
    -- Otros
    ('Camote', 8.00, 'INGREDIENT', 'KG'),
    ('Cancha', 4.00, 'INGREDIENT', 'KG'),
    ('Plátano', 6.00, 'INGREDIENT', 'KG');

-- =====================================================
-- RECETAS: Vinculación de productos con ingredientes
-- =====================================================

-- -------------------------------------------------
-- ENTRADAS (product_type_id = 1)
-- -------------------------------------------------

-- 1. HUANCAINA (id=1): Papa, Ají Amarillo, Leche, Queso, Galleta, Sal, Aceite
INSERT INTO product_inventory (product_id, inventory_id, quantity, unit_of_measure)
VALUES
    (1, 2, 0.200, 'KG'),   -- Papa Amarilla 200g
    (1, 8, 0.050, 'KG'),   -- Ají Amarillo 50g
    (1, 22, 0.5, 'UNIDAD'), -- Leche Evaporada 1/2 tarro
    (1, 23, 0.100, 'KG'),   -- Queso Fresco 100g
    (1, 29, 0.030, 'KG'),   -- Galleta 30g
    (1, 30, 0.005, 'KG');   -- Sal 5g

-- 2. YUCA RELLENA (id=2): Yuca, Carne de Res, Cebolla, Ají, Aceite
INSERT INTO product_inventory (product_id, inventory_id, quantity, unit_of_measure)
VALUES
    (2, 3, 0.250, 'KG'),   -- Yuca 250g
    (2, 15, 0.100, 'KG'),   -- Carne de Res 100g
    (2, 4, 0.050, 'KG'),   -- Cebolla 50g
    (2, 31, 0.100, 'L');       -- Aceite 100ml

-- 3. SANGRECITA (id=3): Sangre de Pollo, Cebolla, Ají, Culantro
INSERT INTO product_inventory (product_id, inventory_id, quantity, unit_of_measure)
VALUES
    (3, 21, 0.150, 'KG'),   -- Sangre de Pollo 150g
    (3, 4, 0.080, 'KG'),   -- Cebolla 80g
    (3, 8, 0.030, 'KG'),   -- Ají Amarillo 30g
    (3, 10, 0.020, 'KG');   -- Culantro 20g

-- 7. AGUADITO (id=7): Pollo, Arroz, Culantro, Arveja, Zanahoria
INSERT INTO product_inventory (product_id, inventory_id, quantity, unit_of_measure)
VALUES
    (7, 13, 0.150, 'KG'),   -- Pollo 150g
    (7, 25, 0.080, 'KG'),   -- Arroz 80g
    (7, 10, 0.040, 'KG'),   -- Culantro 40g
    (7, 11, 0.050, 'KG'),   -- Arveja 50g
    (7, 6, 0.030, 'KG');   -- Zanahoria 30g

-- 8. CEVICHE (id=8): Pescado, Limón, Cebolla, Ají Limo, Culantro, Camote, Cancha
INSERT INTO product_inventory (product_id, inventory_id, quantity, unit_of_measure)
VALUES
    (8, 17, 0.200, 'KG'),   -- Pescado 200g
    (8, 35, 0.100, 'KG'),   -- Limón 100g
    (8, 4, 0.080, 'KG'),   -- Cebolla 80g
    (8, 9, 0.020, 'KG'),   -- Ají Limo 20g
    (8, 39, 0.100, 'KG'),   -- Camote 100g
    (8, 40, 0.030, 'KG');   -- Cancha 30g

-- 9. ENSALADA RUSA (id=9): Papa, Zanahoria, Arveja, Mayonesa, Huevo
INSERT INTO product_inventory (product_id, inventory_id, quantity, unit_of_measure)
VALUES
    (9, 1, 0.150, 'KG'),   -- Papa Blanca 150g
    (9, 6, 0.080, 'KG'),   -- Zanahoria 80g
    (9, 11, 0.060, 'KG'),   -- Arveja 60g
    (9, 34, 0.080, 'KG'),   -- Mayonesa 80g
    (9, 24, 1, 'UNIDAD');          -- Huevo 1 unidad

-- 10. SOPA DE FIDEO (id=10): Fideo, Papa, Pollo, Zanahoria
INSERT INTO product_inventory (product_id, inventory_id, quantity, unit_of_measure)
VALUES
    (10, 26, 0.080, 'KG'),  -- Fideo 80g
    (10, 1, 0.100, 'KG'),  -- Papa Blanca 100g
    (10, 13, 0.100, 'KG'),  -- Pollo 100g
    (10, 6, 0.040, 'KG');  -- Zanahoria 40g

-- 11. SOPA DE SEMOLA (id=11): Sémola, Huevo, Culantro
INSERT INTO product_inventory (product_id, inventory_id, quantity, unit_of_measure)
VALUES
    (11, 27, 0.060, 'KG'),  -- Sémola 60g
    (11, 24, 1, 'UNIDAD'),         -- Huevo 1 unidad
    (11, 10, 0.015, 'KG');  -- Culantro 15g

-- 12. ENSALADA MIXTA (id=12): Lechuga, Tomate, Zanahoria, Limón
INSERT INTO product_inventory (product_id, inventory_id, quantity, unit_of_measure)
VALUES
    (12, 7, 0.100, 'KG'),  -- Lechuga 100g
    (12, 5, 0.100, 'KG'),  -- Tomate 100g
    (12, 6, 0.050, 'KG'),  -- Zanahoria 50g
    (12, 35, 0.030, 'KG');  -- Limón 30g

-- 13. ENSALADA BLANCA (id=13): Papa, Arveja, Zanahoria, Mayonesa
INSERT INTO product_inventory (product_id, inventory_id, quantity, unit_of_measure)
VALUES
    (13, 1, 0.150, 'KG'),  -- Papa Blanca 150g
    (13, 11, 0.060, 'KG'),  -- Arveja 60g
    (13, 6, 0.060, 'KG'),  -- Zanahoria 60g
    (13, 34, 0.070, 'KG');  -- Mayonesa 70g

-- -------------------------------------------------
-- SEGUNDOS (product_type_id = 4)
-- -------------------------------------------------

-- 6. LOMITO SALTADO (id=6): Lomo, Cebolla, Tomate, Sillao, Papa, Arroz
INSERT INTO product_inventory (product_id, inventory_id, quantity, unit_of_measure)
VALUES
    (6, 16, 0.200, 'KG'::unit_of_measure),   -- Lomo de Res (id=16)
    (6, 4, 0.100, 'KG'::unit_of_measure),   -- Cebolla (id=4)
    (6, 5, 0.100, 'KG'::unit_of_measure),   -- Tomate (id=5)
    (6, 33, 0.030, 'L'::unit_of_measure),    -- Sillao (id=33)
    (6, 1, 0.150, 'KG'::unit_of_measure),   -- Papa Blanca (id=1)
    (6, 25, 0.150, 'KG'::unit_of_measure);   -- Arroz (id=25)

-- 14. AJI DE GALLINA (id=14): Gallina, Ají Amarillo, Leche, Pan, Arroz
INSERT INTO product_inventory (product_id, inventory_id, quantity, unit_of_measure)
VALUES
    (14, 14, 0.200, 'KG'::unit_of_measure),  -- Gallina (id=14)
    (14, 8, 0.060, 'KG'::unit_of_measure),  -- Ají Amarillo (id=8)
    (14, 22, 0.5, 'UNIDAD'::unit_of_measure),   -- Leche Evaporada (id=22)
    (14, 28, 2, 'UNIDAD'::unit_of_measure),     -- Pan (id=28)
    (14, 25, 0.150, 'KG'::unit_of_measure);  -- Arroz (id=25)

-- 15. MILANESA (id=15): Carne de Res, Huevo, Pan, Aceite, Arroz
INSERT INTO product_inventory (product_id, inventory_id, quantity, unit_of_measure)
VALUES
    (15, 15, 0.200, 'KG'::unit_of_measure),  -- Carne de Res (id=15)
    (15, 24, 1, 'UNIDAD'::unit_of_measure),     -- Huevo (id=24)
    (15, 28, 1, 'UNIDAD'::unit_of_measure),     -- Pan (id=28)
    (15, 31, 0.150, 'L'::unit_of_measure),   -- Aceite (id=31)
    (15, 25, 0.150, 'KG'::unit_of_measure);  -- Arroz (id=25)

-- 16. GALLINA GUISADA (id=16): Gallina, Cebolla, Tomate, Ají, Arroz
INSERT INTO product_inventory (product_id, inventory_id, quantity, unit_of_measure)
VALUES
    (16, 14, 0.250, 'KG'::unit_of_measure),  -- Gallina (id=14)
    (16, 4, 0.080, 'KG'::unit_of_measure),  -- Cebolla (id=4)
    (16, 5, 0.080, 'KG'::unit_of_measure),  -- Tomate (id=5)
    (16, 8, 0.030, 'KG'::unit_of_measure),  -- Ají Amarillo (id=8)
    (16, 25, 0.150, 'KG'::unit_of_measure);  -- Arroz (id=25)

-- 17. CAIGUA RELLENA (id=17): Caigua, Carne de Res, Cebolla, Arroz
INSERT INTO product_inventory (product_id, inventory_id, quantity, unit_of_measure)
VALUES
    (17, 12, 0.200, 'KG'::unit_of_measure),  -- Caigua (id=12)
    (17, 15, 0.150, 'KG'::unit_of_measure),  -- Carne de Res (id=15)
    (17, 4, 0.050, 'KG'::unit_of_measure),  -- Cebolla (id=4)
    (17, 25, 0.150, 'KG'::unit_of_measure);  -- Arroz (id=25)

-- 18. POLLO FRITO C/ AJIACO (id=18): Pollo, Papa Amarilla, Ají, Aceite
INSERT INTO product_inventory (product_id, inventory_id, quantity, unit_of_measure)
VALUES
    (18, 13, 0.250, 'KG'::unit_of_measure),  -- Pollo (id=13)
    (18, 2, 0.200, 'KG'::unit_of_measure),   -- Papa Amarilla (id=2)
    (18, 8, 0.040, 'KG'::unit_of_measure),  -- Ají Amarillo (id=8)
    (18, 31, 0.200, 'L'::unit_of_measure);   -- Aceite (id=31)

-- 19. SUDADO DE PESCADO (id=19): Pescado, Cebolla, Tomate, Ají, Arroz
INSERT INTO product_inventory (product_id, inventory_id, quantity, unit_of_measure)
VALUES
    (19, 17, 0.250, 'KG'::unit_of_measure),  -- Pescado (id=17)
    (19, 4, 0.100, 'KG'::unit_of_measure),  -- Cebolla (id=4)
    (19, 5, 0.100, 'KG'::unit_of_measure),  -- Tomate (id=5)
    (19, 8, 0.030, 'KG'::unit_of_measure),  -- Ají Amarillo (id=8)
    (19, 25, 0.150, 'KG'::unit_of_measure);  -- Arroz (id=25)

-- 20. ESCABECHE DE POLLO (id=20): Pollo, Cebolla, Vinagre, Ají, Zanahoria, Arroz
INSERT INTO product_inventory (product_id, inventory_id, quantity, unit_of_measure)
VALUES
    (20, 13, 0.250, 'KG'::unit_of_measure),  -- Pollo (id=13)
    (20, 4, 0.150, 'KG'::unit_of_measure),  -- Cebolla (id=4)
    (20, 32, 0.050, 'L'::unit_of_measure),   -- Vinagre (id=32)
    (20, 8, 0.040, 'KG'::unit_of_measure),  -- Ají Amarillo (id=8)
    (20, 6, 0.050, 'KG'::unit_of_measure),  -- Zanahoria (id=6)
    (20, 25, 0.150, 'KG'::unit_of_measure);  -- Arroz (id=25)

-- 21. ASADO DE RES (id=21): Carne de Res, Cebolla, Ajo, Arroz
INSERT INTO product_inventory (product_id, inventory_id, quantity, unit_of_measure)
VALUES
    (21, 15, 0.250, 'KG'::unit_of_measure),  -- Carne de Res (id=15)
    (21, 4, 0.080, 'KG'::unit_of_measure),  -- Cebolla (id=4)
    (21, 36, 0.020, 'KG'::unit_of_measure),  -- Ajo (id=36)
    (21, 25, 0.150, 'KG'::unit_of_measure);  -- Arroz (id=25)

-- 22. ARROZ VERDE C/ POLLO (id=22): Pollo, Arroz, Culantro, Arveja
INSERT INTO product_inventory (product_id, inventory_id, quantity, unit_of_measure)
VALUES
    (22, 13, 0.200, 'KG'::unit_of_measure),  -- Pollo (id=13)
    (22, 25, 0.200, 'KG'::unit_of_measure),  -- Arroz (id=25)
    (22, 10, 0.060, 'KG'::unit_of_measure),  -- Culantro (id=10)
    (22, 11, 0.050, 'KG'::unit_of_measure);  -- Arveja (id=11)

-- -------------------------------------------------
-- CARTA (product_type_id = 5)
-- -------------------------------------------------

-- 26. CABRITO A LA NORTEÑA (id=26): Cabrito, Chicha de Jora, Cebolla, Ají, Arroz
INSERT INTO product_inventory (product_id, inventory_id, quantity, unit_of_measure)
VALUES
    (26, 19, 0.350, 'KG'::unit_of_measure),  -- Cabrito (id=19)
    (26, 38, 0.200, 'L'::unit_of_measure),   -- Chicha de Jora (id=38)
    (26, 4, 0.100, 'KG'::unit_of_measure),  -- Cebolla (id=4)
    (26, 8, 0.050, 'KG'::unit_of_measure),  -- Ají Amarillo (id=8)
    (26, 25, 0.200, 'KG'::unit_of_measure);  -- Arroz (id=25)

-- 27. PATO GUISADO (id=27): Pato, Culantro, Arroz, Yuca
INSERT INTO product_inventory (product_id, inventory_id, quantity, unit_of_measure)
VALUES
    (27, 20, 0.350, 'KG'::unit_of_measure),  -- Pato (id=20)
    (27, 10, 0.080, 'KG'::unit_of_measure),  -- Culantro (id=10)
    (27, 25, 0.200, 'KG'::unit_of_measure),  -- Arroz (id=25)
    (27, 3, 0.150, 'KG'::unit_of_measure);   -- Yuca (id=3)

-- 28. LOMITO A LO POBRE (id=28): Lomo, Papa, Plátano, Huevo, Arroz
INSERT INTO product_inventory (product_id, inventory_id, quantity, unit_of_measure)
VALUES
    (28, 16, 0.250, 'KG'::unit_of_measure),  -- Lomo de Res (id=16)
    (28, 1, 0.200, 'KG'::unit_of_measure),   -- Papa Blanca (id=1)
    (28, 41, 0.150, 'KG'::unit_of_measure),  -- Plátano (id=41)
    (28, 24, 2, 'UNIDAD'::unit_of_measure),  -- Huevo (id=24)
    (28, 25, 0.150, 'KG'::unit_of_measure);  -- Arroz (id=25)

-- 29. CHULETA DE CERDO (id=29): Cerdo, Ajo, Arroz, Papa
INSERT INTO product_inventory (product_id, inventory_id, quantity, unit_of_measure)
VALUES
    (29, 18, 0.300, 'KG'::unit_of_measure),  -- Cerdo (id=18)
    (29, 36, 0.020, 'KG'::unit_of_measure),  -- Ajo (id=36)
    (29, 25, 0.150, 'KG'::unit_of_measure),  -- Arroz (id=25)
    (29, 1, 0.150, 'KG'::unit_of_measure);   -- Papa Blanca (id=1)

-- 30. CHICHARRON DE POLLO (id=30): Pollo, Ajo, Aceite, Papa
INSERT INTO product_inventory (product_id, inventory_id, quantity, unit_of_measure)
VALUES
    (30, 13, 0.300, 'KG'::unit_of_measure),  -- Pollo (id=13)
    (30, 36, 0.020, 'KG'::unit_of_measure),  -- Ajo (id=36)
    (30, 31, 0.250, 'L'::unit_of_measure),   -- Aceite (id=31)
    (30, 1, 0.200, 'KG'::unit_of_measure);   -- Papa Blanca (id=1)
