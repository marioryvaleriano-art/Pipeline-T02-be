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

    @Override
    public List<Producto> listarProductos() {
        return repository.findAll();
    }

    @Override
    public Producto guardarProducto(Producto producto) {
        return repository.save(producto);
    }

    @Override
    public Producto actualizarProducto(Integer id, Producto producto) {
        Producto existente = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Producto no encontrado con id: " + id));
        existente.setNombre(producto.getNombre());
        existente.setDescripcion(producto.getDescripcion());
        existente.setCodigoBarrasSku(producto.getCodigoBarrasSku());
        existente.setUnidadMedida(producto.getUnidadMedida());
        existente.setPrecioUnitario(producto.getPrecioUnitario());
        existente.setIdCategoria(producto.getIdCategoria());
        existente.setFechaActualizacion(LocalDateTime.now());
        return repository.save(existente);
    }

    @Override
    public void eliminarLogico(Integer id) {
        Producto p = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Producto no encontrado con id: " + id));
        p.setEstado(false);
        p.setFechaEliminacion(LocalDateTime.now());
        repository.save(p);
    }

    @Override
    public void restaurarLogico(Integer id) {
        Producto p = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Producto no encontrado con id: " + id));
        p.setEstado(true);
        p.setFechaRestauracion(LocalDateTime.now());
        repository.save(p);
    }
}