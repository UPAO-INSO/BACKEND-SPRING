-- =====================================================
-- V6: Ingredientes (Insumos) y Recetas para Platillos
-- =====================================================
-- NOTA: Los IDs empiezan en 200 para no conflictuar con 
-- los IDs 100-104 de bebidas/descartables en V5
-- =====================================================

-- =====================================================
-- INGREDIENTES BASE (Insumos en inventario)
-- =====================================================

INSERT INTO `inventory` (id, name, quantity, type, unit_of_measure)
VALUES
    -- Tubérculos y Vegetales
    (200, 'Papa Blanca', 25.00, 'INGREDIENT', 'KG'),
    (201, 'Papa Amarilla', 15.00, 'INGREDIENT', 'KG'),
    (202, 'Yuca', 10.00, 'INGREDIENT', 'KG'),
    (203, 'Cebolla Roja', 12.00, 'INGREDIENT', 'KG'),
    (204, 'Tomate', 8.00, 'INGREDIENT', 'KG'),
    (205, 'Zanahoria', 6.00, 'INGREDIENT', 'KG'),
    (206, 'Lechuga', 4.00, 'INGREDIENT', 'KG'),
    (207, 'Ají Amarillo', 3.00, 'INGREDIENT', 'KG'),
    (208, 'Ají Limo', 1.50, 'INGREDIENT', 'KG'),
    (209, 'Culantro', 2.00, 'INGREDIENT', 'KG'),
    (210, 'Arveja', 5.00, 'INGREDIENT', 'KG'),
    (211, 'Caigua', 3.00, 'INGREDIENT', 'KG'),
    
    -- Carnes y Proteínas
    (212, 'Pollo', 20.00, 'INGREDIENT', 'KG'),
    (213, 'Gallina', 10.00, 'INGREDIENT', 'KG'),
    (214, 'Carne de Res', 15.00, 'INGREDIENT', 'KG'),
    (215, 'Lomo de Res', 8.00, 'INGREDIENT', 'KG'),
    (216, 'Pescado', 12.00, 'INGREDIENT', 'KG'),
    (217, 'Cerdo', 10.00, 'INGREDIENT', 'KG'),
    (218, 'Cabrito', 6.00, 'INGREDIENT', 'KG'),
    (219, 'Pato', 5.00, 'INGREDIENT', 'KG'),
    (220, 'Sangre de Pollo', 3.00, 'INGREDIENT', 'KG'),
    
    -- Lácteos y Derivados
    (221, 'Leche Evaporada', 24.00, 'INGREDIENT', 'UNIDAD'),
    (222, 'Queso Fresco', 4.00, 'INGREDIENT', 'KG'),
    (223, 'Huevo', 60.00, 'INGREDIENT', 'UNIDAD'),
    
    -- Granos y Carbohidratos
    (224, 'Arroz', 30.00, 'INGREDIENT', 'KG'),
    (225, 'Fideo', 10.00, 'INGREDIENT', 'KG'),
    (226, 'Sémola', 5.00, 'INGREDIENT', 'KG'),
    (227, 'Pan', 50.00, 'INGREDIENT', 'UNIDAD'),
    (228, 'Galleta de Soda', 3.00, 'INGREDIENT', 'KG'),
    
    -- Condimentos y Salsas
    (229, 'Sal', 5.00, 'INGREDIENT', 'KG'),
    (230, 'Aceite Vegetal', 10.00, 'INGREDIENT', 'L'),
    (231, 'Vinagre', 3.00, 'INGREDIENT', 'L'),
    (232, 'Sillao', 2.00, 'INGREDIENT', 'L'),
    (233, 'Mayonesa', 4.00, 'INGREDIENT', 'KG'),
    (234, 'Limón', 5.00, 'INGREDIENT', 'KG'),
    (235, 'Ajo', 2.00, 'INGREDIENT', 'KG'),
    (236, 'Pimienta', 1.00, 'INGREDIENT', 'KG'),
    (237, 'Chicha de Jora', 5.00, 'INGREDIENT', 'L'),
    
    -- Otros
    (238, 'Camote', 8.00, 'INGREDIENT', 'KG'),
    (239, 'Cancha', 4.00, 'INGREDIENT', 'KG'),
    (240, 'Plátano', 6.00, 'INGREDIENT', 'KG');

-- =====================================================
-- RECETAS: Vinculación de productos con ingredientes
-- =====================================================

-- -------------------------------------------------
-- ENTRADAS (product_type_id = 1)
-- -------------------------------------------------

