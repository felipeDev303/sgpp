package ipss.cl.sgpp.dto.response;

import ipss.cl.sgpp.model.EstadoPractica;
import ipss.cl.sgpp.model.Practica;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PracticaResponseDTO {
    
    private Long id;
    private LocalDate fechaInicio;
    private LocalDate fechaTermino;
    private String descripcionActividades;
    private EstadoPractica estado;
    private String estadoDescripcion;
    
    // Datos del estudiante
    private Long estudianteId;
    private String estudianteNombre;
    private String estudianteRut;
    private String estudianteCarrera;
    
    // Datos del profesor (puede ser null)
    private Long profesorId;
    private String profesorNombre;
    private String profesorDepartamento;
    
    // Datos de la empresa
    private Long empresaId;
    private String empresaNombre;
    private String empresaRut;
    
    // Datos del jefe directo
    private Long jefeDirectoId;
    private String jefeDirectoNombre;
    
    /**
     * Método factory para convertir una entidad Practica a DTO
     * 
     * @param practica Entidad de práctica
     * @return DTO con los datos de la práctica
     */
    public static PracticaResponseDTO from(Practica practica) {
        if (practica == null) {
            return null;
        }
        
        PracticaResponseDTOBuilder builder = PracticaResponseDTO.builder()
            .id(practica.getId())
            .fechaInicio(practica.getFechaInicio())
            .fechaTermino(practica.getFechaTermino())
            .descripcionActividades(practica.getDescripcionActividades())
            .estado(practica.getEstado())
            .estadoDescripcion(practica.getEstado() != null ? practica.getEstado().getDescripcion() : null);
        
        // Datos del estudiante
        if (practica.getEstudiante() != null) {
            builder.estudianteId(practica.getEstudiante().getId())
                   .estudianteNombre(practica.getEstudiante().getNombreCompleto())
                   .estudianteRut(practica.getEstudiante().getRut())
                   .estudianteCarrera(practica.getEstudiante().getCarrera());
        }
        
        // Datos del profesor (opcional)
        if (practica.getProfesor() != null) {
            builder.profesorId(practica.getProfesor().getId())
                   .profesorNombre(practica.getProfesor().getNombreCompleto())
                   .profesorDepartamento(practica.getProfesor().getDepartamento());
        }
        
        // Datos de la empresa
        if (practica.getEmpresa() != null) {
            builder.empresaId(practica.getEmpresa().getId())
                   .empresaNombre(practica.getEmpresa().getNombre())
                   .empresaRut(practica.getEmpresa().getRut());
        }
        
        // Datos del jefe directo
        if (practica.getJefeDirecto() != null) {
            builder.jefeDirectoId(practica.getJefeDirecto().getId())
                   .jefeDirectoNombre(practica.getJefeDirecto().getNombre());
        }
        
        return builder.build();
    }
}
