CREATE TABLE usuarios (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    nombre VARCHAR(255) NOT NULL,
    rol VARCHAR(50) NOT NULL
    -- Se recomienda añadir aquí los campos de auditoría (creado_en, actualizado_por, etc.)
);

-- 2. Tabla para Estudiantes (Hereda de Usuario)
-- La clave primaria (id) es también una clave foránea a la tabla 'usuarios'
CREATE TABLE estudiantes (
    id BIGINT PRIMARY KEY REFERENCES usuarios(id),
    rut VARCHAR(12) NOT NULL UNIQUE,
    carrera VARCHAR(255),
    anio_ingreso INTEGER
);

-- 3. Tabla para Profesores (Hereda de Usuario)
-- La clave primaria (id) es también una clave foránea a la tabla 'usuarios'
CREATE TABLE profesores (
    id BIGINT PRIMARY KEY REFERENCES usuarios(id),
    departamento VARCHAR(255),
    especialidad VARCHAR(255)
);
-- 4. Tabla para Empresas
CREATE TABLE empresas (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    rut VARCHAR(15) NOT NULL UNIQUE,
    direccion VARCHAR(255),
    contacto_email VARCHAR(255)
);