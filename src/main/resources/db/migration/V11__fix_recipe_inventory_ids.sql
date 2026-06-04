-- =====================================================
-- V11: Corregir referencias de inventory_id en recetas
-- =====================================================
-- BUG: V7 insertó los ingredientes en inventory con IDs 6-46
-- (porque V5 ya ocupó IDs 1-5 con bebidas/descartables),
-- pero las recetas en product_inventory usaban inventory_id 1-41
-- como si los ingredientes comenzaran en ID 1.
-- Efecto: cada receta apuntaba a ítems incorrectos, algunos
-- a bebidas con stock 0 y unidad UNIDAD, causando error de
-- "unidades incompatibles" que se reportaba como "stock insuficiente".
-- Fix: desplazar todos los inventory_id de recetas de comida en +5.
-- =====================================================

UPDATE product_inventory
SET inventory_id = inventory_id + 5
WHERE product_id IN (
    1,  -- HUANCAINA
    2,  -- YUCA RELLENA
    3,  -- SANGRECITA
    6,  -- LOMITO SALTADO
    7,  -- AGUADITO
    8,  -- CEVICHE
    9,  -- ENSALADA RUSA
    10, -- SOPA DE FIDEO
    11, -- SOPA DE SEMOLA
    12, -- ENSALADA MIXTA
    13, -- ENSALADA BLANCA
    14, -- AJI DE GALLINA
    15, -- MILANESA
    16, -- GALLINA GUISADA
    17, -- CAIGUA RELLENA
    18, -- POLLO FRITO C/ AJIACO
    19, -- SUDADO DE PESCADO
    20, -- ESCABECHE DE POLLO
    21, -- ASADO DE RES
    22, -- ARROZ VERDE C/ POLLO
    26, -- CABRITO A LA NORTEÑA
    27, -- PATO GUISADO
    28, -- LOMITO A LO POBRE
    29, -- CHULETA DE CERDO
    30  -- CHICHARRON DE POLLO
);
