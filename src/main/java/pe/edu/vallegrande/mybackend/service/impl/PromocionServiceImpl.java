package pe.edu.vallegrande.mybackend.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pe.edu.vallegrande.mybackend.model.Promocion;
import pe.edu.vallegrande.mybackend.repository.PromocionRepository;
import pe.edu.vallegrande.mybackend.service.PromocionService;

@Service
@Transactional
public class PromocionServiceImpl implements PromocionService {

    @Autowired
    private PromocionRepository promocionRepository;

    @Override
    public List<Promocion> listarTodos() {
        return promocionRepository.findAll();
    }

    @Override
    public Optional<Promocion> buscarPorId(Integer id) {
        return promocionRepository.findById(id);
    }

    @Override
    public Promocion guardar(Promocion promocion) {
        // Validar que no exista otra promoción con el mismo nombre
        if (promocionRepository.existsByNombre(promocion.getNombre())) {
            throw new RuntimeException("Ya existe una promoción con el nombre: " + promocion.getNombre());
        }
        
        // Validar fechas
        if (promocion.getFechaFin().isBefore(promocion.getFechaInicio())) {
            throw new RuntimeException("La fecha de fin debe ser posterior a la fecha de inicio");
        }
        
        promocion.setFechaCreacion(LocalDateTime.now());
        promocion.setEstado(true);
        return promocionRepository.save(promocion);
    }

    @Override
    public Promocion actualizar(Integer id, Promocion promocion) {
        Promocion existing = promocionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Promoción no encontrada con ID: " + id));
        
        existing.setNombre(promocion.getNombre());
        existing.setDescripcion(promocion.getDescripcion());
        existing.setFechaInicio(promocion.getFechaInicio());
        existing.setFechaFin(promocion.getFechaFin());
        existing.setTipoPromocion(promocion.getTipoPromocion());
        existing.setValorDescuento(promocion.getValorDescuento());
        
        return promocionRepository.save(existing);
    }

    @Override
    public void eliminar(Integer id) {
        promocionRepository.deleteById(id);
    }

    @Override
    public void eliminarLogico(Integer id) {
        Promocion promocion = promocionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Promoción no encontrada"));
        promocion.setEstado(false);
        promocionRepository.save(promocion);
    }

    @Override
    public List<Promocion> listarActivas() {
        return promocionRepository.findPromocionesActivas(LocalDateTime.now());
    }

    @Override
    public List<Promocion> listarPorTipo(String tipoPromocion) {
        return promocionRepository.findByTipoPromocionAndEstado(tipoPromocion, true);
    }

    @Override
    public List<Promocion> listarPromocionesPorTerminar() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime dentroDe7Dias = now.plusDays(7);
        return promocionRepository.findPromocionesPorTerminar(now, dentroDe7Dias);
    }

    @Override
    public boolean existePorNombre(String nombre) {
        return promocionRepository.existsByNombre(nombre);
    }

    @Override
    public Promocion activarDesactivar(Integer id, Boolean estado) {
        Promocion promocion = promocionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Promoción no encontrada"));
        promocion.setEstado(estado);
        return promocionRepository.save(promocion);
    }
}