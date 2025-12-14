# Plan de Testing Manual - SGPP

## 1. Preparación del Entorno

### 1.1 Pre-requisitos

- [x] Verificar que la base de datos esté corriendo
- [ ] Verificar que todas las migraciones de Flyway se ejecuten correctamente
- [ ] Revisar application.properties para confirmar configuración
- [ ] Levantar la aplicación Spring Boot
- [ ] Acceder a Swagger UI en http://localhost:8080/swagger-ui/index.html

### 1.2 Herramientas Recomendadas

- Postman para pruebas de API
- Navegador para Swagger UI
- Cliente de base de datos para verificar datos

## 2. Testing por Módulo

### 2.1 Módulo: Empresa

**Crear Empresa (POST /api/empresas)**

    ```json
    {
    "rut": "12345678-9",
    "nombre": "Empresa Test S.A.",
    "direccion": "Av. Principal 123",
    "telefono": "+56912345678",
    "email": "contacto@empresatest.cl"
    }```

- [ ] Crear empresa válida
- [ ] Intentar crear con RUT duplicado
- [ ] Validar email inválido
- [ ] Validar campos obligatorios vacíos
- [ ] Validar formato de RUT chileno

**Listar Empresas (GET /api/empresas)**

- [ ] Obtener lista completa
- [ ] Verificar paginación (si aplica)

**Obtener Empresa por ID (GET /api/empresas/{id})**

- [ ] Obtener empresa existente
- [ ] Intentar obtener empresa inexistente (404)

**Actualizar Empresa (PUT /api/empresas/{id})**

- [ ] Actualizar datos válidos
- [ ] Intentar actualizar con RUT duplicado
- [ ] Actualizar empresa inexistente

**Eliminar Empresa (DELETE /api/empresas/{id})**

- [ ] Eliminar empresa sin prácticas asociadas
- [ ] Intentar eliminar empresa con prácticas asociadas

**2.2 Módulo: Profesor**

**Crear Profesor (POST /api/profesores)**

`````json
    {
"rut": "98765432-1",
"nombre": "Juan",
"apellido": "Pérez",
"email": "juan.perez@universidad.cl",
"telefono": "+56987654321",
"especialidad": "Ingeniería de Software"
}
```

- [ ] Crear profesor válido
- [ ] RUT duplicado
- [ ] Email duplicado
- [ ] Validar formato de email

**CRUD Completo**
- [ ] Listar profesores (GET)
- [ ] Obtener por ID (GET)
- [ ] Actualizar (PUT)
- [ ] Eliminar (DELETE)

**2.3 Módulo: Estudiante**

**Crear Estudiante (POST /api/estudiantes)**

```json
{
  "rut": "11223344-5",
  "nombre": "María",
  "apellido": "González",
  "email": "maria.gonzalez@alumno.cl",
  "telefono": "+56911223344",
  "carrera": "Ingeniería Civil Informática",
  "semestreActual": 8
}```

- [ ] Crear estudiante válido
- [ ] Validar semestre (rango válido)
- [ ] RUT y email únicos
- [ ] Campos obligatorios

**CRUD Completo**
- [ ] Listar estudiantes (GET)
- [ ] Obtener por ID (GET)
- [ ] Actualizar (PUT)
- [ ] Eliminar (DELETE)
---

**2.4 Módulo: Jefe Directo**

**Crear Jefe Directo (POST /api/jefes-directos)**

```json
{
  "rut": "55667788-9",
  "nombre": "Carlos",
  "apellido": "Ramírez",
  "email": "carlos.ramirez@empresa.cl",
  "telefono": "+56955667788",
  "cargo": "Gerente de Proyectos",
  "empresaId": 1
}```

- [ ] Crear jefe directo válido
- [ ] Asociar a empresa existente
- [ ] Intentar asociar a empresa inexistente
- [ ] Validaciones de unicidad

**CRUD Completo**

- [ ] Listar jefes directos (GET)
- [ ] Filtrar por empresa (si aplica)
- [ ] Obtener por ID (GET)
- [ ] Actualizar (PUT)
- [ ] Eliminar (DELETE)

**2.5 Módulo: Práctica**

**Crear Práctica (POST /api/estudiantes/{estudianteId}/practicas)**

```json
{
  "empresaId": 1,
  "profesorId": 1,
  "jefeDirectoId": 1,
  "fechaInicio": "2025-01-15",
  "fechaTermino": "2025-06-15",
  "descripcion": "Desarrollo de sistema web",
  "objetivos": "Aplicar conocimientos de Spring Boot"
}
```

- [ ] Crear práctica válida
- [ ] Validar fechas coherentes (inicio < término)
- [ ] Validar que estudiante no tenga práctica activa
- [ ] Validar existencia de empresa, profesor y jefe directo
- [ ] Estado inicial debe ser "PENDIENTE"

**Cambiar Estado de Práctica (PATCH /api/practicas/{id}/estado)**

