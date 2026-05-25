package pe.edu.vallegrande.mybackend.service;

import pe.edu.vallegrande.mybackend.model.Producto;
import java.util.List;

public interface ProductoService {
    List<Producto> listarProductos();
    Producto guardarProducto(Producto producto);
    Producto actualizarProducto(Integer id, Producto producto);
    void eliminarLogico(Integer id);
    void restaurarLogico(Integer id);
}