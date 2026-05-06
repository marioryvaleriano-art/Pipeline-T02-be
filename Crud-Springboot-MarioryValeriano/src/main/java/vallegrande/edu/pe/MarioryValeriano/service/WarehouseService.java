package vallegrande.edu.pe.MarioryValeriano.service;

import vallegrande.edu.pe.MarioryValeriano.model.Warehouse;
import vallegrande.edu.pe.MarioryValeriano.repository.WarehouseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class WarehouseService {

    @Autowired
    private WarehouseRepository repository;

    public List<Warehouse> findAll() {
        return repository.findAll();
    }

    public Warehouse save(Warehouse warehouse) {
        return repository.save(warehouse);
    }

    public void deleteLogical(Integer id) {
        Warehouse warehouse = repository.findById(id).orElse(null);
        if (warehouse != null) {
            // Asumiendo que tienes un campo status. Si se llama diferente, puedes cambiar la "I"
            warehouse.setStatus("I");
            repository.save(warehouse);
        }
    }

    public void restoreLogical(Integer id) {
        Warehouse warehouse = repository.findById(id).orElse(null);
        if (warehouse != null) {
            // Asumiendo que tienes un campo status. Si se llama diferente, puedes cambiar la "A"
            warehouse.setStatus("A");
            repository.save(warehouse);
        }
    }
}