```json
{
  "nuevoEstado": "APROBADA",
  "comentario": "Práctica aprobada por cumplir requisitos"
}
```

- [ ] PENDIENTE → APROBADA
- [ ] PENDIENTE → RECHAZADA
- [ ] APROBADA → EN_CURSO
- [ ] EN_CURSO → FINALIZADA
- [ ] Intentar transiciones inválidas
- [ ] Verificar que el comentario se guarde

**Listar Prácticas**
- [ ] Listar todas las prácticas (GET /api/practicas)
- [ ] Filtrar por estudiante (GET /api/estudiantes/{id}/practicas)
- [ ] Filtrar por profesor (GET /api/profesores/{id}/practicas)
- [ ] Filtrar por estado

**Actualizar y Eliminar**
- [ ] Actualizar práctica (PUT)
- [ ] Soft delete de práctica (DELETE)
- [ ] Verificar que no se elimine físicamente

---

**3. Testing de Validaciones**

**3.1 Validaciones de Negocio**

- [ ] Estudiante no puede tener múltiples prácticas simultáneas en estado APROBADA o EN_CURSO
- [ ] Fecha de inicio debe ser anterior a fecha de término
- [ ] No se puede cambiar estado de práctica eliminada
- [ ] Profesor no puede supervisar más de X prácticas simultáneas (si aplica)

**3.2 Validaciones de Datos**
- [ ] RUT chileno válido (formato XX.XXX.XXX-X)
- [ ] Email válido
- [ ] Teléfono con formato chileno
- [ ] Fechas en formato ISO (YYYY-MM-DD)
- [ ] Campos numéricos dentro de rangos válidos

**4.1 Flujo: Registro de Nueva Práctica**
- [ ] Crear empresa
- [ ] Crear jefe directo asociado a empresa
- [ ] Crear profesor
- [ ] Crear estudiante
- [ ] Crear práctica asociando todos los anteriores
- [ ] Verificar estado inicial PENDIENTE
- [ ] Aprobar práctica
- [ ] Cambiar estado a EN_CURSO
- [ ] Finalizar práctica
- [ ] Verificar campos de auditoría (createdAt, updatedAt)

**4.2 Flujo: Gestión de Estudiante con Múltiples Prácticas**
- [ ] Crear estudiante
- [ ] Crear primera práctica (PENDIENTE)
- [ ] Crear segunda práctica (debe permitir mientras la primera esté PENDIENTE)
- [ ] Aprobar primera práctica
- [ ] Intentar aprobar segunda (debe fallar si hay restricción)
- [ ] Finalizar primera práctica
- [ ] Aprobar segunda práctica

---

**5. Testing de Manejo de Errores**

**5.1 Errores 400 (Bad Request)**

- [ ] Enviar JSON malformado
- [ ] Campos requeridos faltantes
- [ ] Tipos de datos incorrectos
- [ ] Validaciones de negocio fallidas

**5.2 Errores 404 (Not Found)**

- [ ] Acceder a recurso inexistente
- [ ] ID inválido o negativo

**5.3 Errores 409 (Conflict)**

- [ ] RUT duplicado
- [ ] Email duplicado
- [ ] Estado de práctica conflictivo

**5.4 Errores 500 (Internal Server Error)**

- [ ] Verificar logs cuando ocurran
- [ ] No deben ocurrir con datos válidos

---

**6. Testing de Auditoría**

- [ ] Verificar campo createdAt al crear
- [ ] Verificar campo updatedAt al actualizar
- [ ] Verificar que campos de auditoría no sean modificables por usuario
- [ ] Soft delete: verificar campo deletedAt

---

7. Checklist Final

**Funcionalidad**
- [ ] Todos los endpoints responden correctamente
- [ ] Validaciones funcionan según lo esperado
- [ ] Flujos de negocio completos funcionan
- [ ] Manejo de errores es apropiado

**Datos**
- [ ] Migraciones crean estructura correcta
- [ ] Datos iniciales se cargan (V4)
- [ ] Integridad referencial se mantiene
- [ ] Soft delete funciona correctamente

**Documentación**
- [ ] Swagger UI muestra todos los endpoints
- [ ] Documentación de API es clara
- [ ] Ejemplos en Swagger son correctos

**Performance (básico)**
- [ ] Endpoints responden en tiempo razonable
- [ ] No hay consultas N+1 evidentes
- [ ] Paginación funciona (si aplica)

---

**8. Formato de Reporte de Defectos**
Para cada error encontrado, documentar:

Nota: Usa Postman Collections para guardar todas las pruebas y poder repetirlas fácilmente después de cambios en el código.

````markdown
ID: BUG-XXX
Módulo: [Empresa/Estudiante/Práctica/etc]
Severidad: [Alta/Media/Baja]
Endpoint: [POST /api/...]
Descripción: [Qué pasó]
Pasos para reproducir:
1. ...
2. ...
Resultado esperado: [...]
Resultado actual: [...]
Logs/Screenshots: [...]
`````
