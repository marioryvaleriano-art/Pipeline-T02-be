package pe.edu.vallegrande.mybackend.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.vallegrande.mybackend.dto.DetalleVentaRequest;
import pe.edu.vallegrande.mybackend.dto.VentaRequest;
import pe.edu.vallegrande.mybackend.dto.DetalleVentaResponse;
import pe.edu.vallegrande.mybackend.dto.VentaResponse;
import pe.edu.vallegrande.mybackend.model.DetalleVenta;
import pe.edu.vallegrande.mybackend.model.Venta;
import pe.edu.vallegrande.mybackend.repository.DetalleVentaRepository;
import pe.edu.vallegrande.mybackend.repository.VentaRepository;
import pe.edu.vallegrande.mybackend.service.VentaService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VentaServiceImpl implements VentaService {

    private final VentaRepository ventaRepository;
    private final DetalleVentaRepository detalleVentaRepository;

    @Override
    public List<VentaResponse> listarTodos() {
        return ventaRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public VentaResponse buscarPorId(Integer id) {
        Venta venta = ventaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Venta con ID " + id + " no encontrada."));
        return toResponse(venta);
    }

    @Override
    @Transactional
    public VentaResponse registrar(VentaRequest request) {

        if (request.getDetalles() == null || request.getDetalles().isEmpty()) {
            throw new RuntimeException("La venta debe tener al menos un detalle.");
        }

        // 1. Calcular subtotal por cada detalle y acumular el total de la venta
        BigDecimal totalVenta = BigDecimal.ZERO;

        for (DetalleVentaRequest item : request.getDetalles()) {
            BigDecimal descuento = item.getDescuento() != null ? item.getDescuento() : BigDecimal.ZERO;
            // subtotal = (cantidad * precioUnitario) - descuento
            BigDecimal subtotal = item.getCantidad()
                    .multiply(item.getPrecioUnitario())
                    .subtract(descuento);
            totalVenta = totalVenta.add(subtotal);
        }

        // 2. Guardar la venta con el total real calculado
        Venta venta = Venta.builder()
                .fechaVenta(LocalDateTime.now())
                .tipoDocumento(request.getTipoDocumento())
                .numeroDocumento(request.getNumeroDocumento())
                .estado(request.getEstado() != null ? request.getEstado() : "C")
                .total(totalVenta)
                .referenciaTransaccion(UUID.randomUUID().toString())
                .idCliente(request.getIdCliente())
                .idAlmacen(request.getIdAlmacen())
                .idMetodoPago(request.getIdMetodoPago())
                .build();

        Venta ventaGuardada = ventaRepository.save(venta);

        // 3. Guardar cada detalle con el id_venta recién generado
        for (DetalleVentaRequest item : request.getDetalles()) {
            BigDecimal descuento = item.getDescuento() != null ? item.getDescuento() : BigDecimal.ZERO;
            BigDecimal subtotal = item.getCantidad()
                    .multiply(item.getPrecioUnitario())
                    .subtract(descuento);

            DetalleVenta detalle = DetalleVenta.builder()
                    .idVenta(ventaGuardada.getIdVenta())
                    .idProducto(item.getIdProducto())
                    .cantidad(item.getCantidad())
                    .precioUnitario(item.getPrecioUnitario())
                    .descuento(descuento)
                    .subtotal(subtotal)
                    .build();

            detalleVentaRepository.save(detalle);
        }

        return toResponse(ventaGuardada);
    }

    @Override
    public VentaResponse anular(Integer id) {
        Venta venta = ventaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Venta con ID " + id + " no encontrada."));
        venta.setEstado("A");
        return toResponse(ventaRepository.save(venta));
    }

    // ─── Mappers ──────────────────────────────────────────────────────────────

    private VentaResponse toResponse(Venta venta) {
        List<DetalleVentaResponse> detalles = detalleVentaRepository
                .findByIdVenta(venta.getIdVenta())
                .stream()
                .map(this::toDetalleResponse)
                .collect(Collectors.toList());

        return VentaResponse.builder()
                .idVenta(venta.getIdVenta())
                .fechaVenta(venta.getFechaVenta())
                .tipoDocumento(venta.getTipoDocumento())
                .numeroDocumento(venta.getNumeroDocumento())
                .estado(venta.getEstado())
                .total(venta.getTotal())
                .referenciaTransaccion(venta.getReferenciaTransaccion())
                .idCliente(venta.getIdCliente())
                .idAlmacen(venta.getIdAlmacen())
                .idMetodoPago(venta.getIdMetodoPago())
                .detalles(detalles)
                .build();
    }

    private DetalleVentaResponse toDetalleResponse(DetalleVenta detalle) {
        return DetalleVentaResponse.builder()
                .idDetalleVenta(detalle.getIdDetalleVenta())
                .idProducto(detalle.getIdProducto())
                .cantidad(detalle.getCantidad())
                .precioUnitario(detalle.getPrecioUnitario())
                .descuento(detalle.getDescuento())
                .subtotal(detalle.getSubtotal())
                .build();
    }
}
