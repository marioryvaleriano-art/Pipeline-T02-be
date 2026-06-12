package pe.edu.vallegrande.mybackend.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pe.edu.vallegrande.mybackend.model.Producto;
import pe.edu.vallegrande.mybackend.repository.ProductoRepository;
import pe.edu.vallegrande.mybackend.service.ProductoService;

@Service
public class ProductoServiceImpl implements ProductoService {

    @Autowired
    private ProductoRepository repository;

    /**
     * Lista todos los productos activos e inactivos del sistema.
     * @return Lista completa de productos
     */
    @Override
    public List<Producto> listarProductos() {
        // Retorna todos los productos sin filtro de estado
        return repository.findAll();
    }

    /**
     * Guarda un nuevo producto en la base de datos.
     * Las fechas de creación y estado se asignan automáticamente en la entidad.
     * @param producto Datos del producto a guardar
     * @return Producto guardado con ID generado
     */
    @Override
    public Producto guardarProducto(Producto producto) {
        return repository.save(producto);
    }

    /**
     * Actualiza un producto existente identificado por su ID.
     * Solo actualiza campos editables, manteniendo fechas de creación.
     * @param id ID del producto a actualizar
     * @param producto Datos nuevos del producto
     * @return Producto actualizado
     * @throws RuntimeException si el producto no existe
     */
    @Override
    public Producto actualizarProducto(Integer id, Producto producto) {
        Producto existente = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Producto no encontrado con id: " + id));
        
        // Actualizar campos editables
        existente.setNombre(producto.getNombre());
        existente.setDescripcion(producto.getDescripcion());
        existente.setCodigoBarrasSku(producto.getCodigoBarrasSku());
        existente.setUnidadMedida(producto.getUnidadMedida());
        existente.setPrecioUnitario(producto.getPrecioUnitario());
        existente.setIdCategoria(producto.getIdCategoria());
        existente.setFechaActualizacion(LocalDateTime.now());
        
        return repository.save(existente);
    }

    /**
     * Eliminación lógica: cambia estado a false y registra fecha de eliminación.
     * @param id ID del producto a eliminar lógicamente
     * @throws RuntimeException si el producto no existe
     */
    @Override
    public void eliminarLogico(Integer id) {
        Producto p = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Producto no encontrado con id: " + id));
        p.setEstado(false);
        p.setFechaEliminacion(LocalDateTime.now());
        repository.save(p);
    }

    /**
     * Restaura un producto eliminado lógicamente: cambia estado a true y registra fecha de restauración.
     * @param id ID del producto a restaurar
     * @throws RuntimeException si el producto no existe
     */
    @Override
    public void restaurarLogico(Integer id) {
        Producto p = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Producto no encontrado con id: " + id));
        p.setEstado(true);
        p.setFechaRestauracion(LocalDateTime.now());
        repository.save(p);
    }
}