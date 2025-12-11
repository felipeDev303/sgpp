package ipss.cl.sgpp.controller;

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
     */
    @PostMapping
    public ResponseEntity<EstudianteResponseDTO> crearEstudiante(
            @Valid @RequestBody EstudianteRequestDTO requestDTO) {
        
        EstudianteResponseDTO responseDTO = estudianteService.crearEstudiante(requestDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }
    
    /**
     * Obtiene la lista de todos los estudiantes registrados.
     * 
     * @return ResponseEntity con lista de estudiantes y estado 200 OK
     */
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
    @GetMapping("/{id}")
    public ResponseEntity<EstudianteResponseDTO> obtenerEstudiantePorId(@PathVariable Long id) {
        EstudianteResponseDTO responseDTO = estudianteService.obtenerEstudiantePorId(id);
        return ResponseEntity.ok(responseDTO);
    }
    
    /**
     * Busca un estudiante por su email.
     * 
     * @param email Email del estudiante a buscar
     * @return ResponseEntity con el estudiante encontrado y estado 200 OK
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<EstudianteResponseDTO> obtenerEstudiantePorEmail(@PathVariable String email) {
        EstudianteResponseDTO responseDTO = estudianteService.obtenerEstudiantePorEmail(email);
        return ResponseEntity.ok(responseDTO);
    }
    
    /**
     * Busca un estudiante por su RUT.
     * 
     * @param rut RUT del estudiante a buscar
     * @return ResponseEntity con el estudiante encontrado y estado 200 OK
     */
    @GetMapping("/rut/{rut}")
    public ResponseEntity<EstudianteResponseDTO> obtenerEstudiantePorRut(@PathVariable String rut) {
        EstudianteResponseDTO responseDTO = estudianteService.obtenerEstudiantePorRut(rut);
        return ResponseEntity.ok(responseDTO);
    }
    
    /**
     * Actualiza los datos de un estudiante existente.
     * 
     * @param id ID del estudiante a actualizar
     * @param requestDTO Nuevos datos del estudiante
     * @return ResponseEntity con el estudiante actualizado y estado 200 OK
     */
    @PutMapping("/{id}")
    public ResponseEntity<EstudianteResponseDTO> actualizarEstudiante(
            @PathVariable Long id,
            @Valid @RequestBody EstudianteRequestDTO requestDTO) {
        
        EstudianteResponseDTO responseDTO = estudianteService.actualizarEstudiante(id, requestDTO);
        return ResponseEntity.ok(responseDTO);
    }
    
    /**
     * Elimina un estudiante del sistema.
     * 
     * @param id ID del estudiante a eliminar
     * @return ResponseEntity vacío con estado 204 NO CONTENT
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarEstudiante(@PathVariable Long id) {
        estudianteService.eliminarEstudiante(id);
        return ResponseEntity.noContent().build();
    }
}
