package ipss.cl.sgpp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import ipss.cl.sgpp.dto.common.ErrorResponseDTO;
import ipss.cl.sgpp.dto.request.EstudianteRequestDTO;
import ipss.cl.sgpp.dto.response.EstudianteResponseDTO;
import ipss.cl.sgpp.service.EstudianteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para consultas de estudiantes.
 * Proporciona endpoints de solo lectura para obtener información de estudiantes
 * registrados en el sistema, necesarios para la gestión de prácticas profesionales.
 */
@Tag(
    name = "Estudiantes",
    description = "Consulta de información de estudiantes registrados en el sistema. " +
                  "Estos endpoints permiten listar y buscar estudiantes disponibles para prácticas profesionales."
)
@RestController
@RequestMapping("/api/v1/estudiantes")
@RequiredArgsConstructor
public class EstudianteController {

    private final EstudianteService estudianteService;
    
    /**
     * Obtiene la lista de todos los estudiantes registrados.
     * 
     * @return ResponseEntity con lista de estudiantes y estado 200 OK
     */
    @Operation(
            summary = "Listar todos los estudiantes",
            description = "Retorna la lista completa de estudiantes registrados en el sistema. Útil para seleccionar estudiantes al crear prácticas."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de estudiantes obtenida exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = EstudianteResponseDTO.class))
            )
    })
    @GetMapping
    public ResponseEntity<List<EstudianteResponseDTO>> obtenerTodosLosEstudiantes() {
        List<EstudianteResponseDTO> estudiantes = estudianteService.obtenerTodosLosEstudiantes();
        return ResponseEntity.ok(estudiantes);
    }
    
    /**
     * Obtiene un estudiante específico por su ID.
     * 
     * @param id ID del estudiante a buscar
     * @return ResponseEntity con el estudiante encontrado y estado 200 OK
     */
    @Operation(
            summary = "Obtener estudiante por ID",
            description = "Retorna los detalles completos de un estudiante específico."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Estudiante encontrado exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = EstudianteResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Estudiante no encontrado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<EstudianteResponseDTO> obtenerEstudiantePorId(
            @Parameter(description = "ID del estudiante", required = true, example = "1")
            @PathVariable Long id) {
        EstudianteResponseDTO responseDTO = estudianteService.obtenerEstudiantePorId(id);
        return ResponseEntity.ok(responseDTO);
    }
    
    /**
     * Busca un estudiante por su email.
     * 
     * @param email Email del estudiante a buscar
     * @return ResponseEntity con el estudiante encontrado y estado 200 OK
     */
    @Operation(
            summary = "Buscar estudiante por email",
            description = "Retorna un estudiante buscando por su dirección de correo electrónico institucional."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Estudiante encontrado exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = EstudianteResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Estudiante no encontrado con ese email",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))
            )
    })
    @GetMapping("/email/{email}")
    public ResponseEntity<EstudianteResponseDTO> obtenerEstudiantePorEmail(
            @Parameter(description = "Email del estudiante", required = true, example = "estudiante@ipss.cl")
            @PathVariable String email) {
        EstudianteResponseDTO responseDTO = estudianteService.obtenerEstudiantePorEmail(email);
        return ResponseEntity.ok(responseDTO);
    }
    
    /**
     * Busca un estudiante por su RUT.
     * 
     * @param rut RUT del estudiante a buscar
     * @return ResponseEntity con el estudiante encontrado y estado 200 OK
     */
    @Operation(
            summary = "Buscar estudiante por RUT",
            description = "Retorna un estudiante buscando por su RUT chileno (sin puntos ni guión)."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Estudiante encontrado exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = EstudianteResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Estudiante no encontrado con ese RUT",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))
            )
    })
    @GetMapping("/rut/{rut}")
    public ResponseEntity<EstudianteResponseDTO> obtenerEstudiantePorRut(
            @Parameter(description = "RUT del estudiante sin puntos ni guión", required = true, example = "12345678-9")
            @PathVariable String rut) {
        EstudianteResponseDTO responseDTO = estudianteService.obtenerEstudiantePorRut(rut);
        return ResponseEntity.ok(responseDTO);
    }
}
