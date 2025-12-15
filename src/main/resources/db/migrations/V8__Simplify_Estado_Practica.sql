-- =====================================================
-- Migration: V8__Simplify_Estado_Practica.sql
-- Description: Simplifica los estados de práctica de 6 a 3 estados básicos.
--              Mapea estados antiguos a nuevos según su semántica:
--              - APROBADA → EN_CURSO (práctica lista para ejecutarse)
--              - RECHAZADA → PENDIENTE (práctica necesita revisión)
--              - SUSPENDIDA → EN_CURSO (práctica retoma actividad)
--
-- Razón: La problemática no menciona flujos de aprobación formal ni suspensión.
--        El foco está en el ciclo de vida básico: PENDIENTE → EN_CURSO → COMPLETADA
--
-- Author: SGPP Team
-- Date: 2025-12-11
-- =====================================================

-- =====================================================
-- 1. VERIFICAR ESTADOS EXISTENTES
-- =====================================================

-- Ver distribución actual de estados antes de la migración
DO $$
DECLARE
    v_pendiente INTEGER;
    v_aprobada INTEGER;
    v_en_curso INTEGER;
    v_completada INTEGER;
    v_rechazada INTEGER;
    v_suspendida INTEGER;
BEGIN
    SELECT 
        COUNT(*) FILTER (WHERE estado = 'PENDIENTE'),
        COUNT(*) FILTER (WHERE estado = 'APROBADA'),
        COUNT(*) FILTER (WHERE estado = 'EN_CURSO'),
        COUNT(*) FILTER (WHERE estado = 'COMPLETADA'),
        COUNT(*) FILTER (WHERE estado = 'RECHAZADA'),
        COUNT(*) FILTER (WHERE estado = 'SUSPENDIDA')
    INTO v_pendiente, v_aprobada, v_en_curso, v_completada, v_rechazada, v_suspendida
    FROM practicas
    WHERE deleted = false;
    
    RAISE NOTICE 'Estados actuales en prácticas activas:';
    RAISE NOTICE '  PENDIENTE: %', v_pendiente;
    RAISE NOTICE '  APROBADA: %', v_aprobada;
    RAISE NOTICE '  EN_CURSO: %', v_en_curso;
    RAISE NOTICE '  COMPLETADA: %', v_completada;
    RAISE NOTICE '  RECHAZADA: %', v_rechazada;
    RAISE NOTICE '  SUSPENDIDA: %', v_suspendida;
END $$;

-- =====================================================
-- 2. MAPEAR ESTADOS ANTIGUOS A NUEVOS
-- =====================================================

-- APROBADA → EN_CURSO
-- Razón: Una práctica aprobada está lista para ejecutarse, es equivalente a "en curso"
UPDATE practicas
SET estado = 'EN_CURSO'
WHERE estado = 'APROBADA'
  AND deleted = false;

-- RECHAZADA → PENDIENTE
-- Razón: Una práctica rechazada puede ser revisada y reactivada, vuelve a estado inicial
UPDATE practicas
SET estado = 'PENDIENTE'
WHERE estado = 'RECHAZADA'
  AND deleted = false;

-- SUSPENDIDA → EN_CURSO
-- Razón: Una práctica suspendida retoma su actividad, se considera activa
UPDATE practicas
SET estado = 'EN_CURSO'
WHERE estado = 'SUSPENDIDA'
  AND deleted = false;

-- =====================================================
-- 3. AGREGAR CONSTRAINT DE VALIDACIÓN
-- =====================================================

-- Eliminar constraint antiguo si existe
ALTER TABLE practicas 
DROP CONSTRAINT IF EXISTS check_estado_practica;

-- Agregar nuevo constraint que solo permite los 3 estados básicos
ALTER TABLE practicas
ADD CONSTRAINT check_estado_practica 
CHECK (estado IN ('PENDIENTE', 'EN_CURSO', 'COMPLETADA'));

COMMENT ON CONSTRAINT check_estado_practica ON practicas IS 
'Valida que el estado de la práctica sea uno de los 3 estados básicos: PENDIENTE, EN_CURSO, COMPLETADA';

-- =====================================================
-- 4. ACTUALIZAR COMENTARIOS Y DOCUMENTACIÓN
-- =====================================================

COMMENT ON COLUMN practicas.estado IS 
'Estado actual de la práctica. Valores permitidos:
- PENDIENTE: Práctica registrada, pendiente de iniciar
- EN_CURSO: Práctica activa, estudiante realizando actividades
- COMPLETADA: Práctica finalizada exitosamente (estado terminal)

Flujo de transiciones: PENDIENTE → EN_CURSO → COMPLETADA';

-- =====================================================
-- 5. VERIFICAR RESULTADO DE LA MIGRACIÓN
-- =====================================================

DO $$
DECLARE
    v_pendiente INTEGER;
    v_en_curso INTEGER;
    v_completada INTEGER;
    v_otros INTEGER;
BEGIN
    SELECT 
        COUNT(*) FILTER (WHERE estado = 'PENDIENTE'),
        COUNT(*) FILTER (WHERE estado = 'EN_CURSO'),
        COUNT(*) FILTER (WHERE estado = 'COMPLETADA'),
        COUNT(*) FILTER (WHERE estado NOT IN ('PENDIENTE', 'EN_CURSO', 'COMPLETADA'))
    INTO v_pendiente, v_en_curso, v_completada, v_otros
    FROM practicas
    WHERE deleted = false;
    
    RAISE NOTICE '';
    RAISE NOTICE 'Estados después de la migración:';
    RAISE NOTICE '  PENDIENTE: %', v_pendiente;
    RAISE NOTICE '  EN_CURSO: %', v_en_curso;
    RAISE NOTICE '  COMPLETADA: %', v_completada;
    RAISE NOTICE '  Otros estados (ERROR si > 0): %', v_otros;
    
    IF v_otros > 0 THEN
        RAISE EXCEPTION 'ERROR: Existen % prácticas con estados inválidos después de la migración', v_otros;
    END IF;
    
    RAISE NOTICE '';
    RAISE NOTICE 'ÉXITO: Migración completada. Todos los estados son válidos.';
END $$;

-- =====================================================
-- 6. ACTUALIZAR DATOS DE PRUEBA SI ES NECESARIO
-- =====================================================

-- Nota: Si V4__Initial_Data.sql contenía prácticas con estados APROBADA, RECHAZADA o SUSPENDIDA,
-- esos registros ya fueron migrados automáticamente por los UPDATE anteriores.
-- No se requiere acción adicional.

-- =====================================================
-- FIN DE MIGRATION V8
-- =====================================================
