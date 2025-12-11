package ipss.cl.sgpp.service;

import ipss.cl.sgpp.dto.response.JefeDirectoResponseDTO;
import ipss.cl.sgpp.exception.ResourceNotFoundException;
import ipss.cl.sgpp.model.JefeDirecto;
import ipss.cl.sgpp.repository.JefeDirectoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio de consulta para jefes directos (supervisores de empresa).
 * 
 * Simplificado a operaciones de solo lectura (GET).
 * La problemática se enfoca en gestión de prácticas, no en CRUD completo de supervisores.
 * Los jefes directos se gestionan como parte de las prácticas.
 * 
 * Operaciones disponibles:
 * - Listar todos los jefes directos
 * - Buscar por ID
 * 
 * @author SGPP Team
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
public class JefeDirectoService {

    private final JefeDirectoRepository jefeDirectoRepository;
    
    /**
     * Obtiene todos los jefes directos registrados.
     * 
     * @return Lista de DTOs con los datos de todos los jefes directos
     */
    public List<JefeDirectoResponseDTO> obtenerTodosLosJefesDirectos() {
        return jefeDirectoRepository.findAll().stream()
            .map(JefeDirectoResponseDTO::from)
            .collect(Collectors.toList());
    }
    
    /**
     * Obtiene un jefe directo por su ID.
     * 
     * @param id ID del jefe directo
     * @return DTO con los datos del jefe directo
     * @throws ResourceNotFoundException si el jefe directo no existe
     */
    public JefeDirectoResponseDTO obtenerJefeDirectoPorId(Long id) {
        JefeDirecto jefeDirecto = jefeDirectoRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("JefeDirecto", "id", id));
        
        return JefeDirectoResponseDTO.from(jefeDirecto);
    }
}
     * 
     * @param id ID del jefe directo a actualizar
     * @param requestDTO Nuevos datos del jefe directo
     * @return DTO con los datos actualizados
     * @throws ResourceNotFoundException si el jefe directo no existe
     * @throws BusinessException si hay errores de validación
     */
    @Transactional
    public JefeDirectoResponseDTO actualizarJefeDirecto(Long id, JefeDirectoRequestDTO requestDTO) {
        // Verificar que el jefe directo existe
        JefeDirecto jefeDirectoExistente = jefeDirectoRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("JefeDirecto", "id", id));
        
        // Validaciones de negocio
        validarNombreNoVacio(requestDTO.getNombre());
        
        // Actualizar campos
        jefeDirectoExistente.setNombre(requestDTO.getNombre());
        jefeDirectoExistente.setContacto(requestDTO.getContacto());
        
        JefeDirecto jefeDirectoActualizado = jefeDirectoRepository.save(jefeDirectoExistente);
        return JefeDirectoResponseDTO.from(jefeDirectoActualizado);
    }
    
    /**
     * Elimina un jefe directo del sistema.
     * 
     * @param id ID del jefe directo a eliminar
     * @throws ResourceNotFoundException si el jefe directo no existe
     * @throws BusinessException si el jefe directo tiene prácticas supervisadas
     */
    @Transactional
    public void eliminarJefeDirecto(Long id) {
        JefeDirecto jefeDirecto = jefeDirectoRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("JefeDirecto", "id", id));
        
        // Validar que no tenga prácticas supervisadas
        if (jefeDirecto.getPracticasSupervisadas() != null && !jefeDirecto.getPracticasSupervisadas().isEmpty()) {
            throw new BusinessException(
                "JEFE_CON_PRACTICAS",
                String.format("No se puede eliminar el jefe directo con ID %d porque tiene %d práctica(s) supervisada(s). " +
                    "Primero debe reasignar o eliminar las prácticas asociadas.",
                    id, jefeDirecto.getPracticasSupervisadas().size())
            );
        }
        
        jefeDirectoRepository.deleteById(id);
    }
    
    /**
     * Valida que el nombre no esté vacío.
     * 
     * @param nombre Nombre a validar
     * @throws BusinessException si el nombre está vacío
     */
    private void validarNombreNoVacio(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new BusinessException(
                "NOMBRE_INVALIDO",
                "El nombre del jefe directo no puede estar vacío"
            );
        }
    }
}
