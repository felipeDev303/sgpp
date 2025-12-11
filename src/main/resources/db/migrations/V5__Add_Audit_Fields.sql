-- ============================================
-- Migración V5: Agregar campos de auditoría
-- ============================================
-- Descripción: Agrega campos de auditoría (created_at, updated_at, created_by, updated_by)
--              a todas las tablas principales del sistema para rastreo de cambios
-- Fecha: 11 de diciembre de 2025
-- ============================================

-- ============================================
-- 1. AGREGAR CAMPOS A TABLA USUARIOS
-- ============================================
ALTER TABLE usuarios
ADD COLUMN created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
ADD COLUMN updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
ADD COLUMN created_by VARCHAR(100),
ADD COLUMN updated_by VARCHAR(100);

-- ============================================
-- 2. AGREGAR CAMPOS A TABLA ESTUDIANTES
-- ============================================
ALTER TABLE estudiantes
ADD COLUMN created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
ADD COLUMN updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
ADD COLUMN created_by VARCHAR(100),
ADD COLUMN updated_by VARCHAR(100);

-- ============================================
-- 3. AGREGAR CAMPOS A TABLA PROFESORES
-- ============================================
ALTER TABLE profesores
ADD COLUMN created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
ADD COLUMN updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
ADD COLUMN created_by VARCHAR(100),
ADD COLUMN updated_by VARCHAR(100);

-- ============================================
-- 4. AGREGAR CAMPOS A TABLA EMPRESAS
-- ============================================
ALTER TABLE empresas
ADD COLUMN created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
ADD COLUMN updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
ADD COLUMN created_by VARCHAR(100),
ADD COLUMN updated_by VARCHAR(100);

-- ============================================
-- 5. AGREGAR CAMPOS A TABLA PRACTICAS
-- ============================================
-- Nota: jefes_directos ya tiene created_at y updated_at desde V2
-- Solo agregamos created_by y updated_by
ALTER TABLE practicas
ADD COLUMN created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
ADD COLUMN updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
ADD COLUMN created_by VARCHAR(100),
ADD COLUMN updated_by VARCHAR(100);

-- ============================================
-- 6. AGREGAR CAMPOS A TABLA JEFES_DIRECTOS
-- ============================================
-- Agregar solo created_by y updated_by (ya tiene created_at y updated_at)
ALTER TABLE jefes_directos
ADD COLUMN created_by VARCHAR(100),
ADD COLUMN updated_by VARCHAR(100);

-- ============================================
-- 7. TRIGGERS PARA ACTUALIZACIÓN AUTOMÁTICA
-- ============================================

-- Función genérica para actualizar updated_at
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger para usuarios
DROP TRIGGER IF EXISTS trigger_update_usuarios_updated_at ON usuarios;
CREATE TRIGGER trigger_update_usuarios_updated_at
    BEFORE UPDATE ON usuarios
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

-- Trigger para estudiantes
DROP TRIGGER IF EXISTS trigger_update_estudiantes_updated_at ON estudiantes;
CREATE TRIGGER trigger_update_estudiantes_updated_at
    BEFORE UPDATE ON estudiantes
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

-- Trigger para profesores
DROP TRIGGER IF EXISTS trigger_update_profesores_updated_at ON profesores;
CREATE TRIGGER trigger_update_profesores_updated_at
    BEFORE UPDATE ON profesores
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

-- Trigger para empresas
DROP TRIGGER IF EXISTS trigger_update_empresas_updated_at ON empresas;
CREATE TRIGGER trigger_update_empresas_updated_at
    BEFORE UPDATE ON empresas
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

-- Trigger para practicas
DROP TRIGGER IF EXISTS trigger_update_practicas_updated_at ON practicas;
CREATE TRIGGER trigger_update_practicas_updated_at
    BEFORE UPDATE ON practicas
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

-- Trigger para jefes_directos (ya existe el trigger desde V2, recrearlo)
DROP TRIGGER IF EXISTS trigger_update_jefes_directos_updated_at ON jefes_directos;
CREATE TRIGGER trigger_update_jefes_directos_updated_at
    BEFORE UPDATE ON jefes_directos
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

