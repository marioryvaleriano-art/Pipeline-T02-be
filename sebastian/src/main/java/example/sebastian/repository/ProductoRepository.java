package example.sebastian.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import example.sebastian.model.Producto;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {
    // Aquí puedes agregar métodos de búsqueda personalizados si lo necesitas
}