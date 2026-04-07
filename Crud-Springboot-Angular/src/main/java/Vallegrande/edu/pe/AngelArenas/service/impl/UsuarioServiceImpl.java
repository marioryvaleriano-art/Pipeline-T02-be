package Vallegrande.edu.pe.AngelArenas.service.impl;

import Vallegrande.edu.pe.AngelArenas.model.Usuario;
import Vallegrande.edu.pe.AngelArenas.repository.UsuarioRepository;
import Vallegrande.edu.pe.AngelArenas.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;

    // ── GET: Listar todos ─────────────────────────────────────────────
    @Override
    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    // ── GET: Listar por estado ────────────────────────────────────────
    // true  → usuarios activos
    // false → usuarios eliminados (papelera)
    @Override
    public List<Usuario> listarPorEstado(Boolean estaActivo) {
        return usuarioRepository.findByEstaActivo(estaActivo);
    }

    // ── GET: Buscar por ID ────────────────────────────────────────────
    @Override
    public Usuario buscarPorId(Integer id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario con ID " + id + " no encontrado."));
    }

    // ── POST: Crear ───────────────────────────────────────────────────
    @Override
    public Usuario crear(Usuario usuario) {
        // Validar email duplicado
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new RuntimeException("Ya existe un usuario registrado con el email: " + usuario.getEmail());
        }
        // El estado siempre inicia en activo
        usuario.setEstaActivo(true);
        return usuarioRepository.save(usuario);
    }

    // ── PUT: Editar ───────────────────────────────────────────────────
    @Override
    public Usuario editar(Integer id, Usuario datos) {
        // Verificar que el usuario existe
        Usuario existente = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario con ID " + id + " no encontrado."));

        // Validar email duplicado excluyendo el propio registro
        if (usuarioRepository.existsByEmailAndIdNot(datos.getEmail(), id)) {
            throw new RuntimeException("Ya existe otro usuario registrado con el email: " + datos.getEmail());
        }

        // Actualizar campos editables
        existente.setNombreCompleto(datos.getNombreCompleto());
        existente.setEmail(datos.getEmail());
        existente.setTelefono(datos.getTelefono());
        existente.setFechaNacimiento(datos.getFechaNacimiento());

        // estaActivo NO se modifica aquí — se controla con eliminar/restaurar
        return usuarioRepository.save(existente);
    }

    // ── PATCH: Eliminación lógica ─────────────────────────────────────
    @Override
    public Usuario eliminar(Integer id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario con ID " + id + " no encontrado."));

        if (!usuario.getEstaActivo()) {
            throw new RuntimeException("El usuario con ID " + id + " ya está eliminado.");
        }

        usuario.setEstaActivo(false);
        return usuarioRepository.save(usuario);
    }

    // ── PATCH: Restauración lógica ────────────────────────────────────
    @Override
    public Usuario restaurar(Integer id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario con ID " + id + " no encontrado."));

        if (usuario.getEstaActivo()) {
            throw new RuntimeException("El usuario con ID " + id + " ya está activo.");
        }

        usuario.setEstaActivo(true);
        return usuarioRepository.save(usuario);
    }
}