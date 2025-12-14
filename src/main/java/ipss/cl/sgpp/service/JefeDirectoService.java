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
 * Servicio de consulta para jefes directos.
 * Operaciones de solo lectura para uso en prácticas profesionales.
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
            .map(JefeDirectoResponseDTO::fromWithoutPracticas)
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
        
        return JefeDirectoResponseDTO.fromWithoutPracticas(jefeDirecto);
    }
    
    /**
     * Busca jefes directos cuyo nombre contenga el texto especificado.
     * 
     * @param nombre Texto a buscar en el nombre
     * @return Lista de jefes directos que coinciden
     */
    public List<JefeDirectoResponseDTO> buscarJefesDirectosPorNombre(String nombre) {
        return jefeDirectoRepository.findAll().stream()
            .filter(jefe -> jefe.getNombre().toLowerCase().contains(nombre.toLowerCase()))
            .map(JefeDirectoResponseDTO::from)
            .collect(Collectors.toList());
    }
}
