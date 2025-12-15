package ipss.cl.sgpp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import ipss.cl.sgpp.dto.common.ErrorResponseDTO;
import ipss.cl.sgpp.dto.request.ProfesorRequestDTO;
import ipss.cl.sgpp.dto.response.ProfesorResponseDTO;
import ipss.cl.sgpp.service.ProfesorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para consultas de profesores.
 * Proporciona endpoints de solo lectura para obtener información de profesores
 * supervisores registrados en el sistema, necesarios para la gestión de prácticas profesionales.
 */
@Tag(
    name = "Profesores",
    description = "Consulta de información de profesores supervisores registrados en el sistema. " +
                  "Estos endpoints permiten listar y buscar profesores disponibles para supervisar prácticas profesionales."
)
@RestController
@RequestMapping("/api/v1/profesores")
@RequiredArgsConstructor
public class ProfesorController {

    private final ProfesorService profesorService;
    
    /**
     * Obtiene la lista de todos los profesores registrados.
     * 
     * @return ResponseEntity con lista de profesores y estado 200 OK
     */
    @Operation(
            summary = "Listar todos los profesores",
            description = "Retorna la lista completa de profesores supervisores registrados en el sistema. Útil para seleccionar supervisores al crear prácticas."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de profesores obtenida exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProfesorResponseDTO.class))
            )
    })
    @GetMapping
    public ResponseEntity<List<ProfesorResponseDTO>> obtenerTodosLosProfesores() {
        List<ProfesorResponseDTO> profesores = profesorService.obtenerTodosLosProfesores();
        return ResponseEntity.ok(profesores);
    }
    
    /**
     * Obtiene un profesor específico por su ID.
     * 
     * @param id ID del profesor a buscar
     * @return ResponseEntity con el profesor encontrado y estado 200 OK
     */
    @Operation(
            summary = "Obtener profesor por ID",
            description = "Retorna los detalles completos de un profesor supervisor específico."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Profesor encontrado exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProfesorResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Profesor no encontrado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProfesorResponseDTO> obtenerProfesorPorId(
            @Parameter(description = "ID del profesor", required = true, example = "1")
            @PathVariable Long id) {
        ProfesorResponseDTO responseDTO = profesorService.obtenerProfesorPorId(id);
        return ResponseEntity.ok(responseDTO);
    }
    
    /**
     * Busca un profesor por su email.
     * 
     * @param email Email del profesor a buscar
     * @return ResponseEntity con el profesor encontrado y estado 200 OK
     */
    @Operation(
            summary = "Buscar profesor por email",
            description = "Retorna un profesor buscando por su dirección de correo electrónico institucional."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Profesor encontrado exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProfesorResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Profesor no encontrado con ese email",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))
            )
    })
    @GetMapping("/email/{email}")
    public ResponseEntity<ProfesorResponseDTO> obtenerProfesorPorEmail(
            @Parameter(description = "Email del profesor", required = true, example = "profesor@ipss.cl")
            @PathVariable String email) {
        ProfesorResponseDTO responseDTO = profesorService.obtenerProfesorPorEmail(email);
        return ResponseEntity.ok(responseDTO);
    }
    
    /**
     * Obtiene todos los profesores de un departamento específico.
     * 
     * @param departamento Nombre del departamento
     * @return ResponseEntity con lista de profesores del departamento y estado 200 OK
     */
    @Operation(
            summary = "Listar profesores por departamento",
            description = "Retorna todos los profesores que pertenecen a un departamento académico específico."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de profesores del departamento obtenida exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProfesorResponseDTO.class))
            )
    })
    @GetMapping("/departamento/{departamento}")
    public ResponseEntity<List<ProfesorResponseDTO>> obtenerProfesoresPorDepartamento(
            @Parameter(description = "Nombre del departamento académico", required = true, example = "Ingeniería")
            @PathVariable String departamento) {
        
        List<ProfesorResponseDTO> profesores = profesorService.obtenerProfesoresPorDepartamento(departamento);
        return ResponseEntity.ok(profesores);
    }
}
