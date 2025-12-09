package ipss.cl.sgpp.service;

import ipss.cl.sgpp.model.*;
import ipss.cl.sgpp.repository.PracticaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // La clave para el Indicador 2.3

import java.util.List;
import java.util.Optional;

@Service // Marca esta clase como un Componente de Servicio de Spring
public class PracticaService {

    // Inyección de Dependencias: Spring gestiona las instancias de los Repositorios
    @Autowired
    private PracticaRepository practicaRepository;
    
    // También necesitarías inyectar EstudianteRepository, EmpresaRepository, etc. 
    // si las usaras en este servicio para validar o buscar dependencias antes de guardar.
    // @Autowired
    // private EstudianteRepository estudianteRepository; 
    
    // =====================================================================
    // C - CREATE (Crear una Práctica)
    // =====================================================================
    // Usamos @Transactional para asegurar que si hay un error al guardar, 
    // toda la operación se revierta (ROLLBACK). ¡Esto es clave para la consistencia!
    @Transactional
    public Practica guardarPractica(Practica practica) {
        // Aquí podrías poner lógica de negocio, como calcular una fecha final, 
        // o validar que el estudiante exista antes de guardar.
        return practicaRepository.save(practica);
    }

    // =====================================================================
    // R - READ (Leer Prácticas)
    // =====================================================================
    
    // Para el perfil Profesor (puede ver todas las prácticas)
    public List<Practica> obtenerTodasLasPracticas() {
        return practicaRepository.findAll();
    }
    
    // Para el perfil Estudiante (solo ve las suyas)
    public List<Practica> obtenerPracticasPorEstudiante(Long estudianteId) {
        return practicaRepository.findByEstudianteId(estudianteId);
    }
    
    public Optional<Practica> obtenerPracticaPorId(Long id) {
        return practicaRepository.findById(id);
    }

    // =====================================================================
    // U - UPDATE (Actualizar una Práctica)
    // =====================================================================
    @Transactional
    public Practica actualizarPractica(Long id, Practica practicaActualizada) {
        // 1. Verificar si la práctica existe
        return practicaRepository.findById(id)
            .map(practicaExistente -> {
                // 2. Aplicar los cambios
                practicaExistente.setFechaInicio(practicaActualizada.getFechaInicio());
                practicaExistente.setFechaTermino(practicaActualizada.getFechaTermino());
                practicaExistente.setDescripcionActividades(practicaActualizada.getDescripcionActividades());
                // Importante: También actualizar las referencias (Estudiante, Profesor, etc.) si es necesario.

                // 3. Guardar y retornar la entidad actualizada
                return practicaRepository.save(practicaExistente);
            })
            // Si no existe, lanza una excepción (buena práctica)
            .orElseThrow(() -> new RuntimeException("Práctica no encontrada con ID: " + id));
    }

    // =====================================================================
    // D - DELETE (Eliminar una Práctica)
    // =====================================================================
    @Transactional
    public void eliminarPractica(Long id) {
        // En un caso real, siempre verifica primero si existe antes de eliminar
        if (!practicaRepository.existsById(id)) {
            throw new RuntimeException("Práctica no encontrada para eliminar con ID: " + id);
        }
        practicaRepository.deleteById(id);
    }
}