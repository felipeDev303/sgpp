# Sistema de Gestión de Prácticas Profesionales (SGPP)

## GUÍA DE IMPLEMENTACIÓN

**Proyecto:** Sistema de seguimiento de prácticas para colegio técnico  
**Stack Tecnológico:** Java 21, Spring Boot 3.58, Spring Data JPA, PostgreSQL  
**Versión:** 1.0  
**Fecha:** Diciembre 2025

---

## Tabla de Contenidos

1. [Dependencias Maven](#dependencias-maven)
2. [Entidades JPA](#entidades-jpa)
3. [DTOs (Data Transfer Objects)](#dtos)
4. [Repositorios](#repositorios)
5. [Servicios](#servicios)
6. [Controladores REST](#controladores-rest)
7. [Seguridad](#seguridad)
8. [Manejo de Errores](#manejo-de-errores)
9. [Testing](#testing)
10. [Configuración](#configuración)

---

## DEPENDENCIAS MAVEN

### pom.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0
         https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.2.0</version>
        <relativePath/>
    </parent>

    <groupId>cl.colegio</groupId>
    <artifactId>sgpp-backend</artifactId>
    <version>1.0.0</version>
    <name>Sistema de Gestión de Prácticas Profesionales</name>
    <description>Backend para gestión de prácticas en colegio técnico</description>

    <properties>
        <java.version>17</java.version>
        <maven.compiler.source>17</maven.compiler.source>
        <maven.compiler.target>17</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>

    <dependencies>
        <!-- Spring Boot Starters -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-security</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-validation</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>

        <!-- Base de Datos -->
        <dependency>
            <groupId>org.postgresql</groupId>
            <artifactId>postgresql</artifactId>
            <scope>runtime</scope>
        </dependency>

        <!-- Migraciones -->
        <dependency>
            <groupId>org.flywaydb</groupId>
            <artifactId>flyway-core</artifactId>
        </dependency>

        <dependency>
            <groupId>org.flywaydb</groupId>
            <artifactId>flyway-database-postgresql</artifactId>
        </dependency>

        <!-- Documentación API -->
        <dependency>
            <groupId>org.springdoc</groupId>
            <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
            <version>2.3.0</version>
        </dependency>

        <!-- JWT -->
        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt-api</artifactId>
            <version>0.12.3</version>
        </dependency>
        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt-impl</artifactId>
            <version>0.12.3</version>
            <scope>runtime</scope>
        </dependency>
        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt-jackson</artifactId>
            <version>0.12.3</version>
            <scope>runtime</scope>
        </dependency>

        <!-- Lombok -->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>

        <!-- Testing -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>

        <dependency>
            <groupId>org.springframework.security</groupId>
            <artifactId>spring-security-test</artifactId>
            <scope>test</scope>
        </dependency>

        <dependency>
            <groupId>com.h2database</groupId>
            <artifactId>h2</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>
```

---

## ENTIDADES JPA

### Usuario.java (Clase Base)

```java
package cl.colegio.sgpp.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "usuarios")
@Inheritance(strategy = InheritanceType.JOINED)
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String nombre;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 50)
    private String rol; // ROLE_ESTUDIANTE, ROLE_PROFESOR, ROLE_ADMIN

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
```

### Estudiante.java

```java
package cl.colegio.sgpp.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "estudiantes")
@PrimaryKeyJoinColumn(name = "usuario_id")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Estudiante extends Usuario {

    @Column(nullable = false, length = 100)
    private String carrera;

    @Column(name = "anio_ingreso", nullable = false)
    private Integer anioIngreso;

    @Column(nullable = false, unique = true, length = 12)
    private String rut;

    @OneToMany(mappedBy = "estudiante", cascade = CascadeType.ALL)
    private List<Practica> practicas = new ArrayList<>();
}
```

### Profesor.java

```java
package cl.colegio.sgpp.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "profesores")
@PrimaryKeyJoinColumn(name = "usuario_id")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Profesor extends Usuario {

    @Column(length = 100)
    private String departamento;

    @Column(length = 100)
    private String especialidad;

    @OneToMany(mappedBy = "profesorSupervisor")
    private List<Practica> practicasSupervisadas = new ArrayList<>();
}
```

### Empresa.java

```java
package cl.colegio.sgpp.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "empresas")
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Empresa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String nombre;

    @Column(nullable = false, unique = true, length = 12)
    private String rut;

    @Column(length = 20)
    private String telefono;

    @Column(length = 150)
    private String email;

    @Column(columnDefinition = "TEXT")
    private String direccion;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "empresa")
    private List<Practica> practicas = new ArrayList<>();
}
```

### Practica.java (Entidad Principal)

```java
package cl.colegio.sgpp.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "practicas")
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Practica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estudiante_id", nullable = false)
    private Estudiante estudiante;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profesor_supervisor_id")
    private Profesor profesorSupervisor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empresa_id", nullable = false)
    private Empresa empresa;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDateTime fechaInicio;

    @Column(name = "fecha_termino", nullable = false)
    private LocalDateTime fechaTermino;

    @Column(nullable = false, length = 50)
    private String estado = "PENDIENTE"; // PENDIENTE, APROBADA, RECHAZADA, COMPLETADA

    @Column(columnDefinition = "TEXT")
    private String observaciones;

    @Column
    private Integer calificacion; // 1-7

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // ============================================
    // LÓGICA DE NEGOCIO (Domain Logic)
    // ============================================

    /**
     * Aprueba la práctica si está en estado PENDIENTE
     */
    public void aprobar() {
        if (!"PENDIENTE".equals(this.estado)) {
            throw new IllegalStateException(
                "Solo se pueden aprobar prácticas en estado PENDIENTE. Estado actual: " + this.estado
            );
        }
        this.estado = "APROBADA";
    }

    /**
     * Rechaza la práctica si está en estado PENDIENTE
     */
    public void rechazar(String motivo) {
        if (!"PENDIENTE".equals(this.estado)) {
            throw new IllegalStateException(
                "Solo se pueden rechazar prácticas en estado PENDIENTE. Estado actual: " + this.estado
            );
        }
        this.estado = "RECHAZADA";
        this.observaciones = motivo;
    }

    /**
     * Marca la práctica como completada
     */
    public void completar(Integer calificacionFinal) {
        if (!"APROBADA".equals(this.estado)) {
            throw new IllegalStateException(
                "Solo se pueden completar prácticas en estado APROBADA. Estado actual: " + this.estado
            );
        }
        if (LocalDateTime.now().isBefore(this.fechaTermino)) {
            throw new IllegalStateException(
                "No se puede completar una práctica antes de su fecha de término"
            );
        }
        if (calificacionFinal < 1 || calificacionFinal > 7) {
            throw new IllegalArgumentException(
                "La calificación debe estar entre 1 y 7"
            );
        }
        this.estado = "COMPLETADA";
        this.calificacion = calificacionFinal;
    }

    /**
     * Valida que las fechas sean coherentes
     */
    @PrePersist
    @PreUpdate
    public void validarFechas() {
        if (fechaTermino != null && fechaInicio != null &&
            fechaTermino.isBefore(fechaInicio)) {
            throw new IllegalArgumentException(
                "La fecha de término no puede ser anterior a la fecha de inicio"
            );
        }
    }
}
```

---

## DTOs

### PracticaRequestDTO.java

```java
package cl.colegio.sgpp.model.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PracticaRequestDTO {

    @NotNull(message = "El ID del estudiante es obligatorio")
    @Positive(message = "El ID del estudiante debe ser positivo")
    private Long estudianteId;

    @Positive(message = "El ID del profesor debe ser positivo")
    private Long profesorSupervisorId;

    @NotNull(message = "El ID de la empresa es obligatorio")
    @Positive(message = "El ID de la empresa debe ser positivo")
    private Long empresaId;

    @NotNull(message = "La fecha de inicio es obligatoria")
    @FutureOrPresent(message = "La fecha de inicio no puede ser en el pasado")
    private LocalDateTime fechaInicio;

    @NotNull(message = "La fecha de término es obligatoria")
    @Future(message = "La fecha de término debe ser futura")
    private LocalDateTime fechaTermino;

    @Size(max = 500, message = "Las observaciones no pueden superar 500 caracteres")
    private String observaciones;

    @AssertTrue(message = "La fecha de término debe ser posterior a la fecha de inicio")
    public boolean isFechasValidas() {
        if (fechaInicio == null || fechaTermino == null) {
            return true;
        }
        return fechaTermino.isAfter(fechaInicio);
    }
}
```

### PracticaResponseDTO.java

```java
package cl.colegio.sgpp.model.dto.response;

import cl.colegio.sgpp.model.entity.Practica;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PracticaResponseDTO {

    private Long id;
    private String estudianteNombre;
    private String estudianteRut;
    private String profesorNombre;
    private String empresaNombre;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaTermino;
    private String estado;
    private String observaciones;
    private Integer calificacion;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static PracticaResponseDTO from(Practica practica) {
        return PracticaResponseDTO.builder()
            .id(practica.getId())
            .estudianteNombre(practica.getEstudiante().getNombre())
            .estudianteRut(practica.getEstudiante().getRut())
            .profesorNombre(practica.getProfesorSupervisor() != null ?
                practica.getProfesorSupervisor().getNombre() : null)
            .empresaNombre(practica.getEmpresa().getNombre())
            .fechaInicio(practica.getFechaInicio())
            .fechaTermino(practica.getFechaTermino())
            .estado(practica.getEstado())
            .observaciones(practica.getObservaciones())
            .calificacion(practica.getCalificacion())
            .createdAt(practica.getCreatedAt())
            .updatedAt(practica.getUpdatedAt())
            .build();
    }
}
```

### ErrorResponseDTO.java

```java
package cl.colegio.sgpp.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponseDTO {

    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private String path;
    private List<String> details;

    public static ErrorResponseDTO of(int status, String error, String message, String path) {
        return ErrorResponseDTO.builder()
            .timestamp(LocalDateTime.now())
            .status(status)
            .error(error)
            .message(message)
            .path(path)
            .build();
    }
}
```

---

## REPOSITORIOS

### PracticaRepository.java

```java
package cl.colegio.sgpp.repository;

import cl.colegio.sgpp.model.entity.Practica;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PracticaRepository extends JpaRepository<Practica, Long> {

    // Consultas por estudiante
    List<Practica> findByEstudianteId(Long estudianteId);
    Page<Practica> findByEstudianteId(Long estudianteId, Pageable pageable);

    // Consultas por estado
    List<Practica> findByEstado(String estado);
    Page<Practica> findByEstado(String estado, Pageable pageable);

    // Consultas por profesor
    List<Practica> findByProfesorSupervisorId(Long profesorId);
    Page<Practica> findByProfesorSupervisorId(Long profesorId, Pageable pageable);

    // Consultas por empresa
    List<Practica> findByEmpresaId(Long empresaId);

    // Consultas por rango de fechas
    @Query("SELECT p FROM Practica p WHERE p.fechaInicio >= :desde AND p.fechaTermino <= :hasta")
    List<Practica> findByRangoFechas(
        @Param("desde") LocalDateTime desde,
        @Param("hasta") LocalDateTime hasta
    );

    // Consulta con fetch join para evitar N+1
    @Query("SELECT p FROM Practica p " +
           "LEFT JOIN FETCH p.estudiante " +
           "LEFT JOIN FETCH p.profesorSupervisor " +
           "LEFT JOIN FETCH p.empresa " +
           "WHERE p.id = :id")
    Optional<Practica> findByIdWithDetails(@Param("id") Long id);

    // Contar prácticas por estado
    long countByEstado(String estado);

    // Verificar si estudiante tiene práctica activa
    @Query("SELECT COUNT(p) > 0 FROM Practica p " +
           "WHERE p.estudiante.id = :estudianteId " +
           "AND p.estado IN ('PENDIENTE', 'APROBADA')")
    boolean estudianteTienePracticaActiva(@Param("estudianteId") Long estudianteId);
}
```

### UsuarioRepository.java

```java
package cl.colegio.sgpp.repository;

import cl.colegio.sgpp.model.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
    boolean existsByEmail(String email);
}
```

---

## SERVICIOS

### PracticaService.java

```java
package cl.colegio.sgpp.service;

