INSERT INTO job (title)
VALUES ('GERENTE'),
       ('CAJERO'),
       ('COCINERO'),
       ('MESERO');

INSERT INTO persons (name, lastname, phone)
VALUES ('Admin', 'Test', '+51987654321'),
       ('Pagos', 'Test', '+51999999999');

INSERT INTO customers (id, document_type, document_number, email, departament, province, district, complete_address)
VALUES (2, 'DNI','00000000', 'gportallinares21@gmail.com', 'LA LIBERTAD', 'TRUJILLO', 'TRUJILLO', 'AV. LOS INCAS 123');

INSERT INTO "user" (username, password, email, role)
VALUES ('admin', '$2a$12$b8bevAwTSbkRUuWftiqli.aNmCzn5IFryaYgr8e1VWjbTCQWZtkE6', 'admin@email.com', 'ADMIN'::user_role);

INSERT INTO employees (salary, id, job_id, user_id)
VALUES (0, 1, 1, 1);

INSERT INTO product_type (name)
VALUES ('ENTRADAS'),
       ('BEBIDAS'),
       ('DESCARTABLES'),
       ('SEGUNDOS'),
       ('CARTA');

INSERT INTO tables (capacity, floor, number, status)
VALUES (4, 1, 1, 'AVAILABLE'::table_status),
       (4, 1, 2, 'AVAILABLE'::table_status),
       (4, 1, 3, 'AVAILABLE'::table_status),
       (4, 1, 4, 'AVAILABLE'::table_status),
       (4, 1, 5, 'AVAILABLE'::table_status),
       (4, 1, 6, 'AVAILABLE'::table_status),
       (4, 1, 7, 'AVAILABLE'::table_status),
       (4, 1, 8, 'AVAILABLE'::table_status),
       (4, 1, 9, 'AVAILABLE'::table_status),
       (4, 1, 10, 'AVAILABLE'::table_status);

