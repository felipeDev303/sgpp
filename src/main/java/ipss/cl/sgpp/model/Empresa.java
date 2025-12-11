package ipss.cl.sgpp.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "empresas")
@Data
public class Empresa {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre de la empresa es obligatorio")
    @Size(max = 255, message = "El nombre no puede superar 255 caracteres")
    @Column(nullable = false, length = 255)
    private String nombre;

    @NotBlank(message = "El RUT de la empresa es obligatorio")
    @Pattern(regexp = "^\\d{7,8}-[\\dkK]$", message = "El RUT debe tener el formato 12345678-9 o 1234567-K")
    @Column(nullable = false, unique = true, length = 15)
    private String rut;

    @Size(max = 255, message = "La dirección no puede superar 255 caracteres")
    @Column(length = 255)
    private String direccion;

    @Email(message = "El email de contacto debe tener un formato válido")
    @Size(max = 255, message = "El email no puede superar 255 caracteres")
    @Column(name = "contacto_email", length = 255)
    private String contactoEmail;
    
    @OneToMany(mappedBy = "empresa")
    private List<Practica> practicas;
}
