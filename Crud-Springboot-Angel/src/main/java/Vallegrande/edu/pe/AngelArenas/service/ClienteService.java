package Vallegrande.edu.pe.AngelArenas.service;

import Vallegrande.edu.pe.AngelArenas.model.Cliente;

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
