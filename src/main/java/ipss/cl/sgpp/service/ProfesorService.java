package ipss.cl.sgpp.service;

import ipss.cl.sgpp.dto.request.ProfesorRequestDTO;
import ipss.cl.sgpp.dto.response.ProfesorResponseDTO;
import ipss.cl.sgpp.exception.BusinessException;
import ipss.cl.sgpp.exception.ResourceNotFoundException;
import ipss.cl.sgpp.model.Profesor;
import ipss.cl.sgpp.model.Rol;
import ipss.cl.sgpp.repository.ProfesorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio para gestión de profesores.
 * Implementa operaciones CRUD con DTOs y validaciones de negocio.
 */
@Service
@RequiredArgsConstructor
public class ProfesorService {

    private final ProfesorRepository profesorRepository;
    
    /**
     * Crea un nuevo profesor.
     * 
     * @param requestDTO Datos del profesor a crear
     * @return DTO con los datos del profesor creado
     * @throws BusinessException si hay errores de validación de negocio
     */
    @Transactional
    public ProfesorResponseDTO crearProfesor(ProfesorRequestDTO requestDTO) {
        // Validaciones de negocio
        validarEmailUnico(requestDTO.getEmail(), null);
        validarPassword(requestDTO.getPassword());
        validarDepartamento(requestDTO.getDepartamento());
        
        // Crear entidad desde DTO
        Profesor profesor = Profesor.builder()
            .nombreCompleto(requestDTO.getNombreCompleto())
            .email(requestDTO.getEmail())
            .password(requestDTO.getPassword()) // En producción: encriptar con BCrypt
            .rol(Rol.PROFESOR)
            .departamento(requestDTO.getDepartamento())
            .especialidad(requestDTO.getEspecialidad())
            .build();
        
        Profesor profesorGuardado = profesorRepository.save(profesor);
        return ProfesorResponseDTO.from(profesorGuardado);
    }
    
    /**
     * Obtiene todos los profesores registrados.
     * 
     * @return Lista de DTOs con los datos de todos los profesores
     */
    public List<ProfesorResponseDTO> obtenerTodosLosProfesores() {
        return profesorRepository.findAll().stream()
            .map(ProfesorResponseDTO::from)
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
        
        return ProfesorResponseDTO.from(profesor);
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
        
        return ProfesorResponseDTO.from(profesor);
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
    
    /**
     * Actualiza los datos de un profesor existente.
     * 
     * @param id ID del profesor a actualizar
     * @param requestDTO Nuevos datos del profesor
     * @return DTO con los datos actualizados
     * @throws ResourceNotFoundException si el profesor no existe
     * @throws BusinessException si hay errores de validación
     */
    @Transactional
    public ProfesorResponseDTO actualizarProfesor(Long id, ProfesorRequestDTO requestDTO) {
        // Verificar que el profesor existe
        Profesor profesorExistente = profesorRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Profesor", "id", id));
        
        // Validaciones de negocio
        validarEmailUnico(requestDTO.getEmail(), id);
        validarDepartamento(requestDTO.getDepartamento());
        
        // Actualizar campos
        profesorExistente.setNombreCompleto(requestDTO.getNombreCompleto());
        profesorExistente.setEmail(requestDTO.getEmail());
        profesorExistente.setDepartamento(requestDTO.getDepartamento());
        profesorExistente.setEspecialidad(requestDTO.getEspecialidad());
        
        // Actualizar password solo si se proporciona uno nuevo
        if (requestDTO.getPassword() != null && !requestDTO.getPassword().isBlank()) {
            validarPassword(requestDTO.getPassword());
            profesorExistente.setPassword(requestDTO.getPassword()); // En producción: encriptar
        }
        
        Profesor profesorActualizado = profesorRepository.save(profesorExistente);
        return ProfesorResponseDTO.from(profesorActualizado);
    }
    
    /**
     * Elimina un profesor del sistema.
     * 
     * @param id ID del profesor a eliminar
     * @throws ResourceNotFoundException si el profesor no existe
     * @throws BusinessException si el profesor tiene prácticas asociadas
     */
    @Transactional
    public void eliminarProfesor(Long id) {
        Profesor profesor = profesorRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Profesor", "id", id));
        
        // Validar que no tenga prácticas supervisadas activas
        if (profesor.getPracticasSupervisadas() != null && !profesor.getPracticasSupervisadas().isEmpty()) {
            throw new BusinessException(
                "PROFESOR_CON_PRACTICAS",
                String.format("No se puede eliminar el profesor con ID %d porque tiene %d práctica(s) supervisada(s). " +
                    "Primero debe reasignar o eliminar las prácticas asociadas.",
                    id, profesor.getPracticasSupervisadas().size())
            );
        }
        
        profesorRepository.deleteById(id);
    }
    
    /**
     * Valida que el email sea único en el sistema.
     * 
     * @param email Email a validar
     * @param profesorIdExcluir ID del profesor a excluir (para actualizaciones)
     * @throws BusinessException si el email ya está registrado
     */
    private void validarEmailUnico(String email, Long profesorIdExcluir) {
        Profesor profesorExistente = profesorRepository.findByEmail(email);
        
        if (profesorExistente != null) {
            // Si estamos actualizando, verificar que no sea otro profesor
            if (profesorIdExcluir == null || !profesorExistente.getId().equals(profesorIdExcluir)) {
                throw new BusinessException(
                    "EMAIL_DUPLICADO",
                    String.format("El email '%s' ya está registrado en el sistema", email)
                );
            }
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
    
    /**
     * Valida que el departamento no esté vacío si se proporciona.
     * 
     * @param departamento Departamento a validar
     * @throws BusinessException si el departamento es inválido
     */
    private void validarDepartamento(String departamento) {
        if (departamento != null && departamento.trim().isEmpty()) {
            throw new BusinessException(
                "DEPARTAMENTO_INVALIDO",
                "El departamento no puede estar vacío. Si no tiene departamento, omita el campo."
            );
        }
    }
}
