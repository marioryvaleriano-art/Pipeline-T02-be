package pe.edu.vallegrande.valeriano.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.vallegrande.valeriano.model.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {
    // Aquí puedes agregar consultas personalizadas más adelante si las necesitas
}