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
    @Column(name = "id_almacen", insertable = false, updatable = false)
    private Integer idAlmacen;


    @Column(name = "nombre", length = 100, nullable = false)
    private String nombre;


    @Column(name = "ubicacion", length = 150)
    private String ubicacion;


    @Column(name = "telefono", length = 20)
    private String telefono;


    @Column(name = "responsable", length = 100)
    private String responsable;


    @Column(name = "codigo_almacen", length = 20, nullable = false, unique = true)
    private String codigoAlmacen;


    @Column(name = "tipo_producto", length = 50)
    private String tipoProducto;


    @Column(name = "cantidad_botellas")
    private Integer cantidadBotellas;


    @Column(name = "estado", length = 20, nullable = false)
    private String estado;


    @Column(name = "ubigeo_id", length = 6, nullable = false)
    private String ubigeoId;


    // ==========================================
    // AUDITORÍA
    // ==========================================


    @Column(name = "fecha_creacion", nullable = false, updatable = false)
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
        if (this.estado == null || this.estado.isEmpty()) {
            this.estado = "Activo";
        }
    }


    @PreUpdate
    public void preUpdate() {
        this.fechaActualizacion = LocalDateTime.now();
    }
}

