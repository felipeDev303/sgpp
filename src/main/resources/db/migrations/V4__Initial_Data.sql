-- ============================================
-- Migración V4: Datos iniciales de prueba
-- ============================================
-- Descripción: Inserta datos de prueba para desarrollo y testing
--              Incluye usuarios, empresas, jefes directos y prácticas
-- Fecha: 11 de diciembre de 2025
-- NOTA: Estos datos son solo para desarrollo/testing
-- ============================================

-- ============================================
-- 1. USUARIOS BASE
-- ============================================
-- Contraseña para todos: "Password123" (en producción usar BCrypt)

-- Estudiantes
INSERT INTO usuarios (id, email, password, nombre, rol) VALUES
(1, 'juan.perez@estudiante.cl', 'Password123', 'Juan Pérez González', 'ESTUDIANTE'),
(2, 'maria.lopez@estudiante.cl', 'Password123', 'María López Silva', 'ESTUDIANTE'),
(3, 'carlos.ruiz@estudiante.cl', 'Password123', 'Carlos Ruiz Morales', 'ESTUDIANTE'),
(4, 'ana.torres@estudiante.cl', 'Password123', 'Ana Torres Díaz', 'ESTUDIANTE'),
(5, 'pedro.sanchez@estudiante.cl', 'Password123', 'Pedro Sánchez Rojas', 'ESTUDIANTE');

-- Profesores
INSERT INTO usuarios (id, email, password, nombre, rol) VALUES
(10, 'rosa.martinez@profesor.cl', 'Password123', 'Rosa Martínez Vega', 'PROFESOR'),
(11, 'luis.gonzalez@profesor.cl', 'Password123', 'Luis González Castro', 'PROFESOR'),
(12, 'patricia.fernandez@profesor.cl', 'Password123', 'Patricia Fernández Muñoz', 'PROFESOR');

-- ============================================
-- 2. ESTUDIANTES (detalles adicionales)
-- ============================================
INSERT INTO estudiantes (id, rut, carrera, anio_ingreso) VALUES
(1, '20123456-7', 'Ingeniería Civil Informática', 2020),
(2, '19876543-2', 'Ingeniería en Sistemas', 2019),
(3, '21234567-8', 'Ingeniería Industrial', 2021),
(4, '20456789-0', 'Ingeniería Civil Informática', 2020),
(5, '21567890-1', 'Ingeniería Comercial', 2021);

-- ============================================
-- 3. PROFESORES (detalles adicionales)
-- ============================================
INSERT INTO profesores (id, departamento, especialidad) VALUES
(10, 'Departamento de Informática', 'Ingeniería de Software'),
(11, 'Departamento de Informática', 'Bases de Datos y Sistemas Distribuidos'),
(12, 'Departamento de Ingeniería Industrial', 'Gestión de Proyectos');

-- ============================================
-- 4. EMPRESAS
-- ============================================
INSERT INTO empresas (id, nombre, rut, direccion, contacto_email) VALUES
(1, 'TechCorp SpA', '76123456-7', 'Av. Providencia 1234, Santiago', 'contacto@techcorp.cl'),
(2, 'Innovatech Ltda', '77234567-8', 'Av. Apoquindo 5678, Las Condes', 'rrhh@innovatech.cl'),
(3, 'DataSolutions SA', '78345678-9', 'Av. Vitacura 9012, Vitacura', 'practicas@datasolutions.cl'),
(4, 'CloudSystems Chile', '79456789-0', 'Av. El Bosque Norte 500, Las Condes', 'info@cloudsystems.cl'),
(5, 'Digital Partners', '80567890-1', 'Av. Andrés Bello 2711, Providencia', 'contacto@digitalpartners.cl');

-- ============================================
-- 5. JEFES DIRECTOS
-- ============================================
INSERT INTO jefes_directos (id, nombre, contacto) VALUES
(1, 'Roberto Campos', '+56912345678'),
(2, 'Carolina Vega', '+56923456789'),
(3, 'Andrés Morales', '+56934567890'),
(4, 'Daniela Reyes', '+56945678901'),
(5, 'Fernando Silva', '+56956789012'),
(6, 'Claudia Núñez', '+56967890123');

-- ============================================
-- 6. PRÁCTICAS
-- ============================================

