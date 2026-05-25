package pe.edu.vallegrande.mybackend.rest;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pe.edu.vallegrande.mybackend.model.Almacen;
import pe.edu.vallegrande.mybackend.service.AlmacenService;


import java.util.List;


@RestController
@RequestMapping("/api/almacen")
@CrossOrigin(origins = "http://localhost:4200")
public class AlmacenRest {


    @Autowired
    private AlmacenService almacenService;


    // LISTAR
    @GetMapping
    public List<Almacen> listar() {
        return almacenService.listarTodos();
    }


    // BUSCAR POR ID
    @GetMapping("/{id}")
    public Almacen buscarPorId(@PathVariable Integer id) {
        return almacenService.buscarPorId(id);
    }


    // REGISTRAR
    @PostMapping
    public Almacen registrar(@RequestBody Almacen almacen) {
        return almacenService.guardar(almacen);
    }


    // EDITAR
    @PutMapping("/{id}")
    public Almacen actualizar(@PathVariable Integer id,
                              @RequestBody Almacen almacen) {


        return almacenService.actualizar(id, almacen);
    }


    // ELIMINAR LÓGICO
    @PatchMapping("/{id}/eliminar")
    public Almacen eliminar(@PathVariable Integer id) {
        return almacenService.eliminar(id);
    }


    // RESTAURAR
    @PatchMapping("/{id}/restaurar")
    public Almacen restaurar(@PathVariable Integer id) {
        return almacenService.restaurar(id);
    }
}