-- 1. HUANCAINA (id=1): Papa, Ají Amarillo, Leche, Queso, Galleta, Sal, Aceite
INSERT INTO `product_inventory` (product_id, inventory_id, quantity, unit_of_measure)
VALUES
    (1, 201, 0.200, 'KG'),   -- Papa Amarilla 200g
    (1, 207, 0.050, 'KG'),   -- Ají Amarillo 50g
    (1, 221, 0.5, 'UNIDAD'),        -- Leche Evaporada 1/2 tarro
    (1, 222, 0.100, 'KG'),   -- Queso Fresco 100g
    (1, 228, 0.030, 'KG'),   -- Galleta 30g
    (1, 229, 0.005, 'KG');   -- Sal 5g

-- 2. YUCA RELLENA (id=2): Yuca, Carne de Res, Cebolla, Ají, Aceite
INSERT INTO `product_inventory` (product_id, inventory_id, quantity, unit_of_measure)
VALUES
    (2, 202, 0.250, 'KG'),   -- Yuca 250g
    (2, 214, 0.100, 'KG'),   -- Carne de Res 100g
    (2, 203, 0.050, 'KG'),   -- Cebolla 50g
    (2, 230, 0.100, 'L');       -- Aceite 100ml

-- 3. SANGRECITA (id=3): Sangre de Pollo, Cebolla, Ají, Culantro
INSERT INTO `product_inventory` (product_id, inventory_id, quantity, unit_of_measure)
VALUES
    (3, 220, 0.150, 'KG'),   -- Sangre de Pollo 150g
    (3, 203, 0.080, 'KG'),   -- Cebolla 80g
    (3, 207, 0.030, 'KG'),   -- Ají Amarillo 30g
    (3, 209, 0.020, 'KG');   -- Culantro 20g

-- 7. AGUADITO (id=7): Pollo, Arroz, Culantro, Arveja, Zanahoria
INSERT INTO `product_inventory` (product_id, inventory_id, quantity, unit_of_measure)
VALUES
    (7, 212, 0.150, 'KG'),   -- Pollo 150g
    (7, 224, 0.080, 'KG'),   -- Arroz 80g
    (7, 209, 0.040, 'KG'),   -- Culantro 40g
    (7, 210, 0.050, 'KG'),   -- Arveja 50g
    (7, 205, 0.030, 'KG');   -- Zanahoria 30g

-- 8. CEVICHE (id=8): Pescado, Limón, Cebolla, Ají Limo, Culantro, Camote, Cancha
INSERT INTO `product_inventory` (product_id, inventory_id, quantity, unit_of_measure)
VALUES
    (8, 216, 0.200, 'KG'),   -- Pescado 200g
    (8, 234, 0.100, 'KG'),   -- Limón 100g
    (8, 203, 0.080, 'KG'),   -- Cebolla 80g
    (8, 208, 0.020, 'KG'),   -- Ají Limo 20g
    (8, 238, 0.100, 'KG'),   -- Camote 100g
    (8, 239, 0.030, 'KG');   -- Cancha 30g

-- 9. ENSALADA RUSA (id=9): Papa, Zanahoria, Arveja, Mayonesa, Huevo
INSERT INTO `product_inventory` (product_id, inventory_id, quantity, unit_of_measure)
VALUES
    (9, 200, 0.150, 'KG'),   -- Papa Blanca 150g
    (9, 205, 0.080, 'KG'),   -- Zanahoria 80g
    (9, 210, 0.060, 'KG'),   -- Arveja 60g
    (9, 233, 0.080, 'KG'),   -- Mayonesa 80g
    (9, 223, 1, 'UNIDAD');          -- Huevo 1 unidad

-- 10. SOPA DE FIDEO (id=10): Fideo, Papa, Pollo, Zanahoria
INSERT INTO `product_inventory` (product_id, inventory_id, quantity, unit_of_measure)
VALUES
    (10, 225, 0.080, 'KG'),  -- Fideo 80g
    (10, 200, 0.100, 'KG'),  -- Papa Blanca 100g
    (10, 212, 0.100, 'KG'),  -- Pollo 100g
    (10, 205, 0.040, 'KG');  -- Zanahoria 40g

-- 11. SOPA DE SEMOLA (id=11): Sémola, Huevo, Culantro
INSERT INTO `product_inventory` (product_id, inventory_id, quantity, unit_of_measure)
VALUES
    (11, 226, 0.060, 'KG'),  -- Sémola 60g
    (11, 223, 1, 'UNIDAD'),         -- Huevo 1 unidad
    (11, 209, 0.015, 'KG');  -- Culantro 15g

-- 12. ENSALADA MIXTA (id=12): Lechuga, Tomate, Zanahoria, Limón
INSERT INTO `product_inventory` (product_id, inventory_id, quantity, unit_of_measure)
VALUES
    (12, 206, 0.100, 'KG'),  -- Lechuga 100g
    (12, 204, 0.100, 'KG'),  -- Tomate 100g
    (12, 205, 0.050, 'KG'),  -- Zanahoria 50g
    (12, 234, 0.030, 'KG');  -- Limón 30g

