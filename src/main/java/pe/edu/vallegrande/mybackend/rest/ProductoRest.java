package pe.edu.vallegrande.mybackend.rest;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.vallegrande.mybackend.model.Producto;
import pe.edu.vallegrande.mybackend.service.ProductoService;

@RestController
@RequestMapping("/api/producto")
// Hemos quitado @CrossOrigin de aquí para manejarlo globalmente
public class ProductoRest {

    private static final Logger log = LoggerFactory.getLogger(ProductoRest.class);

    @Autowired
    private ProductoService productoService;

    @GetMapping
    public ResponseEntity<List<Producto>> listar() {
        log.info("[ProductoRest] Solicitando listado de todos los productos");
        List<Producto> productos = productoService.listarProductos();
        log.info("[ProductoRest] Se encontraron {} productos", productos.size());
        return ResponseEntity.ok(productos);
    }

    @PostMapping
    public ResponseEntity<Producto> guardar(@RequestBody Producto producto) {
        log.info("[ProductoRest] Creando nuevo producto: {}", producto.getNombre());
        Producto nuevo = productoService.guardarProducto(producto);
        log.info("[ProductoRest] Producto creado con ID: {}", nuevo.getIdProducto());
        return ResponseEntity.status(201).body(nuevo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizar(@PathVariable Integer id, @RequestBody Producto producto) {
        log.info("[ProductoRest] Actualizando producto con ID: {}", id);
        Producto actualizado = productoService.actualizarProducto(id, producto);
        if (actualizado != null) {
            log.info("[ProductoRest] Producto ID: {} actualizado correctamente", id);
            return ResponseEntity.ok(actualizado);
        } else {
            log.warn("[ProductoRest] Producto ID: {} no encontrado para actualizar", id);
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        log.info("[ProductoRest] Eliminando lógicamente producto con ID: {}", id);
        productoService.eliminarLogico(id);
        log.info("[ProductoRest] Producto ID: {} eliminado lógicamente", id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/restaurar/{id}")
    public ResponseEntity<Void> restaurar(@PathVariable Integer id) {
        log.info("[ProductoRest] Restaurando producto con ID: {}", id);
        productoService.restaurarLogico(id);
        log.info("[ProductoRest] Producto ID: {} restaurado correctamente", id);
        return ResponseEntity.noContent().build();
    }
}