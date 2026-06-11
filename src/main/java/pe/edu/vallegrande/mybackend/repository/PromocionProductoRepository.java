package pe.edu.vallegrande.mybackend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import pe.edu.vallegrande.mybackend.model.PromocionProducto;

@Repository
public interface PromocionProductoRepository extends JpaRepository<PromocionProducto, Integer> {

    // Métodos derived query
    List<PromocionProducto> findByPromocion_IdPromocion(Integer idPromocion);
    
    List<PromocionProducto> findByProducto_IdProducto(Integer idProducto);
    
    boolean existsByPromocion_IdPromocionAndProducto_IdProducto(Integer idPromocion, Integer idProducto);
    
    void deleteByPromocion_IdPromocion(Integer idPromocion);
    
    void deleteByPromocion_IdPromocionAndProducto_IdProducto(Integer idPromocion, Integer idProducto);

    // Obtener promociones activas para un producto específico
    @Query("SELECT pp FROM PromocionProducto pp " +
           "JOIN FETCH pp.promocion p " +
           "WHERE pp.producto.idProducto = :idProducto " +
           "AND p.estado = true " +
           "AND CURRENT_TIMESTAMP BETWEEN p.fechaInicio AND p.fechaFin")
    List<PromocionProducto> findPromocionesActivasByProducto(@Param("idProducto") Integer idProducto);

    // Obtener todos los productos con sus promociones activas
    @Query("SELECT pp FROM PromocionProducto pp " +
           "JOIN FETCH pp.promocion p " +
           "WHERE p.estado = true " +
           "AND CURRENT_TIMESTAMP BETWEEN p.fechaInicio AND p.fechaFin")
    List<PromocionProducto> findAllPromocionesActivas();
}