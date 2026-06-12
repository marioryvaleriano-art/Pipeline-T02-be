package pe.edu.vallegrande.mybackend.dto;

import java.util.List;
import lombok.Data;

@Data
public class VentaRequest {

    private String tipoDocumento;
    private String numeroDocumento;
    private String estado;

    private Integer idCliente;
    private Integer idAlmacen;
    private Integer idMetodoPago;

    private List<DetalleVentaRequest> detalles;
}
