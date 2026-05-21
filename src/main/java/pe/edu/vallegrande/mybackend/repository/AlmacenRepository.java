package pe.edu.vallegrande.mybackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.vallegrande.mybackend.model.Almacen;

public interface AlmacenRepository extends JpaRepository<Almacen, Integer> {
}