import cl.colegio.sgpp.exception.BusinessException;
import cl.colegio.sgpp.exception.ResourceNotFoundException;
import cl.colegio.sgpp.model.dto.request.PracticaRequestDTO;
import cl.colegio.sgpp.model.dto.response.PracticaResponseDTO;
import cl.colegio.sgpp.model.entity.Empresa;
import cl.colegio.sgpp.model.entity.Estudiante;
import cl.colegio.sgpp.model.entity.Practica;
import cl.colegio.sgpp.model.entity.Profesor;
import cl.colegio.sgpp.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PracticaService {

    private final PracticaRepository practicaRepository;
    private final EstudianteRepository estudianteRepository;
    private final ProfesorRepository profesorRepository;
    private final EmpresaRepository empresaRepository;

    @Transactional
    public PracticaResponseDTO registrarPractica(PracticaRequestDTO request) {
        log.info("Registrando nueva práctica para estudiante ID: {}", request.getEstudianteId());

        Estudiante estudiante = estudianteRepository.findById(request.getEstudianteId())
            .orElseThrow(() -> new ResourceNotFoundException(
                "Estudiante no encontrado con ID: " + request.getEstudianteId()
            ));

        if (practicaRepository.estudianteTienePracticaActiva(estudiante.getId())) {
            throw new BusinessException(
                "El estudiante ya tiene una práctica activa (PENDIENTE o APROBADA)"
            );
        }

        Empresa empresa = empresaRepository.findById(request.getEmpresaId())
            .orElseThrow(() -> new ResourceNotFoundException(
                "Empresa no encontrada con ID: " + request.getEmpresaId()
            ));

        Profesor profesor = null;
        if (request.getProfesorSupervisorId() != null) {
            profesor = profesorRepository.findById(request.getProfesorSupervisorId())
                .orElseThrow(() -> new ResourceNotFoundException(
                    "Profesor no encontrado con ID: " + request.getProfesorSupervisorId()
                ));
        }

        Practica practica = new Practica();
        practica.setEstudiante(estudiante);
        practica.setProfesorSupervisor(profesor);
        practica.setEmpresa(empresa);
        practica.setFechaInicio(request.getFechaInicio());
        practica.setFechaTermino(request.getFechaTermino());
        practica.setObservaciones(request.getObservaciones());
        practica.setEstado("PENDIENTE");

        Practica guardada = practicaRepository.save(practica);
        log.info("Práctica registrada exitosamente con ID: {}", guardada.getId());

        return PracticaResponseDTO.from(guardada);
    }

    @Transactional(readOnly = true)
    public PracticaResponseDTO obtenerPorId(Long id) {
        Practica practica = practicaRepository.findByIdWithDetails(id)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Práctica no encontrada con ID: " + id
            ));
        return PracticaResponseDTO.from(practica);
    }

    @Transactional(readOnly = true)
    public Page<PracticaResponseDTO> listarTodas(Pageable pageable) {
        return practicaRepository.findAll(pageable)
            .map(PracticaResponseDTO::from);
    }

    @Transactional(readOnly = true)
    public List<PracticaResponseDTO> listarPorEstudiante(Long estudianteId) {
        return practicaRepository.findByEstudianteId(estudianteId).stream()
            .map(PracticaResponseDTO::from)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<PracticaResponseDTO> listarPorEstado(String estado, Pageable pageable) {
        return practicaRepository.findByEstado(estado, pageable)
            .map(PracticaResponseDTO::from);
    }

    @Transactional
    public PracticaResponseDTO aprobarPractica(Long id) {
        log.info("Aprobando práctica ID: {}", id);

        Practica practica = practicaRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Práctica no encontrada con ID: " + id
            ));

        practica.aprobar();
        Practica actualizada = practicaRepository.save(practica);

        log.info("Práctica ID: {} aprobada exitosamente", id);
        return PracticaResponseDTO.from(actualizada);
    }

    @Transactional
    public PracticaResponseDTO rechazarPractica(Long id, String motivo) {
        log.info("Rechazando práctica ID: {}", id);

        Practica practica = practicaRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Práctica no encontrada con ID: " + id
            ));

        practica.rechazar(motivo);
        Practica actualizada = practicaRepository.save(practica);

        log.info("Práctica ID: {} rechazada", id);
        return PracticaResponseDTO.from(actualizada);
    }

    @Transactional
    public PracticaResponseDTO completarPractica(Long id, Integer calificacion) {
        log.info("Completando práctica ID: {} con calificación: {}", id, calificacion);

        Practica practica = practicaRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Práctica no encontrada con ID: " + id
            ));

        practica.completar(calificacion);
        Practica actualizada = practicaRepository.save(practica);

        log.info("Práctica ID: {} completada exitosamente", id);
        return PracticaResponseDTO.from(actualizada);
    }

    @Transactional
    public void eliminarPractica(Long id) {
        log.info("Eliminando práctica ID: {}", id);

        if (!practicaRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                "Práctica no encontrada con ID: " + id
            );
        }

        practicaRepository.deleteById(id);
        log.info("Práctica ID: {} eliminada exitosamente", id);
    }
}
```

---

## CONTROLADORES REST

### PracticaController.java

```java
package cl.colegio.sgpp.controller;