-- 13. ENSALADA BLANCA (id=13): Papa, Arveja, Zanahoria, Mayonesa
INSERT INTO `product_inventory` (product_id, inventory_id, quantity, unit_of_measure)
VALUES
    (13, 200, 0.150, 'KG'),  -- Papa Blanca 150g
    (13, 210, 0.060, 'KG'),  -- Arveja 60g
    (13, 205, 0.060, 'KG'),  -- Zanahoria 60g
    (13, 233, 0.070, 'KG');  -- Mayonesa 70g

-- -------------------------------------------------
-- SEGUNDOS (product_type_id = 4)
-- -------------------------------------------------

-- 6. LOMITO SALTADO (id=6): Lomo, Cebolla, Tomate, Sillao, Papa, Arroz
INSERT INTO `product_inventory` (product_id, inventory_id, quantity, unit_of_measure)
VALUES
    (6, 215, 0.200, 'KG'),   -- Lomo de Res 200g
    (6, 203, 0.100, 'KG'),   -- Cebolla 100g
    (6, 204, 0.100, 'KG'),   -- Tomate 100g
    (6, 232, 0.030, 'L'),       -- Sillao 30ml
    (6, 200, 0.150, 'KG'),   -- Papa Blanca 150g
    (6, 224, 0.150, 'KG');   -- Arroz 150g

-- 14. AJI DE GALLINA (id=14): Gallina, Ají Amarillo, Leche, Pan, Arroz
INSERT INTO `product_inventory` (product_id, inventory_id, quantity, unit_of_measure)
VALUES
    (14, 213, 0.200, 'KG'),  -- Gallina 200g
    (14, 207, 0.060, 'KG'),  -- Ají Amarillo 60g
    (14, 221, 0.5, 'UNIDAD'),       -- Leche Evaporada 1/2 tarro
    (14, 227, 2, 'UNIDAD'),         -- Pan 2 unidades
    (14, 224, 0.150, 'KG');  -- Arroz 150g

-- 15. MILANESA (id=15): Carne de Res, Huevo, Pan, Aceite, Arroz
INSERT INTO `product_inventory` (product_id, inventory_id, quantity, unit_of_measure)
VALUES
    (15, 214, 0.200, 'KG'),  -- Carne de Res 200g
    (15, 223, 1, 'UNIDAD'),         -- Huevo 1 unidad
    (15, 227, 1, 'UNIDAD'),         -- Pan 1 unidad (para empanizar)
    (15, 230, 0.150, 'L'),      -- Aceite 150ml
    (15, 224, 0.150, 'KG');  -- Arroz 150g

-- 16. GALLINA GUISADA (id=16): Gallina, Cebolla, Tomate, Ají, Arroz
INSERT INTO `product_inventory` (product_id, inventory_id, quantity, unit_of_measure)
VALUES
    (16, 213, 0.250, 'KG'),  -- Gallina 250g
    (16, 203, 0.080, 'KG'),  -- Cebolla 80g
    (16, 204, 0.080, 'KG'),  -- Tomate 80g
    (16, 207, 0.030, 'KG'),  -- Ají Amarillo 30g
    (16, 224, 0.150, 'KG');  -- Arroz 150g

-- 17. CAIGUA RELLENA (id=17): Caigua, Carne de Res, Cebolla, Arroz
INSERT INTO `product_inventory` (product_id, inventory_id, quantity, unit_of_measure)
VALUES
    (17, 211, 0.200, 'KG'),  -- Caigua 200g
    (17, 214, 0.150, 'KG'),  -- Carne de Res 150g
    (17, 203, 0.050, 'KG'),  -- Cebolla 50g
    (17, 224, 0.150, 'KG');  -- Arroz 150g

-- 18. POLLO FRITO C/ AJIACO (id=18): Pollo, Papa Amarilla, Ají, Aceite
INSERT INTO `product_inventory` (product_id, inventory_id, quantity, unit_of_measure)
VALUES
    (18, 212, 0.250, 'KG'),  -- Pollo 250g
    (18, 201, 0.200, 'KG'),  -- Papa Amarilla 200g
    (18, 207, 0.040, 'KG'),  -- Ají Amarillo 40g
    (18, 230, 0.200, 'L');      -- Aceite 200ml

-- 19. SUDADO DE PESCADO (id=19): Pescado, Cebolla, Tomate, Ají, Arroz
INSERT INTO `product_inventory` (product_id, inventory_id, quantity, unit_of_measure)
VALUES
    (19, 216, 0.250, 'KG'),  -- Pescado 250g
    (19, 203, 0.100, 'KG'),  -- Cebolla 100g
    (19, 204, 0.100, 'KG'),  -- Tomate 100g
    (19, 207, 0.030, 'KG'),  -- Ají Amarillo 30g
    (19, 224, 0.150, 'KG');  -- Arroz 150g

