package pe.edu.vallegrande.mybackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.vallegrande.mybackend.model.Ubigeo;

public interface UbigeoRepository extends JpaRepository<Ubigeo, String> {
}