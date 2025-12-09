package ipss.cl.sgpp.controller;

import ipss.cl.sgpp.model.Practica;
import ipss.cl.sgpp.service.PracticaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController // Indica que esta clase manejará peticiones REST
@RequestMapping("/api/v1/practicas") // Define la URL base para todos los métodos
public class PracticaController {

    @Autowired
    private PracticaService practicaService; // Inyectamos nuestro servicio de lógica de negocio

    // =====================================================================
    // C - CREATE (Crear Práctica)
    // Para Estudiantes y Profesores (Indicador 1)
    // =====================================================================
    // Mapea peticiones POST a /api/v1/practicas
    @PostMapping 
    public ResponseEntity<Practica> crearPractica(@Valid @RequestBody Practica practica) {
        Practica nuevaPractica = practicaService.guardarPractica(practica);
        // Retornamos el estado 201 Created junto con la práctica guardada
        return new ResponseEntity<>(nuevaPractica, HttpStatus.CREATED); 
    }

    // =====================================================================
    // R - READ (Leer Prácticas)
    // Para Profesores (todas) y Estudiantes (sus propias - lo haremos más tarde con seguridad)
    // =====================================================================
    // Mapea peticiones GET a /api/v1/practicas
    @GetMapping 
    public ResponseEntity<List<Practica>> obtenerTodasLasPracticas() {
        // En un paso posterior, filtraremos esta lista según el rol del usuario (Profesor vs Estudiante)
        List<Practica> practicas = practicaService.obtenerTodasLasPracticas();
        return ResponseEntity.ok(practicas); // Retorna estado 200 OK
    }
    
    // Mapea peticiones GET a /api/v1/practicas/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Practica> obtenerPracticaPorId(@PathVariable Long id) {
        return practicaService.obtenerPracticaPorId(id)
            .map(ResponseEntity::ok) // Si encuentra, retorna 200 OK
            .orElseGet(() -> ResponseEntity.notFound().build()); // Si no encuentra, retorna 404 Not Found
    }

    // =====================================================================
    // U - UPDATE (Actualizar Práctica)
    // Exclusivo para Profesores (lo aseguraremos con Spring Security en el paso 6)
    // =====================================================================
    // Mapea peticiones PUT a /api/v1/practicas/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Practica> actualizarPractica(@PathVariable Long id, @Valid @RequestBody Practica practicaActualizada) {
        try {
            Practica practica = practicaService.actualizarPractica(id, practicaActualizada);
            return ResponseEntity.ok(practica);
        } catch (RuntimeException e) {
            // Manejo básico de error si el ID no existe
            return ResponseEntity.notFound().build(); 
        }
    }

    // =====================================================================
    // D - DELETE (Eliminar Práctica)
    // Exclusivo para Profesores (lo aseguraremos con Spring Security en el paso 6)
    // =====================================================================
    // Mapea peticiones DELETE a /api/v1/practicas/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPractica(@PathVariable Long id) {
        try {
            practicaService.eliminarPractica(id);
            // Retorna estado 204 No Content, que es la convención para una eliminación exitosa
            return ResponseEntity.noContent().build(); 
        } catch (RuntimeException e) {
            // Manejo básico si el ID no existe
            return ResponseEntity.notFound().build();
        }
    }
}