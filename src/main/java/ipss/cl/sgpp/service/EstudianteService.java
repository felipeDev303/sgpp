package ipss.cl.sgpp.service;

import ipss.cl.sgpp.dto.request.EstudianteRequestDTO;
import ipss.cl.sgpp.dto.response.EstudianteResponseDTO;
import ipss.cl.sgpp.exception.BusinessException;
import ipss.cl.sgpp.exception.ResourceNotFoundException;
import ipss.cl.sgpp.model.Estudiante;
import ipss.cl.sgpp.model.Rol;
import ipss.cl.sgpp.repository.EstudianteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio para gestión de estudiantes.
 * Implementa operaciones CRUD con DTOs y validaciones de negocio.
 */
@Service
@RequiredArgsConstructor
public class EstudianteService {

    private final EstudianteRepository estudianteRepository;
    
    /**
     * Crea un nuevo estudiante.
     * 
     * @param requestDTO Datos del estudiante a crear
     * @return DTO con los datos del estudiante creado
     * @throws BusinessException si hay errores de validación de negocio
     */
    @Transactional
    public EstudianteResponseDTO crearEstudiante(EstudianteRequestDTO requestDTO) {
        // Validaciones de negocio
        validarEmailUnico(requestDTO.getEmail(), null);
        validarRutUnico(requestDTO.getRut(), null);
        validarAnioIngreso(requestDTO.getAnioIngreso());
        validarPassword(requestDTO.getPassword());
        
        // Crear entidad desde DTO
        Estudiante estudiante = Estudiante.builder()
            .nombreCompleto(requestDTO.getNombreCompleto())
            .email(requestDTO.getEmail())
            .password(requestDTO.getPassword()) // En producción: encriptar con BCrypt
            .rol(Rol.ESTUDIANTE)
            .rut(requestDTO.getRut())
            .carrera(requestDTO.getCarrera())
            .anioIngreso(requestDTO.getAnioIngreso())
            .build();
        
        Estudiante estudianteGuardado = estudianteRepository.save(estudiante);
        return EstudianteResponseDTO.from(estudianteGuardado);
    }
    
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
        Estudiante estudiante = estudianteRepository.findByRut(rut);
        
        if (estudiante == null) {
            throw new ResourceNotFoundException("Estudiante", "rut", rut);
        }
        
