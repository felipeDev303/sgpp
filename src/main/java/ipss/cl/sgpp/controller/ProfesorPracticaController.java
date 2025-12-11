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
 * Controlador REST para gestión de prácticas desde la perspectiva del profesor.
 * 
 * Rutas bajo /api/v1/profesores/practicas
 * 
 * Funcionalidades permitidas para profesores (según requisitos):
 * - Agregar Práctica: Ingresar prácticas en nombre de estudiantes
 * - Leer Práctica: Acceso a todos los registros para supervisión y seguimiento
 * - Actualizar Práctica: Modificar información incorrecta o desactualizada
 * - Eliminar Práctica: Borrado lógico de registros obsoletos o incorrectos
 * - Cambiar Estado: Gestionar el flujo de estados de las prácticas
 * 
 * @author SGPP Team
 * @since 1.0
 */
@RestController
@RequestMapping("/api/v1/profesores/practicas")
@RequiredArgsConstructor
public class ProfesorPracticaController {

    private final PracticaService practicaService;

    /**
     * Crear una nueva práctica (profesor registra práctica en nombre de un estudiante).
     * 
     * Requisito: "Posibilidad de ingresar prácticas en nombre de los estudiantes"
     * 
     * @param requestDTO Datos de la práctica a crear con validaciones
     * @return ResponseEntity con la práctica creada y estado HTTP 201
     */
    @PostMapping
    public ResponseEntity<PracticaResponseDTO> crearPractica(
            @Valid @RequestBody PracticaRequestDTO requestDTO) {
        PracticaResponseDTO nuevaPractica = practicaService.guardarPractica(requestDTO);
        return new ResponseEntity<>(nuevaPractica, HttpStatus.CREATED);
    }

    /**
     * Obtener todas las prácticas registradas en el sistema.
     * 
     * Requisito: "Acceso a todos los registros de prácticas para supervisión y seguimiento"
     * 
     * @return ResponseEntity con lista de todas las prácticas activas y estado HTTP 200
     */
    @GetMapping
    public ResponseEntity<List<PracticaResponseDTO>> obtenerTodasLasPracticas() {
        List<PracticaResponseDTO> practicas = practicaService.obtenerTodasLasPracticas();
        return ResponseEntity.ok(practicas);
    }

    /**
     * Obtener una práctica específica por su ID.
     * 
     * Requisito: "Acceso a todos los registros de prácticas"
     * 
     * @param id ID de la práctica
     * @return ResponseEntity con la práctica encontrada y estado HTTP 200
     * @throws ResourceNotFoundException si la práctica no existe o está eliminada
     */
    @GetMapping("/{id}")
    public ResponseEntity<PracticaResponseDTO> obtenerPracticaPorId(@PathVariable Long id) {
        PracticaResponseDTO practica = practicaService.obtenerPracticaPorId(id);
        return ResponseEntity.ok(practica);
    }

    /**
     * Obtener todas las prácticas de un estudiante específico.
     * 
     * Útil para que el profesor supervise las prácticas de un estudiante en particular.
     * 
     * @param estudianteId ID del estudiante
     * @return ResponseEntity con lista de prácticas del estudiante y estado HTTP 200
     */
    @GetMapping("/estudiante/{estudianteId}")
    public ResponseEntity<List<PracticaResponseDTO>> obtenerPracticasPorEstudiante(
            @PathVariable Long estudianteId) {
        List<PracticaResponseDTO> practicas = practicaService.obtenerPracticasPorEstudiante(estudianteId);
        return ResponseEntity.ok(practicas);
    }

    /**
     * Obtener todas las prácticas supervisadas por un profesor específico.
     * 
     * @param profesorId ID del profesor supervisor
     * @return ResponseEntity con lista de prácticas supervisadas y estado HTTP 200
     */
    @GetMapping("/profesor/{profesorId}")
    public ResponseEntity<List<PracticaResponseDTO>> obtenerPracticasPorProfesor(
            @PathVariable Long profesorId) {
        // Este método requiere implementación en PracticaService
        // Por ahora retornamos todas las prácticas como placeholder
        List<PracticaResponseDTO> practicas = practicaService.obtenerTodasLasPracticas();
        return ResponseEntity.ok(practicas);
    }

    /**
     * Actualizar una práctica existente.
     * 
     * Requisito: "Actualizar cualquier información incorrecta o desactualizada 
     *            en el registro de prácticas"
     * 
     * Exclusivo para profesores. Los estudiantes no pueden actualizar.
     * 
     * @param id ID de la práctica a actualizar
     * @param requestDTO Nuevos datos de la práctica
     * @return ResponseEntity con la práctica actualizada y estado HTTP 200
     * @throws ResourceNotFoundException si la práctica no existe
     * @throws BusinessException si hay errores de validación de negocio
     */
    @PutMapping("/{id}")
    public ResponseEntity<PracticaResponseDTO> actualizarPractica(
            @PathVariable Long id,
            @Valid @RequestBody PracticaRequestDTO requestDTO) {
        PracticaResponseDTO practica = practicaService.actualizarPractica(id, requestDTO);
        return ResponseEntity.ok(practica);
    }

    /**
     * Eliminar una práctica (borrado lógico).
     * 
     * Requisito: "Eliminar registros obsoletos o incorrectos de prácticas (borrado lógico)"
     * 
     * Exclusivo para profesores. La práctica se marca como eliminada pero permanece
     * en la base de datos para auditoría.
     * 
     * @param id ID de la práctica a eliminar
     * @return ResponseEntity con estado HTTP 204 No Content
     * @throws ResourceNotFoundException si la práctica no existe
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPractica(@PathVariable Long id) {
        practicaService.eliminarPractica(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Cambiar el estado de una práctica.
     * 
     * Exclusivo para profesores. Permite gestionar el flujo de estados:
     * PENDIENTE → APROBADA → EN_CURSO → COMPLETADA
     * 
     * @param id ID de la práctica
     * @param request DTO con el nuevo estado
     * @return ResponseEntity con la práctica actualizada y estado HTTP 200
     * @throws ResourceNotFoundException si la práctica no existe
     * @throws BusinessException si el cambio de estado no es válido
     */
    @PatchMapping("/{id}/estado")
    public ResponseEntity<PracticaResponseDTO> cambiarEstado(
            @PathVariable Long id,
            @Valid @RequestBody CambioEstadoRequestDTO request) {
        PracticaResponseDTO practica = practicaService.cambiarEstadoPractica(id, request.getNuevoEstado());
        return ResponseEntity.ok(practica);
    }

    /**
     * Restaurar una práctica eliminada lógicamente.
     * 
     * Funcionalidad administrativa exclusiva para profesores.
     * Permite deshacer eliminaciones accidentales.
     * 
     * @param id ID de la práctica a restaurar
     * @return ResponseEntity con la práctica restaurada y estado HTTP 200
     * @throws ResourceNotFoundException si la práctica no existe
     * @throws BusinessException si la práctica no está eliminada
     */
    @PostMapping("/{id}/restaurar")
    public ResponseEntity<PracticaResponseDTO> restaurarPractica(@PathVariable Long id) {
        PracticaResponseDTO practica = practicaService.restaurarPractica(id);
        return ResponseEntity.ok(practica);
    }
}
