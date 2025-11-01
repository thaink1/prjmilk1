package com.example.demo.controller;

import com.example.demo.dto.BaseResponse;
import com.example.demo.service.PaymentService;
import com.example.demo.service.VnPayService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/vnpay")
@RequiredArgsConstructor
@Slf4j
public class VnPayController {

    private final VnPayService vnPayService;
    private final PaymentService paymentService;

    // -------------------------
    // Tạo link thanh toán
    // -------------------------
    @GetMapping("/create")
    public void createPayment(@RequestParam Long saleId,
                              HttpServletRequest request,
                              HttpServletResponse response) throws IOException {
        log.info("Creating VNPay payment URL for saleId: {}", saleId);
        String paymentUrl = vnPayService.createPaymentUrl(saleId, request);
        log.info("Redirecting to VNPay URL: {}", paymentUrl);
        response.sendRedirect(paymentUrl);
    }

    // -------------------------
    // Xử lý callback từ VNPay
    // -------------------------
    @GetMapping("/return")
    public BaseResponse<String> vnPayReturn(HttpServletRequest request) {
        BaseResponse<String> response = new BaseResponse<>();
        Map<String, String> vnpParams = new HashMap<>();
        try {
            // Lấy toàn bộ params từ query string gốc
            String query = request.getQueryString();
            if (query != null) {
                for (String param : query.split("&")) {
                    String[] pair = param.split("=", 2);
                    if (pair.length == 2) {
                        vnpParams.put(pair[0], pair[1]);
                    }
                }
            }

            // Lấy và loại bỏ chữ ký
            String vnp_SecureHash = vnpParams.remove("vnp_SecureHash");
            vnpParams.remove("vnp_SecureHashType");

            log.info("VNPay callback parameters: {}", vnpParams);
            log.info("Received vnp_SecureHash: {}", vnp_SecureHash);

            // Xác minh chữ ký
            boolean isValid = vnPayService.validateSignature(vnpParams, vnp_SecureHash);
            if (!isValid) {
                log.warn("Invalid VNPay signature! Possible tampering detected.");
                response.setMessage("Chữ ký không hợp lệ - nghi ngờ giả mạo dữ liệu!");
                return response;
            }

            String responseCode = vnpParams.get("vnp_ResponseCode");
            String saleIdStr = vnpParams.get("vnp_TxnRef");
            String amountStr = vnpParams.get("vnp_Amount");
            String transactionNo = vnpParams.get("vnp_TransactionNo");

            if (responseCode == null || saleIdStr == null) {
                response.setMessage("Thiếu dữ liệu từ VNPay callback!");
                return response;
            }

            if ("00".equals(responseCode)) {
                Long saleId = Long.parseLong(saleIdStr);
                BigDecimal amount = new BigDecimal(amountStr).divide(BigDecimal.valueOf(100));
                paymentService.savePaymentSuccess(saleId, transactionNo);

                response.setBody("Thanh toán thành công cho hóa đơn #" + saleId
                        + " - Số tiền: " + amount + " VND");
                response.setMessage("Payment successful");
                log.info("Payment success for saleId {}: amount={} transactionNo={}", saleId, amount, transactionNo);
            } else {
                response.setMessage("Thanh toán thất bại! Mã lỗi: " + responseCode);
                log.warn("Payment failed with responseCode: {}", responseCode);
            }

        } catch (Exception e) {
            log.error("Error processing VNPay callback", e);
            response.setMessage("Lỗi xử lý callback từ VNPay: " + e.getMessage());
        }

        return response;
    }
}
