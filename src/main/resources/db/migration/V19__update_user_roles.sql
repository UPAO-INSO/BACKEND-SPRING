-- Reemplaza el enum user_role genérico por los roles reales del restaurante.
-- También agrega ADMINISTRADOR a job_title y a la tabla job.

-- 1. Ampliar job_title con ADMINISTRADOR
ALTER TYPE job_title ADD VALUE IF NOT EXISTS 'ADMINISTRADOR';

-- 2. Insertar el job ADMINISTRADOR en la tabla job
INSERT INTO job (title) VALUES ('ADMINISTRADOR') ON CONFLICT DO NOTHING;

-- 3. Convertir columna role a texto para poder hacer el cambio de tipo
ALTER TABLE "user" ALTER COLUMN role TYPE VARCHAR(50);

-- 4. Migrar valores viejos a nuevos roles
UPDATE "user" SET role = 'ADMINISTRADOR' WHERE role = 'ADMIN';
UPDATE "user" SET role = 'GERENTE'       WHERE role = 'SUPER_USER';
UPDATE "user" SET role = 'MESERO'        WHERE role = 'USER';

-- 5. Eliminar el tipo antiguo y crear el nuevo
DROP TYPE user_role;
CREATE TYPE user_role AS ENUM ('GERENTE', 'CAJERO', 'COCINERO', 'MESERO', 'ADMINISTRADOR');

-- 6. Restaurar la columna con el nuevo ENUM
ALTER TABLE "user" ALTER COLUMN role TYPE user_role USING role::user_role;
