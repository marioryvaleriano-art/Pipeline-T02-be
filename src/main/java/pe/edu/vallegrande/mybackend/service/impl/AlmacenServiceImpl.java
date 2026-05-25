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


        // ESTADO Y FECHA
        almacen.setEstado("Activo");
        almacen.setFechaCreacion(LocalDateTime.now());


        // GENERAR PREFIJO SEGÚN TIPO DE PRODUCTO
        String prefijo = "";


        switch (almacen.getTipoProducto()) {


            case "Vino Tinto":
                prefijo = "VT";
                break;


            case "Vino Blanco":
                prefijo = "VB";
                break;


            case "Vino Rosado":
                prefijo = "VR";
                break;


            case "Espumante":
                prefijo = "ES";
                break;


            case "Destilado":
                prefijo = "DS";
                break;


            default:
                prefijo = "AL";
                break;
        }


        // GENERAR CORRELATIVO
        long cantidad = almacenRepository.count() + 1;


        // FORMATO: VB-0001
        String codigo = prefijo + "-" + String.format("%04d", cantidad);


        almacen.setCodigoAlmacen(codigo);


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
            actual.setTipoProducto(almacen.getTipoProducto());
            actual.setCantidadBotellas(almacen.getCantidadBotellas());


            // MANTENER EL CÓDIGO ACTUAL
            actual.setCodigoAlmacen(actual.getCodigoAlmacen());


            // ESTADO
            actual.setEstado(almacen.getEstado());


            // FECHA ACTUALIZACIÓN
            actual.setFechaActualizacion(LocalDateTime.now());


            return almacenRepository.save(actual);
        }


        return null;
    }


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
