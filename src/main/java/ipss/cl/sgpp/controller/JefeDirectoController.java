package ipss.cl.sgpp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import ipss.cl.sgpp.dto.request.JefeDirectoRequestDTO;
import ipss.cl.sgpp.dto.response.JefeDirectoResponseDTO;
import ipss.cl.sgpp.dto.response.ErrorResponseDTO;
import ipss.cl.sgpp.service.JefeDirectoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para consultas de jefes directos.
 * 
 * NOTA: Los endpoints POST, PUT, DELETE ya NO están soportados en JefeDirectoService
 * (simplificado a solo consultas). La gestión de jefes directos se realiza con las prácticas.
 * Solo endpoints GET están funcionales.
 * 
 * @deprecated Los métodos CRUD (crear, actualizar, eliminar) fueron removidos en PR #25
 */
@Tag(
    name = "Jefes Directos",
    description = "Endpoints para consultar información de jefes directos (supervisores de empresas). " +
                  "**NOTA**: Los métodos CRUD (POST, PUT, DELETE) fueron removidos en PR #25 " +
                  "ya que la gestión de jefes directos se realiza automáticamente al crear prácticas. " +
                  "Solo se mantienen endpoints de consulta."
)
@RestController
@RequestMapping("/api/v1/jefes-directos")
@RequiredArgsConstructor
public class JefeDirectoController {

    private final JefeDirectoService jefeDirectoService;
    
    /**
     * Crea un nuevo jefe directo en el sistema.
     * 
     * @param requestDTO Datos del jefe directo a crear
     * @return ResponseEntity con el jefe directo creado y estado 201 CREATED
     */
    @Operation(
        summary = "⚠️ [NO FUNCIONAL] Crear jefe directo",
        description = "**ADVERTENCIA**: Este endpoint fue deshabilitado en PR #25. " +
                      "La gestión de jefes directos se realiza automáticamente al crear prácticas. " +
                      "Use únicamente los endpoints GET para consultas."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "500",
            description = "Método no implementado (lanza UnsupportedOperationException)",
            content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
        )
    })
    @Deprecated
    @PostMapping
    public ResponseEntity<JefeDirectoResponseDTO> crearJefeDirecto(
            @Valid @RequestBody JefeDirectoRequestDTO requestDTO) {
        
        JefeDirectoResponseDTO responseDTO = jefeDirectoService.crearJefeDirecto(requestDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }
    
    /**
     * Obtiene la lista de todos los jefes directos registrados.
     * 
     * @return ResponseEntity con lista de jefes directos y estado 200 OK
     */
    @Operation(
        summary = "Listar todos los jefes directos",
        description = "Retorna la lista completa de jefes directos (supervisores) registrados en el sistema. " +
                      "Útil para seleccionar supervisores al crear o editar prácticas profesionales."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Lista de jefes directos obtenida exitosamente"
        )
    })
    @GetMapping
    public ResponseEntity<List<JefeDirectoResponseDTO>> obtenerTodosLosJefesDirectos() {
        List<JefeDirectoResponseDTO> jefesDirectos = jefeDirectoService.obtenerTodosLosJefesDirectos();
        return ResponseEntity.ok(jefesDirectos);
    }
    
    /**
     * Obtiene un jefe directo específico por su ID.
     * 
     * @param id ID del jefe directo a buscar
     * @return ResponseEntity con el jefe directo encontrado y estado 200 OK
     */
    @Operation(
        summary = "Obtener jefe directo por ID",
        description = "Busca y retorna un jefe directo específico según su identificador único."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Jefe directo encontrado exitosamente"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Jefe directo no encontrado con el ID especificado",
            content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
        )
    })
    @GetMapping("/{id}")
    public ResponseEntity<JefeDirectoResponseDTO> obtenerJefeDirectoPorId(
            @Parameter(description = "ID del jefe directo", example = "1") @PathVariable Long id) {
        JefeDirectoResponseDTO responseDTO = jefeDirectoService.obtenerJefeDirectoPorId(id);
        return ResponseEntity.ok(responseDTO);
    }
    
    /**
     * Busca jefes directos por nombre (búsqueda parcial).
     * 
     * @param nombre Nombre o parte del nombre a buscar
     * @return ResponseEntity con lista de jefes directos encontrados y estado 200 OK
     */
    @Operation(
        summary = "Buscar jefes directos por nombre",
        description = "Realiza una búsqueda parcial de jefes directos por su nombre. " +
                      "Permite encontrar supervisores aunque no se conozca el nombre completo."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Búsqueda completada exitosamente (puede retornar lista vacía si no hay coincidencias)"
        )
    })
    @GetMapping("/buscar")
    public ResponseEntity<List<JefeDirectoResponseDTO>> buscarJefesDirectosPorNombre(
            @Parameter(description = "Nombre o parte del nombre del jefe directo", example = "Juan") @RequestParam String nombre) {
        
        List<JefeDirectoResponseDTO> jefesDirectos = jefeDirectoService.buscarJefesDirectosPorNombre(nombre);
        return ResponseEntity.ok(jefesDirectos);
    }
    
    /**
     * Actualiza los datos de un jefe directo existente.
     * 
     * @param id ID del jefe directo a actualizar
     * @param requestDTO Nuevos datos del jefe directo
     * @return ResponseEntity con el jefe directo actualizado y estado 200 OK
     */
    @Operation(
        summary = "⚠️ [NO FUNCIONAL] Actualizar jefe directo",
        description = "**ADVERTENCIA**: Este endpoint fue deshabilitado en PR #25. " +
                      "La gestión de jefes directos se realiza automáticamente al crear prácticas. " +
                      "Use únicamente los endpoints GET para consultas."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "500",
            description = "Método no implementado (lanza UnsupportedOperationException)",
            content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
        )
    })
    @Deprecated
    @PutMapping("/{id}")
    public ResponseEntity<JefeDirectoResponseDTO> actualizarJefeDirecto(
            @PathVariable Long id,
            @Valid @RequestBody JefeDirectoRequestDTO requestDTO) {
        
        JefeDirectoResponseDTO responseDTO = jefeDirectoService.actualizarJefeDirecto(id, requestDTO);
        return ResponseEntity.ok(responseDTO);
    }
    
    /**
     * Elimina un jefe directo del sistema.
     * 
     * @param id ID del jefe directo a eliminar
     * @return ResponseEntity vacío con estado 204 NO CONTENT
     */
    @Operation(
        summary = "⚠️ [NO FUNCIONAL] Eliminar jefe directo",
        description = "**ADVERTENCIA**: Este endpoint fue deshabilitado en PR #25. " +
                      "La gestión de jefes directos se realiza automáticamente al crear prácticas. " +
                      "Use únicamente los endpoints GET para consultas."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "500",
            description = "Método no implementado (lanza UnsupportedOperationException)",
            content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
        )
    })
    @Deprecated
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarJefeDirecto(@PathVariable Long id) {
        jefeDirectoService.eliminarJefeDirecto(id);
        return ResponseEntity.noContent().build();
    }
}
