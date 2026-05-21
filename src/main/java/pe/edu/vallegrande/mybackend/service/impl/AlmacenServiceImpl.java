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

    @Override
    public List<Almacen> listarTodos() {
        return almacenRepository.findAll();
    }

    @Override
    public Almacen buscarPorId(Integer id) {
        return almacenRepository.findById(id).orElse(null);
    }

    @Override
    public Almacen guardar(Almacen almacen) {

        almacen.setEstado(true);
        almacen.setFechaCreacion(LocalDateTime.now());

        return almacenRepository.save(almacen);
    }

    @Override
    public Almacen actualizar(Integer id, Almacen almacen) {

        Almacen actual = almacenRepository.findById(id).orElse(null);

        if (actual != null) {

            actual.setNombre(almacen.getNombre());
            actual.setUbicacion(almacen.getUbicacion());
            actual.setTelefono(almacen.getTelefono());
            actual.setResponsable(almacen.getResponsable());
            actual.setCodigoAlmacen(almacen.getCodigoAlmacen());
            actual.setTipoProducto(almacen.getTipoProducto());
            actual.setCantidadBotellas(almacen.getCantidadBotellas());

            actual.setFechaActualizacion(LocalDateTime.now());

            return almacenRepository.save(actual);
        }

        return null;
    }

    @Override
    public Almacen eliminar(Integer id) {

        Almacen almacen = almacenRepository.findById(id).orElse(null);

        if (almacen != null) {

            almacen.setEstado(false);
            almacen.setFechaEliminacion(LocalDateTime.now());

            return almacenRepository.save(almacen);
        }

        return null;
    }

    @Override
    public Almacen restaurar(Integer id) {

        Almacen almacen = almacenRepository.findById(id).orElse(null);

        if (almacen != null) {

            almacen.setEstado(true);
            almacen.setFechaRestauracion(LocalDateTime.now());

            return almacenRepository.save(almacen);
        }

        return null;
    }
}