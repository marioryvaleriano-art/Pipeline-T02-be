package Vallegrande.edu.pe.AngelArenas.service;

import Vallegrande.edu.pe.AngelArenas.model.Usuario;

import java.util.List;

public interface UsuarioService {

    // ── GET ───────────────────────────────────────────────────────────
    // Listar todos los usuarios (activos e inactivos)
    List<Usuario> listarTodos();

    // Listar por estado: true = activos | false = eliminados
    List<Usuario> listarPorEstado(Boolean estaActivo);

    // Buscar por ID
    Usuario buscarPorId(Integer id);

    // ── POST ──────────────────────────────────────────────────────────
    // Crear nuevo usuario
    Usuario crear(Usuario usuario);

    // ── PUT ───────────────────────────────────────────────────────────
    // Editar usuario existente
    Usuario editar(Integer id, Usuario usuario);

    // ── PATCH ─────────────────────────────────────────────────────────
    // Eliminación lógica: estaActivo = false
    Usuario eliminar(Integer id);

    // Restauración lógica: estaActivo = true
    Usuario restaurar(Integer id);
}