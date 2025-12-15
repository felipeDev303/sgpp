package ipss.cl.sgpp.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "DTO de respuesta con información completa de una práctica profesional")
public class PracticaResponseDTO {
    
    @Schema(description = "ID único de la práctica", example = "1")
    private Long id;
    
    @Schema(description = "Fecha de inicio de la práctica", example = "2025-01-15")
    private LocalDate fechaInicio;
    
    @Schema(description = "Fecha de término de la práctica", example = "2025-06-30")
    private LocalDate fechaTermino;
    
    @Schema(description = "Descripción de las actividades realizadas", example = "Desarrollo de aplicaciones web con Spring Boot")
    private String descripcionActividades;
    
    @Schema(description = "Estado actual de la práctica", example = "EN_CURSO")
    private EstadoPractica estado;
    
    @Schema(description = "Descripción legible del estado", example = "En Curso")
    private String estadoDescripcion;
    
    // Datos del estudiante
    @Schema(description = "ID del estudiante", example = "1")
    private Long estudianteId;
    
    @Schema(description = "Nombre completo del estudiante", example = "Juan Pérez García")
    private String estudianteNombre;
    
    @Schema(description = "RUT del estudiante", example = "12345678-9")
    private String estudianteRut;
    
    @Schema(description = "Carrera del estudiante", example = "Ingeniería en Informática")
    private String estudianteCarrera;
    
    // Datos del profesor (puede ser null)
    @Schema(description = "ID del profesor guía (puede ser null)", example = "2")
    private Long profesorId;
    
    @Schema(description = "Nombre completo del profesor", example = "María González López")
    private String profesorNombre;
    
    @Schema(description = "Departamento del profesor", example = "Departamento de Informática")
    private String profesorDepartamento;
    
    // Datos de la empresa
    @Schema(description = "ID de la empresa", example = "1")
    private Long empresaId;
    
    @Schema(description = "Nombre de la empresa", example = "Tech Solutions SpA")
    private String empresaNombre;
    
    @Schema(description = "RUT de la empresa", example = "76123456-7")
    private String empresaRut;
    
    // Datos del jefe directo
    @Schema(description = "ID del jefe directo", example = "1")
    private Long jefeDirectoId;
    
    @Schema(description = "Nombre del jefe directo o supervisor", example = "Carlos Ramírez Soto")
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
