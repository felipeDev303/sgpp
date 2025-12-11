-- Migración V3: Agregar campo estado a la tabla practicas
-- Fecha: 10 de diciembre de 2025

-- Agregar columna estado con valor por defecto PENDIENTE
ALTER TABLE practicas 
ADD COLUMN estado VARCHAR(20) NOT NULL DEFAULT 'PENDIENTE';

-- Agregar constraint para validar los valores permitidos
ALTER TABLE practicas
ADD CONSTRAINT chk_practica_estado 
CHECK (estado IN ('PENDIENTE', 'APROBADA', 'EN_CURSO', 'COMPLETADA', 'RECHAZADA', 'SUSPENDIDA'));

-- Crear índice para optimizar consultas por estado
CREATE INDEX idx_practicas_estado ON practicas(estado);

-- Crear índice compuesto para consultas de estudiante + estado
CREATE INDEX idx_practicas_estudiante_estado ON practicas(estudiante_id, estado);

-- Comentarios para documentación
COMMENT ON COLUMN practicas.estado IS 'Estado actual de la práctica: PENDIENTE, APROBADA, EN_CURSO, COMPLETADA, RECHAZADA, SUSPENDIDA';
COMMENT ON CONSTRAINT chk_practica_estado ON practicas IS 'Valida que el estado sea uno de los valores permitidos por el enum EstadoPractica';
