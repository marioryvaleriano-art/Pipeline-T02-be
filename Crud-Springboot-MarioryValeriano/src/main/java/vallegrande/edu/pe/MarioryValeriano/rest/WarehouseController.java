package vallegrande.edu.pe.MarioryValeriano.rest;

import vallegrande.edu.pe.MarioryValeriano.model.Warehouse;
import vallegrande.edu.pe.MarioryValeriano.service.WarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/warehouses")
public class WarehouseController {

    @Autowired
    private WarehouseService service;

    @GetMapping
    public List<Warehouse> getAll() {
        return service.findAll();
    }

    @PostMapping
    public Warehouse create(@RequestBody Warehouse warehouse) {
        return service.save(warehouse);
    }

    @PutMapping("/{id}")
    public Warehouse update(@PathVariable Integer id, @RequestBody Warehouse warehouse) {
        warehouse.setIdWarehouse(id);
        return service.save(warehouse);
    }

    @PatchMapping("/{id}/delete")
    public void delete(@PathVariable Integer id) {
        service.deleteLogical(id);
    }

    @PatchMapping("/{id}/restore")
    public void restore(@PathVariable Integer id) {
        service.restoreLogical(id);
    }
}