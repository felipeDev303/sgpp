-- ============================================
-- Migración V2: Crear tabla de Prácticas
-- ============================================
-- Descripción: Crea la tabla principal de prácticas profesionales con sus relaciones,
--              constraints de integridad, e índices para optimización de consultas.
-- Fecha: 10 de diciembre de 2025
-- ============================================

-- 1. Tabla para Jefes Directos (supervisores de empresa)
CREATE TABLE jefes_directos (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    contacto VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 2. Tabla principal de Prácticas
CREATE TABLE practicas (
    id BIGSERIAL PRIMARY KEY,
    
    -- Fechas de la práctica
    fecha_inicio DATE NOT NULL,
    fecha_termino DATE NOT NULL,
    
    -- Descripción de actividades
    descripcion_actividades TEXT NOT NULL,
    
    -- Relaciones con otras entidades
    estudiante_id BIGINT NOT NULL,
    profesor_id BIGINT,  -- Opcional: puede no tener profesor asignado inicialmente
    empresa_id BIGINT NOT NULL,
    jefe_directo_id BIGINT NOT NULL,
    
    -- Campos de auditoría
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    -- ============================================
    -- CONSTRAINTS DE INTEGRIDAD REFERENCIAL
    -- ============================================
    
    -- Foreign Key: Estudiante (debe existir en tabla estudiantes)
    CONSTRAINT fk_practica_estudiante 
        FOREIGN KEY (estudiante_id) 
        REFERENCES estudiantes(id) 
        ON DELETE RESTRICT 
        ON UPDATE CASCADE,
    
    -- Foreign Key: Profesor Supervisor (puede ser NULL)
    CONSTRAINT fk_practica_profesor 
        FOREIGN KEY (profesor_id) 
        REFERENCES profesores(id) 
        ON DELETE SET NULL 
        ON UPDATE CASCADE,
    
    -- Foreign Key: Empresa (debe existir en tabla empresas)
    CONSTRAINT fk_practica_empresa 
        FOREIGN KEY (empresa_id) 
        REFERENCES empresas(id) 
        ON DELETE RESTRICT 
        ON UPDATE CASCADE,
    
    -- Foreign Key: Jefe Directo (debe existir en tabla jefes_directos)
    CONSTRAINT fk_practica_jefe_directo 
        FOREIGN KEY (jefe_directo_id) 
        REFERENCES jefes_directos(id) 
        ON DELETE RESTRICT 
        ON UPDATE CASCADE,
    
    -- ============================================
    -- CONSTRAINTS DE VALIDACIÓN DE NEGOCIO
    -- ============================================
    
    -- Check: La fecha de término debe ser posterior a la fecha de inicio
    CONSTRAINT chk_fechas_validas 
        CHECK (fecha_termino > fecha_inicio),
    
    -- Check: Las fechas de inicio no pueden ser muy antiguas (últimos 5 años)
    CONSTRAINT chk_fecha_inicio_reciente 
        CHECK (fecha_inicio >= CURRENT_DATE - INTERVAL '5 years'),
    
    -- Check: Descripción de actividades debe tener contenido significativo
    CONSTRAINT chk_descripcion_minima 
        CHECK (LENGTH(TRIM(descripcion_actividades)) >= 10)
);

-- ============================================
-- ÍNDICES PARA OPTIMIZACIÓN DE CONSULTAS
-- ============================================

-- Índice: Búsquedas por estudiante (query más común)
CREATE INDEX idx_practica_estudiante 
    ON practicas(estudiante_id);

-- Índice: Búsquedas por empresa
CREATE INDEX idx_practica_empresa 
    ON practicas(empresa_id);

-- Índice: Búsquedas por profesor supervisor
CREATE INDEX idx_practica_profesor 
    ON practicas(profesor_id);

-- Índice: Búsquedas por jefe directo
CREATE INDEX idx_practica_jefe_directo 
    ON practicas(jefe_directo_id);

-- Índice: Búsquedas por rango de fechas (reportes)
CREATE INDEX idx_practica_fechas 
    ON practicas(fecha_inicio, fecha_termino);

-- Índice compuesto: Búsquedas de prácticas activas por estudiante
CREATE INDEX idx_practica_estudiante_fechas 
    ON practicas(estudiante_id, fecha_inicio, fecha_termino);

-- ============================================
-- COMENTARIOS DE DOCUMENTACIÓN
-- ============================================

COMMENT ON TABLE practicas IS 
    'Tabla principal que almacena las prácticas profesionales de los estudiantes';

COMMENT ON COLUMN practicas.fecha_inicio IS 
    'Fecha de inicio de la práctica (no puede ser anterior a 5 años)';

COMMENT ON COLUMN practicas.fecha_termino IS 
    'Fecha de término de la práctica (debe ser posterior a fecha_inicio)';

COMMENT ON COLUMN practicas.descripcion_actividades IS 
    'Descripción detallada de las actividades realizadas durante la práctica (mínimo 10 caracteres)';

COMMENT ON COLUMN practicas.profesor_id IS 
    'Profesor supervisor asignado (puede ser NULL si aún no se ha asignado)';

-- ============================================
-- TRIGGER PARA ACTUALIZAR updated_at
-- ============================================

-- Función para actualizar automáticamente el campo updated_at
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger para la tabla practicas
CREATE TRIGGER trigger_update_practica_timestamp
    BEFORE UPDATE ON practicas
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

-- Trigger para la tabla jefes_directos
CREATE TRIGGER trigger_update_jefe_directo_timestamp
    BEFORE UPDATE ON jefes_directos
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

-- ============================================
-- CONSTRAINT ÚNICO: Un estudiante no puede tener prácticas superpuestas
-- ============================================

-- Nota: PostgreSQL no soporta constraints de rangos directamente sin extensiones.
-- Esta validación debe implementarse a nivel de aplicación (Service layer)
-- o mediante un trigger más complejo si se requiere a nivel de BD.

-- Ejemplo de comentario para el equipo:
COMMENT ON INDEX idx_practica_estudiante_fechas IS 
    'Índice usado para verificar prácticas superpuestas a nivel de aplicación';
