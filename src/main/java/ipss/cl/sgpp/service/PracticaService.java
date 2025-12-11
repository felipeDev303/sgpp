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
        
        // Validación de negocio: verificar que el estudiante no tenga prácticas activas superpuestas
        validarPracticasSuperpuestas(estudiante.getId(), requestDTO.getFechaInicio(), requestDTO.getFechaTermino(), null);
        
        // Validación de negocio: fechas
        if (requestDTO.getFechaTermino().isBefore(requestDTO.getFechaInicio())) {
            throw new BusinessException("FECHAS_INVALIDAS", "La fecha de término debe ser posterior a la fecha de inicio");
        }
        
        // Crear entidad desde DTO
        Practica practica = Practica.builder()
            .estudiante(estudiante)
            .profesor(profesor)
            .empresa(empresa)
            .jefeDirecto(jefeDirecto)
            .fechaInicio(requestDTO.getFechaInicio())
            .fechaTermino(requestDTO.getFechaTermino())
            .descripcionActividades(requestDTO.getDescripcionActividades())
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
        
        // Validación de negocio: verificar que no haya prácticas superpuestas (excluyendo la actual)
        validarPracticasSuperpuestas(estudiante.getId(), requestDTO.getFechaInicio(), requestDTO.getFechaTermino(), id);
        
        // Validación de negocio: fechas
        if (requestDTO.getFechaTermino().isBefore(requestDTO.getFechaInicio())) {
            throw new BusinessException("FECHAS_INVALIDAS", "La fecha de término debe ser posterior a la fecha de inicio");
        }
        
        // Actualizar campos
        practicaExistente.setEstudiante(estudiante);
        practicaExistente.setProfesor(profesor);
        practicaExistente.setEmpresa(empresa);
        practicaExistente.setJefeDirecto(jefeDirecto);
        practicaExistente.setFechaInicio(requestDTO.getFechaInicio());
        practicaExistente.setFechaTermino(requestDTO.getFechaTermino());
        practicaExistente.setDescripcionActividades(requestDTO.getDescripcionActividades());
        
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
            boolean seSuperpone = !(fechaTermino.isBefore(practica.getFechaInicio()) || 
                                    fechaInicio.isAfter(practica.getFechaTermino()));
            
            if (seSuperpone) {
                throw new BusinessException(
                    "PRACTICAS_SUPERPUESTAS", 
                    String.format("El estudiante ya tiene una práctica en el período %s - %s", 
                        practica.getFechaInicio(), practica.getFechaTermino())
                );
            }
        }
    }
}