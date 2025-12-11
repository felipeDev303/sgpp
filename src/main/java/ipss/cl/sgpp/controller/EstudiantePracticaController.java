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

/**
 * Controlador REST para gestión de prácticas desde la perspectiva del estudiante.
 * 
 * Rutas bajo /api/v1/estudiantes/practicas
 * 
 * Funcionalidades permitidas para estudiantes (según requisitos):
 * - Agregar Práctica: Crear nuevos registros de prácticas
 * - Leer Práctica: Consultar detalles de sus propias prácticas
 * 
 * @author SGPP Team
 * @since 1.0
 */
@RestController
@RequestMapping("/api/v1/estudiantes/practicas")
@RequiredArgsConstructor
public class EstudiantePracticaController {

    private final PracticaService practicaService;

    /**
     * Crear una nueva práctica (estudiante registra su propia práctica).
     * 
     * Requisito: "Los estudiantes pueden ingresar nuevos registros de sus prácticas profesionales"
     * 
     * @param requestDTO Datos de la práctica a crear con validaciones
     * @return ResponseEntity con la práctica creada y estado HTTP 201
     */
    @PostMapping
    public ResponseEntity<PracticaResponseDTO> crearPractica(
            @Valid @RequestBody PracticaRequestDTO requestDTO) {
        PracticaResponseDTO nuevaPractica = practicaService.guardarPractica(requestDTO);
        return new ResponseEntity<>(nuevaPractica, HttpStatus.CREATED);
    }

    /**
     * Obtener todas las prácticas de un estudiante específico.
     * 
     * Requisito: "Permite a los estudiantes consultar los detalles de sus prácticas registradas"
     * 
     * Nota: En un sistema con autenticación, el estudianteId vendría del token JWT.
     * Por ahora se recibe como parámetro de ruta.
     * 
     * @param estudianteId ID del estudiante (en producción vendría del contexto de seguridad)
     * @return ResponseEntity con lista de prácticas del estudiante y estado HTTP 200
     */
    @GetMapping("/estudiante/{estudianteId}")
    public ResponseEntity<List<PracticaResponseDTO>> obtenerMisPracticas(
            @PathVariable Long estudianteId) {
        List<PracticaResponseDTO> practicas = practicaService.obtenerPracticasPorEstudiante(estudianteId);
        return ResponseEntity.ok(practicas);
    }

    /**
     * Obtener una práctica específica por su ID.
     * 
     * Requisito: "Consultar los detalles de sus prácticas registradas"
     * 
     * Nota: En un sistema con autenticación, se validaría que el estudiante
     * solo pueda ver sus propias prácticas.
     * 
     * @param id ID de la práctica
     * @return ResponseEntity con la práctica encontrada y estado HTTP 200
     * @throws ResourceNotFoundException si la práctica no existe o está eliminada
     */
    @GetMapping("/{id}")
    public ResponseEntity<PracticaResponseDTO> obtenerPracticaPorId(@PathVariable Long id) {
        PracticaResponseDTO practica = practicaService.obtenerPracticaPorId(id);
        return ResponseEntity.ok(practica);
    }

    /**
     * Obtener todas las prácticas activas (para vista general del estudiante).
     * 
     * Nota: En producción, este endpoint podría estar restringido o filtrado
     * automáticamente por el ID del estudiante autenticado.
     * 
     * @return ResponseEntity con lista de todas las prácticas activas y estado HTTP 200
     */
    @GetMapping
    public ResponseEntity<List<PracticaResponseDTO>> listarPracticasActivas() {
        List<PracticaResponseDTO> practicas = practicaService.obtenerTodasLasPracticas();
        return ResponseEntity.ok(practicas);
    }
}
