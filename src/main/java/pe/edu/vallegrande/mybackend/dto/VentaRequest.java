package pe.edu.vallegrande.mybackend.dto;

import lombok.Data;
import java.util.List;



@Data
public class VentaRequest {

    private String tipoDocumento;
    private String numeroDocumento;
    private String estado;

    // IDs reales — ya no son temporales
    private Integer idCliente;
    private Integer idAlmacen;
    private Integer idMetodoPago;

    // Lista de productos de la venta
    private List<DetalleVentaRequest> detalles;
}
