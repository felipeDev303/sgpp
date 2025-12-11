package ipss.cl.sgpp.controller;

import ipss.cl.sgpp.dto.request.JefeDirectoRequestDTO;
import ipss.cl.sgpp.dto.response.JefeDirectoResponseDTO;
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
    @GetMapping("/{id}")
    public ResponseEntity<JefeDirectoResponseDTO> obtenerJefeDirectoPorId(@PathVariable Long id) {
        JefeDirectoResponseDTO responseDTO = jefeDirectoService.obtenerJefeDirectoPorId(id);
        return ResponseEntity.ok(responseDTO);
    }
    
    /**
     * Busca jefes directos por nombre (búsqueda parcial).
     * 
     * @param nombre Nombre o parte del nombre a buscar
     * @return ResponseEntity con lista de jefes directos encontrados y estado 200 OK
     */
    @GetMapping("/buscar")
    public ResponseEntity<List<JefeDirectoResponseDTO>> buscarJefesDirectosPorNombre(
            @RequestParam String nombre) {
        
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
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarJefeDirecto(@PathVariable Long id) {
        jefeDirectoService.eliminarJefeDirecto(id);
        return ResponseEntity.noContent().build();
    }
}
