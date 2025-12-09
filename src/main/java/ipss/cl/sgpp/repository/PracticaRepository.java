package ipss.cl.sgpp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ipss.cl.sgpp.model.Practica;

public interface PracticaRepository extends JpaRepository<Practica, Long> {
    
    // Requisito: Los estudiantes solo deben ver sus propias prácticas.
    // Creamos un método para buscar todas las prácticas de un estudiante específico.
    List<Practica> findByEstudianteId(Long estudianteId);

    // Requisito: Los profesores deben ver todas las prácticas (o las que supervisan).
    // El método findAll() de JpaRepository nos da todas las prácticas, que es lo que 
    // usaremos para los profesores (si tienen el permiso).
}