package ipss.cl.sgpp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ipss.cl.sgpp.model.Practica;

public interface PracticaRepository extends JpaRepository<Practica, Long> {
    
    // ========== Métodos con Soft Delete (excluyen eliminadas) ==========
    
    /**
     * Busca una práctica activa (no eliminada) por su ID.
     * 
     * @param id ID de la práctica
     * @return Optional con la práctica si existe y no está eliminada
     */
    @Query("SELECT p FROM Practica p WHERE p.id = :id AND p.deleted = false")
    Optional<Practica> findByIdAndNotDeleted(@Param("id") Long id);
    
    /**
     * Obtiene todas las prácticas activas (no eliminadas).
     * 
     * @return Lista de prácticas no eliminadas
     */
    @Query("SELECT p FROM Practica p WHERE p.deleted = false")
    List<Practica> findAllActive();
    
    /**
     * Busca todas las prácticas activas de un estudiante específico.
     * Los estudiantes solo deben ver sus propias prácticas no eliminadas.
     * 
     * @param estudianteId ID del estudiante
     * @return Lista de prácticas del estudiante que no están eliminadas
     */
    @Query("SELECT p FROM Practica p WHERE p.estudiante.id = :estudianteId AND p.deleted = false")
    List<Practica> findByEstudianteIdAndNotDeleted(@Param("estudianteId") Long estudianteId);
    
    /**
     * Busca todas las prácticas activas supervisadas por un profesor.
     * 
     * @param profesorId ID del profesor
     * @return Lista de prácticas supervisadas que no están eliminadas
     */
    @Query("SELECT p FROM Practica p WHERE p.profesor.id = :profesorId AND p.deleted = false")
    List<Practica> findByProfesorIdAndNotDeleted(@Param("profesorId") Long profesorId);
    
    // ========== Métodos legacy (mantener compatibilidad) ==========
    
    /**
     * Busca todas las prácticas de un estudiante (incluye eliminadas).
     * Método legacy - se recomienda usar findByEstudianteIdAndNotDeleted.
     * 
     * @param estudianteId ID del estudiante
     * @return Lista de todas las prácticas del estudiante
     */
    List<Practica> findByEstudianteId(Long estudianteId);
    
    // ========== Métodos de auditoría ==========
    
    /**
     * Cuenta las prácticas eliminadas de un estudiante.
     * Útil para auditoría y reportes.
     * 
     * @param estudianteId ID del estudiante
     * @return Cantidad de prácticas eliminadas
     */
    @Query("SELECT COUNT(p) FROM Practica p WHERE p.estudiante.id = :estudianteId AND p.deleted = true")
    long countDeletedByEstudianteId(@Param("estudianteId") Long estudianteId);
    
    /**
     * Obtiene todas las prácticas eliminadas (para auditoría).
     * 
     * @return Lista de prácticas eliminadas lógicamente
     */
    @Query("SELECT p FROM Practica p WHERE p.deleted = true ORDER BY p.deletedAt DESC")
    List<Practica> findAllDeleted();
}