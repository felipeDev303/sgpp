package ipss.cl.sgpp.service;

import ipss.cl.sgpp.dto.request.PracticaRequestDTO;
import ipss.cl.sgpp.dto.response.PracticaResponseDTO;
import ipss.cl.sgpp.exception.BusinessException;
import ipss.cl.sgpp.exception.ResourceNotFoundException;
import ipss.cl.sgpp.model.*;
import ipss.cl.sgpp.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PracticaService {

    private final PracticaRepository practicaRepository;
    private final EstudianteRepository estudianteRepository;
    private final ProfesorRepository profesorRepository;
    private final EmpresaRepository empresaRepository;
    private final JefeDirectoRepository jefeDirectoRepository; 
    
    @Transactional
    public PracticaResponseDTO guardarPractica(PracticaRequestDTO requestDTO) {
        // Validar que todas las entidades relacionadas existan
        Estudiante estudiante = estudianteRepository.findById(requestDTO.getEstudianteId())
            .orElseThrow(() -> new ResourceNotFoundException("Estudiante", "id", requestDTO.getEstudianteId()));
        
        Profesor profesor = profesorRepository.findById(requestDTO.getProfesorId())
            .orElseThrow(() -> new ResourceNotFoundException("Profesor", "id", requestDTO.getProfesorId()));
        
        Empresa empresa = empresaRepository.findById(requestDTO.getEmpresaId())
            .orElseThrow(() -> new ResourceNotFoundException("Empresa", "id", requestDTO.getEmpresaId()));
        
        JefeDirecto jefeDirecto = jefeDirectoRepository.findById(requestDTO.getJefeDirectoId())
            .orElseThrow(() -> new ResourceNotFoundException("JefeDirecto", "id", requestDTO.getJefeDirectoId()));
        
        // Validaciones de negocio
        validarFechas(requestDTO.getFechaInicio(), requestDTO.getFechaTermino());
        validarDuracionPractica(requestDTO.getFechaInicio(), requestDTO.getFechaTermino());
        validarPracticasSuperpuestas(estudiante.getId(), requestDTO.getFechaInicio(), requestDTO.getFechaTermino(), null);
        validarDescripcionActividades(requestDTO.getDescripcionActividades());
        
        // Determinar estado inicial (por defecto PENDIENTE si no se especifica)
        EstadoPractica estadoInicial = requestDTO.getEstado() != null ? 
            requestDTO.getEstado() : EstadoPractica.PENDIENTE;
        
        // Crear entidad desde DTO
        Practica practica = Practica.builder()
            .estudiante(estudiante)
            .profesor(profesor)
            .empresa(empresa)
            .jefeDirecto(jefeDirecto)
            .fechaInicio(requestDTO.getFechaInicio())
            .fechaTermino(requestDTO.getFechaTermino())
            .descripcionActividades(requestDTO.getDescripcionActividades())
            .estado(estadoInicial)
            .build();
        
        Practica practicaGuardada = practicaRepository.save(practica);
        return PracticaResponseDTO.from(practicaGuardada);
    }

    public List<PracticaResponseDTO> obtenerTodasLasPracticas() {
        return practicaRepository.findAll().stream()
            .map(PracticaResponseDTO::from)
            .collect(Collectors.toList());
    }
    
    public List<PracticaResponseDTO> obtenerPracticasPorEstudiante(Long estudianteId) {
        // Validar que el estudiante exista
        if (!estudianteRepository.existsById(estudianteId)) {
            throw new ResourceNotFoundException("Estudiante", "id", estudianteId);
        }
        
        return practicaRepository.findByEstudianteId(estudianteId).stream()
            .map(PracticaResponseDTO::from)
            .collect(Collectors.toList());
    }
    
    public PracticaResponseDTO obtenerPracticaPorId(Long id) {
        Practica practica = practicaRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Practica", "id", id));
        
        return PracticaResponseDTO.from(practica);
    }

    @Transactional
    public PracticaResponseDTO actualizarPractica(Long id, PracticaRequestDTO requestDTO) {
        // Verificar que la práctica existe
        Practica practicaExistente = practicaRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Practica", "id", id));
        
        // Validar que todas las entidades relacionadas existan
        Estudiante estudiante = estudianteRepository.findById(requestDTO.getEstudianteId())
            .orElseThrow(() -> new ResourceNotFoundException("Estudiante", "id", requestDTO.getEstudianteId()));
        
        Profesor profesor = profesorRepository.findById(requestDTO.getProfesorId())
            .orElseThrow(() -> new ResourceNotFoundException("Profesor", "id", requestDTO.getProfesorId()));
        
        Empresa empresa = empresaRepository.findById(requestDTO.getEmpresaId())
            .orElseThrow(() -> new ResourceNotFoundException("Empresa", "id", requestDTO.getEmpresaId()));
        
        JefeDirecto jefeDirecto = jefeDirectoRepository.findById(requestDTO.getJefeDirectoId())
            .orElseThrow(() -> new ResourceNotFoundException("JefeDirecto", "id", requestDTO.getJefeDirectoId()));
        
        // Validaciones de negocio
        validarFechas(requestDTO.getFechaInicio(), requestDTO.getFechaTermino());
        validarDuracionPractica(requestDTO.getFechaInicio(), requestDTO.getFechaTermino());
        validarPracticasSuperpuestas(estudiante.getId(), requestDTO.getFechaInicio(), requestDTO.getFechaTermino(), id);
        validarDescripcionActividades(requestDTO.getDescripcionActividades());
        validarCambioEstudiante(practicaExistente.getEstudiante().getId(), estudiante.getId());
        validarPuedeSerModificada(practicaExistente);
        
        // Actualizar campos
        practicaExistente.setEstudiante(estudiante);
        practicaExistente.setProfesor(profesor);
        practicaExistente.setEmpresa(empresa);
        practicaExistente.setJefeDirecto(jefeDirecto);
        practicaExistente.setFechaInicio(requestDTO.getFechaInicio());
        practicaExistente.setFechaTermino(requestDTO.getFechaTermino());
        practicaExistente.setDescripcionActividades(requestDTO.getDescripcionActividades());
        
        // Actualizar estado si se proporciona y es válido
        if (requestDTO.getEstado() != null && requestDTO.getEstado() != practicaExistente.getEstado()) {
            validarCambioEstado(practicaExistente, requestDTO.getEstado());
        }
        
        Practica practicaActualizada = practicaRepository.save(practicaExistente);
        return PracticaResponseDTO.from(practicaActualizada);
    }

    @Transactional
    public void eliminarPractica(Long id) {
        if (!practicaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Practica", "id", id);
        }
        practicaRepository.deleteById(id);
    }
    
    /**
     * Cambia el estado de una práctica existente.
     * 
     * @param id ID de la práctica
     * @param nuevoEstado Nuevo estado a asignar
     * @return DTO con los datos actualizados de la práctica
     * @throws ResourceNotFoundException si la práctica no existe
     * @throws BusinessException si el cambio de estado no es válido
     */
    @Transactional
    public PracticaResponseDTO cambiarEstadoPractica(Long id, EstadoPractica nuevoEstado) {
        Practica practica = practicaRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Practica", "id", id));
        
        validarCambioEstado(practica, nuevoEstado);
        
        Practica practicaActualizada = practicaRepository.save(practica);
        return PracticaResponseDTO.from(practicaActualizada);
    }
    
    /**
     * Valida que la fecha de término sea posterior a la fecha de inicio.
     * 
     * @param fechaInicio Fecha de inicio de la práctica
     * @param fechaTermino Fecha de término de la práctica
     * @throws BusinessException si la fecha de término es anterior o igual a la de inicio
     */
    private void validarFechas(java.time.LocalDate fechaInicio, java.time.LocalDate fechaTermino) {
        if (fechaTermino.isBefore(fechaInicio)) {
            throw new BusinessException(
                "FECHAS_INVALIDAS", 
                "La fecha de término debe ser posterior a la fecha de inicio"
            );
        }
        
        if (fechaTermino.isEqual(fechaInicio)) {
            throw new BusinessException(
                "FECHAS_INVALIDAS", 
                "La fecha de término no puede ser igual a la fecha de inicio"
            );
        }
    }
    
    /**
     * Valida que la duración de la práctica esté dentro de los límites permitidos.
     * Mínimo: 4 semanas (28 días)
     * Máximo: 6 meses (180 días)
     * 
     * @param fechaInicio Fecha de inicio de la práctica
     * @param fechaTermino Fecha de término de la práctica
     * @throws BusinessException si la duración no está dentro de los límites
     */
    private void validarDuracionPractica(java.time.LocalDate fechaInicio, java.time.LocalDate fechaTermino) {
        long diasDuracion = java.time.temporal.ChronoUnit.DAYS.between(fechaInicio, fechaTermino);
        
        final int DURACION_MINIMA_DIAS = 28; // 4 semanas
        final int DURACION_MAXIMA_DIAS = 180; // ~6 meses
        
        if (diasDuracion < DURACION_MINIMA_DIAS) {
            throw new BusinessException(
                "DURACION_INVALIDA", 
                String.format("La práctica debe tener una duración mínima de %d días (4 semanas). Duración actual: %d días", 
                    DURACION_MINIMA_DIAS, diasDuracion)
            );
        }
        
        if (diasDuracion > DURACION_MAXIMA_DIAS) {
            throw new BusinessException(
                "DURACION_INVALIDA", 
                String.format("La práctica no puede superar los %d días (6 meses). Duración actual: %d días", 
                    DURACION_MAXIMA_DIAS, diasDuracion)
            );
        }
    }
    
    /**
     * Valida que la descripción de actividades cumpla con los requisitos mínimos.
     * 
     * @param descripcion Descripción de las actividades de la práctica
     * @throws BusinessException si la descripción no cumple los requisitos
     */
    private void validarDescripcionActividades(String descripcion) {
        if (descripcion == null || descripcion.trim().isEmpty()) {
            throw new BusinessException(
                "DESCRIPCION_INVALIDA", 
                "La descripción de actividades es obligatoria"
            );
        }
        
        final int LONGITUD_MINIMA = 50;
        final int LONGITUD_MAXIMA = 5000;
        
        int longitud = descripcion.trim().length();
        
        if (longitud < LONGITUD_MINIMA) {
            throw new BusinessException(
                "DESCRIPCION_INVALIDA", 
                String.format("La descripción debe tener al menos %d caracteres. Longitud actual: %d caracteres", 
                    LONGITUD_MINIMA, longitud)
            );
        }
        
        if (longitud > LONGITUD_MAXIMA) {
            throw new BusinessException(
                "DESCRIPCION_INVALIDA", 
                String.format("La descripción no puede superar los %d caracteres. Longitud actual: %d caracteres", 
                    LONGITUD_MAXIMA, longitud)
            );
        }
    }
    
    /**
     * Valida que no se cambie el estudiante asignado a una práctica existente.
     * 
     * @param estudianteIdOriginal ID del estudiante original
     * @param estudianteIdNuevo ID del nuevo estudiante
     * @throws BusinessException si se intenta cambiar el estudiante
     */
    private void validarCambioEstudiante(Long estudianteIdOriginal, Long estudianteIdNuevo) {
        if (!estudianteIdOriginal.equals(estudianteIdNuevo)) {
            throw new BusinessException(
                "CAMBIO_ESTUDIANTE_NO_PERMITIDO", 
                "No se permite cambiar el estudiante asignado a una práctica existente"
            );
        }
    }
    
    /**
     * Valida que no existan prácticas superpuestas para el mismo estudiante.
     * 
     * @param estudianteId ID del estudiante
     * @param fechaInicio Fecha de inicio de la práctica
     * @param fechaTermino Fecha de término de la práctica
     * @param practicaIdExcluir ID de la práctica a excluir (para actualización), null para creación
     * @throws BusinessException si hay prácticas superpuestas
     */
    private void validarPracticasSuperpuestas(Long estudianteId, 
                                              java.time.LocalDate fechaInicio, 
                                              java.time.LocalDate fechaTermino, 
                                              Long practicaIdExcluir) {
        List<Practica> practicasEstudiante = practicaRepository.findByEstudianteId(estudianteId);
        
        for (Practica practica : practicasEstudiante) {
            // Excluir la práctica actual si estamos actualizando
            if (practicaIdExcluir != null && practica.getId().equals(practicaIdExcluir)) {
                continue;
            }
            
            // Verificar superposición de fechas
            // Dos períodos se superponen si: inicio1 <= fin2 AND inicio2 <= fin1
            boolean seSuperpone = !(fechaTermino.isBefore(practica.getFechaInicio()) || 
                                    fechaInicio.isAfter(practica.getFechaTermino()));
            
            if (seSuperpone) {
                throw new BusinessException(
                    "PRACTICAS_SUPERPUESTAS", 
                    String.format("El estudiante ya tiene una práctica registrada en el período del %s al %s que se superpone con las fechas solicitadas", 
                        practica.getFechaInicio(), practica.getFechaTermino())
                );
            }
        }
    }
    
    /**
     * Valida que una práctica pueda ser modificada según su estado actual.
     * 
     * @param practica La práctica a validar
     * @throws BusinessException si la práctica no puede ser modificada
     */
    private void validarPuedeSerModificada(Practica practica) {
        if (!practica.puedeSerModificada()) {
            throw new BusinessException(
                "PRACTICA_NO_MODIFICABLE",
                String.format("La práctica en estado %s no puede ser modificada", 
                    practica.getEstado().getDescripcion())
            );
        }
    }
    
    /**
     * Valida y ejecuta un cambio de estado en la práctica.
     * 
     * @param practica La práctica cuyo estado se va a cambiar
     * @param nuevoEstado El nuevo estado solicitado
     * @throws BusinessException si el cambio de estado no es válido
     */
    private void validarCambioEstado(Practica practica, EstadoPractica nuevoEstado) {
        if (nuevoEstado == null) {
            throw new BusinessException(
                "ESTADO_INVALIDO",
                "El nuevo estado no puede ser nulo"
            );
        }
        
        EstadoPractica estadoActual = practica.getEstado();
        
        if (estadoActual == nuevoEstado) {
            return; // No hay cambio de estado
        }
        
        // Intentar cambiar el estado usando el método de la entidad
        boolean cambioExitoso = practica.cambiarEstado(nuevoEstado);
        
        if (!cambioExitoso) {
            throw new BusinessException(
                "TRANSICION_ESTADO_INVALIDA",
                String.format("No se puede cambiar el estado de la práctica de %s a %s. Transición no permitida.",
                    estadoActual.getDescripcion(),
                    nuevoEstado.getDescripcion())
            );
        }
    }
}