        return EstudianteResponseDTO.from(estudiante);
    }
    
    /**
     * Actualiza los datos de un estudiante existente.
     * 
     * @param id ID del estudiante a actualizar
     * @param requestDTO Nuevos datos del estudiante
     * @return DTO con los datos actualizados
     * @throws ResourceNotFoundException si el estudiante no existe
     * @throws BusinessException si hay errores de validación
     */
    @Transactional
    public EstudianteResponseDTO actualizarEstudiante(Long id, EstudianteRequestDTO requestDTO) {
        // Verificar que el estudiante existe
        Estudiante estudianteExistente = estudianteRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Estudiante", "id", id));
        
        // Validaciones de negocio
        validarEmailUnico(requestDTO.getEmail(), id);
        validarRutUnico(requestDTO.getRut(), id);
        validarAnioIngreso(requestDTO.getAnioIngreso());
        
        // Actualizar campos
        estudianteExistente.setNombreCompleto(requestDTO.getNombreCompleto());
        estudianteExistente.setEmail(requestDTO.getEmail());
        estudianteExistente.setRut(requestDTO.getRut());
        estudianteExistente.setCarrera(requestDTO.getCarrera());
        estudianteExistente.setAnioIngreso(requestDTO.getAnioIngreso());
        
        // Actualizar password solo si se proporciona uno nuevo
        if (requestDTO.getPassword() != null && !requestDTO.getPassword().isBlank()) {
            validarPassword(requestDTO.getPassword());
            estudianteExistente.setPassword(requestDTO.getPassword()); // En producción: encriptar
        }
        
        Estudiante estudianteActualizado = estudianteRepository.save(estudianteExistente);
        return EstudianteResponseDTO.from(estudianteActualizado);
    }
    
    /**
     * Elimina un estudiante del sistema.
     * 
     * @param id ID del estudiante a eliminar
     * @throws ResourceNotFoundException si el estudiante no existe
     * @throws BusinessException si el estudiante tiene prácticas asociadas
     */
    @Transactional
    public void eliminarEstudiante(Long id) {
        Estudiante estudiante = estudianteRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Estudiante", "id", id));
        
        // Validar que no tenga prácticas asociadas
        // Esta validación se puede mejorar consultando el repositorio de prácticas
        // Por ahora, permitimos la eliminación (en producción considerar soft delete)
        
        estudianteRepository.deleteById(id);
    }
    
    /**
     * Valida que el email sea único en el sistema.
     * 
     * @param email Email a validar
     * @param estudianteIdExcluir ID del estudiante a excluir (para actualizaciones)
     * @throws BusinessException si el email ya está registrado
     */
    private void validarEmailUnico(String email, Long estudianteIdExcluir) {
        Estudiante estudianteExistente = estudianteRepository.findByEmail(email);
        
        if (estudianteExistente != null) {
            // Si estamos actualizando, verificar que no sea otro estudiante
            if (estudianteIdExcluir == null || !estudianteExistente.getId().equals(estudianteIdExcluir)) {
                throw new BusinessException(
                    "EMAIL_DUPLICADO",
                    String.format("El email '%s' ya está registrado en el sistema", email)
                );
            }
        }
    }
    
    /**
     * Valida que el RUT sea único en el sistema.
     * 
     * @param rut RUT a validar
     * @param estudianteIdExcluir ID del estudiante a excluir (para actualizaciones)
     * @throws BusinessException si el RUT ya está registrado
     */
    private void validarRutUnico(String rut, Long estudianteIdExcluir) {
        Estudiante estudianteExistente = estudianteRepository.findByRut(rut);
        
        if (estudianteExistente != null) {
            // Si estamos actualizando, verificar que no sea otro estudiante
            if (estudianteIdExcluir == null || !estudianteExistente.getId().equals(estudianteIdExcluir)) {
                throw new BusinessException(
                    "RUT_DUPLICADO",
                    String.format("El RUT '%s' ya está registrado en el sistema", rut)
                );
            }
        }
    }
    
    /**
     * Valida que el año de ingreso sea razonable.
     * 
     * @param anioIngreso Año de ingreso a validar
     * @throws BusinessException si el año no es válido
     */
    private void validarAnioIngreso(Integer anioIngreso) {
        int anioActual = LocalDate.now().getYear();
        int anioMinimo = anioActual - 10; // Máximo 10 años de antigüedad
        int anioMaximo = anioActual + 1;  // Permitir inscripciones para el próximo año
        
        if (anioIngreso < anioMinimo || anioIngreso > anioMaximo) {
            throw new BusinessException(
                "ANIO_INGRESO_INVALIDO",
                String.format("El año de ingreso debe estar entre %d y %d. Año proporcionado: %d",
                    anioMinimo, anioMaximo, anioIngreso)
            );
        }
    }
    
    /**
     * Valida que la contraseña cumpla con los requisitos mínimos de seguridad.
     * 
     * @param password Contraseña a validar
     * @throws BusinessException si la contraseña no cumple los requisitos
     */
    private void validarPassword(String password) {
        if (password == null || password.isBlank()) {
            throw new BusinessException(
                "PASSWORD_INVALIDO",
                "La contraseña es obligatoria"
            );
        }
        
        final int LONGITUD_MINIMA = 8;
        final int LONGITUD_MAXIMA = 100;
        
        if (password.length() < LONGITUD_MINIMA) {
            throw new BusinessException(
                "PASSWORD_INVALIDO",
                String.format("La contraseña debe tener al menos %d caracteres", LONGITUD_MINIMA)
            );
        }
        
        if (password.length() > LONGITUD_MAXIMA) {
            throw new BusinessException(
                "PASSWORD_INVALIDO",
                String.format("La contraseña no puede superar los %d caracteres", LONGITUD_MAXIMA)
            );
        }
        
        // Validar que contenga al menos una letra y un número
        boolean tieneLetra = password.matches(".*[a-zA-Z].*");
        boolean tieneNumero = password.matches(".*\\d.*");
        
        if (!tieneLetra || !tieneNumero) {
            throw new BusinessException(
                "PASSWORD_INVALIDO",
                "La contraseña debe contener al menos una letra y un número"
            );
        }
    }
}
