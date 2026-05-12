package pe.edu.vallegrande.mybackend.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import pe.edu.vallegrande.mybackend.model.Producto;
import pe.edu.vallegrande.mybackend.service.ProductoService;

@RestController
@RequestMapping("/api/producto")
// Configuramos CORS para permitir métodos, encabezados y el origen específico
@CrossOrigin(origins = "http://localhost:4200", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.PATCH, RequestMethod.OPTIONS}, allowedHeaders = "*")
public class ProductoRest {

    @Autowired
    private ProductoService productoService;

    @GetMapping
    public ResponseEntity<List<Producto>> listar() {
        return ResponseEntity.ok(productoService.listarProductos());
    }

    @PostMapping
    public ResponseEntity<Producto> guardar(@RequestBody Producto producto) {
        return ResponseEntity.status(201).body(productoService.guardarProducto(producto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizar(@PathVariable Integer id, @RequestBody Producto producto) {
        Producto actualizado = productoService.actualizarProducto(id, producto);
        return actualizado != null ? ResponseEntity.ok(actualizado) : ResponseEntity.notFound().build();
    }

    @PatchMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        productoService.eliminarLogico(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/restaurar/{id}")
    public ResponseEntity<Void> restaurar(@PathVariable Integer id) {
        productoService.restaurarLogico(id);
        return ResponseEntity.noContent().build();
    }
}