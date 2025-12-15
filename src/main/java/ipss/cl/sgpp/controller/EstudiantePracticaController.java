package ipss.cl.sgpp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import ipss.cl.sgpp.dto.common.ErrorResponseDTO;
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
 * Controlador REST para gestión de prácticas desde la perspectiva del estudiante.
 * 
 * Rutas bajo /api/v1/estudiantes/practicas
 * 
 * Funcionalidades permitidas para estudiantes (según requisitos):
 * - Agregar Práctica: Crear nuevos registros de prácticas
 * - Leer Práctica: Consultar detalles de sus propias prácticas
 * 
 * @author SGPP Team
 * @since 1.0
 */
@Tag(name = "Estudiantes - Prácticas", description = "Endpoints para gestión de prácticas desde la perspectiva del estudiante")
@RestController
@RequestMapping("/api/v1/estudiantes/practicas")
@RequiredArgsConstructor
public class EstudiantePracticaController {

    private final PracticaService practicaService;

    /**
     * Crear una nueva práctica (estudiante registra su propia práctica).
     * 
     * Requisito: "Los estudiantes pueden ingresar nuevos registros de sus prácticas profesionales"
     * 
     * @param requestDTO Datos de la práctica a crear con validaciones
     * @return ResponseEntity con la práctica creada y estado HTTP 201
     */
    @Operation(
            summary = "Crear nueva práctica",
            description = "Permite al estudiante registrar una nueva práctica profesional con todos sus datos: empresa, supervisor, fechas y actividades."
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
                    description = "Estudiante, profesor, empresa o supervisor no encontrado",
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
     * Obtener todas las prácticas de un estudiante específico.
     * 
     * Requisito: "Permite a los estudiantes consultar los detalles de sus prácticas registradas"
     * 
     * Nota: En un sistema con autenticación, el estudianteId vendría del token JWT.
     * Por ahora se recibe como parámetro de ruta.
     * 
     * @param estudianteId ID del estudiante (en producción vendría del contexto de seguridad)
     * @return ResponseEntity con lista de prácticas del estudiante y estado HTTP 200
     */
    @Operation(
            summary = "Obtener mis prácticas",
            description = "Retorna todas las prácticas registradas de un estudiante específico. En producción, el ID vendría del token de autenticación."
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
    public ResponseEntity<List<PracticaResponseDTO>> obtenerMisPracticas(
            @Parameter(description = "ID del estudiante", required = true, example = "1")
            @PathVariable Long estudianteId) {
        List<PracticaResponseDTO> practicas = practicaService.obtenerPracticasPorEstudiante(estudianteId);
        return ResponseEntity.ok(practicas);
    }

    /**
     * Obtener una práctica específica por su ID.
     * 
     * Requisito: "Consultar los detalles de sus prácticas registradas"
     * 
     * Nota: En un sistema con autenticación, se validaría que el estudiante
     * solo pueda ver sus propias prácticas.
     * 
     * @param id ID de la práctica
     * @return ResponseEntity con la práctica encontrada y estado HTTP 200
     * @throws ResourceNotFoundException si la práctica no existe o está eliminada
     */
    @Operation(
            summary = "Obtener práctica por ID",
            description = "Retorna los detalles completos de una práctica específica. En producción, se validaría que el estudiante solo acceda a sus propias prácticas."
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
     * Obtener todas las prácticas activas (para vista general del estudiante).
     * 
     * Nota: En producción, este endpoint podría estar restringido o filtrado
     * automáticamente por el ID del estudiante autenticado.
     * 
     * @return ResponseEntity con lista de todas las prácticas activas y estado HTTP 200
     */
    @Operation(
            summary = "Listar todas las prácticas activas",
            description = "Retorna todas las prácticas activas (no eliminadas) del sistema. En producción, se filtraría automáticamente por el estudiante autenticado."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de prácticas obtenida exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PracticaResponseDTO.class))
            )
    })
    @GetMapping
    public ResponseEntity<List<PracticaResponseDTO>> listarPracticasActivas() {
        List<PracticaResponseDTO> practicas = practicaService.obtenerTodasLasPracticas();
        return ResponseEntity.ok(practicas);
    }
}
