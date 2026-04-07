package Vallegrande.edu.pe.AngelArenas.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "usuarios")
public class Usuario {

    // 1. TIPO: INTEGER — Identificador primario
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    // 2. TIPO: VARCHAR — Nombre completo
    @Column(name = "nombre_completo", nullable = false, length = 100)
    private String nombreCompleto;

    // 2. TIPO: VARCHAR — Email único
    @Column(name = "email", nullable = false, unique = true, length = 150)
    private String email;

    // 2. TIPO: VARCHAR — Teléfono opcional
    @Column(name = "telefono", length = 20)
    private String telefono;

    // 3. TIPO: DATE — Fecha de nacimiento
    @Column(name = "fecha_nacimiento", nullable = false)
    private LocalDate fechaNacimiento;

    // 4. TIPO: BOOLEAN — Estado lógico (activo / eliminado)
    @Column(name = "esta_activo", nullable = false)
    private Boolean estaActivo = true;
}