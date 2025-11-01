package com.example.demo.service;

import com.example.demo.model.Sale_Invoice;
import com.example.demo.repo.Sale_InvoiceRepo;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class VnPayService {

    private final Sale_InvoiceRepo saleInvoiceRepo;

    @Value("${vnpay.vnp_TmnCode}")
    private String vnpTmnCode;

    @Value("${vnpay.vnp_HashSecret}")
    private String vnpHashSecret;

    @Value("${vnpay.vnp_PayUrl}")
    private String vnpPayUrl;

    @Value("${vnpay.vnp_ReturnUrl}")
    private String vnpReturnUrl;

    public String createPaymentUrl(Long saleId, HttpServletRequest request) {
        try {
            log.info("Creating VNPay payment URL for SaleInvoice ID: {}", saleId);

            Sale_Invoice saleInvoice = saleInvoiceRepo.findById(saleId)
                    .orElseThrow(() -> {
                        log.warn("SaleInvoice not found with ID: {}", saleId);
                        return new RuntimeException("SaleInvoice Not Found");
                    });

            Map<String, String> vnp_Params = new HashMap<>();
            vnp_Params.put("vnp_Version", "2.1.0");
            vnp_Params.put("vnp_Command", "pay");
            vnp_Params.put("vnp_TmnCode", vnpTmnCode);
            vnp_Params.put("vnp_Amount", String.valueOf(
                    saleInvoice.getTotalAmount().multiply(BigDecimal.valueOf(100)).intValue()
            ));
            vnp_Params.put("vnp_CurrCode", "VND");
            vnp_Params.put("vnp_TxnRef", String.valueOf(saleId));
            vnp_Params.put("vnp_OrderInfo", "Thanh toan hoa don " + saleId);
            vnp_Params.put("vnp_OrderType", "other");
            vnp_Params.put("vnp_Locale", "vn");
            vnp_Params.put("vnp_ReturnUrl", vnpReturnUrl);
            vnp_Params.put("vnp_IpAddr", getClientIpAddress(request));
            vnp_Params.put("vnp_BankCode", "NCB");

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
            vnp_Params.put("vnp_CreateDate", LocalDateTime.now().format(formatter));
            vnp_Params.put("vnp_ExpireDate", LocalDateTime.now().plusMinutes(15).format(formatter));

            // Sắp xếp key alphabet
            List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
            Collections.sort(fieldNames);

            StringBuilder hashData = new StringBuilder();
            StringBuilder query = new StringBuilder();
            boolean first = true;

            for (String name : fieldNames) {
                String value = vnp_Params.get(name);
                if (value != null && !value.isEmpty()) {
                    if (!first) {
                        hashData.append("&");
                        query.append("&");
                    }
                    first = false;

                    String encodedName = URLEncoder.encode(name, StandardCharsets.US_ASCII);
                    String encodedValue = URLEncoder.encode(value, StandardCharsets.US_ASCII);

                    hashData.append(encodedName).append("=").append(encodedValue);
                    query.append(encodedName).append("=").append(encodedValue);
                }
            }

            // Sinh SecureHash
            String vnp_SecureHash = hmacSHA512(vnpHashSecret, hashData.toString());
            query.append("&vnp_SecureHashType=HmacSHA512&vnp_SecureHash=").append(vnp_SecureHash);

            String paymentUrl = vnpPayUrl + "?" + query.toString().replace("+", "%20");

            log.info("VNPay Payment URL generated successfully for sale ID: {}", saleId);
            log.debug("Client HashData: {}", hashData);
            log.debug("Client SecureHash: {}", vnp_SecureHash);
            log.debug("Final Payment URL: {}", paymentUrl);

            return paymentUrl;

        } catch (Exception e) {
            log.error("Error creating VNPay payment URL for SaleInvoice ID: {}", saleId, e);
            throw e;
        }
    }

    private String getClientIpAddress(HttpServletRequest request) {
        try {
            String ip = request.getHeader("X-Forwarded-For");
            if (ip == null || ip.isEmpty()) ip = request.getRemoteAddr();
            if ("0:0:0:0:0:0:0:1".equals(ip)) ip = "127.0.0.1";
            return ip;
        } catch (Exception e) {
            log.warn("Error detecting client IP address, fallback to localhost", e);
            return "127.0.0.1";
        }
    }

    private String hmacSHA512(String key, String data) {
        try {
            Mac hmac512 = Mac.getInstance("HmacSHA512");
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
            hmac512.init(secretKey);
            byte[] hashBytes = hmac512.doFinal(data.getBytes(StandardCharsets.UTF_8));
            StringBuilder result = new StringBuilder();
            for (byte b : hashBytes) result.append(String.format("%02x", b));
            return result.toString();
        } catch (Exception e) {
            log.error("Error while generating HMAC SHA512 hash", e);
            throw new RuntimeException("Error while hashing HMAC SHA512", e);
        }
    }

    // --- Xác minh chữ ký khi VNPay callback ---
    public boolean validateSignature(Map<String, String> vnpParams, String vnp_SecureHash) {
        try {
            log.info("Validating VNPay signature...");

            List<String> fieldNames = new ArrayList<>(vnpParams.keySet());
            Collections.sort(fieldNames);

            StringBuilder hashData = new StringBuilder();
            boolean first = true;

            for (String name : fieldNames) {
                String value = vnpParams.get(name);
                if (value != null && !value.isEmpty()) {
                    if (!first) hashData.append("&");
                    first = false;
                    hashData.append(name).append("=").append(value);
                }
            }

            String calculatedHash = hmacSHA512(vnpHashSecret, hashData.toString());

            log.debug("Server HashData: {}", hashData);
            log.debug("Server SecureHash (calculated): {}", calculatedHash);
            log.debug("Received vnp_SecureHash: {}", vnp_SecureHash);

            boolean isValid = calculatedHash.equalsIgnoreCase(vnp_SecureHash);
            if (isValid) {
                log.info("VNPay signature validated successfully.");
            } else {
                log.warn("VNPay signature validation failed!");
            }
            return isValid;
        } catch (Exception e) {
            log.error("Error validating VNPay signature", e);
            return false;
        }
    }
}
