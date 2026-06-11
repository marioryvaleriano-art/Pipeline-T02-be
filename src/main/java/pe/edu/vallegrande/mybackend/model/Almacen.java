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

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "ubicacion")
    private String ubicacion;

    @Column(name = "telefono")
    private String telefono;

    @Column(name = "responsable")
    private String responsable;

    @Column(name = "codigo_almacen", nullable = false, unique = true)
    private String codigoAlmacen;

    @Column(name = "tipo_producto")
    private String tipoProducto;

    @Column(name = "cantidad_botellas")
    private Integer cantidadBotellas;

    @Column(name = "estado")
    private String estado;

    @Column(name = "ubigeo_id", nullable = false)
    private String ubigeoId;

    @Column(name = "fecha_creacion", updatable = false)
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

        if (this.estado == null) {
            this.estado = "Activo";
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.fechaActualizacion = LocalDateTime.now();
    }
}