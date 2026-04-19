package pe.edu.vallegrande.valeriano.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.vallegrande.valeriano.model.Payment;
import pe.edu.vallegrande.valeriano.repository.PaymentRepository;
import pe.edu.vallegrande.valeriano.service.PaymentService;

import java.util.List;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Override
    public List<Payment> findAll() {
        return paymentRepository.findAll();
    }

    @Override
    public Payment findById(Integer id) {
        return paymentRepository.findById(id).orElse(null);
    }

    @Override
    public Payment save(Payment payment) {
        return paymentRepository.save(payment);
    }

    @Override
    public void delete(Integer id) {
        paymentRepository.deleteById(id);
    }
}