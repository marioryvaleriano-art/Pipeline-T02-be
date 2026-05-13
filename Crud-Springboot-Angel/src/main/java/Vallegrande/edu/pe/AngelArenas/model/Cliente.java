package Vallegrande.edu.pe.AngelArenas.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Cliente")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cliente")
    private Integer id;

    @Column(name = "tipo_documento", nullable = false, length = 3)
    private String tipoDocumento;

    @Pattern(regexp = "^[0-9]{8,12}$", message = "El número de documento solo debe contener entre 8 y 12 dígitos")
    @Column(name = "numero_docum", nullable = false, length = 12)
    private String numeroDocum;

    @Column(nullable = false, length = 50)
    private String nombre;

    @Column(nullable = false, length = 50)
    private String apellido;

    @Email(message = "El correo no tiene un formato válido")
    @Column(nullable = false, unique = true, length = 150)
    private String correo;

    @Pattern(regexp = "^[+]?[0-9]{7,15}$", message = "El teléfono solo debe contener números y opcionalmente el signo +")
    @Column(nullable = false, length = 15)
    private String telefono;

    @Column(name = "fecha_nacimiento", nullable = false)
    private LocalDate fechaNacimiento;

    @Column(nullable = false)
    private Boolean estado = true;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    // Audit fields
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    @Column(name = "fecha_eliminacion")
    private LocalDateTime fechaEliminacion;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    @Column(name = "fecha_restauracion")
    private LocalDateTime fechaRestauracion;
}
