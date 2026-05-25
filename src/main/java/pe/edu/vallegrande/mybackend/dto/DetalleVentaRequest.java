package pe.edu.vallegrande.mybackend.dto;

import lombok.Data;
import java.math.BigDecimal;

//Request para detelle venta añadido
@Data
public class DetalleVentaRequest {

    private Integer idProducto;
    private BigDecimal cantidad;
    private BigDecimal precioUnitario;

    // Opcional — si no viene, el backend asume 0
    private BigDecimal descuento;
}
