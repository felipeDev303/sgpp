-- =====================================================
-- Migration: V7__Remove_Audit_User_Fields.sql
-- Description: Elimina campos de auditoría de usuario (created_by, updated_by)
--              de todas las tablas que los tienen.
--              
-- Razón: Sin sistema de autenticación, estos campos siempre retornan "system"
--        sin proporcionar valor real. Se mantienen solo created_at y updated_at.
--
-- Author: SGPP Team
-- Date: 2025-12-11
-- =====================================================

-- =====================================================
-- 1. ELIMINAR COLUMNAS DE AUDITORÍA DE USUARIO
-- =====================================================

-- Tabla: usuarios
COMMENT ON TABLE usuarios IS 'Tabla de usuarios (tabla padre de herencia JOINED). Campos de auditoría de usuario removidos en V7.';

ALTER TABLE usuarios 
DROP COLUMN IF EXISTS created_by,
DROP COLUMN IF EXISTS updated_by;

-- Tabla: estudiantes
COMMENT ON TABLE estudiantes IS 'Tabla de estudiantes (hereda de usuarios). Campos de auditoría de usuario removidos en V7.';

ALTER TABLE estudiantes 
DROP COLUMN IF EXISTS created_by,
DROP COLUMN IF EXISTS updated_by;

-- Tabla: profesores
COMMENT ON TABLE profesores IS 'Tabla de profesores (hereda de usuarios). Campos de auditoría de usuario removidos en V7.';

ALTER TABLE profesores 
DROP COLUMN IF EXISTS created_by,
DROP COLUMN IF EXISTS updated_by;

-- Tabla: empresas
COMMENT ON TABLE empresas IS 'Tabla de empresas. Campos de auditoría de usuario removidos en V7.';

ALTER TABLE empresas 
DROP COLUMN IF EXISTS created_by,
DROP COLUMN IF EXISTS updated_by;

-- Tabla: jefes_directos
COMMENT ON TABLE jefes_directos IS 'Tabla de jefes directos (supervisores de empresa). Campos de auditoría de usuario removidos en V7.';

ALTER TABLE jefes_directos 
DROP COLUMN IF EXISTS created_by,
DROP COLUMN IF EXISTS updated_by;

-- Tabla: practicas
COMMENT ON TABLE practicas IS 'Tabla principal de prácticas profesionales. Campos de auditoría de usuario removidos en V7.';

ALTER TABLE practicas 
DROP COLUMN IF EXISTS created_by,
DROP COLUMN IF EXISTS updated_by;

-- =====================================================
-- 2. VERIFICACIÓN FINAL
-- =====================================================

-- Verificar que las columnas fueron eliminadas correctamente
DO $$
DECLARE
    v_count INTEGER;
BEGIN
    -- Contar columnas created_by/updated_by restantes en el esquema
    SELECT COUNT(*) INTO v_count
    FROM information_schema.columns
    WHERE table_schema = 'public'
      AND column_name IN ('created_by', 'updated_by');
    
    IF v_count > 0 THEN
        RAISE EXCEPTION 'ERROR: Aún existen % columnas created_by/updated_by en el esquema', v_count;
    ELSE
        RAISE NOTICE 'ÉXITO: Todas las columnas de auditoría de usuario fueron eliminadas correctamente';
    END IF;
END $$;

-- =====================================================
-- 3. RESUMEN DE CAMBIOS
-- =====================================================

COMMENT ON COLUMN usuarios.created_at IS 'Fecha de creación del registro (UTC). Auditado automáticamente por JPA.';
COMMENT ON COLUMN usuarios.updated_at IS 'Fecha de última modificación (UTC). Auditado automáticamente por JPA.';

COMMENT ON COLUMN estudiantes.created_at IS 'Fecha de creación del registro (UTC). Auditado automáticamente por JPA.';
COMMENT ON COLUMN estudiantes.updated_at IS 'Fecha de última modificación (UTC). Auditado automáticamente por JPA.';

COMMENT ON COLUMN profesores.created_at IS 'Fecha de creación del registro (UTC). Auditado automáticamente por JPA.';
COMMENT ON COLUMN profesores.updated_at IS 'Fecha de última modificación (UTC). Auditado automáticamente por JPA.';

COMMENT ON COLUMN empresas.created_at IS 'Fecha de creación del registro (UTC). Auditado automáticamente por JPA.';
COMMENT ON COLUMN empresas.updated_at IS 'Fecha de última modificación (UTC). Auditado automáticamente por JPA.';

COMMENT ON COLUMN jefes_directos.created_at IS 'Fecha de creación del registro (UTC). Auditado automáticamente por JPA.';
COMMENT ON COLUMN jefes_directos.updated_at IS 'Fecha de última modificación (UTC). Auditado automáticamente por JPA.';

COMMENT ON COLUMN practicas.created_at IS 'Fecha de creación del registro (UTC). Auditado automáticamente por JPA.';
COMMENT ON COLUMN practicas.updated_at IS 'Fecha de última modificación (UTC). Auditado automáticamente por JPA.';

-- =====================================================
-- FIN DE MIGRATION V7
-- =====================================================
