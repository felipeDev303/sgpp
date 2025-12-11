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
 * 
 * NOTA: Los endpoints POST, PUT, DELETE ya NO están soportados en EstudianteService
 * (simplificado a solo consultas). La gestión de estudiantes se realiza externamente.
 * Solo endpoints GET están funcionales.
 * 
 * @deprecated Los métodos CRUD (crear, actualizar, eliminar) fueron removidos en PR #25
 */
@Tag(name = "Estudiantes", description = "Endpoints de consulta para estudiantes (solo lectura - CRUD removido en PR #25)")
@RestController
@RequestMapping("/api/v1/estudiantes")
@RequiredArgsConstructor
public class EstudianteController {

    private final EstudianteService estudianteService;
    
    /**
     * Crea un nuevo estudiante en el sistema.
     * 
     * @param requestDTO Datos del estudiante a crear
     * @return ResponseEntity con el estudiante creado y estado 201 CREATED
     * @deprecated Funcionalidad removida en PR #25. Este endpoint fallará en runtime.
     */
    @Deprecated
    @Operation(
            summary = "[NO FUNCIONAL] Crear estudiante",
            description = "⚠️ **DEPRECADO**: Este endpoint NO está funcional. La funcionalidad CRUD fue removida en PR #25. La gestión de estudiantes se realiza externamente (sistema de RRHH, matrícula, etc.)."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "500",
                    description = "Método no implementado - servicio simplificado a solo consultas",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))
            )
    })
    @PostMapping
    public ResponseEntity<EstudianteResponseDTO> crearEstudiante(
            @Valid @RequestBody EstudianteRequestDTO requestDTO) {
        throw new UnsupportedOperationException("Endpoint deshabilitado en PR #25. Use solo endpoints GET para consultas.");
    }
    
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
    
    /**
     * Actualiza los datos de un estudiante existente.
     * 
     * @param id ID del estudiante a actualizar
     * @param requestDTO Nuevos datos del estudiante
     * @return ResponseEntity con el estudiante actualizado y estado 200 OK
     * @deprecated Funcionalidad removida en PR #25. Este endpoint fallará en runtime.
     */
    @Deprecated
    @Operation(
            summary = "[NO FUNCIONAL] Actualizar estudiante",
            description = "⚠️ **DEPRECADO**: Este endpoint NO está funcional. La funcionalidad CRUD fue removida en PR #25. La gestión de estudiantes se realiza externamente."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "500",
                    description = "Método no implementado - servicio simplificado a solo consultas",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<EstudianteResponseDTO> actualizarEstudiante(
            @Parameter(description = "ID del estudiante", required = true, example = "1")
            @PathVariable Long id,
            @Valid @RequestBody EstudianteRequestDTO requestDTO) {
        throw new UnsupportedOperationException("Endpoint deshabilitado en PR #25. Use solo endpoints GET para consultas.");
    }
    
    /**
     * Elimina un estudiante del sistema.
     * 
     * @param id ID del estudiante a eliminar
     * @return ResponseEntity vacío con estado 204 NO CONTENT
     * @deprecated Funcionalidad removida en PR #25. Este endpoint fallará en runtime.
     */
    @Deprecated
    @Operation(
            summary = "[NO FUNCIONAL] Eliminar estudiante",
            description = "⚠️ **DEPRECADO**: Este endpoint NO está funcional. La funcionalidad CRUD fue removida en PR #25. La gestión de estudiantes se realiza externamente."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "500",
                    description = "Método no implementado - servicio simplificado a solo consultas",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarEstudiante(
            @Parameter(description = "ID del estudiante", required = true, example = "1")
            @PathVariable Long id) {
        throw new UnsupportedOperationException("Endpoint deshabilitado en PR #25. Use solo endpoints GET para consultas.");
    }
}
