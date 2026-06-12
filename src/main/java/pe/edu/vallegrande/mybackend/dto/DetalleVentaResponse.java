package pe.edu.vallegrande.mybackend.dto;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DetalleVentaResponse {

    private Integer idDetalleVenta;
    private Integer idProducto;
    private BigDecimal cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal descuento;
    private BigDecimal subtotal;
}
