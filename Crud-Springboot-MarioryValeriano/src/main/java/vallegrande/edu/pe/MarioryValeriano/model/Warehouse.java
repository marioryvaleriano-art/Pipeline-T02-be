package vallegrande.edu.pe.MarioryValeriano.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data // Esto crea los Getters y Setters automáticamente
@Table(name = "warehouse")
public class Warehouse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idWarehouse;

    private String name;
    private String location;
    private Double capacity;
    private Integer rackCount;
    private Boolean isRefrigerated;
    private String contactPhone;
    private String status; // "A" o "I"

    // CAMPOS DE AUDITORÍA (Obligatorios)
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.createdBy = "USER_SYSTEM";
        this.status = "A";
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
        this.updatedBy = "USER_SYSTEM";
    }
}