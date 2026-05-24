package pe.edu.vallegrande.mybackend.repository;

import pe.edu.vallegrande.mybackend.model.PromocionProducto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PromocionProductoRepository extends JpaRepository<PromocionProducto, Integer> {

    // Buscar productos por promoción
    List<PromocionProducto> findByIdPromocion(Integer idPromocion);

    // Buscar promociones por producto
    List<PromocionProducto> findByIdProducto(Integer idProducto);

    // Verificar si ya existe relación
    boolean existsByIdPromocionAndIdProducto(Integer idPromocion, Integer idProducto);

    // Eliminar todas las relaciones de una promoción
    void deleteByIdPromocion(Integer idPromocion);

    // Obtener promociones activas para un producto específico
    @Query("SELECT pp FROM PromocionProducto pp " +
           "JOIN pp.promocion p " +
           "WHERE pp.idProducto = :idProducto " +
           "AND p.estado = true " +
           "AND CURRENT_TIMESTAMP BETWEEN p.fechaInicio AND p.fechaFin")
    List<PromocionProducto> findPromocionesActivasByProducto(@Param("idProducto") Integer idProducto);

    // Obtener todos los productos con sus promociones activas
    @Query("SELECT pp FROM PromocionProducto pp " +
           "JOIN pp.promocion p " +
           "WHERE p.estado = true " +
           "AND CURRENT_TIMESTAMP BETWEEN p.fechaInicio AND p.fechaFin")
    List<PromocionProducto> findAllPromocionesActivas();
}