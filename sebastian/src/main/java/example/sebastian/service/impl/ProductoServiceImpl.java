package example.sebastian.service.impl;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import example.sebastian.model.Producto;
import example.sebastian.repository.ProductoRepository;
import example.sebastian.service.ProductoService;

@Service
public class ProductoServiceImpl implements ProductoService {

    @Autowired
    private ProductoRepository repository;

    @Override
    public List<Producto> listarTodos() {
        return repository.findAll();
    }

    @Override
    public Producto registrar(Producto p) {
        p.setEstado(true);
        p.setFechaRegistro(LocalDateTime.now(ZoneId.of("America/Lima")));
        return repository.save(p);
    }

    @Override
    public Producto actualizar(Integer id, Producto p) {
        Producto existente = repository.findById(id).orElseThrow();
        existente.setNombre(p.getNombre());
        existente.setPrecio(p.getPrecio());
        existente.setFechaEdicion(LocalDateTime.now());
        return repository.save(existente);
    }

    @Override
    public void eliminarLogico(Integer id) {
        Producto p = repository.findById(id).orElseThrow();
        p.setEstado(false);
        p.setFechaEliminacion(LocalDateTime.now());
        repository.save(p);
    }

    @Override
    public Producto restaurar(Integer id) {
        Producto p = repository.findById(id).orElseThrow();
        p.setEstado(true);
        p.setFechaRestauracion(LocalDateTime.now());
        return repository.save(p);
    }
}