package pe.edu.vallegrande.mybackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.vallegrande.mybackend.model.Producto;

/**
 * Repositorio para la entidad Producto.
 * Extiende JpaRepository que proporciona métodos CRUD básicos:
 * <ul>
 *   <li>save() - Guardar o actualizar un producto</li>
 *   <li>findById() - Buscar por ID</li>
 *   <li>findAll() - Listar todos los productos</li>
 *   <li>deleteById() - Eliminar por ID (físico)</li>
 *   <li>existsById() - Verificar existencia</li>
 * </ul>
 * 
 * @author Equipo de desarrollo
 * @see Producto
 */
@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {
    // JpaRepository ya incluye los métodos básicos:
    // save(), findById(), findAll(), deleteById(), etc.
    // Para métodos personalizados, agregar consultas aquí.
}