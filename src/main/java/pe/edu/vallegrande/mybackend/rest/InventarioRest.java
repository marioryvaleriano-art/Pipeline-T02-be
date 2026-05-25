package pe.edu.vallegrande.mybackend.rest;

import org.springframework.web.bind.annotation.*;
import pe.edu.vallegrande.mybackend.model.Inventario;
import pe.edu.vallegrande.mybackend.service.InventarioService;
import java.util.List;

@RestController
@RequestMapping("/api/inventario")
@CrossOrigin(origins = "*") // Permite conectar con Angular sin problemas de CORS
public class InventarioRest {

    private final InventarioService service;

    public InventarioRest(InventarioService service) {
        this.service = service;
    }

    @GetMapping
    public List<Inventario> listar() {
        return service.listarTodo();
    }

    @PostMapping
    public Inventario crear(@RequestBody Inventario inventario) {
        return service.guardar(inventario);
    }

    @GetMapping("/{id}")
    public Inventario obtenerPorId(@PathVariable Integer id) {
        return service.buscarPorId(id);
    }

    @PatchMapping("/{id}/desactivar") // 👈 Muestra el botón "PARCHE" en Swagger
    public void desactivar(@PathVariable Integer id) {
        service.desactivar(id);
    }
}