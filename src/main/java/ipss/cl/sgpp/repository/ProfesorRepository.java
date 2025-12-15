package ipss.cl.sgpp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ipss.cl.sgpp.model.Profesor;

public interface ProfesorRepository extends JpaRepository<Profesor, Long> {

    // Método de búsqueda para el login o validación
    Profesor findByEmail(String email);
}