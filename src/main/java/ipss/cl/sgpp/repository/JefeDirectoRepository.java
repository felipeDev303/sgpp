package ipss.cl.sgpp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ipss.cl.sgpp.model.JefeDirecto;
import java.util.List;

public interface JefeDirectoRepository extends JpaRepository<JefeDirecto, Long> {
    
    /**
     * Busca jefes directos por nombre (contiene, ignorando mayúsculas).
     * @param nombre Nombre o parte del nombre a buscar
     * @return Lista de jefes directos encontrados
     */
    List<JefeDirecto> findByNombreContainingIgnoreCase(String nombre);
}