package pe.edu.vallegrande.mybackend.service;

import java.util.List;
import java.util.Map;

import pe.edu.vallegrande.mybackend.model.PromocionProducto;

public interface PromocionProductoService {
    
    // CRUD Básico
    List<PromocionProducto> listarTodos();
    PromocionProducto guardar(PromocionProducto relacion);
    void eliminar(Integer id);
    
    // Métodos específicos
    List<PromocionProducto> listarPorPromocion(Integer idPromocion);
    List<PromocionProducto> listarPorProducto(Integer idProducto);
    void agregarProductosAPromocion(Integer idPromocion, List<Integer> idsProductos);
    void quitarProductoDePromocion(Integer idPromocion, Integer idProducto);
    
    // Método útil para ventas: calcular precio con descuento
    Map<String, Object> calcularPrecioConDescuento(Integer idProducto, Double precioOriginal, Integer cantidad);
    
    // Obtener promociones activas para un producto
    List<PromocionProducto> obtenerPromocionesActivasPorProducto(Integer idProducto);
}