INSERT INTO product (active, available, description, name, price, product_type_id)
VALUES (true, true, 'Crema color amarillo, preparada con leche, galleta, pan, queso, y mas', 'HUANCAINA', 5, 1),
       (true, true, 'Masa de yuca cocida rellena de carne sazonada y luego frita.', 'YUCA RELLENA', 5, 1),
       (true, true, 'Hecho a base de sangre de pollo cocida y sazonada con hierbas y especias.', 'SANGRECITA', 5, 1),
       (true, true, 'Bebida gasificada peruana en presentacion de 300ml', 'INCA KOLA PERSONAL', 3, 2),
       (true, true, 'Bebida gasificada peruana en presentacion de 1L', 'INCA KOLA 1L', 7, 2),
       (true, true, 'Carne de lomo de res salteada con sillao y especias', 'LOMITO SALTADO', 10, 4),
       (true, true, 'Caldo espeso preparado con arroz, culantro, pollo y verduras, sazonado con ají y hierbas frescas.', 'AGUADITO', 5, 1),
       (true, true, 'Plato frío a base de pescado fresco marinado en jugo de limón, cebolla roja, ají limo y culantro, acompañado con camote y cancha.', 'CEVICHE', 5, 1),
       (true, true, 'Ensalada fría preparada con papas, arvejas, zanahorias y mayonesa, a veces acompañada con huevo duro o betarraga', 'ENSALADA RUSA', 5, 1),
       (true, true, 'Caldo ligero con fideos delgados, papa, verduras y trozos de pollo o carne.', 'SOPA DE FIDEO', 5, 1),
       (true, true, 'Caldo casero preparado con sémola, huevo batido y hierbas, ideal como entrada suave.', 'SOPA DE SEMOLA', 5, 1),
       (true, true, 'Ensalada fresca con lechuga, tomate, pepino, zanahoria rallada y un toque de limón o vinagreta.', 'ENSALADA MIXTA', 5, 1),
       (true, true, 'Preparada con papas cocidas, arvejas y zanahorias en cubos, mezcladas con mayonesa.', 'ENSALADA BLANCA', 5, 1),
       (true, true, 'Guiso cremoso de gallina desmenuzada con ají amarillo y pan remojado', 'AJI DE GALLINA', 10, 4),
       (true, true, 'Filete empanizado y frito, crujiente por fuera y tierno por dentro.', 'MILANESA', 10, 4),
       (true, true, 'Gallina cocida lentamente en salsa criolla con especias.', 'GALLINA GUISADA', 10, 4),
       (true, true, 'Caigua cocida rellena de carne molida y condimentos tradicionales.', 'CAIGUA RELLENA', 10, 4),
       (true, true, 'Pollo crocante acompañado de papa amarilla sazonada con hierbas.', 'POLLO FRITO C/ AJIACO', 10, 4),
       (true, true, 'Pescado cocido al vapor con tomate, cebolla y especias.', 'SUDADO DE PESCADO', 10, 4),
       (true, true, 'Pollo frito bañado en salsa de vinagre con cebolla, ají y zanahoria.', 'ESCABECHE DE POLLO', 10, 4),
       (true, true, 'Carne de res cocinada en salsa oscura y especiada hasta quedar suave.', 'ASADO DE RES', 10, 4),
       (true, true, 'Arroz aromatizado con culantro acompañado de pollo jugoso.', 'ARROZ VERDE C/ POLLO', 10, 4),
       (true, true, '', 'TAPER GRANDE', 1.5, 3),
       (true, true, '', 'TAPER CHICO', 1, 3),
       (true, true, '', 'TAPER SOPA', 1, 3),
       (true, true, 'Carne de cabrito guisada en salsa de chicha de jora con especias norteñas.', 'CABRITO A LA NORTEÑA', 15, 5),
       (true, true, 'Pato cocinado lentamente en salsa de culantro con arroz y yuca.', 'PATO GUISADO', 15, 5),
       (true, true, 'Lomo salteado servido con plátano frito, huevo y papas fritas.', 'LOMITO A LO POBRE', 12, 5),
       (true, true, 'Chuleta de cerdo dorada a la plancha con condimentos caseros.', 'CHULETA DE CERDO', 12, 5),
       (true, true, 'Trozos de pollo fritos hasta quedar dorados y crujientes con papas fritas', 'CHICHARRON DE POLLO', 13, 5);

-- =====================================================
-- Inventario para bebidas y descartables
-- =====================================================

-- Crear registros en inventory para bebidas (tipo BEVERAGE)
INSERT INTO inventory (name, quantity, type, unit_of_measure)
VALUES ('INCA KOLA PERSONAL', 0, 'BEVERAGE'::inventory_type, 'UNIDAD'::unit_of_measure),
       ('INCA KOLA 1L', 0, 'BEVERAGE'::inventory_type, 'UNIDAD'::unit_of_measure);

-- Crear registros en inventory para descartables (tipo DISPOSABLE)
INSERT INTO inventory (name, quantity, type, unit_of_measure)
VALUES ('TAPER GRANDE', 0, 'DISPOSABLE'::inventory_type, 'UNIDAD'::unit_of_measure),
       ('TAPER CHICO', 0, 'DISPOSABLE'::inventory_type, 'UNIDAD'::unit_of_measure),
       ('TAPER SOPA', 0, 'DISPOSABLE'::inventory_type, 'UNIDAD'::unit_of_measure);

-- Relaciones product_inventory para bebidas
INSERT INTO product_inventory (product_id, inventory_id, quantity, unit_of_measure)
VALUES (4, 1, 1, 'UNIDAD'::unit_of_measure),
       (5, 2, 1, 'UNIDAD'::unit_of_measure);

-- Relaciones product_inventory para descartables
INSERT INTO product_inventory (product_id, inventory_id, quantity, unit_of_measure)
VALUES (23, 3, 1, 'UNIDAD'::unit_of_measure),
       (24, 4, 1, 'UNIDAD'::unit_of_measure),
       (25, 5, 1, 'UNIDAD'::unit_of_measure);
