package pe.edu.vallegrande.mybackend.dto;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class VentaResponse {

    private Integer idVenta;
    private LocalDateTime fechaVenta;
    private String tipoDocumento;
    private String numeroDocumento;
    private String estado;
    private BigDecimal total; // SUM(subtotal) de todos los detalles
    private String referenciaTransaccion;
    private Integer idCliente;
    private Integer idAlmacen;
    private Integer idMetodoPago;

    // Detalles incluidos en la respuesta
    private List<DetalleVentaResponse> detalles;
}
