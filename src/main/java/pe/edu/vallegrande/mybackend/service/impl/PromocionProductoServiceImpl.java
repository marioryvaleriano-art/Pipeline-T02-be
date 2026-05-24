package pe.edu.vallegrande.mybackend.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pe.edu.vallegrande.mybackend.model.Promocion;
import pe.edu.vallegrande.mybackend.model.PromocionProducto;
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

    @Override
    public List<PromocionProducto> listarTodos() {
        return relacionRepository.findAll();
    }

    @Override
    public PromocionProducto guardar(PromocionProducto relacion) {
        // Validar que no exista duplicado
        if (relacionRepository.existsByIdPromocionAndIdProducto(
                relacion.getIdPromocion(), relacion.getIdProducto())) {
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
        return relacionRepository.findByIdPromocion(idPromocion);
    }

    @Override
    public List<PromocionProducto> listarPorProducto(Integer idProducto) {
        return relacionRepository.findByIdProducto(idProducto);
    }

    @Override
    public void agregarProductosAPromocion(Integer idPromocion, List<Integer> idsProductos) {
        for (Integer idProducto : idsProductos) {
            if (!relacionRepository.existsByIdPromocionAndIdProducto(idPromocion, idProducto)) {
                PromocionProducto relacion = new PromocionProducto(idPromocion, idProducto, false);
                relacionRepository.save(relacion);
            }
        }
    }

    @Override
    public void quitarProductoDePromocion(Integer idPromocion, Integer idProducto) {
        List<PromocionProducto> relaciones = relacionRepository.findByIdPromocion(idPromocion);
        relaciones.stream()
                .filter(r -> r.getIdProducto().equals(idProducto))
                .findFirst()
                .ifPresent(r -> relacionRepository.delete(r));
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
        
        // Tomar la primera promoción activa (podrías implementar lógica para elegir la mejor)
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
            BigDecimal llevar = promocion.getValorDescuento(); // X
            BigDecimal pagar = llevar.subtract(BigDecimal.ONE); // Y
            
            // Calcular número de paquetes completos y sobrantes
            BigDecimal[] division = cantidadBD.divideAndRemainder(llevar);
            BigDecimal paquetes = division[0];
            BigDecimal sobrantes = division[1];
            
            // Total a pagar = (paquetes * pagar * precioUnitario) + (sobrantes * precioUnitario)
            BigDecimal totalPagar = paquetes.multiply(pagar).multiply(precioOriginalBD)
                    .add(sobrantes.multiply(precioOriginalBD))
                    .setScale(2, RoundingMode.HALF_UP);
            
            descuentoAplicado = subtotalOriginal.subtract(totalPagar).setScale(2, RoundingMode.HALF_UP);
            subtotalFinal = totalPagar;
            
            // Precio final por unidad (promedio)
            if (cantidad > 0) {
                precioFinal = totalPagar.divide(cantidadBD, 2, RoundingMode.HALF_UP);
            }
            
        } else if ("F".equals(promocion.getTipoPromocion())) {
            // Envío gratis o beneficio especial (sin cambio de precio)
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
        
        // Información adicional para depuración
        resultado.put("descripcionPromocion", promocion.getDescripcionPromocion());
        resultado.put("fechaInicio", promocion.getFechaInicio());
        resultado.put("fechaFin", promocion.getFechaFin());
        
        return resultado;
    }

    @Override
    public List<PromocionProducto> obtenerPromocionesActivasPorProducto(Integer idProducto) {
        return relacionRepository.findPromocionesActivasByProducto(idProducto);
    }
    
    // Método adicional útil: Obtener la mejor promoción para un producto (si hay múltiples)
    public Promocion obtenerMejorPromocionParaProducto(Integer idProducto, BigDecimal precioOriginal) {
        List<PromocionProducto> promocionesActivas = relacionRepository.findPromocionesActivasByProducto(idProducto);
        
        if (promocionesActivas.isEmpty()) {
            return null;
        }
        
        Promocion mejorPromocion = null;
        BigDecimal mayorAhorro = BigDecimal.ZERO;
        
        for (PromocionProducto pp : promocionesActivas) {
            Promocion p = pp.getPromocion();
            BigDecimal ahorro = BigDecimal.ZERO;
            
            if ("P".equals(p.getTipoPromocion())) {
                // Ahorro porcentual
                ahorro = precioOriginal.multiply(p.getValorDescuento().divide(new BigDecimal(100), 4, RoundingMode.HALF_UP));
            } else if ("L".equals(p.getTipoPromocion())) {
                // Para 2x1, ahorro = precioOriginal
                // Para 3x2, ahorro = precioOriginal (paga 2, lleva 3)
                ahorro = precioOriginal;
            }
            
            if (ahorro.compareTo(mayorAhorro) > 0) {
                mayorAhorro = ahorro;
                mejorPromocion = p;
            }
        }
        
        return mejorPromocion;
    }
    
    // Método adicional útil: Validar si un producto tiene promoción activa
    public boolean tienePromocionActiva(Integer idProducto) {
        List<PromocionProducto> promociones = relacionRepository.findPromocionesActivasByProducto(idProducto);
        return !promociones.isEmpty();
    }
    
    // Método adicional útil: Obtener todas las promociones activas con sus productos
    public Map<Promocion, List<PromocionProducto>> getPromocionesActivasAgrupadas() {
        List<PromocionProducto> todasActivas = relacionRepository.findAllPromocionesActivas();
        Map<Promocion, List<PromocionProducto>> mapa = new HashMap<>();
        
        for (PromocionProducto pp : todasActivas) {
            mapa.computeIfAbsent(pp.getPromocion(), k -> new ArrayList<>()).add(pp);
        }
        
        return mapa;
    }
}