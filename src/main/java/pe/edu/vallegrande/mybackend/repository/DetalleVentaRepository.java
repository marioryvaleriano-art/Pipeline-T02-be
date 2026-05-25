package pe.edu.vallegrande.mybackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.vallegrande.mybackend.model.DetalleVenta;

import java.util.List;

public interface DetalleVentaRepository extends JpaRepository<DetalleVenta, Integer> {

    List<DetalleVenta> findByIdVenta(Integer idVenta);
}
