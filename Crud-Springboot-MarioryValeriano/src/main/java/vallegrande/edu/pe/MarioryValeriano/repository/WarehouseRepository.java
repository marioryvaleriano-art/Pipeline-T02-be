package vallegrande.edu.pe.MarioryValeriano.repository;

import vallegrande.edu.pe.MarioryValeriano.model.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse, Integer> {
}