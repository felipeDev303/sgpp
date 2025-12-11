package ipss.cl.sgpp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ipss.cl.sgpp.model.Empresa;

public interface EmpresaRepository extends JpaRepository<Empresa, Long> {
    
    /**
     * Busca una empresa por su RUT.
     * @param rut RUT de la empresa
     * @return Empresa encontrada o null
     */
    Empresa findByRut(String rut);
    
    /**
     * Busca una empresa por su nombre (ignorando mayúsculas/minúsculas).
     * @param nombre Nombre de la empresa
     * @return Empresa encontrada o null
     */
    Empresa findByNombreIgnoreCase(String nombre);
}
