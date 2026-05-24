package pe.edu.vallegrande.mybackend.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.vallegrande.mybackend.model.Almacen;
import java.util.List;


@Repository
public interface AlmacenRepository extends JpaRepository<Almacen, Integer> {


    // 🔍 Filtra por estado (Trae los "Activos" o "Inactivos")
    List<Almacen> findByEstado(String estado);


    // 📊 Adicional: Los trae filtrados por estado y ordenados alfabéticamente de la A a la Z
    List<Almacen> findByEstadoOrderByNombreAsc(String estado);
}
