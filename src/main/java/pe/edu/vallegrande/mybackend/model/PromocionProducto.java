package pe.edu.vallegrande.mybackend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "promocion_producto")
public class PromocionProducto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_promocion_producto")
    private Integer idPromocionProducto;

    @Column(name = "id_promocion", nullable = false)
    private Integer idPromocion;

    @Column(name = "id_producto", nullable = false)
    private Integer idProducto;

    @Column(name = "aplica_descuento_adicional", nullable = false)
    private Boolean aplicaDescuentoAdicional = false;

    @Column(name = "fecha_asignacion", nullable = false)
    private LocalDateTime fechaAsignacion;

    // Relaciones (opcional para consultas)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_promocion", insertable = false, updatable = false)
    private Promocion promocion;

    // Constructores
    public PromocionProducto() {
        this.fechaAsignacion = LocalDateTime.now();
    }

    public PromocionProducto(Integer idPromocion, Integer idProducto, Boolean aplicaDescuentoAdicional) {
        this.idPromocion = idPromocion;
        this.idProducto = idProducto;
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

    public Integer getIdPromocion() {
        return idPromocion;
    }

    public void setIdPromocion(Integer idPromocion) {
        this.idPromocion = idPromocion;
    }

    public Integer getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Integer idProducto) {
        this.idProducto = idProducto;
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

    public Promocion getPromocion() {
        return promocion;
    }

    public void setPromocion(Promocion promocion) {
        this.promocion = promocion;
    }
}