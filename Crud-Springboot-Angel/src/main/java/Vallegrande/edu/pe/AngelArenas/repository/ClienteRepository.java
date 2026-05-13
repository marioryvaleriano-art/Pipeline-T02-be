package Vallegrande.edu.pe.AngelArenas.repository;

import Vallegrande.edu.pe.AngelArenas.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer> {

    List<Cliente> findByEstado(Boolean estado);

    Optional<Cliente> findByIdAndEstado(Integer id, Boolean estado);

    boolean existsByCorreo(String correo);

    boolean existsByCorreoAndIdNot(String correo, Integer id);

    boolean existsByNumeroDocum(String numeroDocum);

    boolean existsByNumeroDocumAndIdNot(String numeroDocum, Integer id);
}
