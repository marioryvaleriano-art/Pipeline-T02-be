package pe.edu.vallegrande.mybackend.service;

import pe.edu.vallegrande.mybackend.model.Cliente;

import java.util.List;



public interface ClienteService {

    List<Cliente> listarTodos();

    List<Cliente> listarPorEstado(Boolean estado);

    Cliente buscarPorId(Integer id);

    Cliente crear(Cliente cliente);

    Cliente editar(Integer id, Cliente cliente);

    Cliente eliminar(Integer id);

    Cliente restaurar(Integer id);
    }

