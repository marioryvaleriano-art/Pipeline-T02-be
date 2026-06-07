package pe.edu.vallegrande.mybackend.rest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import pe.edu.vallegrande.mybackend.model.Producto;
import pe.edu.vallegrande.mybackend.model.Promocion;
import pe.edu.vallegrande.mybackend.model.PromocionProducto;
import pe.edu.vallegrande.mybackend.repository.ProductoRepository;
import pe.edu.vallegrande.mybackend.repository.PromocionRepository;
import pe.edu.vallegrande.mybackend.service.PromocionProductoService;

@RestController
@RequestMapping("/api/promociones-producto")
@CrossOrigin(origins = "*")
public class PromocionProductoRest {

    @Autowired
    private PromocionProductoService relacionService;
    
    @Autowired
    private PromocionRepository promocionRepository;
    
    @Autowired
    private ProductoRepository productoRepository;

    @GetMapping
    public ResponseEntity<List<PromocionProducto>> listarTodos() {
        return ResponseEntity.ok(relacionService.listarTodos());
    }

    @GetMapping("/promocion/{idPromocion}")
    public ResponseEntity<List<PromocionProducto>> listarPorPromocion(@PathVariable Integer idPromocion) {
        return ResponseEntity.ok(relacionService.listarPorPromocion(idPromocion));
    }

    @GetMapping("/producto/{idProducto}")
    public ResponseEntity<List<PromocionProducto>> listarPorProducto(@PathVariable Integer idProducto) {
        return ResponseEntity.ok(relacionService.listarPorProducto(idProducto));
    }

    @GetMapping("/producto/{idProducto}/activas")
    public ResponseEntity<List<PromocionProducto>> obtenerPromocionesActivas(@PathVariable Integer idProducto) {
        return ResponseEntity.ok(relacionService.obtenerPromocionesActivasPorProducto(idProducto));
    }

    @GetMapping("/calcular-descuento")
    public ResponseEntity<Map<String, Object>> calcularDescuento(
            @RequestParam Integer idProducto,
            @RequestParam Double precioOriginal,
            @RequestParam Integer cantidad) {
        Map<String, Object> resultado = relacionService.calcularPrecioConDescuento(idProducto, precioOriginal, cantidad);
        return ResponseEntity.ok(resultado);
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> crear(@RequestBody Map<String, Object> payload) {
        Map<String, Object> response = new HashMap<>();
        try {
            Integer idPromocion = (Integer) payload.get("idPromocion");
            Integer idProducto = (Integer) payload.get("idProducto");
            Boolean aplicaDescuentoAdicional = (Boolean) payload.getOrDefault("aplicaDescuentoAdicional", false);
            
            Promocion promocion = promocionRepository.findById(idPromocion)
                    .orElseThrow(() -> new RuntimeException("Promoción no encontrada"));
            Producto producto = productoRepository.findById(idProducto)
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
            
            PromocionProducto relacion = new PromocionProducto(promocion, producto, aplicaDescuentoAdicional);
            PromocionProducto nueva = relacionService.guardar(relacion);
            
            response.put("success", true);
            response.put("message", "Producto asociado a la promoción");
            response.put("data", nueva);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/promocion/{idPromocion}/productos")
    public ResponseEntity<Map<String, Object>> agregarMultiplesProductos(
            @PathVariable Integer idPromocion,
            @RequestBody List<Integer> idsProductos) {
        Map<String, Object> response = new HashMap<>();
        try {
            relacionService.agregarProductosAPromocion(idPromocion, idsProductos);
            response.put("success", true);
            response.put("message", "Productos asociados exitosamente");
            response.put("cantidad", idsProductos.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> eliminar(@PathVariable Integer id) {
        Map<String, Object> response = new HashMap<>();
        try {
            relacionService.eliminar(id);
            response.put("success", true);
            response.put("message", "Relación eliminada");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al eliminar");
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/promocion/{idPromocion}/producto/{idProducto}")
    public ResponseEntity<Map<String, Object>> quitarProducto(
            @PathVariable Integer idPromocion,
            @PathVariable Integer idProducto) {
        Map<String, Object> response = new HashMap<>();
        try {
            relacionService.quitarProductoDePromocion(idPromocion, idProducto);
            response.put("success", true);
            response.put("message", "Producto removido de la promoción");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}