-- 20. ESCABECHE DE POLLO (id=20): Pollo, Cebolla, Vinagre, Ají, Zanahoria, Arroz
INSERT INTO `product_inventory` (product_id, inventory_id, quantity, unit_of_measure)
VALUES
    (20, 212, 0.250, 'KG'),  -- Pollo 250g
    (20, 203, 0.150, 'KG'),  -- Cebolla 150g
    (20, 231, 0.050, 'L'),      -- Vinagre 50ml
    (20, 207, 0.040, 'KG'),  -- Ají Amarillo 40g
    (20, 205, 0.050, 'KG'),  -- Zanahoria 50g
    (20, 224, 0.150, 'KG');  -- Arroz 150g

-- 21. ASADO DE RES (id=21): Carne de Res, Cebolla, Ajo, Arroz
INSERT INTO `product_inventory` (product_id, inventory_id, quantity, unit_of_measure)
VALUES
    (21, 214, 0.250, 'KG'),  -- Carne de Res 250g
    (21, 203, 0.080, 'KG'),  -- Cebolla 80g
    (21, 235, 0.020, 'KG'),  -- Ajo 20g
    (21, 224, 0.150, 'KG');  -- Arroz 150g

-- 22. ARROZ VERDE C/ POLLO (id=22): Pollo, Arroz, Culantro, Arveja
INSERT INTO `product_inventory` (product_id, inventory_id, quantity, unit_of_measure)
VALUES
    (22, 212, 0.200, 'KG'),  -- Pollo 200g
    (22, 224, 0.200, 'KG'),  -- Arroz 200g
    (22, 209, 0.060, 'KG'),  -- Culantro 60g
    (22, 210, 0.050, 'KG');  -- Arveja 50g

-- -------------------------------------------------
-- CARTA (product_type_id = 5)
-- -------------------------------------------------

-- 26. CABRITO A LA NORTEÑA (id=26): Cabrito, Chicha de Jora, Cebolla, Ají, Arroz
INSERT INTO `product_inventory` (product_id, inventory_id, quantity, unit_of_measure)
VALUES
    (26, 218, 0.350, 'KG'),  -- Cabrito 350g
    (26, 237, 0.200, 'L'),      -- Chicha de Jora 200ml
    (26, 203, 0.100, 'KG'),  -- Cebolla 100g
    (26, 207, 0.050, 'KG'),  -- Ají Amarillo 50g
    (26, 224, 0.200, 'KG');  -- Arroz 200g

-- 27. PATO GUISADO (id=27): Pato, Culantro, Arroz, Yuca
INSERT INTO `product_inventory` (product_id, inventory_id, quantity, unit_of_measure)
VALUES
    (27, 219, 0.350, 'KG'),  -- Pato 350g
    (27, 209, 0.080, 'KG'),  -- Culantro 80g
    (27, 224, 0.200, 'KG'),  -- Arroz 200g
    (27, 202, 0.150, 'KG');  -- Yuca 150g

-- 28. LOMITO A LO POBRE (id=28): Lomo, Papa, Plátano, Huevo, Arroz
INSERT INTO `product_inventory` (product_id, inventory_id, quantity, unit_of_measure)
VALUES
    (28, 215, 0.250, 'KG'),  -- Lomo de Res 250g
    (28, 200, 0.200, 'KG'),  -- Papa Blanca 200g
    (28, 240, 0.150, 'KG'),  -- Plátano 150g
    (28, 223, 2, 'UNIDAD'),         -- Huevo 2 unidades
    (28, 224, 0.150, 'KG');  -- Arroz 150g

-- 29. CHULETA DE CERDO (id=29): Cerdo, Ajo, Arroz, Papa
INSERT INTO `product_inventory` (product_id, inventory_id, quantity, unit_of_measure)
VALUES
    (29, 217, 0.300, 'KG'),  -- Cerdo 300g
    (29, 235, 0.020, 'KG'),  -- Ajo 20g
    (29, 224, 0.150, 'KG'),  -- Arroz 150g
    (29, 200, 0.150, 'KG');  -- Papa Blanca 150g

-- 30. CHICHARRON DE POLLO (id=30): Pollo, Ajo, Aceite, Papa
INSERT INTO `product_inventory` (product_id, inventory_id, quantity, unit_of_measure)
VALUES
    (30, 212, 0.300, 'KG'),  -- Pollo 300g
    (30, 235, 0.020, 'KG'),  -- Ajo 20g
    (30, 230, 0.250, 'L'),      -- Aceite 250ml
    (30, 200, 0.200, 'KG');  -- Papa Blanca 200g
