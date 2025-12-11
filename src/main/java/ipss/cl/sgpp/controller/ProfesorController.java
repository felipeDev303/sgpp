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
 * 
 * NOTA: Los endpoints POST, PUT, DELETE ya NO están soportados en ProfesorService
 * (simplificado a solo consultas). La gestión de profesores se realiza externamente.
 * Solo endpoints GET están funcionales.
 * 
 * @deprecated Los métodos CRUD (crear, actualizar, eliminar) fueron removidos en PR #25
 */
@Tag(name = "Profesores", description = "Endpoints de consulta para profesores supervisores (solo lectura - CRUD removido en PR #25)")
@RestController
@RequestMapping("/api/v1/profesores")
@RequiredArgsConstructor
public class ProfesorController {

    private final ProfesorService profesorService;
    
    /**
     * Crea un nuevo profesor en el sistema.
     * 
     * @param requestDTO Datos del profesor a crear
     * @return ResponseEntity con el profesor creado y estado 201 CREATED
     * @deprecated Funcionalidad removida en PR #25. Este endpoint fallará en runtime.
     */
    @Deprecated
    @Operation(
            summary = "[NO FUNCIONAL] Crear profesor",
            description = "⚠️ **DEPRECADO**: Este endpoint NO está funcional. La funcionalidad CRUD fue removida en PR #25. La gestión de profesores se realiza externamente (sistema de RRHH, nómina, etc.)."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "500",
                    description = "Método no implementado - servicio simplificado a solo consultas",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))
            )
    })
    @PostMapping
    public ResponseEntity<ProfesorResponseDTO> crearProfesor(
            @Valid @RequestBody ProfesorRequestDTO requestDTO) {
        
        ProfesorResponseDTO responseDTO = profesorService.crearProfesor(requestDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }
    
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
    
    /**
     * Actualiza los datos de un profesor existente.
     * 
     * @param id ID del profesor a actualizar
     * @param requestDTO Nuevos datos del profesor
     * @return ResponseEntity con el profesor actualizado y estado 200 OK
     * @deprecated Funcionalidad removida en PR #25. Este endpoint fallará en runtime.
     */
    @Deprecated
    @Operation(
            summary = "[NO FUNCIONAL] Actualizar profesor",
            description = "⚠️ **DEPRECADO**: Este endpoint NO está funcional. La funcionalidad CRUD fue removida en PR #25. La gestión de profesores se realiza externamente."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "500",
                    description = "Método no implementado - servicio simplificado a solo consultas",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<ProfesorResponseDTO> actualizarProfesor(
            @Parameter(description = "ID del profesor", required = true, example = "1")
            @PathVariable Long id,
            @Valid @RequestBody ProfesorRequestDTO requestDTO) {
        
        ProfesorResponseDTO responseDTO = profesorService.actualizarProfesor(id, requestDTO);
        return ResponseEntity.ok(responseDTO);
    }
    
    /**
     * Elimina un profesor del sistema.
     * 
     * @param id ID del profesor a eliminar
     * @return ResponseEntity vacío con estado 204 NO CONTENT
     * @deprecated Funcionalidad removida en PR #25. Este endpoint fallará en runtime.
     */
    @Deprecated
    @Operation(
            summary = "[NO FUNCIONAL] Eliminar profesor",
            description = "⚠️ **DEPRECADO**: Este endpoint NO está funcional. La funcionalidad CRUD fue removida en PR #25. La gestión de profesores se realiza externamente."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "500",
                    description = "Método no implementado - servicio simplificado a solo consultas",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProfesor(
            @Parameter(description = "ID del profesor", required = true, example = "1")
            @PathVariable Long id) {
        profesorService.eliminarProfesor(id);
        return ResponseEntity.noContent().build();
    }
}
