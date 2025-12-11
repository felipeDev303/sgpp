package ipss.cl.sgpp.service;

import ipss.cl.sgpp.dto.request.EmpresaRequestDTO;
import ipss.cl.sgpp.dto.response.EmpresaResponseDTO;
import ipss.cl.sgpp.exception.BusinessException;
import ipss.cl.sgpp.exception.ResourceNotFoundException;
import ipss.cl.sgpp.model.Empresa;
import ipss.cl.sgpp.repository.EmpresaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio para gestión de empresas.
 * Implementa operaciones CRUD con DTOs y validaciones de negocio.
 */
@Service
@RequiredArgsConstructor
public class EmpresaService {

    private final EmpresaRepository empresaRepository;
    
    /**
     * Crea una nueva empresa.
     * 
     * @param requestDTO Datos de la empresa a crear
     * @return DTO con los datos de la empresa creada
     * @throws BusinessException si hay errores de validación de negocio
     */
    @Transactional
    public EmpresaResponseDTO crearEmpresa(EmpresaRequestDTO requestDTO) {
        // Validaciones de negocio
        validarRutUnico(requestDTO.getRut(), null);
        validarNombreUnico(requestDTO.getNombre(), null);
        
        // Crear entidad desde DTO
        Empresa empresa = Empresa.builder()
            .nombre(requestDTO.getNombre())
            .rut(requestDTO.getRut())
            .direccion(requestDTO.getDireccion())
            .contactoEmail(requestDTO.getContactoEmail())
            .build();
        
        Empresa empresaGuardada = empresaRepository.save(empresa);
        return EmpresaResponseDTO.from(empresaGuardada);
    }
    
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
    
    /**
     * Actualiza los datos de una empresa existente.
     * 
     * @param id ID de la empresa a actualizar
     * @param requestDTO Nuevos datos de la empresa
     * @return DTO con los datos actualizados
     * @throws ResourceNotFoundException si la empresa no existe
     * @throws BusinessException si hay errores de validación
     */
    @Transactional
    public EmpresaResponseDTO actualizarEmpresa(Long id, EmpresaRequestDTO requestDTO) {
        // Verificar que la empresa existe
        Empresa empresaExistente = empresaRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Empresa", "id", id));
        
        // Validaciones de negocio
        validarRutUnico(requestDTO.getRut(), id);
        validarNombreUnico(requestDTO.getNombre(), id);
        
        // Actualizar campos
        empresaExistente.setNombre(requestDTO.getNombre());
        empresaExistente.setRut(requestDTO.getRut());
        empresaExistente.setDireccion(requestDTO.getDireccion());
        empresaExistente.setContactoEmail(requestDTO.getContactoEmail());
        
        Empresa empresaActualizada = empresaRepository.save(empresaExistente);
        return EmpresaResponseDTO.from(empresaActualizada);
    }
    
    /**
     * Elimina una empresa del sistema.
     * 
     * @param id ID de la empresa a eliminar
     * @throws ResourceNotFoundException si la empresa no existe
     * @throws BusinessException si la empresa tiene prácticas asociadas
     */
    @Transactional
    public void eliminarEmpresa(Long id) {
        Empresa empresa = empresaRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Empresa", "id", id));
        
        // Validar que no tenga prácticas asociadas
        if (empresa.getPracticas() != null && !empresa.getPracticas().isEmpty()) {
            throw new BusinessException(
                "EMPRESA_CON_PRACTICAS",
                String.format("No se puede eliminar la empresa con ID %d porque tiene %d práctica(s) asociada(s). " +
                    "Primero debe reasignar o eliminar las prácticas asociadas.",
                    id, empresa.getPracticas().size())
            );
        }
        
        empresaRepository.deleteById(id);
    }
    
    /**
     * Valida que el RUT sea único en el sistema.
     * 
     * @param rut RUT a validar
     * @param empresaIdExcluir ID de la empresa a excluir (para actualizaciones)
     * @throws BusinessException si el RUT ya está registrado
     */
    private void validarRutUnico(String rut, Long empresaIdExcluir) {
        Empresa empresaExistente = empresaRepository.findByRut(rut);
        
        if (empresaExistente != null) {
            // Si estamos actualizando, verificar que no sea otra empresa
            if (empresaIdExcluir == null || !empresaExistente.getId().equals(empresaIdExcluir)) {
                throw new BusinessException(
                    "RUT_DUPLICADO",
                    String.format("El RUT '%s' ya está registrado en el sistema", rut)
                );
            }
        }
    }
    
    /**
     * Valida que el nombre sea único en el sistema.
     * 
     * @param nombre Nombre a validar
     * @param empresaIdExcluir ID de la empresa a excluir (para actualizaciones)
     * @throws BusinessException si el nombre ya está registrado
     */
    private void validarNombreUnico(String nombre, Long empresaIdExcluir) {
        Empresa empresaExistente = empresaRepository.findByNombreIgnoreCase(nombre);
        
        if (empresaExistente != null) {
            // Si estamos actualizando, verificar que no sea otra empresa
            if (empresaIdExcluir == null || !empresaExistente.getId().equals(empresaIdExcluir)) {
                throw new BusinessException(
                    "NOMBRE_DUPLICADO",
                    String.format("El nombre '%s' ya está registrado en el sistema", nombre)
                );
            }
        }
    }
}
