package example.sebastian.service;

import java.util.List;

import example.sebastian.model.Producto;

public interface ProductoService {
    List<Producto> listarTodos();
    Producto registrar(Producto p);
    Producto actualizar(Integer id, Producto p);
    void eliminarLogico(Integer id); // Este activa fecha_eliminacion
    Producto restaurar(Integer id);      // Este activa fecha_restauracion
}