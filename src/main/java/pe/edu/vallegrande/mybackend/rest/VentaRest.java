package pe.edu.vallegrande.mybackend.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.vallegrande.mybackend.dto.VentaRequest;
import pe.edu.vallegrande.mybackend.dto.VentaResponse;
import pe.edu.vallegrande.mybackend.service.VentaService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ventas")
//@CrossOrigin(origins = "${cors.allowed-origins}")
@RequiredArgsConstructor
public class VentaRest {

    private final VentaService ventaService;

    @GetMapping
    public ResponseEntity<List<VentaResponse>> listarTodos() {
        return ResponseEntity.ok(ventaService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(ventaService.buscarPorId(id));
        } catch (RuntimeException e) {
            return buildError(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> registrar(@RequestBody VentaRequest request) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(ventaService.registrar(request));
        } catch (RuntimeException e) {
            return buildError(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PatchMapping("/{id}/anular")
    public ResponseEntity<?> anular(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(ventaService.anular(id));
        } catch (RuntimeException e) {
            return buildError(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    // ─── Helper ───────────────────────────────────────────────────────────────

    private ResponseEntity<Map<String, Object>> buildError(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(Map.of(
                "timestamp", LocalDateTime.now().toString(),
                "status", status.value(),
                "error", message
        ));
    }
}
