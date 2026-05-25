package pe.edu.vallegrande.mybackend.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pe.edu.vallegrande.mybackend.model.Almacen;
import pe.edu.vallegrande.mybackend.repository.AlmacenRepository;
import pe.edu.vallegrande.mybackend.service.AlmacenService;

import java.time.LocalDateTime;
import java.util.List;
@Service
public class AlmacenServiceImpl implements AlmacenService {

    @Autowired
    private AlmacenRepository almacenRepository;

    // =========================
    // LISTAR
    // =========================
    @Override
    public List<Almacen> listarTodos() {
        return almacenRepository.findAll();
    }

    // =========================
    // BUSCAR POR ID
    // =========================
    @Override
    public Almacen buscarPorId(Integer id) {
        return almacenRepository.findById(id).orElse(null);
    }

    // =========================
    // GUARDAR
    // =========================
    @Override
    public Almacen guardar(Almacen almacen) {

        // ESTADO POR DEFECTO
        almacen.setEstado("Activo");

        // FECHA CREACIÓN
        almacen.setFechaCreacion(LocalDateTime.now());

        // =========================
        // GENERAR CÓDIGO BOD-001
        // =========================
        long count = almacenRepository.count() + 1;

        String codigo = "BOD-" + String.format("%03d", count);

        almacen.setCodigoAlmacen(codigo);

        return almacenRepository.save(almacen);
    }

    // =========================
    // ACTUALIZAR
    // =========================
    @Override
    public Almacen actualizar(Integer id, Almacen almacen) {

        Almacen actual = almacenRepository.findById(id).orElse(null);

        if (actual != null) {

            actual.setNombre(almacen.getNombre());
            actual.setUbicacion(almacen.getUbicacion());
            actual.setTelefono(almacen.getTelefono());
            actual.setResponsable(almacen.getResponsable());
            actual.setTipoProducto(almacen.getTipoProducto());
            actual.setCantidadBotellas(almacen.getCantidadBotellas());
            actual.setEstado(almacen.getEstado());
            actual.setUbigeoId(almacen.getUbigeoId());

            actual.setFechaActualizacion(LocalDateTime.now());

            return almacenRepository.save(actual);
        }

        return null;
    }

    // =========================
    // ELIMINAR (SOFT DELETE)
    // =========================
    @Override
    public Almacen eliminar(Integer id) {

        Almacen almacen = almacenRepository.findById(id).orElse(null);

        if (almacen != null) {
            almacen.setEstado("Inactivo");
            almacen.setFechaEliminacion(LocalDateTime.now());

            return almacenRepository.save(almacen);
        }

        return null;
    }

    // =========================
    // RESTAURAR
    // =========================
    @Override
    public Almacen restaurar(Integer id) {

        Almacen almacen = almacenRepository.findById(id).orElse(null);

        if (almacen != null) {
            almacen.setEstado("Activo");
            almacen.setFechaRestauracion(LocalDateTime.now());

            return almacenRepository.save(almacen);
        }

        return null;
    }
}