-- Prácticas EN_CURSO
INSERT INTO practicas (
    id, fecha_inicio, fecha_termino, descripcion_actividades, 
    estudiante_id, profesor_id, empresa_id, jefe_directo_id, estado
) VALUES
(1, '2025-01-15', '2025-04-15', 
 'Desarrollo de aplicación web con Spring Boot y React. Implementación de módulos de gestión de usuarios, autenticación JWT y reportes. Trabajo en equipo ágil con metodología SCRUM, participación en daily meetings y sprints de 2 semanas.',
 1, 10, 1, 1, 'EN_CURSO'),

(2, '2025-02-01', '2025-05-01',
 'Análisis y diseño de base de datos PostgreSQL para sistema ERP. Optimización de consultas, creación de índices y procedimientos almacenados. Documentación técnica de arquitectura de datos.',
 2, 11, 2, 2, 'EN_CURSO');

-- Prácticas APROBADAS (próximas a iniciar)
INSERT INTO practicas (
    id, fecha_inicio, fecha_termino, descripcion_actividades,
    estudiante_id, profesor_id, empresa_id, jefe_directo_id, estado
) VALUES
(3, '2025-03-01', '2025-06-01',
 'Implementación de servicios REST con Node.js y Express. Integración con APIs externas, manejo de autenticación y autorización. Testing con Jest y Mocha.',
 3, 10, 3, 3, 'APROBADA'),

(4, '2025-03-15', '2025-06-15',
 'Desarrollo de dashboard analítico con Power BI y Python. Extracción de datos desde múltiples fuentes, transformación ETL y visualización de KPIs empresariales.',
 4, 12, 4, 4, 'APROBADA');

-- Práctica COMPLETADA
INSERT INTO practicas (
    id, fecha_inicio, fecha_termino, descripcion_actividades,
    estudiante_id, profesor_id, empresa_id, jefe_directo_id, estado
) VALUES
(5, '2024-09-01', '2024-12-01',
 'Desarrollo full-stack de sistema de gestión documental. Frontend con Angular 15, backend con Java Spring Boot, base de datos Oracle. Implementación de módulos de carga, búsqueda y versionamiento de documentos. Despliegue en ambiente productivo.',
 5, 11, 5, 5, 'COMPLETADA');

-- Práctica PENDIENTE (esperando aprobación)
INSERT INTO practicas (
    id, fecha_inicio, fecha_termino, descripcion_actividades,
    estudiante_id, profesor_id, empresa_id, jefe_directo_id, estado
) VALUES
(6, '2025-04-01', '2025-07-01',
 'Soporte técnico y mantención de infraestructura cloud en AWS. Monitoreo de servicios EC2, S3 y RDS. Automatización de despliegues con CI/CD usando Jenkins y Docker.',
 1, 10, 1, 1, 'PENDIENTE');

-- ============================================
-- 7. ACTUALIZAR SECUENCIAS
-- ============================================
-- Asegurar que las secuencias continúen desde el último ID insertado

SELECT setval('usuarios_id_seq', (SELECT MAX(id) FROM usuarios));
SELECT setval('empresas_id_seq', (SELECT MAX(id) FROM empresas));
SELECT setval('jefes_directos_id_seq', (SELECT MAX(id) FROM jefes_directos));
SELECT setval('practicas_id_seq', (SELECT MAX(id) FROM practicas));

-- ============================================
-- 8. VERIFICACIÓN DE DATOS
-- ============================================
-- Comentarios para verificar la inserción

-- Verificar conteos
-- SELECT COUNT(*) as total_usuarios FROM usuarios;
-- SELECT COUNT(*) as total_estudiantes FROM estudiantes;
-- SELECT COUNT(*) as total_profesores FROM profesores;
-- SELECT COUNT(*) as total_empresas FROM empresas;
-- SELECT COUNT(*) as total_jefes FROM jefes_directos;
-- SELECT COUNT(*) as total_practicas FROM practicas;

-- Verificar estados de prácticas
-- SELECT estado, COUNT(*) as cantidad FROM practicas GROUP BY estado ORDER BY estado;

-- ============================================
-- FIN DE MIGRACIÓN V4
-- ============================================

COMMENT ON TABLE usuarios IS 'Contiene 8 usuarios de prueba: 5 estudiantes y 3 profesores';
COMMENT ON TABLE estudiantes IS 'Contiene 5 estudiantes con datos completos de prueba';
COMMENT ON TABLE profesores IS 'Contiene 3 profesores con especialidades variadas';
COMMENT ON TABLE empresas IS 'Contiene 5 empresas del sector tecnológico';
COMMENT ON TABLE jefes_directos IS 'Contiene 6 jefes directos para supervisión de prácticas';
COMMENT ON TABLE practicas IS 'Contiene 6 prácticas de ejemplo en diferentes estados';
