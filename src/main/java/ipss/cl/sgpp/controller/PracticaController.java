package ipss.cl.sgpp.controller;

import ipss.cl.sgpp.dto.request.PracticaRequestDTO;
import ipss.cl.sgpp.dto.response.PracticaResponseDTO;
import ipss.cl.sgpp.service.PracticaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/practicas")
@RequiredArgsConstructor
public class PracticaController {

    private final PracticaService practicaService;

    /**
     * Crear una nueva práctica.
     * Endpoint para estudiantes y profesores.
     * 
     * @param requestDTO Datos de la práctica a crear
     * @return ResponseEntity con la práctica creada y estado HTTP 201
     */
    @PostMapping 
    public ResponseEntity<PracticaResponseDTO> crearPractica(@Valid @RequestBody PracticaRequestDTO requestDTO) {
        PracticaResponseDTO nuevaPractica = practicaService.guardarPractica(requestDTO);
        return new ResponseEntity<>(nuevaPractica, HttpStatus.CREATED); 
    }

    /**
     * Obtener todas las prácticas.
     * Endpoint para profesores (todas) y estudiantes (filtradas por seguridad más adelante).
     * 
     * @return ResponseEntity con lista de prácticas y estado HTTP 200
     */
    @GetMapping 
    public ResponseEntity<List<PracticaResponseDTO>> obtenerTodasLasPracticas() {
        List<PracticaResponseDTO> practicas = practicaService.obtenerTodasLasPracticas();
        return ResponseEntity.ok(practicas);
    }
    
    /**
     * Obtener una práctica por su ID.
     * 
     * @param id ID de la práctica
     * @return ResponseEntity con la práctica encontrada y estado HTTP 200
     * @throws ResourceNotFoundException si la práctica no existe (manejado por GlobalExceptionHandler)
     */
    @GetMapping("/{id}")
    public ResponseEntity<PracticaResponseDTO> obtenerPracticaPorId(@PathVariable Long id) {
        PracticaResponseDTO practica = practicaService.obtenerPracticaPorId(id);
        return ResponseEntity.ok(practica);
    }

    /**
     * Actualizar una práctica existente.
     * Exclusivo para profesores (se asegurará con Spring Security posteriormente).
     * 
     * @param id ID de la práctica a actualizar
     * @param requestDTO Nuevos datos de la práctica
     * @return ResponseEntity con la práctica actualizada y estado HTTP 200
     * @throws ResourceNotFoundException si la práctica no existe (manejado por GlobalExceptionHandler)
     * @throws BusinessException si hay errores de validación de negocio (manejado por GlobalExceptionHandler)
     */
    @PutMapping("/{id}")
    public ResponseEntity<PracticaResponseDTO> actualizarPractica(
            @PathVariable Long id, 
            @Valid @RequestBody PracticaRequestDTO requestDTO) {
        PracticaResponseDTO practica = practicaService.actualizarPractica(id, requestDTO);
        return ResponseEntity.ok(practica);
    }

    /**
     * Eliminar una práctica.
     * Exclusivo para profesores (se asegurará con Spring Security posteriormente).
     * 
     * @param id ID de la práctica a eliminar
     * @return ResponseEntity con estado HTTP 204 No Content
     * @throws ResourceNotFoundException si la práctica no existe (manejado por GlobalExceptionHandler)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPractica(@PathVariable Long id) {
        practicaService.eliminarPractica(id);
        return ResponseEntity.noContent().build(); 
    }
    
    /**
     * Obtener todas las prácticas de un estudiante específico.
     * 
     * @param estudianteId ID del estudiante
     * @return ResponseEntity con lista de prácticas del estudiante y estado HTTP 200
     * @throws ResourceNotFoundException si el estudiante no existe (manejado por GlobalExceptionHandler)
     */
    @GetMapping("/estudiante/{estudianteId}")
    public ResponseEntity<List<PracticaResponseDTO>> obtenerPracticasPorEstudiante(@PathVariable Long estudianteId) {
        List<PracticaResponseDTO> practicas = practicaService.obtenerPracticasPorEstudiante(estudianteId);
        return ResponseEntity.ok(practicas);
    }
}