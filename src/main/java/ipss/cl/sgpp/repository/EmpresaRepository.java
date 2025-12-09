package ipss.cl.sgpp.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import ipss.cl.sgpp.model.Empresa;
public interface EmpresaRepository extends JpaRepository<Empresa, Long> {}
