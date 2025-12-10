package ipss.cl.sgpp.dto.response;

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
    
    // Datos del estudiante
    private Long estudianteId;
    private String estudianteNombre;
    private String estudianteRut;
    private String estudianteCarrera;
    
    // Datos del profesor supervisor (puede ser null)
    private Long profesorSupervisorId;
    private String profesorSupervisorNombre;
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
            .descripcionActividades(practica.getDescripcionActividades());
        
        // Datos del estudiante
        if (practica.getEstudiante() != null) {
            builder.estudianteId(practica.getEstudiante().getId())
                   .estudianteNombre(practica.getEstudiante().getNombreCompleto())
                   .estudianteRut(practica.getEstudiante().getRut())
                   .estudianteCarrera(practica.getEstudiante().getCarrera());
        }
        
        // Datos del profesor supervisor (opcional)
        if (practica.getProfesorSupervisor() != null) {
            builder.profesorSupervisorId(practica.getProfesorSupervisor().getId())
                   .profesorSupervisorNombre(practica.getProfesorSupervisor().getNombreCompleto())
                   .profesorDepartamento(practica.getProfesorSupervisor().getDepartamento());
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
                   .jefeDirectoNombre(practica.getJefeDirecto().getNombreCompleto());
        }
        
        return builder.build();
    }
}