import cl.colegio.sgpp.model.dto.request.PracticaRequestDTO;
import cl.colegio.sgpp.model.dto.response.PracticaResponseDTO;
import cl.colegio.sgpp.service.PracticaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/practicas")
@RequiredArgsConstructor
@Tag(name = "Prácticas", description = "API para gestión de prácticas profesionales")
public class PracticaController {

    private final PracticaService practicaService;

    @Operation(summary = "Registrar nueva práctica")
    @PostMapping
    @PreAuthorize("hasAnyRole('ESTUDIANTE', 'PROFESOR', 'ADMIN')")
    public ResponseEntity<PracticaResponseDTO> registrar(
            @Valid @RequestBody PracticaRequestDTO request) {
        PracticaResponseDTO response = practicaService.registrarPractica(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Obtener práctica por ID")
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ESTUDIANTE', 'PROFESOR', 'ADMIN')")
    public ResponseEntity<PracticaResponseDTO> obtenerPorId(@PathVariable Long id) {
        PracticaResponseDTO response = practicaService.obtenerPorId(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Listar todas las prácticas (paginado)")
    @GetMapping
    @PreAuthorize("hasAnyRole('PROFESOR', 'ADMIN')")
    public ResponseEntity<Page<PracticaResponseDTO>> listarTodas(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String direction) {

        Sort sort = direction.equalsIgnoreCase("ASC") ?
            Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<PracticaResponseDTO> response = practicaService.listarTodas(pageable);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Listar prácticas de un estudiante")
    @GetMapping("/estudiante/{estudianteId}")
    @PreAuthorize("hasAnyRole('ESTUDIANTE', 'PROFESOR', 'ADMIN')")
    public ResponseEntity<List<PracticaResponseDTO>> listarPorEstudiante(
            @PathVariable Long estudianteId) {
        List<PracticaResponseDTO> response = practicaService.listarPorEstudiante(estudianteId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Aprobar práctica (solo profesores)")
    @PostMapping("/{id}/aprobar")
    @PreAuthorize("hasAnyRole('PROFESOR', 'ADMIN')")
    public ResponseEntity<PracticaResponseDTO> aprobar(@PathVariable Long id) {
        PracticaResponseDTO response = practicaService.aprobarPractica(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Rechazar práctica (solo profesores)")
    @PostMapping("/{id}/rechazar")
    @PreAuthorize("hasAnyRole('PROFESOR', 'ADMIN')")
    public ResponseEntity<PracticaResponseDTO> rechazar(
            @PathVariable Long id,
            @RequestParam String motivo) {
        PracticaResponseDTO response = practicaService.rechazarPractica(id, motivo);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Completar práctica con calificación")
    @PostMapping("/{id}/completar")
    @PreAuthorize("hasAnyRole('PROFESOR', 'ADMIN')")
    public ResponseEntity<PracticaResponseDTO> completar(
            @PathVariable Long id,
            @RequestParam Integer calificacion) {
        PracticaResponseDTO response = practicaService.completarPractica(id, calificacion);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Eliminar práctica (solo admin)")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        practicaService.eliminarPractica(id);
        return ResponseEntity.noContent().build();
    }
}
```

---

## SEGURIDAD

### SecurityConfig.java

```java
package cl.colegio.sgpp.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/**", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                .requestMatchers("/actuator/health").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/practicas").hasAnyRole("ESTUDIANTE", "PROFESOR", "ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/practicas/**").hasAnyRole("PROFESOR", "ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/practicas/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .httpBasic(basic -> {});

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}
```

### CustomUserDetailsService.java

```java
package cl.colegio.sgpp.security;

import cl.colegio.sgpp.model.entity.Usuario;
import cl.colegio.sgpp.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException(
                "Usuario no encontrado con email: " + email
            ));

        return User.builder()
            .username(usuario.getEmail())
            .password(usuario.getPassword())
            .authorities(Collections.singletonList(
                new SimpleGrantedAuthority(usuario.getRol())
            ))
            .build();
    }
}
```

---

## MANEJO DE ERRORES

### Excepciones Personalizadas

```java
package cl.colegio.sgpp.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
```

```java
package cl.colegio.sgpp.exception;

public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}
```

### GlobalExceptionHandler.java

```java
package cl.colegio.sgpp.exception;

import cl.colegio.sgpp.model.dto.response.ErrorResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleResourceNotFound(
            ResourceNotFoundException ex, HttpServletRequest request) {
        log.error("Recurso no encontrado: {}", ex.getMessage());
        ErrorResponseDTO error = ErrorResponseDTO.of(
            HttpStatus.NOT_FOUND.value(),
            "Not Found",
            ex.getMessage(),
            request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponseDTO> handleBusinessException(
            BusinessException ex, HttpServletRequest request) {
        log.error("Error de negocio: {}", ex.getMessage());
        ErrorResponseDTO error = ErrorResponseDTO.of(
            HttpStatus.BAD_REQUEST.value(),
            "Business Rule Violation",
            ex.getMessage(),
            request.getRequestURI()
        );
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidationErrors(
            MethodArgumentNotValidException ex, HttpServletRequest request) {
        List<String> errors = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(error -> error.getField() + ": " + error.getDefaultMessage())
            .collect(Collectors.toList());

        ErrorResponseDTO error = ErrorResponseDTO.builder()
            .timestamp(java.time.LocalDateTime.now())
            .status(HttpStatus.BAD_REQUEST.value())
            .error("Validation Error")
            .message("Error en la validación de los datos de entrada")
            .path(request.getRequestURI())
            .details(errors)
            .build();

        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGeneralException(
            Exception ex, HttpServletRequest request) {
        log.error("Error interno del servidor", ex);
        ErrorResponseDTO error = ErrorResponseDTO.of(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "Internal Server Error",
            "Ha ocurrido un error inesperado",
            request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
```

---

## TESTING

### PracticaServiceTest.java

```java
package cl.colegio.sgpp.service;

import cl.colegio.sgpp.exception.BusinessException;
import cl.colegio.sgpp.exception.ResourceNotFoundException;
import cl.colegio.sgpp.model.dto.request.PracticaRequestDTO;
import cl.colegio.sgpp.model.entity.*;
import cl.colegio.sgpp.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PracticaServiceTest {

    @Mock
    private PracticaRepository practicaRepository;

    @Mock
    private EstudianteRepository estudianteRepository;

    @Mock
    private EmpresaRepository empresaRepository;

    @InjectMocks
    private PracticaService practicaService;

    private Estudiante estudiante;
    private Empresa empresa;

    @BeforeEach
    void setUp() {
        estudiante = new Estudiante();
        estudiante.setId(1L);
        estudiante.setNombre("Juan Pérez");

        empresa = new Empresa();
        empresa.setId(1L);
        empresa.setNombre("TechCorp");
    }

    @Test
    void debeRegistrarPracticaExitosamente() {
        PracticaRequestDTO request = new PracticaRequestDTO();
        request.setEstudianteId(1L);
        request.setEmpresaId(1L);
        request.setFechaInicio(LocalDateTime.now().plusDays(1));
        request.setFechaTermino(LocalDateTime.now().plusMonths(3));

        when(estudianteRepository.findById(1L)).thenReturn(Optional.of(estudiante));
        when(empresaRepository.findById(1L)).thenReturn(Optional.of(empresa));
        when(practicaRepository.estudianteTienePracticaActiva(1L)).thenReturn(false);
        when(practicaRepository.save(any(Practica.class))).thenAnswer(i -> i.getArgument(0));

        var response = practicaService.registrarPractica(request);

        assertNotNull(response);
        verify(practicaRepository, times(1)).save(any(Practica.class));
    }

    @Test
    void debeLanzarExcepcionSiEstudianteNoExiste() {
        PracticaRequestDTO request = new PracticaRequestDTO();
        request.setEstudianteId(999L);

        when(estudianteRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            practicaService.registrarPractica(request);
        });
    }
}
```

---

## CONFIGURACIÓN

### application.properties

```properties
# Aplicación
spring.application.name=sgpp-backend
server.port=8080

# Base de Datos
spring.datasource.url=jdbc:postgresql://localhost:5432/sgpp_db
spring.datasource.username=postgres
spring.datasource.password=postgres
spring.datasource.driver-class-name=org.postgresql.Driver

# Pool de Conexiones HikariCP
spring.datasource.hikari.maximum-pool-size=20
spring.datasource.hikari.minimum-idle=5
spring.datasource.hikari.connection-timeout=30000

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.open-in-view=false

# Flyway
spring.flyway.enabled=true
spring.flyway.baseline-on-migrate=true
spring.flyway.locations=classpath:db/migration

# Security
spring.security.user.name=admin
spring.security.user.password=admin123

# Actuator
management.endpoints.web.exposure.include=health,info,metrics
management.endpoint.health.show-details=always

# Logging
logging.level.root=INFO
logging.level.cl.colegio.sgpp=DEBUG
logging.level.org.springframework.web=INFO

# Swagger
springdoc.api-docs.path=/v3/api-docs
springdoc.swagger-ui.path=/swagger-ui.html
```

### application-dev.properties

```properties
# H2 en memoria para desarrollo
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driver-class-name=org.h2.Driver
spring.h2.console.enabled=true
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true
```

### JpaAuditingConfig.java

```java
package cl.colegio.sgpp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class JpaAuditingConfig {

    @Bean
    public AuditorAware<String> auditorProvider() {
        return () -> {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth == null || !auth.isAuthenticated()) {
                return Optional.of("SYSTEM");
            }
            return Optional.of(auth.getName());
        };
    }
}
```

---

## Recursos Adicionales

- **Documentación API:** http://localhost:8080/swagger-ui.html
- **API Docs JSON:** http://localhost:8080/v3/api-docs
- **Health Check:** http://localhost:8080/actuator/health
- **Métricas:** http://localhost:8080/actuator/metrics

## Comandos Útiles

```bash
# Compilar proyecto
mvn clean install

# Ejecutar tests
mvn test

# Ejecutar aplicación
mvn spring-boot:run

# Ejecutar con perfil dev
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# Empaquetar JAR
mvn package

# Ejecutar JAR
java -jar target/sgpp-backend-1.0.0.jar
```

---

**Referencia:** Este documento complementa [INVESTIGACION-PLANIFICACION.MD](./INVESTIGACION-PLANIFICACION.MD)
