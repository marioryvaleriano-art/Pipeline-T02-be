package pe.edu.vallegrande.mybackend.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Data;

/**
 * Entidad que representa un producto en el sistema.
 * Contiene información básica del producto, sus atributos comerciales
 * y fechas de auditoría (creación, actualización, eliminación, restauración).
 * 
 * @author Equipo de desarrollo
 * @version 1.0
 */
@Data
@Entity
@Table(name = "producto")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto")
    /** Identificador único del producto (autogenerado) */
    private Integer idProducto;

    @Column(name = "nombre", nullable = false)
    /** Nombre comercial del producto (obligatorio) */
    private String nombre;

    @Column(name = "descripcion")
    /** Descripción detallada del producto (opcional) */
    private String descripcion;

    @Column(name = "codigo_barras_sku", nullable = false)
    /** Código de barras o SKU único del producto (obligatorio) */
    private String codigoBarrasSku;

    @Column(name = "unidad_medida", nullable = false)
    /** Unidad de medida: 750ml, 1 Litro, 500ml, Galon 4 Lt, Pack */
    private String unidadMedida;

    @Column(name = "precio_unitario", nullable = false)
    /** Precio unitario del producto en soles */
    private Double precioUnitario;

    @Column(name = "estado", nullable = false)
    /** Estado del producto: true = activo, false = inactivo/eliminado lógicamente */
    private Boolean estado;

    @Column(name = "id_categoria", nullable = false)
    /** ID de la categoría a la que pertenece el producto (1-15) */
    private Integer idCategoria;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    /** Fecha y hora de creación del registro (autogenerada) */
    private LocalDateTime fechaCreacion;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
    @Column(name = "fecha_actualizacion")
    /** Fecha y hora de la última actualización del registro */
    private LocalDateTime fechaActualizacion;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
    @Column(name = "fecha_eliminacion")
    /** Fecha y hora de eliminación lógica del registro */
    private LocalDateTime fechaEliminacion;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
    @Column(name = "fecha_restauracion")
    /** Fecha y hora de restauración del registro */
    private LocalDateTime fechaRestauracion;

    // =====================================================
    // RELACIÓN CON PROMOCION_PRODUCTO (NUEVO)
    // =====================================================
    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    /** Lista de relaciones promoción-producto asociadas (ignoradas en JSON para evitar ciclos) */
    private List<PromocionProducto> promocionProductos = new ArrayList<>();

    /**
     * Método ejecutado automáticamente antes de persistir la entidad.
     * Asigna la fecha de creación y establece estado activo por defecto.
     */
    @PrePersist
    protected void onCreate() {
        this.fechaCreacion = LocalDateTime.now();
        if (this.estado == null) this.estado = true;
    }
    
    /**
     * Método ejecutado automáticamente antes de actualizar la entidad.
     * Actualiza la fecha de modificación.
     */
    @PreUpdate
    protected void onUpdate() {
        this.fechaActualizacion = LocalDateTime.now();
    }
}