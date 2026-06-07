package pe.edu.vallegrande.mybackend.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "promocion")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Promocion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_promocion")
    private Integer idPromocion;

    @Column(name = "nombre", nullable = false, length = 100, unique = true)
    private String nombre;

    @Column(name = "descripcion", length = 255)
    private String descripcion;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDateTime fechaInicio;

    @Column(name = "fecha_fin", nullable = false)
    private LocalDateTime fechaFin;

    @Column(name = "tipo_promocion", nullable = false, length = 1)
    private String tipoPromocion;

    @Column(name = "valor_descuento", nullable = false, precision = 10, scale = 2)
    private BigDecimal valorDescuento;

    @Column(name = "estado", nullable = false)
    private Boolean estado = true;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @OneToMany(mappedBy = "promocion", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore  // 👈 IGNORAR PARA EVITAR EL BUCLE
    private List<PromocionProducto> promocionProductos = new ArrayList<>();

    @Transient
    private Long diasVigencia;

    @Transient
    private Boolean activa;

    // Constructores
    public Promocion() {}

    public Promocion(String nombre, String descripcion, LocalDateTime fechaInicio, 
                     LocalDateTime fechaFin, String tipoPromocion, 
                     BigDecimal valorDescuento, Boolean estado) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.tipoPromocion = tipoPromocion;
        this.valorDescuento = valorDescuento;
        this.estado = estado;
        this.fechaCreacion = LocalDateTime.now();
    }

    // Getters y Setters
    public Integer getIdPromocion() {
        return idPromocion;
    }

    public void setIdPromocion(Integer idPromocion) {
        this.idPromocion = idPromocion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDateTime getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDateTime fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDateTime getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDateTime fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getTipoPromocion() {
        return tipoPromocion;
    }

    public void setTipoPromocion(String tipoPromocion) {
        this.tipoPromocion = tipoPromocion;
    }

    public BigDecimal getValorDescuento() {
        return valorDescuento;
    }

    public void setValorDescuento(BigDecimal valorDescuento) {
        this.valorDescuento = valorDescuento;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    @JsonIgnore
    public List<PromocionProducto> getPromocionProductos() {
        return promocionProductos;
    }

    public void setPromocionProductos(List<PromocionProducto> promocionProductos) {
        this.promocionProductos = promocionProductos;
    }

    public Long getDiasVigencia() {
        if (fechaInicio != null && fechaFin != null) {
            return ChronoUnit.DAYS.between(fechaInicio, fechaFin);
        }
        return 0L;
    }

    public Boolean getActiva() {
        if (estado != null && estado && fechaInicio != null && fechaFin != null) {
            LocalDateTime now = LocalDateTime.now();
            return now.isAfter(fechaInicio) && now.isBefore(fechaFin);
        }
        return false;
    }

    public String getDescripcionPromocion() {
        if ("P".equals(tipoPromocion)) {
            return String.format("%.0f%% descuento", valorDescuento);
        } else if ("L".equals(tipoPromocion)) {
            return String.format("Lleva %.0f paga %.0f", valorDescuento, valorDescuento.doubleValue() - 1);
        } else if ("F".equals(tipoPromocion)) {
            return "Envío gratis";
        }
        return "Promoción especial";
    }
}