-- ============================================
-- 8. ÍNDICES PARA OPTIMIZACIÓN DE CONSULTAS
-- ============================================

-- Índices para consultas de auditoría por fecha de creación
CREATE INDEX idx_usuarios_created_at ON usuarios(created_at);
CREATE INDEX idx_estudiantes_created_at ON estudiantes(created_at);
CREATE INDEX idx_profesores_created_at ON profesores(created_at);
CREATE INDEX idx_empresas_created_at ON empresas(created_at);
CREATE INDEX idx_practicas_created_at ON practicas(created_at);
CREATE INDEX idx_jefes_directos_created_at ON jefes_directos(created_at);

-- Índices para consultas de auditoría por fecha de actualización
CREATE INDEX idx_usuarios_updated_at ON usuarios(updated_at);
CREATE INDEX idx_estudiantes_updated_at ON estudiantes(updated_at);
CREATE INDEX idx_profesores_updated_at ON profesores(updated_at);
CREATE INDEX idx_empresas_updated_at ON empresas(updated_at);
CREATE INDEX idx_practicas_updated_at ON practicas(updated_at);
CREATE INDEX idx_jefes_directos_updated_at ON jefes_directos(updated_at);

-- Índices para consultas de auditoría por usuario que creó
CREATE INDEX idx_usuarios_created_by ON usuarios(created_by);
CREATE INDEX idx_estudiantes_created_by ON estudiantes(created_by);
CREATE INDEX idx_profesores_created_by ON profesores(created_by);
CREATE INDEX idx_empresas_created_by ON empresas(created_by);
CREATE INDEX idx_practicas_created_by ON practicas(created_by);
CREATE INDEX idx_jefes_directos_created_by ON jefes_directos(created_by);

-- Índices para consultas de auditoría por usuario que actualizó
CREATE INDEX idx_usuarios_updated_by ON usuarios(updated_by);
CREATE INDEX idx_estudiantes_updated_by ON estudiantes(updated_by);
CREATE INDEX idx_profesores_updated_by ON profesores(updated_by);
CREATE INDEX idx_empresas_updated_by ON empresas(updated_by);
CREATE INDEX idx_practicas_updated_by ON practicas(updated_by);
CREATE INDEX idx_jefes_directos_updated_by ON jefes_directos(updated_by);

-- ============================================
-- 9. ACTUALIZAR DATOS EXISTENTES
-- ============================================
-- Establecer created_at para registros existentes (datos de prueba de V4)

UPDATE usuarios SET created_at = CURRENT_TIMESTAMP WHERE created_at IS NULL;
UPDATE estudiantes SET created_at = CURRENT_TIMESTAMP WHERE created_at IS NULL;
UPDATE profesores SET created_at = CURRENT_TIMESTAMP WHERE created_at IS NULL;
UPDATE empresas SET created_at = CURRENT_TIMESTAMP WHERE created_at IS NULL;
UPDATE practicas SET created_at = CURRENT_TIMESTAMP WHERE created_at IS NULL;
-- jefes_directos ya tiene created_at desde V2

-- ============================================
-- 10. COMENTARIOS DE DOCUMENTACIÓN
-- ============================================

COMMENT ON COLUMN usuarios.created_at IS 'Fecha y hora de creación del registro';
COMMENT ON COLUMN usuarios.updated_at IS 'Fecha y hora de última actualización del registro';
COMMENT ON COLUMN usuarios.created_by IS 'Usuario que creó el registro (email)';
COMMENT ON COLUMN usuarios.updated_by IS 'Usuario que actualizó el registro por última vez (email)';

COMMENT ON COLUMN estudiantes.created_at IS 'Fecha y hora de creación del registro';
COMMENT ON COLUMN estudiantes.updated_at IS 'Fecha y hora de última actualización del registro';
COMMENT ON COLUMN estudiantes.created_by IS 'Usuario que creó el registro (email)';
COMMENT ON COLUMN estudiantes.updated_by IS 'Usuario que actualizó el registro por última vez (email)';

