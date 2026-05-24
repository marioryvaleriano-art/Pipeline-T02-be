package pe.edu.vallegrande.mybackend.service;

import pe.edu.vallegrande.mybackend.model.Inventario;
import java.util.List;

public interface InventarioService {
    List<Inventario> listarTodo();
    Inventario guardar(Inventario inventario);
    Inventario buscarPorId(Integer id);
    void desactivar(Integer id); // 👈 Método para el eliminado lógico
}