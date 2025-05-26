-- ==================================================
-- 01-schemas.sql
-- Archivo de inicialización de esquemas y extensiones
-- ==================================================

-- Crear esquema fit_me si no existe
CREATE SCHEMA IF NOT EXISTS fit_me;

-- Crear extensiones necesarias
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

-- Configurar permisos del esquema
GRANT USAGE ON SCHEMA fit_me TO PUBLIC;
GRANT CREATE ON SCHEMA fit_me TO PUBLIC;

-- Comentarios del esquema
COMMENT ON SCHEMA fit_me IS 'Schema principal para la aplicación FitMe - gestión de entrenadores y deportistas';

-- Configuración de búsqueda de esquemas
SET search_path TO public, fit_me;

-- Mensaje de confirmación
DO $$
BEGIN
    RAISE NOTICE 'Esquema fit_me y extensiones creados exitosamente';
END $$;