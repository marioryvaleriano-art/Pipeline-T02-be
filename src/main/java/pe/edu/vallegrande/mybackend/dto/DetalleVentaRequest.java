package pe.edu.vallegrande.mybackend.dto;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class DetalleVentaRequest {

    private Integer idProducto;
    private BigDecimal cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal descuento;
}
