package ipss.cl.sgpp.service;

import ipss.cl.sgpp.dto.response.EstudianteResponseDTO;
import ipss.cl.sgpp.exception.ResourceNotFoundException;
import ipss.cl.sgpp.model.Estudiante;
import ipss.cl.sgpp.repository.EstudianteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio de consulta para estudiantes.
 * 
 * Simplificado a operaciones de solo lectura (GET).
 * La problemática se enfoca en gestión de prácticas, no en CRUD completo de usuarios.
 * Los estudiantes se gestionan externamente (sistema académico, admisión, etc.).
 * 
 * Operaciones disponibles:
 * - Listar todos los estudiantes
 * - Buscar por ID
 * - Buscar por email
 * - Buscar por RUT
 * 
 * @author SGPP Team
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
public class EstudianteService {

    private final EstudianteRepository estudianteRepository;
    
    /**
     * Obtiene todos los estudiantes registrados.
     * 
     * @return Lista de DTOs con los datos de todos los estudiantes
     */
    public List<EstudianteResponseDTO> obtenerTodosLosEstudiantes() {
        return estudianteRepository.findAll().stream()
            .map(EstudianteResponseDTO::from)
            .collect(Collectors.toList());
    }
    
    /**
     * Obtiene un estudiante por su ID.
     * 
     * @param id ID del estudiante
     * @return DTO con los datos del estudiante
     * @throws ResourceNotFoundException si el estudiante no existe
     */
    public EstudianteResponseDTO obtenerEstudiantePorId(Long id) {
        Estudiante estudiante = estudianteRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Estudiante", "id", id));
        
        return EstudianteResponseDTO.from(estudiante);
    }
    
    /**
     * Busca un estudiante por su email.
     * 
     * @param email Email del estudiante
     * @return DTO con los datos del estudiante
     * @throws ResourceNotFoundException si el estudiante no existe
     */
    public EstudianteResponseDTO obtenerEstudiantePorEmail(String email) {
        Estudiante estudiante = estudianteRepository.findByEmail(email);
        
        if (estudiante == null) {
            throw new ResourceNotFoundException("Estudiante", "email", email);
        }
        
        return EstudianteResponseDTO.from(estudiante);
    }
    
    /**
     * Busca un estudiante por su RUT.
     * 
     * @param rut RUT del estudiante
     * @return DTO con los datos del estudiante
     * @throws ResourceNotFoundException si el estudiante no existe
     */
    public EstudianteResponseDTO obtenerEstudiantePorRut(String rut) {
        // TODO: Implementar método findByRut en estudianteRepository
        // Estudiante estudiante = estudianteRepository.findByRut(rut);
        Estudiante estudiante = null; // Temporal hasta implementar findByRut
        
        if (estudiante == null) {
            throw new ResourceNotFoundException("Estudiante", "rut", rut);
        }
        
        return EstudianteResponseDTO.from(estudiante);
    }
}
