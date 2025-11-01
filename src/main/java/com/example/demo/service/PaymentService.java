package com.example.demo.service;

import com.example.demo.model.Payment;
import com.example.demo.model.Sale_Invoice;
import com.example.demo.repo.PaymentRepo;
import com.example.demo.repo.Sale_InvoiceRepo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
@Slf4j
public class PaymentService {

    private final PaymentRepo paymentRepo;
    private final Sale_InvoiceRepo saleInvoiceRepo;

    public void savePaymentSuccess(Long saleId, String transactionCode) {
        try {
            log.info("Processing successful payment for sale ID: {}, transactionCode: {}", saleId, transactionCode);

            Sale_Invoice saleInvoice = saleInvoiceRepo.findById(saleId)
                    .orElseThrow(() -> {
                        log.warn("Sale invoice not found with ID: {}", saleId);
                        return new RuntimeException("Sale invoice not found");
                    });

            Payment payment = new Payment();
            payment.setSaleId(saleId);
            payment.setPaymentMethod("VNPay");
            payment.setAmount(saleInvoice.getTotalAmount());
            payment.setStatus("SUCCESS");
            payment.setTransactionCode(transactionCode);
            payment.setCreatedAt(LocalDateTime.now());

            paymentRepo.save(payment);
            log.info("Payment recorded successfully for sale ID: {} with transactionCode: {}", saleId, transactionCode);

        } catch (Exception e) {
            log.error("Error while saving successful payment for sale ID: {}", saleId, e);
            throw e;
        }
    }
}
