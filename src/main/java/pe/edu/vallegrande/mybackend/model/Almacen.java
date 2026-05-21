package pe.edu.vallegrande.mybackend.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "almacen")
public class Almacen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_almacen")
    private Integer idAlmacen;

    @Column(name = "nombre")
    private String nombre; // Bodega de vinos

    @Column(name = "ubicacion")
    private String ubicacion;

    @Column(name = "telefono")
    private String telefono;

    @Column(name = "responsable")
    private String responsable;

    @Column(name = "codigo_almacen")
    private String codigoAlmacen;

    @Column(name = "tipo_producto")
    private String tipoProducto; // vino / pisco

    @Column(name = "cantidad_botellas")
    private Integer cantidadBotellas;

    @Column(name = "estado")
    private Boolean estado;

    // AUDITORÍA
    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    @Column(name = "fecha_eliminacion")
    private LocalDateTime fechaEliminacion;

    @Column(name = "fecha_restauracion")
    private LocalDateTime fechaRestauracion;

    @PrePersist
    public void prePersist() {
        this.fechaCreacion = LocalDateTime.now();
        this.estado = true;
    }

    @PreUpdate
    public void preUpdate() {
        this.fechaActualizacion = LocalDateTime.now();
    }
}