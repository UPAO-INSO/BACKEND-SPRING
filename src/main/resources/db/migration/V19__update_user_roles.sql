-- Agrega el job ADMINISTRADOR y migra los valores de rol heredados.
-- Las columnas ya son VARCHAR desde V10, no se necesita ALTER TYPE.

INSERT INTO job (title) VALUES ('ADMINISTRADOR') ON CONFLICT DO NOTHING;

UPDATE "user" SET role = 'ADMINISTRADOR' WHERE role = 'ADMIN';
UPDATE "user" SET role = 'GERENTE'       WHERE role = 'SUPER_USER';
UPDATE "user" SET role = 'MESERO'        WHERE role = 'USER';
