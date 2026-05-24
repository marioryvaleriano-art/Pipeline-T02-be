package pe.edu.vallegrande.mybackend.repository;

import pe.edu.vallegrande.mybackend.model.Promocion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PromocionRepository extends JpaRepository<Promocion, Integer> {

    // Buscar promociones activas por fecha
    @Query("SELECT p FROM Promocion p WHERE p.estado = true " +
           "AND :fechaActual BETWEEN p.fechaInicio AND p.fechaFin")
    List<Promocion> findPromocionesActivas(@Param("fechaActual") LocalDateTime fechaActual);

    // Buscar promociones por tipo
    List<Promocion> findByTipoPromocionAndEstado(String tipoPromocion, Boolean estado);

    // Buscar promociones vigentes en un rango de fechas
    List<Promocion> findByFechaInicioLessThanEqualAndFechaFinGreaterThanEqualAndEstadoTrue(
            LocalDateTime fechaFin, LocalDateTime fechaInicio);

    // Verificar si existe promoción con nombre (evitar duplicados)
    boolean existsByNombre(String nombre);

    // Buscar promociones por estado
    List<Promocion> findByEstado(Boolean estado);

    // Buscar promociones que terminan pronto (próximos 7 días)
    @Query("SELECT p FROM Promocion p WHERE p.estado = true " +
           "AND p.fechaFin BETWEEN :inicio AND :fin")
    List<Promocion> findPromocionesPorTerminar(
            @Param("inicio") LocalDateTime inicio,
            @Param("fin") LocalDateTime fin);
}