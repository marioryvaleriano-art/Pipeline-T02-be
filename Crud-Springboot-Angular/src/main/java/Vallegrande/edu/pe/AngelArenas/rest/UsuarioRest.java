package Vallegrande.edu.pe.AngelArenas.rest;

import Vallegrande.edu.pe.AngelArenas.model.Usuario;
import Vallegrande.edu.pe.AngelArenas.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "http://localhost:4200") // Puerto de Angular en desarrollo
@RequiredArgsConstructor
public class UsuarioRest {

    private final UsuarioService usuarioService;

    // ════════════════════════════════════════════════════════════════
    // GET /api/usuarios
    // Listar todos los usuarios (activos e inactivos)
    // ════════════════════════════════════════════════════════════════
    @GetMapping
    public ResponseEntity<List<Usuario>> listarTodos() {
        return ResponseEntity.ok(usuarioService.listarTodos());
    }

    // ════════════════════════════════════════════════════════════════
    // GET /api/usuarios/{id}
    // Buscar usuario por ID
    // ════════════════════════════════════════════════════════════════
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(usuarioService.buscarPorId(id));
        } catch (RuntimeException e) {
            return buildError(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    // ════════════════════════════════════════════════════════════════
    // GET /api/usuarios/estado?activo=true  → activos
    // GET /api/usuarios/estado?activo=false → eliminados (papelera)
    // Compatible con el frontend Angular (listar / listarEliminados)
    // ════════════════════════════════════════════════════════════════
    @GetMapping("/estado")
    public ResponseEntity<List<Usuario>> listarPorEstado(
            @RequestParam(name = "activo", defaultValue = "true") Boolean activo) {
        return ResponseEntity.ok(usuarioService.listarPorEstado(activo));
    }

    // ════════════════════════════════════════════════════════════════
    // POST /api/usuarios
    // Crear nuevo usuario
    // Body: { nombreCompleto, email, telefono, fechaNacimiento }
    // ════════════════════════════════════════════════════════════════
    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Usuario usuario) {
        try {
            Usuario creado = usuarioService.crear(usuario);
            return ResponseEntity.status(HttpStatus.CREATED).body(creado);
        } catch (RuntimeException e) {
            return buildError(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    // ════════════════════════════════════════════════════════════════
    // PUT /api/usuarios/{id}
    // Editar usuario existente
    // Body: { nombreCompleto, email, telefono, fechaNacimiento }
    // ════════════════════════════════════════════════════════════════
    @PutMapping("/{id}")
    public ResponseEntity<?> editar(
            @PathVariable Integer id,
            @RequestBody Usuario usuario) {
        try {
            return ResponseEntity.ok(usuarioService.editar(id, usuario));
        } catch (RuntimeException e) {
            HttpStatus status = e.getMessage().contains("no encontrado")
                    ? HttpStatus.NOT_FOUND
                    : HttpStatus.CONFLICT;
            return buildError(status, e.getMessage());
        }
    }

    // ════════════════════════════════════════════════════════════════
    // PATCH /api/usuarios/{id}/eliminar
    // Eliminación lógica: estaActivo = false
    // ════════════════════════════════════════════════════════════════
    @PatchMapping("/{id}/eliminar")
    public ResponseEntity<?> eliminar(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(usuarioService.eliminar(id));
        } catch (RuntimeException e) {
            return buildError(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    // ════════════════════════════════════════════════════════════════
    // PATCH /api/usuarios/{id}/restaurar
    // Restauración lógica: estaActivo = true
    // ════════════════════════════════════════════════════════════════
    @PatchMapping("/{id}/restaurar")
    public ResponseEntity<?> restaurar(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(usuarioService.restaurar(id));
        } catch (RuntimeException e) {
            return buildError(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    // ── Helper: construir respuesta de error uniforme ─────────────────
    private ResponseEntity<Map<String, Object>> buildError(HttpStatus status, String mensaje) {
        Map<String, Object> error = new HashMap<>();
        error.put("exito", false);
        error.put("mensaje", mensaje);
        error.put("status", status.value());
        return ResponseEntity.status(status).body(error);
    }
}