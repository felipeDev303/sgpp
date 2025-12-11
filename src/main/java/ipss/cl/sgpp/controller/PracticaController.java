package ipss.cl.sgpp.controller;

import ipss.cl.sgpp.dto.request.CambioEstadoRequestDTO;
import ipss.cl.sgpp.dto.request.PracticaRequestDTO;
import ipss.cl.sgpp.dto.response.PracticaResponseDTO;
import ipss.cl.sgpp.service.PracticaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST legacy para gestión de prácticas.
 * 
 * @deprecated Este controlador está deprecado. Use en su lugar:
 *             - {@link EstudiantePracticaController} para operaciones de estudiantes
 *             - {@link ProfesorPracticaController} para operaciones de profesores
 * 
 * Se mantiene temporalmente para compatibilidad con clientes existentes.
 * Será removido en versiones futuras.
 * 
 * Rutas nuevas recomendadas:
 * - Estudiantes: /api/v1/estudiantes/practicas
 * - Profesores: /api/v1/profesores/practicas
 * 
 * @author SGPP Team
 * @since 1.0
 */
@Deprecated(since = "1.0", forRemoval = true)
@RestController
@RequestMapping("/api/v1/practicas")
@RequiredArgsConstructor
public class PracticaController {

    private final PracticaService practicaService;

    /**
     * Crear una nueva práctica.
     * 
     * @deprecated Use {@link EstudiantePracticaController#crearPractica} o
     *             {@link ProfesorPracticaController#crearPractica} en su lugar.
     * 
     * @param requestDTO Datos de la práctica a crear
     * @return ResponseEntity con la práctica creada y estado HTTP 201
     */
    @Deprecated(since = "1.0", forRemoval = true)
    @PostMapping 
    public ResponseEntity<PracticaResponseDTO> crearPractica(@Valid @RequestBody PracticaRequestDTO requestDTO) {
        PracticaResponseDTO nuevaPractica = practicaService.guardarPractica(requestDTO);
        return new ResponseEntity<>(nuevaPractica, HttpStatus.CREATED); 
    }

    /**
     * Obtener todas las prácticas.
     * 
     * @deprecated Use {@link ProfesorPracticaController#obtenerTodasLasPracticas} en su lugar.
     * 
     * @return ResponseEntity con lista de prácticas y estado HTTP 200
     */
    @Deprecated(since = "1.0", forRemoval = true)
    @GetMapping 
    public ResponseEntity<List<PracticaResponseDTO>> obtenerTodasLasPracticas() {
        List<PracticaResponseDTO> practicas = practicaService.obtenerTodasLasPracticas();
        return ResponseEntity.ok(practicas);
    }
    
    /**
     * Obtener una práctica por su ID.
     * 
     * @param id ID de la práctica
     * @return ResponseEntity con la práctica encontrada y estado HTTP 200
     * @throws ResourceNotFoundException si la práctica no existe (manejado por GlobalExceptionHandler)
     */
    @GetMapping("/{id}")
    public ResponseEntity<PracticaResponseDTO> obtenerPracticaPorId(@PathVariable Long id) {
        PracticaResponseDTO practica = practicaService.obtenerPracticaPorId(id);
        return ResponseEntity.ok(practica);
    }

    /**
     * Actualizar una práctica existente.
     * Exclusivo para profesores (se asegurará con Spring Security posteriormente).
     * 
     * @param id ID de la práctica a actualizar
     * @param requestDTO Nuevos datos de la práctica
     * @return ResponseEntity con la práctica actualizada y estado HTTP 200
     * @throws ResourceNotFoundException si la práctica no existe (manejado por GlobalExceptionHandler)
     * @throws BusinessException si hay errores de validación de negocio (manejado por GlobalExceptionHandler)
     */
    @PutMapping("/{id}")
    public ResponseEntity<PracticaResponseDTO> actualizarPractica(
            @PathVariable Long id, 
            @Valid @RequestBody PracticaRequestDTO requestDTO) {
        PracticaResponseDTO practica = practicaService.actualizarPractica(id, requestDTO);
        return ResponseEntity.ok(practica);
    }

    /**
     * Eliminar una práctica.
     * Exclusivo para profesores (se asegurará con Spring Security posteriormente).
     * 
     * @param id ID de la práctica a eliminar
     * @return ResponseEntity con estado HTTP 204 No Content
     * @throws ResourceNotFoundException si la práctica no existe (manejado por GlobalExceptionHandler)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPractica(@PathVariable Long id) {
        practicaService.eliminarPractica(id);
        return ResponseEntity.noContent().build(); 
    }
    
    /**
     * Obtener todas las prácticas de un estudiante específico.
     * 
     * @param estudianteId ID del estudiante
     * @return ResponseEntity con lista de prácticas del estudiante y estado HTTP 200
     * @throws ResourceNotFoundException si el estudiante no existe (manejado por GlobalExceptionHandler)
     */
    @GetMapping("/estudiante/{estudianteId}")
    public ResponseEntity<List<PracticaResponseDTO>> obtenerPracticasPorEstudiante(@PathVariable Long estudianteId) {
        List<PracticaResponseDTO> practicas = practicaService.obtenerPracticasPorEstudiante(estudianteId);
        return ResponseEntity.ok(practicas);
    }
    
    /**
     * Cambiar el estado de una práctica.
     * Exclusivo para profesores (se asegurará con Spring Security posteriormente).
     * 
     * @param id ID de la práctica
     * @param request DTO con el nuevo estado
     * @return ResponseEntity con la práctica actualizada y estado HTTP 200
     * @throws ResourceNotFoundException si la práctica no existe (manejado por GlobalExceptionHandler)
     * @throws BusinessException si el cambio de estado no es válido (manejado por GlobalExceptionHandler)
     */
    @PatchMapping("/{id}/estado")
    public ResponseEntity<PracticaResponseDTO> cambiarEstado(
            @PathVariable Long id,
            @Valid @RequestBody CambioEstadoRequestDTO request) {
        PracticaResponseDTO practica = practicaService.cambiarEstadoPractica(id, request.getNuevoEstado());
        return ResponseEntity.ok(practica);
    }
}