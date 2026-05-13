package Vallegrande.edu.pe.AngelArenas.rest;

import Vallegrande.edu.pe.AngelArenas.model.Cliente;
import Vallegrande.edu.pe.AngelArenas.service.ClienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/clientes")
@CrossOrigin(origins = "${cors.allowed-origins}") // Origen leído desde application.yml → variable de entorno del docker-compose
@RequiredArgsConstructor
public class ClienteRest {

    private final ClienteService clienteService;

    @GetMapping
    public ResponseEntity<List<Cliente>> listarTodos() {
        return ResponseEntity.ok(clienteService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(clienteService.buscarPorId(id));
        } catch (RuntimeException e) {
            return buildError(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/estado")
    public ResponseEntity<List<Cliente>> listarPorEstado(
            @RequestParam(name = "activo", defaultValue = "true") Boolean activo) {
        return ResponseEntity.ok(clienteService.listarPorEstado(activo));
    }

    @PostMapping
    public ResponseEntity<?> crear(@Valid @RequestBody Cliente cliente) {
        try {
            Cliente creado = clienteService.crear(cliente);
            return ResponseEntity.status(HttpStatus.CREATED).body(creado);
        } catch (RuntimeException e) {
            return buildError(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editar(
            @PathVariable Integer id,
            @Valid
            @RequestBody Cliente cliente) {
        try {
            return ResponseEntity.ok(clienteService.editar(id, cliente));
        } catch (RuntimeException e) {
            HttpStatus status = e.getMessage().contains("no encontrado")
                    ? HttpStatus.NOT_FOUND
                    : HttpStatus.CONFLICT;
            return buildError(status, e.getMessage());
        }
    }

    @PatchMapping("/{id}/eliminar")
    public ResponseEntity<?> eliminar(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(clienteService.eliminar(id));
        } catch (RuntimeException e) {
            return buildError(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PatchMapping("/{id}/restaurar")
    public ResponseEntity<?> restaurar(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(clienteService.restaurar(id));
        } catch (RuntimeException e) {
            return buildError(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    private ResponseEntity<Map<String, Object>> buildError(HttpStatus status, String mensaje) {
        Map<String, Object> error = new HashMap<>();
        error.put("exito", false);
        error.put("mensaje", mensaje);
        error.put("status", status.value());
        return ResponseEntity.status(status).body(error);
    }
}