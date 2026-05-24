package pe.edu.vallegrande.mybackend.service;

import pe.edu.vallegrande.mybackend.dto.VentaRequest;
import pe.edu.vallegrande.mybackend.dto.VentaResponse;

import java.util.List;

public interface VentaService {

    List<VentaResponse> listarTodos();

    VentaResponse buscarPorId(Integer id);

    VentaResponse registrar(VentaRequest request);

    VentaResponse anular(Integer id);
}
