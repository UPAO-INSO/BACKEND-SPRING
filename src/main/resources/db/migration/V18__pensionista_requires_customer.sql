-- V18: Pensionista debe ser un cliente registrado.
-- Elimina los campos redundantes name/phone (vienen del CustomerModel)
-- y hace obligatorio el FK customer_id.

-- Limpiar registros sin cliente vinculado (entorno dev)
DELETE FROM pensionista_consumo
WHERE pensionista_id IN (SELECT id FROM pensionista WHERE customer_id IS NULL);

DELETE FROM separacion
WHERE pensionista_id IN (SELECT id FROM pensionista WHERE customer_id IS NULL);

DELETE FROM pensionista WHERE customer_id IS NULL;

-- Hacer customer_id obligatorio y eliminar campos redundantes
ALTER TABLE pensionista ALTER COLUMN customer_id SET NOT NULL;
ALTER TABLE pensionista DROP COLUMN IF EXISTS name;
ALTER TABLE pensionista DROP COLUMN IF EXISTS phone;
