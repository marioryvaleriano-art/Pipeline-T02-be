package example.sebastian.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import example.sebastian.model.Producto;
import example.sebastian.service.ProductoService;

@RestController
@RequestMapping("/api/productos")
@CrossOrigin(origins = "*") // Permite la comunicación con el frontend de Angular
public class ProductoController {

    private final ProductoService service;

    // Inyección por Constructor (Estándar profesional)
    public ProductoController(ProductoService service) {
        this.service = service;
    }

    @GetMapping
    public List<Producto> listar() {
        return service.listarTodos();
    }

    @PostMapping
    public Producto crear(@RequestBody Producto p) {
        return service.registrar(p);
    }

    @PutMapping("/{id}")
    public Producto editar(@PathVariable Integer id, @RequestBody Producto p) {
        return service.actualizar(id, p);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id) {
        service.eliminarLogico(id);
    }

    @PatchMapping("/restaurar/{id}")
    public Producto restaurar(@PathVariable Integer id) {
        return service.restaurar(id);
    }
}