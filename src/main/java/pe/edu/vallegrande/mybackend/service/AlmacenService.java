package pe.edu.vallegrande.mybackend.service;


import pe.edu.vallegrande.mybackend.model.Almacen;


import java.util.List;


public interface AlmacenService {


    List<Almacen> listarTodos();


    Almacen buscarPorId(Integer id);


    Almacen guardar(Almacen almacen);


    Almacen actualizar(Integer id, Almacen almacen);


    Almacen eliminar(Integer id);


    Almacen restaurar(Integer id);
}

