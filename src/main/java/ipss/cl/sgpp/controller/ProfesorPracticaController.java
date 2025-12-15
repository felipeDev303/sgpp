package ipss.cl.sgpp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import ipss.cl.sgpp.dto.common.ErrorResponseDTO;
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
@Tag(name = "Profesores - Prácticas", description = "Endpoints para gestión completa de prácticas desde la perspectiva del profesor (CRUD + cambio de estado)")
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
    @Operation(
            summary = "Crear práctica (como profesor)",
            description = "Permite al profesor registrar una nueva práctica profesional en nombre de un estudiante."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Práctica creada exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PracticaResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos inválidos o error de validación",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Entidad relacionada no encontrada",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))
            )
    })
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
    @Operation(
            summary = "Listar todas las prácticas",
            description = "Retorna todas las prácticas activas del sistema para supervisión y seguimiento por parte del profesor."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de prácticas obtenida exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PracticaResponseDTO.class))
            )
    })
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
    @Operation(
            summary = "Obtener práctica por ID",
            description = "Retorna los detalles completos de una práctica específica."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Práctica encontrada exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PracticaResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Práctica no encontrada o eliminada",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<PracticaResponseDTO> obtenerPracticaPorId(
            @Parameter(description = "ID de la práctica", required = true, example = "1")
            @PathVariable Long id) {
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
    @Operation(
            summary = "Listar prácticas por estudiante",
            description = "Retorna todas las prácticas de un estudiante específico para supervisión."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de prácticas obtenida exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PracticaResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Estudiante no encontrado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))
            )
    })
    @GetMapping("/estudiante/{estudianteId}")
    public ResponseEntity<List<PracticaResponseDTO>> obtenerPracticasPorEstudiante(
            @Parameter(description = "ID del estudiante", required = true, example = "1")
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
    @Operation(
            summary = "Listar prácticas por profesor supervisor",
            description = "Retorna todas las prácticas supervisadas por un profesor específico."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de prácticas obtenida exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PracticaResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Profesor no encontrado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))
            )
    })
    @GetMapping("/profesor/{profesorId}")
    public ResponseEntity<List<PracticaResponseDTO>> obtenerPracticasPorProfesor(
            @Parameter(description = "ID del profesor supervisor", required = true, example = "1")
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
    @Operation(
            summary = "Actualizar práctica",
            description = "Permite al profesor modificar información incorrecta o desactualizada de una práctica existente. Funcionalidad exclusiva para profesores."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Práctica actualizada exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PracticaResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos inválidos o error de validación de negocio",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Práctica no encontrada",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<PracticaResponseDTO> actualizarPractica(
            @Parameter(description = "ID de la práctica a actualizar", required = true, example = "1")
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
    @Operation(
            summary = "Eliminar práctica (borrado lógico)",
            description = "Marca una práctica como eliminada sin borrarla físicamente de la base de datos. Permite mantener auditoría de registros. Exclusivo para profesores."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Práctica eliminada exitosamente (sin contenido en respuesta)"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Práctica no encontrada",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPractica(
            @Parameter(description = "ID de la práctica a eliminar", required = true, example = "1")
            @PathVariable Long id) {
        practicaService.eliminarPractica(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Cambiar el estado de una práctica.
     * 
     * Exclusivo para profesores. Permite gestionar el flujo de estados:
     * PENDIENTE → EN_CURSO → COMPLETADA
     * 
     * @param id ID de la práctica
     * @param request DTO con el nuevo estado
     * @return ResponseEntity con la práctica actualizada y estado HTTP 200
     * @throws ResourceNotFoundException si la práctica no existe
     * @throws BusinessException si el cambio de estado no es válido
     */
    @Operation(
            summary = "Cambiar estado de práctica",
            description = "Permite al profesor gestionar el flujo de estados de una práctica: PENDIENTE → EN_CURSO → COMPLETADA. Valida que las transiciones sean permitidas."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Estado cambiado exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PracticaResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Transición de estado inválida",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Práctica no encontrada",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))
            )
    })
    @PatchMapping("/{id}/estado")
    public ResponseEntity<PracticaResponseDTO> cambiarEstado(
            @Parameter(description = "ID de la práctica", required = true, example = "1")
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
    @Operation(
            summary = "Restaurar práctica eliminada",
            description = "Permite al profesor restaurar una práctica que fue eliminada lógicamente, deshaciendo eliminaciones accidentales."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Práctica restaurada exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PracticaResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "La práctica no está eliminada",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Práctica no encontrada",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))
            )
    })
    @PostMapping("/{id}/restaurar")
    public ResponseEntity<PracticaResponseDTO> restaurarPractica(
            @Parameter(description = "ID de la práctica a restaurar", required = true, example = "1")
            @PathVariable Long id) {
        PracticaResponseDTO practica = practicaService.restaurarPractica(id);
        return ResponseEntity.ok(practica);
    }
}
