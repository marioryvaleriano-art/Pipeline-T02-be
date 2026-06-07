package pe.edu.vallegrande.mybackend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "promocion_producto")
public class PromocionProducto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_promocion_producto")
    private Integer idPromocionProducto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_promocion", nullable = false)
    @JsonIgnore  // 👈 IGNORAR PARA EVITAR BUCLE
    private Promocion promocion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_producto", nullable = false)
    @JsonIgnore  // 👈 IGNORAR PARA EVITAR BUCLE
    private Producto producto;

    @Column(name = "aplica_descuento_adicional", nullable = false)
    private Boolean aplicaDescuentoAdicional = false;

    @Column(name = "fecha_asignacion", nullable = false)
    private LocalDateTime fechaAsignacion;

    // Campos adicionales para serialización (sin causar bucle)
    @Transient
    private Integer idPromocion;
    
    @Transient
    private Integer idProducto;

    // Constructores
    public PromocionProducto() {
        this.fechaAsignacion = LocalDateTime.now();
    }

    public PromocionProducto(Promocion promocion, Producto producto, Boolean aplicaDescuentoAdicional) {
        this.promocion = promocion;
        this.producto = producto;
        this.aplicaDescuentoAdicional = aplicaDescuentoAdicional;
        this.fechaAsignacion = LocalDateTime.now();
    }

    // Getters y Setters
    public Integer getIdPromocionProducto() {
        return idPromocionProducto;
    }

    public void setIdPromocionProducto(Integer idPromocionProducto) {
        this.idPromocionProducto = idPromocionProducto;
    }

    @JsonIgnore
    public Promocion getPromocion() {
        return promocion;
    }

    public void setPromocion(Promocion promocion) {
        this.promocion = promocion;
    }

    @JsonIgnore
    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    // Métodos para serializar solo los IDs (sin causar bucle)
    public Integer getIdPromocion() {
        return promocion != null ? promocion.getIdPromocion() : null;
    }

    public Integer getIdProducto() {
        return producto != null ? producto.getIdProducto() : null;
    }

    public Boolean getAplicaDescuentoAdicional() {
        return aplicaDescuentoAdicional;
    }

    public void setAplicaDescuentoAdicional(Boolean aplicaDescuentoAdicional) {
        this.aplicaDescuentoAdicional = aplicaDescuentoAdicional;
    }

    public LocalDateTime getFechaAsignacion() {
        return fechaAsignacion;
    }

    public void setFechaAsignacion(LocalDateTime fechaAsignacion) {
        this.fechaAsignacion = fechaAsignacion;
    }
}