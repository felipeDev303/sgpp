package ipss.cl.sgpp.service;

import ipss.cl.sgpp.dto.response.EmpresaResponseDTO;
import ipss.cl.sgpp.exception.ResourceNotFoundException;
import ipss.cl.sgpp.model.Empresa;
import ipss.cl.sgpp.repository.EmpresaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio de consulta para empresas.
 * Operaciones de solo lectura para uso en prácticas profesionales.
 */
@Service
@RequiredArgsConstructor
public class EmpresaService {

    private final EmpresaRepository empresaRepository;
    
    /**
     * Obtiene todas las empresas registradas.
     * 
     * @return Lista de DTOs con los datos de todas las empresas
     */
    public List<EmpresaResponseDTO> obtenerTodasLasEmpresas() {
        return empresaRepository.findAll().stream()
            .map(EmpresaResponseDTO::from)
            .collect(Collectors.toList());
    }
    
    /**
     * Obtiene una empresa por su ID.
     * 
     * @param id ID de la empresa
     * @return DTO con los datos de la empresa
     * @throws ResourceNotFoundException si la empresa no existe
     */
    public EmpresaResponseDTO obtenerEmpresaPorId(Long id) {
        Empresa empresa = empresaRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Empresa", "id", id));
        
        return EmpresaResponseDTO.from(empresa);
    }
    
    /**
     * Busca una empresa por su RUT.
     * 
     * @param rut RUT de la empresa a buscar
     * @return DTO con los datos de la empresa
     * @throws ResourceNotFoundException si la empresa no existe
     */
    public EmpresaResponseDTO obtenerEmpresaPorRut(String rut) {
        Empresa empresa = empresaRepository.findByRut(rut);
        
        if (empresa == null) {
            throw new ResourceNotFoundException("Empresa", "rut", rut);
        }
        
        return EmpresaResponseDTO.from(empresa);
    }
    
    /**
     * Busca una empresa por su nombre.
     * 
     * @param nombre Nombre de la empresa a buscar
     * @return DTO con los datos de la empresa
     * @throws ResourceNotFoundException si la empresa no existe
     */
    public EmpresaResponseDTO obtenerEmpresaPorNombre(String nombre) {
        Empresa empresa = empresaRepository.findByNombreIgnoreCase(nombre);
        
        if (empresa == null) {
            throw new ResourceNotFoundException("Empresa", "nombre", nombre);
        }
        
        return EmpresaResponseDTO.from(empresa);
    }
}