COMMENT ON COLUMN profesores.created_at IS 'Fecha y hora de creación del registro';
COMMENT ON COLUMN profesores.updated_at IS 'Fecha y hora de última actualización del registro';
COMMENT ON COLUMN profesores.created_by IS 'Usuario que creó el registro (email)';
COMMENT ON COLUMN profesores.updated_by IS 'Usuario que actualizó el registro por última vez (email)';

COMMENT ON COLUMN empresas.created_at IS 'Fecha y hora de creación del registro';
COMMENT ON COLUMN empresas.updated_at IS 'Fecha y hora de última actualización del registro';
COMMENT ON COLUMN empresas.created_by IS 'Usuario que creó el registro (email)';
COMMENT ON COLUMN empresas.updated_by IS 'Usuario que actualizó el registro por última vez (email)';

COMMENT ON COLUMN practicas.created_at IS 'Fecha y hora de creación del registro';
COMMENT ON COLUMN practicas.updated_at IS 'Fecha y hora de última actualización del registro';
COMMENT ON COLUMN practicas.created_by IS 'Usuario que creó el registro (email)';
COMMENT ON COLUMN practicas.updated_by IS 'Usuario que actualizó el registro por última vez (email)';

COMMENT ON COLUMN jefes_directos.created_by IS 'Usuario que creó el registro (email)';
COMMENT ON COLUMN jefes_directos.updated_by IS 'Usuario que actualizó el registro por última vez (email)';

-- ============================================
-- 11. VISTA DE AUDITORÍA (OPCIONAL)
-- ============================================
-- Vista para consultar fácilmente la información de auditoría de todas las tablas

CREATE OR REPLACE VIEW v_audit_summary AS
SELECT 
    'usuarios' as tabla,
    COUNT(*) as total_registros,
    MIN(created_at) as primer_registro,
    MAX(created_at) as ultimo_registro,
    MAX(updated_at) as ultima_actualizacion
FROM usuarios
UNION ALL
SELECT 
    'estudiantes' as tabla,
    COUNT(*) as total_registros,
    MIN(created_at) as primer_registro,
    MAX(created_at) as ultimo_registro,
    MAX(updated_at) as ultima_actualizacion
FROM estudiantes
UNION ALL
SELECT 
    'profesores' as tabla,
    COUNT(*) as total_registros,
    MIN(created_at) as primer_registro,
    MAX(created_at) as ultimo_registro,
    MAX(updated_at) as ultima_actualizacion
FROM profesores
UNION ALL
SELECT 
    'empresas' as tabla,
    COUNT(*) as total_registros,
    MIN(created_at) as primer_registro,
    MAX(created_at) as ultimo_registro,
    MAX(updated_at) as ultima_actualizacion
FROM empresas
UNION ALL
SELECT 
    'practicas' as tabla,
    COUNT(*) as total_registros,
    MIN(created_at) as primer_registro,
    MAX(created_at) as ultimo_registro,
    MAX(updated_at) as ultima_actualizacion
FROM practicas
UNION ALL
SELECT 
    'jefes_directos' as tabla,
    COUNT(*) as total_registros,
    MIN(created_at) as primer_registro,
    MAX(created_at) as ultimo_registro,
    MAX(updated_at) as ultima_actualizacion
FROM jefes_directos
ORDER BY tabla;

COMMENT ON VIEW v_audit_summary IS 'Vista resumen de auditoría para todas las tablas del sistema';

-- ============================================
-- FIN DE MIGRACIÓN V5
-- ============================================

-- Verificar campos agregados
-- SELECT table_name, column_name, data_type 
-- FROM information_schema.columns 
-- WHERE column_name IN ('created_at', 'updated_at', 'created_by', 'updated_by')
-- AND table_schema = 'public'
-- ORDER BY table_name, column_name;

-- Consultar vista de auditoría
-- SELECT * FROM v_audit_summary;
