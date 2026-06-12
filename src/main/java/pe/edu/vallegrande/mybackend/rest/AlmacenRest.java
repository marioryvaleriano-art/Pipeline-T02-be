package pe.edu.vallegrande.mybackend.rest;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pe.edu.vallegrande.mybackend.model.Almacen;
import pe.edu.vallegrande.mybackend.service.AlmacenService;

@RestController
@RequestMapping("/api/almacen")
@CrossOrigin(origins = "http://localhost:4200")
public class AlmacenRest {

    @Autowired
    private AlmacenService almacenService;

    @GetMapping
    public List<Almacen> listar() {
        return almacenService.listarTodos();
    }

    @GetMapping("/{id}")
    public Almacen buscarPorId(@PathVariable Integer id) {
        return almacenService.buscarPorId(id);
    }

    @PostMapping
    public Almacen registrar(@RequestBody Almacen almacen) {
        return almacenService.guardar(almacen);
    }

    @PutMapping("/{id}")
    public Almacen actualizar(@PathVariable Integer id,
                              @RequestBody Almacen almacen) {
        return almacenService.actualizar(id, almacen);
    }

    @PatchMapping("/{id}/eliminar")
    public Almacen eliminar(@PathVariable Integer id) {
        return almacenService.eliminar(id);
    }

    @PatchMapping("/{id}/restaurar")
    public Almacen restaurar(@PathVariable Integer id) {
        return almacenService.restaurar(id);
    }
}

