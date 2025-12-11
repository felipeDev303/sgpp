package ipss.cl.sgpp.controller;

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
 * Controlador REST para la gestión de profesores.
 * Expone endpoints para operaciones CRUD.
 */
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
     */
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
    @GetMapping("/{id}")
    public ResponseEntity<ProfesorResponseDTO> obtenerProfesorPorId(@PathVariable Long id) {
        ProfesorResponseDTO responseDTO = profesorService.obtenerProfesorPorId(id);
        return ResponseEntity.ok(responseDTO);
    }
    
    /**
     * Busca un profesor por su email.
     * 
     * @param email Email del profesor a buscar
     * @return ResponseEntity con el profesor encontrado y estado 200 OK
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<ProfesorResponseDTO> obtenerProfesorPorEmail(@PathVariable String email) {
        ProfesorResponseDTO responseDTO = profesorService.obtenerProfesorPorEmail(email);
        return ResponseEntity.ok(responseDTO);
    }
    
    /**
     * Obtiene todos los profesores de un departamento específico.
     * 
     * @param departamento Nombre del departamento
     * @return ResponseEntity con lista de profesores del departamento y estado 200 OK
     */
    @GetMapping("/departamento/{departamento}")
    public ResponseEntity<List<ProfesorResponseDTO>> obtenerProfesoresPorDepartamento(
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
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProfesorResponseDTO> actualizarProfesor(
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
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProfesor(@PathVariable Long id) {
        profesorService.eliminarProfesor(id);
        return ResponseEntity.noContent().build();
    }
}
