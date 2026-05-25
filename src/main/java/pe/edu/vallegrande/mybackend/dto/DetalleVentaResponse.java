package pe.edu.vallegrande.mybackend.dto;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
//Response para detalle venta añadido
@Data
@Builder
public class DetalleVentaResponse {

    private Integer idDetalleVenta;
    private Integer idProducto;
    private BigDecimal cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal descuento;
    private BigDecimal subtotal; // calculado por el backend: (cantidad * precioUnitario) - descuento
}
