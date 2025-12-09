package ipss.cl.sgpp.repository;

import ipss.cl.sgpp.model.Estudiante;
import org.springframework.data.jpa.repository.JpaRepository;

// La interfaz hereda de JpaRepository. 
// Le pasamos la Entidad (Estudiante) y el tipo de su ID (Long).
public interface EstudianteRepository extends JpaRepository<Estudiante, Long> {
    
    // Spring te permite definir métodos personalizados
    // basados en la convención de nombres.
    // Ejemplo: Buscar un estudiante por su email.
    Estudiante findByEmail(String email);
}