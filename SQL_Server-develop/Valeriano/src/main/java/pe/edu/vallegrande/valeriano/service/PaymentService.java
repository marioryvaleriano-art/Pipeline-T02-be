package pe.edu.vallegrande.valeriano.service;

import pe.edu.vallegrande.valeriano.model.Payment;
import java.util.List;

public interface PaymentService {
    List<Payment> findAll();
    Payment findById(Integer id);
    Payment save(Payment payment);
    void delete(Integer id);
}