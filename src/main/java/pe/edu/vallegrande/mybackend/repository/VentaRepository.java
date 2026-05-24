package pe.edu.vallegrande.mybackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pe.edu.vallegrande.mybackend.model.Venta;

public interface VentaRepository extends JpaRepository<Venta, Integer> {

    // Último id_cliente registrado (lógica temporal)
    @Query(value = "SELECT TOP 1 id_cliente FROM cliente ORDER BY id_cliente DESC", nativeQuery = true)
    Integer findUltimoIdCliente();

    // Último id_almacen registrado (lógica temporal)
    @Query(value = "SELECT TOP 1 id_almacen FROM almacen ORDER BY id_almacen DESC", nativeQuery = true)
    Integer findUltimoIdAlmacen();

    // Último id_metodo_pago registrado (lógica temporal)
    @Query(value = "SELECT TOP 1 id_metodo_pago FROM metodo_pago ORDER BY id_metodo_pago DESC", nativeQuery = true)
    Integer findUltimoIdMetodoPago();
}
