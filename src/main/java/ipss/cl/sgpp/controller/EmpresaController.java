package ipss.cl.sgpp.controller;

import ipss.cl.sgpp.dto.request.EmpresaRequestDTO;
import ipss.cl.sgpp.dto.response.EmpresaResponseDTO;
import ipss.cl.sgpp.service.EmpresaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión de empresas.
 * Expone endpoints para operaciones CRUD.
 */
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
    @PostMapping
    public ResponseEntity<EmpresaResponseDTO> crearEmpresa(
            @Valid @RequestBody EmpresaRequestDTO requestDTO) {
        
        EmpresaResponseDTO responseDTO = empresaService.crearEmpresa(requestDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }
    
    /**
     * Obtiene la lista de todas las empresas registradas.
     * 
     * @return ResponseEntity con lista de empresas y estado 200 OK
     */
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
    @GetMapping("/{id}")
    public ResponseEntity<EmpresaResponseDTO> obtenerEmpresaPorId(@PathVariable Long id) {
        EmpresaResponseDTO responseDTO = empresaService.obtenerEmpresaPorId(id);
        return ResponseEntity.ok(responseDTO);
    }
    
    /**
     * Busca una empresa por su RUT.
     * 
     * @param rut RUT de la empresa a buscar
     * @return ResponseEntity con la empresa encontrada y estado 200 OK
     */
    @GetMapping("/rut/{rut}")
    public ResponseEntity<EmpresaResponseDTO> obtenerEmpresaPorRut(@PathVariable String rut) {
        EmpresaResponseDTO responseDTO = empresaService.obtenerEmpresaPorRut(rut);
        return ResponseEntity.ok(responseDTO);
    }
    
    /**
     * Busca una empresa por su nombre.
     * 
     * @param nombre Nombre de la empresa a buscar
     * @return ResponseEntity con la empresa encontrada y estado 200 OK
     */
    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<EmpresaResponseDTO> obtenerEmpresaPorNombre(@PathVariable String nombre) {
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
    @PutMapping("/{id}")
    public ResponseEntity<EmpresaResponseDTO> actualizarEmpresa(
            @PathVariable Long id,
            @Valid @RequestBody EmpresaRequestDTO requestDTO) {
        
        EmpresaResponseDTO responseDTO = empresaService.actualizarEmpresa(id, requestDTO);
        return ResponseEntity.ok(responseDTO);
    }
    
    /**
     * Elimina una empresa del sistema.
     * 
     * @param id ID de la empresa a eliminar
     * @return ResponseEntity vacío con estado 204 NO CONTENT
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarEmpresa(@PathVariable Long id) {
        empresaService.eliminarEmpresa(id);
        return ResponseEntity.noContent().build();
    }
}
