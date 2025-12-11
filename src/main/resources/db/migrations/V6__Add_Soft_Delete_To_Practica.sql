-- =====================================================================
-- V6: Agregar campos de borrado lógico a tabla practicas
-- =====================================================================
-- Descripción: Implementa soft delete agregando campos deleted y deleted_at
--              para cumplir con requisito explícito de la problemática
-- Autor: SGPP Team
-- Fecha: 11 de diciembre de 2025
-- =====================================================================

-- Agregar campo deleted (booleano) con valor por defecto FALSE
ALTER TABLE practicas 
ADD COLUMN deleted BOOLEAN NOT NULL DEFAULT FALSE;

-- Agregar campo deleted_at (timestamp) para registrar cuándo se eliminó
ALTER TABLE practicas 
ADD COLUMN deleted_at TIMESTAMP;

-- Crear comentarios descriptivos para documentación
COMMENT ON COLUMN practicas.deleted IS 
'Indica si la práctica ha sido eliminada lógicamente (soft delete). TRUE = eliminada, FALSE = activa';

COMMENT ON COLUMN practicas.deleted_at IS 
'Timestamp que registra cuándo se realizó el borrado lógico. NULL si no ha sido eliminada';

-- Crear índice para optimizar consultas que filtran por deleted
CREATE INDEX idx_practicas_deleted ON practicas(deleted);

-- Crear índice compuesto para consultas comunes (activas por estudiante)
CREATE INDEX idx_practicas_deleted_estudiante ON practicas(deleted, estudiante_id) 
WHERE deleted = FALSE;

-- Crear índice compuesto para consultas de profesor (activas por profesor)
CREATE INDEX idx_practicas_deleted_profesor ON practicas(deleted, profesor_id) 
WHERE deleted = FALSE;

-- Constraint: deleted_at debe ser NULL si deleted es FALSE
ALTER TABLE practicas 
ADD CONSTRAINT check_deleted_at_consistency 
CHECK (
    (deleted = FALSE AND deleted_at IS NULL) OR 
    (deleted = TRUE AND deleted_at IS NOT NULL)
);

-- Vista para facilitar consultas de prácticas activas (no eliminadas)
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
    p.created_by,
    p.updated_by,
    e.nombre_completo AS estudiante_nombre,
    e.rut AS estudiante_rut,
    prof.nombre_completo AS profesor_nombre,
    emp.nombre AS empresa_nombre
FROM practicas p
LEFT JOIN estudiantes est ON p.estudiante_id = est.id
LEFT JOIN usuarios e ON est.id = e.id
LEFT JOIN profesores pro ON p.profesor_id = pro.id
LEFT JOIN usuarios prof ON pro.id = prof.id
LEFT JOIN empresas emp ON p.empresa_id = emp.id
WHERE p.deleted = FALSE;

COMMENT ON VIEW v_practicas_activas IS 
'Vista que muestra solo las prácticas activas (no eliminadas lógicamente) con información relacionada';

-- Vista para auditoría de eliminaciones
CREATE OR REPLACE VIEW v_practicas_eliminadas AS
SELECT 
    p.id,
    p.fecha_inicio,
    p.fecha_termino,
    p.estado,
    p.deleted_at AS fecha_eliminacion,
    p.updated_by AS eliminado_por,
    e.nombre_completo AS estudiante_nombre,
    emp.nombre AS empresa_nombre,
    EXTRACT(DAY FROM (p.deleted_at - p.created_at)) AS dias_antes_eliminacion
FROM practicas p
LEFT JOIN estudiantes est ON p.estudiante_id = est.id
LEFT JOIN usuarios e ON est.id = e.id
LEFT JOIN empresas emp ON p.empresa_id = emp.id
WHERE p.deleted = TRUE
ORDER BY p.deleted_at DESC;

COMMENT ON VIEW v_practicas_eliminadas IS 
'Vista de auditoría que muestra prácticas eliminadas lógicamente con información del contexto';
