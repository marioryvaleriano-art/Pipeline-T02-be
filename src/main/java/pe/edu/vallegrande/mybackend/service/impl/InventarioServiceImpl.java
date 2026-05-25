package pe.edu.vallegrande.mybackend.service.impl;

import org.springframework.stereotype.Service;
import pe.edu.vallegrande.mybackend.model.Inventario;
import pe.edu.vallegrande.mybackend.repository.InventarioRepository;
import pe.edu.vallegrande.mybackend.service.InventarioService;
import java.util.List;

@Service
public class InventarioServiceImpl implements InventarioService {

    private final InventarioRepository repository;

    public InventarioServiceImpl(InventarioRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Inventario> listarTodo() {
        return repository.findAll();
    }

    @Override
    public Inventario guardar(Inventario inventario) {
        return repository.save(inventario); // 👈 Guarda directo sin trabas
    }

    @Override
    public Inventario buscarPorId(Integer id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public void desactivar(Integer id) {
        Inventario inventario = repository.findById(id).orElse(null);
        if (inventario != null) {
            inventario.setEstado("inactivo");
            repository.save(inventario);
        }
    }
}