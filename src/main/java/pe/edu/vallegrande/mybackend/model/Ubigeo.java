package pe.edu.vallegrande.mybackend.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "ubigeo")
@Data
public class Ubigeo {

    @Id
    @Column(name = "ubigeo_id", length = 6)
    private String ubigeoId;
}
