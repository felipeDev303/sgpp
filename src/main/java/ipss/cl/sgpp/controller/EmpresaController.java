package ipss.cl.sgpp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import ipss.cl.sgpp.dto.request.EmpresaRequestDTO;
import ipss.cl.sgpp.dto.response.EmpresaResponseDTO;
import ipss.cl.sgpp.dto.common.ErrorResponseDTO;
import ipss.cl.sgpp.service.EmpresaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para consultas de empresas.
 * 
 * NOTA: Los endpoints POST, PUT, DELETE ya NO están soportados en EmpresaService
 * (simplificado a solo consultas). La gestión de empresas se realiza externamente.
 * Solo endpoints GET están funcionales.
 * 
 * @deprecated Los métodos CRUD (crear, actualizar, eliminar) fueron removidos en PR #25
 */
@Tag(
    name = "Empresas",
    description = "Endpoints para consultar información de empresas. " +
                  "**NOTA**: Los métodos CRUD (POST, PUT, DELETE) fueron removidos en PR #25 " +
                  "ya que la gestión de empresas se realiza externamente. Solo se mantienen endpoints de consulta."
)
@RestController
@RequestMapping("/api/v1/empresas")
@RequiredArgsConstructor
public class EmpresaController {

    private final EmpresaService empresaService;
    
    /**
     * Crea una nueva empresa en el sistema.
     * 
     * @param requestDTO Datos de la empresa a crear
     * @return ResponseEntity con la empresa creada y estado 201 CREATED
     */
    @Operation(
        summary = "⚠️ [NO FUNCIONAL] Crear empresa",
        description = "**ADVERTENCIA**: Este endpoint fue deshabilitado en PR #25. " +
                      "La gestión de empresas se realiza externamente. " +
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
    public ResponseEntity<EmpresaResponseDTO> crearEmpresa(
            @Valid @RequestBody EmpresaRequestDTO requestDTO) {
        throw new UnsupportedOperationException("Endpoint deshabilitado en PR #25. Use solo endpoints GET para consultas.");
    }
    
    /**
     * Obtiene la lista de todas las empresas registradas.
     * 
     * @return ResponseEntity con lista de empresas y estado 200 OK
     */
    @Operation(
        summary = "Listar todas las empresas",
        description = "Retorna la lista completa de empresas registradas en el sistema. " +
                      "Útil para seleccionar empresas al crear prácticas profesionales."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Lista de empresas obtenida exitosamente"
        )
    })
    @GetMapping
    public ResponseEntity<List<EmpresaResponseDTO>> obtenerTodasLasEmpresas() {
        List<EmpresaResponseDTO> empresas = empresaService.obtenerTodasLasEmpresas();
        return ResponseEntity.ok(empresas);
    }
    
    /**
     * Obtiene una empresa específica por su ID.
     * 
     * @param id ID de la empresa a buscar
     * @return ResponseEntity con la empresa encontrada y estado 200 OK
     */
    @Operation(
        summary = "Obtener empresa por ID",
        description = "Busca y retorna una empresa específica según su identificador único."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Empresa encontrada exitosamente"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Empresa no encontrada con el ID especificado",
            content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
        )
    })
    @GetMapping("/{id}")
    public ResponseEntity<EmpresaResponseDTO> obtenerEmpresaPorId(
            @Parameter(description = "ID de la empresa", example = "1") @PathVariable Long id) {
        EmpresaResponseDTO responseDTO = empresaService.obtenerEmpresaPorId(id);
        return ResponseEntity.ok(responseDTO);
    }
    
    /**
     * Busca una empresa por su RUT.
     * 
     * @param rut RUT de la empresa a buscar
     * @return ResponseEntity con la empresa encontrada y estado 200 OK
     */
    @Operation(
        summary = "Buscar empresa por RUT",
        description = "Busca y retorna una empresa específica según su RUT (identificador único tributario)."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Empresa encontrada exitosamente"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Empresa no encontrada con el RUT especificado",
            content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
        )
    })
    @GetMapping("/rut/{rut}")
    public ResponseEntity<EmpresaResponseDTO> obtenerEmpresaPorRut(
            @Parameter(description = "RUT de la empresa", example = "76.123.456-7") @PathVariable String rut) {
        EmpresaResponseDTO responseDTO = empresaService.obtenerEmpresaPorRut(rut);
        return ResponseEntity.ok(responseDTO);
    }
    
    /**
     * Busca una empresa por su nombre.
     * 
     * @param nombre Nombre de la empresa a buscar
     * @return ResponseEntity con la empresa encontrada y estado 200 OK
     */
    @Operation(
        summary = "Buscar empresa por nombre",
        description = "Busca y retorna una empresa específica según su razón social o nombre comercial."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Empresa encontrada exitosamente"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Empresa no encontrada con el nombre especificado",
            content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
        )
    })
    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<EmpresaResponseDTO> obtenerEmpresaPorNombre(
            @Parameter(description = "Nombre de la empresa", example = "Empresa Ejemplo S.A.") @PathVariable String nombre) {
        EmpresaResponseDTO responseDTO = empresaService.obtenerEmpresaPorNombre(nombre);
        return ResponseEntity.ok(responseDTO);
    }
    
    /**
     * Actualiza los datos de una empresa existente.
     * 
     * @param id ID de la empresa a actualizar
     * @param requestDTO Nuevos datos de la empresa
     * @return ResponseEntity con la empresa actualizada y estado 200 OK
     */
    @Operation(
        summary = "⚠️ [NO FUNCIONAL] Actualizar empresa",
        description = "**ADVERTENCIA**: Este endpoint fue deshabilitado en PR #25. " +
                      "La gestión de empresas se realiza externamente. " +
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
    public ResponseEntity<EmpresaResponseDTO> actualizarEmpresa(
            @PathVariable Long id,
            @Valid @RequestBody EmpresaRequestDTO requestDTO) {
        throw new UnsupportedOperationException("Endpoint deshabilitado en PR #25. Use solo endpoints GET para consultas.");
    }
    
    /**
     * Elimina una empresa del sistema.
     * 
     * @param id ID de la empresa a eliminar
     * @return ResponseEntity vacío con estado 204 NO CONTENT
     */
    @Operation(
        summary = "⚠️ [NO FUNCIONAL] Eliminar empresa",
        description = "**ADVERTENCIA**: Este endpoint fue deshabilitado en PR #25. " +
                      "La gestión de empresas se realiza externamente. " +
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
    public ResponseEntity<Void> eliminarEmpresa(@PathVariable Long id) {
        throw new UnsupportedOperationException("Endpoint deshabilitado en PR #25. Use solo endpoints GET para consultas.");
    }
}
