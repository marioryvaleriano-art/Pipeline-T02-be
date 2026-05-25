package pe.edu.vallegrande.mybackend.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "inventario")
@Data
public class Inventario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_inventario")
    private Integer idInventario;

    @Column(name = "stock_actual", nullable = false, precision = 10, scale = 2)
    private BigDecimal stockActual;

    @Column(name = "stock_minimo", nullable = false, precision = 10, scale = 2)
    private BigDecimal stockMinimo;

    @Column(name = "ultima_actualizacion", nullable = false)
    private LocalDateTime ultimaActualizacion;

    @Column(name = "id_producto", nullable = false)
    private Integer idProducto; // 👈 Cambiado a Integer para que calce con el INT de SQL Server

    @Column(name = "id_almacen", nullable = false)
    private Integer idAlmacen;

    @Column(name = "estado", nullable = false, length = 10)
    private String estado;

    // Asigna la fecha y el estado automáticamente antes de registrar en la BD
    @PrePersist
    protected void onCreate() {
        this.ultimaActualizacion = LocalDateTime.now();
        if (this.estado == null) {
            this.estado = "activo";
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.ultimaActualizacion = LocalDateTime.now();
    }
}