package pe.edu.vallegrande.mybackend.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pe.edu.vallegrande.mybackend.model.Producto;
import pe.edu.vallegrande.mybackend.model.Promocion;
import pe.edu.vallegrande.mybackend.model.PromocionProducto;
import pe.edu.vallegrande.mybackend.repository.ProductoRepository;
import pe.edu.vallegrande.mybackend.repository.PromocionProductoRepository;
import pe.edu.vallegrande.mybackend.repository.PromocionRepository;
import pe.edu.vallegrande.mybackend.service.PromocionProductoService;

@Service
@Transactional
public class PromocionProductoServiceImpl implements PromocionProductoService {

    @Autowired
    private PromocionProductoRepository relacionRepository;
    
    @Autowired
    private PromocionRepository promocionRepository;
    
    @Autowired
    private ProductoRepository productoRepository;

    @Override
    public List<PromocionProducto> listarTodos() {
        return relacionRepository.findAll();
    }

    @Override
    public PromocionProducto guardar(PromocionProducto relacion) {
        // Validar que no exista duplicado
        if (relacionRepository.existsByPromocion_IdPromocionAndProducto_IdProducto(
                relacion.getPromocion().getIdPromocion(), 
                relacion.getProducto().getIdProducto())) {
            throw new RuntimeException("Este producto ya está asociado a la promoción");
        }
        return relacionRepository.save(relacion);
    }

    @Override
    public void eliminar(Integer id) {
        relacionRepository.deleteById(id);
    }

    @Override
    public List<PromocionProducto> listarPorPromocion(Integer idPromocion) {
        return relacionRepository.findByPromocion_IdPromocion(idPromocion);
    }

    @Override
    public List<PromocionProducto> listarPorProducto(Integer idProducto) {
        return relacionRepository.findByProducto_IdProducto(idProducto);
    }

    @Override
    public void agregarProductosAPromocion(Integer idPromocion, List<Integer> idsProductos) {
        Promocion promocion = promocionRepository.findById(idPromocion)
                .orElseThrow(() -> new RuntimeException("Promoción no encontrada"));
        
        for (Integer idProducto : idsProductos) {
            if (!relacionRepository.existsByPromocion_IdPromocionAndProducto_IdProducto(idPromocion, idProducto)) {
                Producto producto = productoRepository.findById(idProducto)
                        .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + idProducto));
                PromocionProducto relacion = new PromocionProducto(promocion, producto, false);
                relacionRepository.save(relacion);
            }
        }
    }

    @Override
    public void quitarProductoDePromocion(Integer idPromocion, Integer idProducto) {
        relacionRepository.deleteByPromocion_IdPromocionAndProducto_IdProducto(idPromocion, idProducto);
    }

    @Override
    public Map<String, Object> calcularPrecioConDescuento(Integer idProducto, Double precioOriginal, Integer cantidad) {
        Map<String, Object> resultado = new HashMap<>();
        
        // Convertir a BigDecimal para cálculos precisos
        BigDecimal precioOriginalBD = BigDecimal.valueOf(precioOriginal);
        BigDecimal cantidadBD = BigDecimal.valueOf(cantidad);
        BigDecimal subtotalOriginal = precioOriginalBD.multiply(cantidadBD);
        
        resultado.put("precioOriginal", precioOriginalBD);
        resultado.put("cantidad", cantidad);
        resultado.put("subtotalOriginal", subtotalOriginal);
        
        // Buscar promociones activas para el producto
        List<PromocionProducto> promocionesActivas = relacionRepository.findPromocionesActivasByProducto(idProducto);
        
        if (promocionesActivas.isEmpty()) {
            resultado.put("tienePromocion", false);
            resultado.put("precioFinal", precioOriginalBD);
            resultado.put("subtotalFinal", subtotalOriginal);
            resultado.put("descuentoAplicado", BigDecimal.ZERO);
            return resultado;
        }
        
        // Tomar la primera promoción activa
        Promocion promocion = promocionesActivas.get(0).getPromocion();
        BigDecimal precioFinal = precioOriginalBD;
        BigDecimal descuentoAplicado = BigDecimal.ZERO;
        BigDecimal subtotalFinal = subtotalOriginal;
        
        if ("P".equals(promocion.getTipoPromocion())) {
            // Descuento porcentual
            BigDecimal porcentajeDescuento = promocion.getValorDescuento();
            BigDecimal factorDescuento = porcentajeDescuento.divide(new BigDecimal(100), 4, RoundingMode.HALF_UP);
            descuentoAplicado = precioOriginalBD.multiply(factorDescuento).setScale(2, RoundingMode.HALF_UP);
            precioFinal = precioOriginalBD.subtract(descuentoAplicado).setScale(2, RoundingMode.HALF_UP);
            subtotalFinal = precioFinal.multiply(cantidadBD).setScale(2, RoundingMode.HALF_UP);
            
        } else if ("L".equals(promocion.getTipoPromocion())) {
            // Lleva X paga Y (ej: 2x1, 3x2)
            BigDecimal llevar = promocion.getValorDescuento();
            BigDecimal pagar = llevar.subtract(BigDecimal.ONE);
            
            BigDecimal[] division = cantidadBD.divideAndRemainder(llevar);
            BigDecimal paquetes = division[0];
            BigDecimal sobrantes = division[1];
            
            BigDecimal totalPagar = paquetes.multiply(pagar).multiply(precioOriginalBD)
                    .add(sobrantes.multiply(precioOriginalBD))
                    .setScale(2, RoundingMode.HALF_UP);
            
            descuentoAplicado = subtotalOriginal.subtract(totalPagar).setScale(2, RoundingMode.HALF_UP);
            subtotalFinal = totalPagar;
            
            if (cantidad > 0) {
                precioFinal = totalPagar.divide(cantidadBD, 2, RoundingMode.HALF_UP);
            }
            
        } else if ("F".equals(promocion.getTipoPromocion())) {
            resultado.put("beneficioEspecial", "Envío gratis");
            subtotalFinal = subtotalOriginal;
        }
        
        resultado.put("tienePromocion", true);
        resultado.put("idPromocion", promocion.getIdPromocion());
        resultado.put("nombrePromocion", promocion.getNombre());
        resultado.put("tipoPromocion", promocion.getTipoPromocion());
        resultado.put("valorDescuento", promocion.getValorDescuento());
        resultado.put("precioFinal", precioFinal);
        resultado.put("subtotalFinal", subtotalFinal);
        resultado.put("descuentoAplicado", descuentoAplicado);
        resultado.put("descripcionPromocion", promocion.getDescripcionPromocion());
        resultado.put("fechaInicio", promocion.getFechaInicio());
        resultado.put("fechaFin", promocion.getFechaFin());
        
        return resultado;
    }

    @Override
    public List<PromocionProducto> obtenerPromocionesActivasPorProducto(Integer idProducto) {
        return relacionRepository.findPromocionesActivasByProducto(idProducto);
    }
}