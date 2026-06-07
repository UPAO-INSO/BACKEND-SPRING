-- V17: Hacer opcionales los campos pesados de Customer/Person
-- Permite crear clientes ligeros (solo nombre) para separaciones rápidas.
-- Los campos de facturación (DNI, dirección, etc.) se llenan después si se requiere boleta/factura.

ALTER TABLE persons ALTER COLUMN lastname DROP NOT NULL;
ALTER TABLE persons ALTER COLUMN phone    DROP NOT NULL;

ALTER TABLE customers ALTER COLUMN email            DROP NOT NULL;
ALTER TABLE customers ALTER COLUMN document_type    DROP NOT NULL;
ALTER TABLE customers ALTER COLUMN document_number  DROP NOT NULL;
ALTER TABLE customers ALTER COLUMN departament      DROP NOT NULL;
ALTER TABLE customers ALTER COLUMN province         DROP NOT NULL;
ALTER TABLE customers ALTER COLUMN district         DROP NOT NULL;
ALTER TABLE customers ALTER COLUMN complete_address DROP NOT NULL;
