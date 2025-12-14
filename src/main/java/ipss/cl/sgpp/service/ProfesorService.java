package ipss.cl.sgpp.service;

import ipss.cl.sgpp.dto.response.ProfesorResponseDTO;
import ipss.cl.sgpp.exception.ResourceNotFoundException;
import ipss.cl.sgpp.model.Profesor;
import ipss.cl.sgpp.repository.ProfesorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio de consulta para profesores.
 * Operaciones de solo lectura para uso en prácticas profesionales.
 */
@Service
@RequiredArgsConstructor
public class ProfesorService {

    private final ProfesorRepository profesorRepository;
    
    /**
     * Obtiene todos los profesores registrados.
     * 
     * @return Lista de DTOs con los datos de todos los profesores
     */
    public List<ProfesorResponseDTO> obtenerTodosLosProfesores() {
        return profesorRepository.findAll().stream()
            .map(ProfesorResponseDTO::fromWithoutPracticas)
            .collect(Collectors.toList());
    }
    
    /**
     * Obtiene un profesor por su ID.
     * 
     * @param id ID del profesor
     * @return DTO con los datos del profesor
     * @throws ResourceNotFoundException si el profesor no existe
     */
    public ProfesorResponseDTO obtenerProfesorPorId(Long id) {
        Profesor profesor = profesorRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Profesor", "id", id));
        
        return ProfesorResponseDTO.fromWithoutPracticas(profesor);
    }
    
    /**
     * Busca un profesor por su email.
     * 
     * @param email Email del profesor a buscar
     * @return DTO con los datos del profesor
     * @throws ResourceNotFoundException si el profesor no existe
     */
    public ProfesorResponseDTO obtenerProfesorPorEmail(String email) {
        Profesor profesor = profesorRepository.findByEmail(email);
        
        if (profesor == null) {
            throw new ResourceNotFoundException("Profesor", "email", email);
        }
        
        return ProfesorResponseDTO.fromWithoutPracticas(profesor);
    }
    
    /**
     * Obtiene todos los profesores de un departamento específico.
     * 
     * @param departamento Nombre del departamento
     * @return Lista de DTOs con los profesores del departamento
     */
    public List<ProfesorResponseDTO> obtenerProfesoresPorDepartamento(String departamento) {
        return profesorRepository.findAll().stream()
            .filter(profesor -> departamento.equalsIgnoreCase(profesor.getDepartamento()))
            .map(ProfesorResponseDTO::from)
            .collect(Collectors.toList());
    }
}
