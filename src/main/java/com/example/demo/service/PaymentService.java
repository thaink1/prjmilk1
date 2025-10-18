package com.example.demo.service;

import com.example.demo.model.Payment;
import com.example.demo.model.Sale_Invoice;
import com.example.demo.repo.PaymentRepo;
import com.example.demo.repo.Sale_InvoiceRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class PaymentService {
    private final PaymentRepo paymentRepo;
    private final Sale_InvoiceRepo saleInvoiceRepo;

    public void savePaymentSuccess(Long saleId, String transactionCode) {
        Sale_Invoice saleInvoice = saleInvoiceRepo.findById(saleId)
                .orElseThrow(() -> new RuntimeException("saleInvoice not found"));
        Payment payment = new Payment();
        payment.setSaleId(saleId);
        payment.setPaymentMethod("VNPay");
        payment.setAmount(saleInvoice.getTotalAmount());
        payment.setStatus("SUCCESS");
        payment.setTransactionCode(transactionCode);
        payment.setCreatedAt(LocalDateTime.now());
        paymentRepo.save(payment);

    }
}
