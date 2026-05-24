package pe.edu.vallegrande.mybackend.rest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import pe.edu.vallegrande.mybackend.model.Promocion;
import pe.edu.vallegrande.mybackend.service.PromocionService;

@RestController
@RequestMapping("/api/promociones")
@CrossOrigin(origins = "*")
public class PromocionRest {

    @Autowired
    private PromocionService promocionService;

    // GET: Listar todas las promociones
    @GetMapping
    public ResponseEntity<List<Promocion>> listarTodos() {
        List<Promocion> promociones = promocionService.listarTodos();
        return ResponseEntity.ok(promociones);
    }

    // GET: Listar solo promociones activas
    @GetMapping("/activas")
    public ResponseEntity<List<Promocion>> listarActivas() {
        List<Promocion> promociones = promocionService.listarActivas();
        return ResponseEntity.ok(promociones);
    }

    // GET: Listar por tipo
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<Promocion>> listarPorTipo(@PathVariable String tipo) {
        List<Promocion> promociones = promocionService.listarPorTipo(tipo);
        return ResponseEntity.ok(promociones);
    }

    // GET: Promociones por terminar (próximos 7 días)
    @GetMapping("/por-terminar")
    public ResponseEntity<List<Promocion>> listarPorTerminar() {
        List<Promocion> promociones = promocionService.listarPromocionesPorTerminar();
        return ResponseEntity.ok(promociones);
    }

    // GET: Buscar por ID
    @GetMapping("/{id}")
    public ResponseEntity<Promocion> buscarPorId(@PathVariable Integer id) {
        Optional<Promocion> promocion = promocionService.buscarPorId(id);
        return promocion.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST: Crear nueva promoción
    @PostMapping
    public ResponseEntity<Map<String, Object>> crear(@RequestBody Promocion promocion) {
        Map<String, Object> response = new HashMap<>();
        try {
            // Verificar si ya existe el nombre
            if (promocionService.existePorNombre(promocion.getNombre())) {
                response.put("success", false);
                response.put("message", "Ya existe una promoción con ese nombre");
                return ResponseEntity.badRequest().body(response);
            }
            
            Promocion nueva = promocionService.guardar(promocion);
            response.put("success", true);
            response.put("message", "Promoción creada exitosamente");
            response.put("data", nueva);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al crear: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // PUT: Actualizar promoción
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> actualizar(@PathVariable Integer id, @RequestBody Promocion promocion) {
        Map<String, Object> response = new HashMap<>();
        try {
            Promocion actualizada = promocionService.actualizar(id, promocion);
            response.put("success", true);
            response.put("message", "Promoción actualizada exitosamente");
            response.put("data", actualizada);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    // PATCH: Activar/Desactivar promoción
    @PatchMapping("/{id}/estado")
    public ResponseEntity<Map<String, Object>> cambiarEstado(@PathVariable Integer id, @RequestParam Boolean estado) {
        Map<String, Object> response = new HashMap<>();
        try {
            Promocion promocion = promocionService.activarDesactivar(id, estado);
            response.put("success", true);
            response.put("message", estado ? "Promoción activada" : "Promoción desactivada");
            response.put("data", promocion);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE: Eliminar físicamente
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> eliminar(@PathVariable Integer id) {
        Map<String, Object> response = new HashMap<>();
        try {
            promocionService.eliminar(id);
            response.put("success", true);
            response.put("message", "Promoción eliminada exitosamente");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al eliminar: " + e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE: Eliminación lógica
    @DeleteMapping("/{id}/logico")
    public ResponseEntity<Map<String, Object>> eliminarLogico(@PathVariable Integer id) {
        Map<String, Object> response = new HashMap<>();
        try {
            promocionService.eliminarLogico(id);
            response.put("success", true);
            response.put("message", "Promoción desactivada (eliminación lógica)");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}