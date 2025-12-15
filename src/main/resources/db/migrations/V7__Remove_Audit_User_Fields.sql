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
-- 1. ELIMINAR VISTAS QUE DEPENDEN DE COLUMNAS AUDIT USER
-- =====================================================
-- Las vistas creadas en V6 dependen de created_by y updated_by
-- Deben eliminarse antes de eliminar las columnas

DROP VIEW IF EXISTS v_practicas_activas CASCADE;
DROP VIEW IF EXISTS v_practicas_eliminadas CASCADE;

-- =====================================================
-- 2. ELIMINAR COLUMNAS DE AUDITORÍA DE USUARIO
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
-- 4. RECREAR VISTAS SIN COLUMNAS DE AUDITORÍA DE USUARIO
-- =====================================================

-- Recrear vista de prácticas activas sin created_by y updated_by
CREATE OR REPLACE VIEW v_practicas_activas AS
SELECT 
    p.id,
    p.fecha_inicio,
    p.fecha_termino,
    p.descripcion_actividades,
    p.estado,
    p.estudiante_id,
    p.profesor_id,
    p.empresa_id,
    p.jefe_directo_id,
    p.created_at,
    p.updated_at,
    e.nombre AS estudiante_nombre,
    est.rut AS estudiante_rut,
    prof.nombre AS profesor_nombre,
    emp.nombre AS empresa_nombre
FROM practicas p
LEFT JOIN estudiantes est ON p.estudiante_id = est.id
LEFT JOIN usuarios e ON est.id = e.id
LEFT JOIN profesores pro ON p.profesor_id = pro.id
LEFT JOIN usuarios prof ON pro.id = prof.id
LEFT JOIN empresas emp ON p.empresa_id = emp.id
WHERE p.deleted = FALSE;

COMMENT ON VIEW v_practicas_activas IS 
'Vista que muestra solo las prácticas activas (no eliminadas lógicamente) con información relacionada. Actualizada en V7 sin campos de auditoría de usuario.';

-- Recrear vista de prácticas eliminadas sin created_by y updated_by
CREATE OR REPLACE VIEW v_practicas_eliminadas AS
SELECT 
    p.id,
    p.fecha_inicio,
    p.fecha_termino,
    p.estado,
    p.deleted_at AS fecha_eliminacion,
    e.nombre AS estudiante_nombre,
    emp.nombre AS empresa_nombre,
    EXTRACT(DAY FROM (p.deleted_at - p.created_at)) AS dias_antes_eliminacion
FROM practicas p
LEFT JOIN estudiantes est ON p.estudiante_id = est.id
LEFT JOIN usuarios e ON est.id = e.id
LEFT JOIN empresas emp ON p.empresa_id = emp.id
WHERE p.deleted = TRUE
ORDER BY p.deleted_at DESC;

COMMENT ON VIEW v_practicas_eliminadas IS 
'Vista de auditoría que muestra prácticas eliminadas lógicamente con información del contexto. Actualizada en V7 sin campos de auditoría de usuario.';

-- =====================================================
-- FIN DE MIGRATION V7
-- =====================================================
