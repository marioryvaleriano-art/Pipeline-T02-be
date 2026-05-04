package pe.edu.vallegrande.mybackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.vallegrande.mybackend.model.Producto;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {
    // JpaRepository ya incluye los métodos básicos:
    // save(), findById(), findAll(), deleteById(), etc.
}