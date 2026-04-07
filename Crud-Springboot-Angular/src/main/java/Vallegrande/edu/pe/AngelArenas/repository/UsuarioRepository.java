package Vallegrande.edu.pe.AngelArenas.repository;

import Vallegrande.edu.pe.AngelArenas.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    // Listar por estado — GET /api/usuarios/estado?activo=true|false
    List<Usuario> findByEstaActivo(Boolean estaActivo);

    // Buscar por ID solo si está activo — seguridad extra en endpoints de edición
    Optional<Usuario> findByIdAndEstaActivo(Integer id, Boolean estaActivo);

    // Verificar email duplicado excluyendo el propio registro (para edición)
    boolean existsByEmailAndIdNot(String email, Integer id);

    // Verificar email duplicado (para creación)
    boolean existsByEmail(String email);
}