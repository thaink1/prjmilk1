package com.example.demo.service;

import com.example.demo.model.Sale_Invoice;
import com.example.demo.repo.Sale_InvoiceRepo;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
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
        Sale_Invoice saleInvoice = saleInvoiceRepo.findById(saleId)
                .orElseThrow(() -> new RuntimeException("SaleInvoice Not Found"));

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

        // --- Sắp xếp key theo alphabet ---
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

                // Encode cả name và value khi tạo hashData (theo tài liệu VNPay)
                String encodedName = URLEncoder.encode(name, StandardCharsets.US_ASCII);
                String encodedValue = URLEncoder.encode(value, StandardCharsets.US_ASCII);

                hashData.append(encodedName).append("=").append(encodedValue);
                query.append(encodedName).append("=").append(encodedValue);
            }
        }

        // --- Sinh SecureHash ---
        String vnp_SecureHash = hmacSHA512(vnpHashSecret, hashData.toString());
        query.append("&vnp_SecureHashType=HmacSHA512&vnp_SecureHash=").append(vnp_SecureHash);

        // --- URL cuối cùng ---
        String paymentUrl = vnpPayUrl + "?" + query.toString().replace("+", "%20");

        // --- Log ra console để kiểm tra ---
        System.out.println("Client HashData: " + hashData);
        System.out.println("Client SecureHash: " + vnp_SecureHash);
        System.out.println("Final URL: " + paymentUrl);

        return paymentUrl;
    }

    private String getClientIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty()) ip = request.getRemoteAddr();
        if ("0:0:0:0:0:0:0:1".equals(ip)) ip = "127.0.0.1";
        return ip;
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
            throw new RuntimeException("Error while hashing HMAC SHA512", e);
        }
    }

    // --- Xác minh chữ ký khi VNPay callback ---
    public boolean validateSignature(Map<String, String> vnpParams, String vnp_SecureHash) {
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

        System.out.println("Server HashData: " + hashData);
        System.out.println("Server SecureHash (calculated): " + calculatedHash);
        System.out.println("Received vnp_SecureHash: " + vnp_SecureHash);

        return calculatedHash.equalsIgnoreCase(vnp_SecureHash);
    }
}
