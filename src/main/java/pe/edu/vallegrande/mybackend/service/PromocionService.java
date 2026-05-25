package pe.edu.vallegrande.mybackend.service;

import java.util.List;
import java.util.Optional;

import pe.edu.vallegrande.mybackend.model.Promocion;

public interface PromocionService {
    
    // CRUD Básico
    List<Promocion> listarTodos();
    Optional<Promocion> buscarPorId(Integer id);
    Promocion guardar(Promocion promocion);
    Promocion actualizar(Integer id, Promocion promocion);
    void eliminar(Integer id);
    void eliminarLogico(Integer id);
    
    // Métodos específicos
    List<Promocion> listarActivas();
    List<Promocion> listarPorTipo(String tipoPromocion);
    List<Promocion> listarPromocionesPorTerminar();
    boolean existePorNombre(String nombre);
    Promocion activarDesactivar(Integer id, Boolean estado);
}