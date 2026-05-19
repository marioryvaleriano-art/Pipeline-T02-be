package pe.edu.vallegrande.mybackend.service.impl;

import pe.edu.vallegrande.mybackend.model.Cliente;
import pe.edu.vallegrande.mybackend.repository.ClienteRepository;
import pe.edu.vallegrande.mybackend.service.ClienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor

public class ClienteServiceImpl implements ClienteService {
    private final ClienteRepository clienteRepository;

    @Override
    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
    }

    @Override
    public List<Cliente> listarPorEstado(Boolean estado) {
        return clienteRepository.findByEstado(estado);
    }

    @Override
    public Cliente buscarPorId(Integer id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente con ID " + id + " no encontrado."));
    }

    @Override
    public Cliente crear(Cliente cliente) {
        if (clienteRepository.existsByCorreo(cliente.getCorreo())) {
            throw new RuntimeException("Ya existe un cliente registrado con el correo: " + cliente.getCorreo());
        }
        if (clienteRepository.existsByNumeroDocum(cliente.getNumeroDocum())) {
            throw new RuntimeException("Ya existe un cliente registrado con el número de documento: " + cliente.getNumeroDocum());
        }
        cliente.setEstado(true);
        cliente.setFechaCreacion(LocalDateTime.now());
        return clienteRepository.save(cliente);
    }

    @Override
    public Cliente editar(Integer id, Cliente datos) {
        Cliente existente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente con ID " + id + " no encontrado."));

        if (clienteRepository.existsByCorreoAndIdNot(datos.getCorreo(), id)) {
            throw new RuntimeException("Ya existe otro cliente registrado con el correo: " + datos.getCorreo());
        }
        if (clienteRepository.existsByNumeroDocumAndIdNot(datos.getNumeroDocum(), id)) {
            throw new RuntimeException("Ya existe otro cliente registrado con el número de documento: " + datos.getNumeroDocum());
        }

        existente.setTipoDocumento(datos.getTipoDocumento());
        existente.setNumeroDocum(datos.getNumeroDocum());
        existente.setNombre(datos.getNombre());
        existente.setApellido(datos.getApellido());
        existente.setCorreo(datos.getCorreo());
        existente.setTelefono(datos.getTelefono());
        existente.setFechaNacimiento(datos.getFechaNacimiento());
        existente.setFechaActualizacion(LocalDateTime.now());

        return clienteRepository.save(existente);
    }

    @Override
    public Cliente eliminar(Integer id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente con ID " + id + " no encontrado."));

        if (!cliente.getEstado()) {
            throw new RuntimeException("El cliente con ID " + id + " ya está eliminado.");
        }

        cliente.setEstado(false);
        cliente.setFechaEliminacion(LocalDateTime.now());
        return clienteRepository.save(cliente);
    }

    @Override
    public Cliente restaurar(Integer id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente con ID " + id + " no encontrado."));

        if (cliente.getEstado()) {
            throw new RuntimeException("El cliente con ID " + id + " ya está activo.");
        }

        cliente.setEstado(true);
        cliente.setFechaRestauracion(LocalDateTime.now());
        return clienteRepository.save(cliente);
    }
}

