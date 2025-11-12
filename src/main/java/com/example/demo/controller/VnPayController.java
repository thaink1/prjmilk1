package com.example.demo.controller;

import com.example.demo.dto.BaseResponse;
import com.example.demo.service.PaymentService;
import com.example.demo.service.VnPayService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/vnpay")
@RequiredArgsConstructor
@Slf4j
public class VnPayController {

    private final VnPayService vnPayService;
    private final PaymentService paymentService;

    /**
     *  API tạo URL thanh toán VNPay
     */
    @GetMapping("/create")
    public void createPayment(@RequestParam Long saleId,
                              HttpServletRequest request,
                              HttpServletResponse response) throws IOException {
        log.info("Creating VNPay payment URL for saleId: {}", saleId);
        String paymentUrl = vnPayService.createPaymentUrl(saleId, request);
        log.info("Redirecting to VNPay URL: {}", paymentUrl);
        response.sendRedirect(paymentUrl);
    }

    /**
     * API callback từ VNPay (sau khi người dùng thanh toán xong)
     */
    @GetMapping("/return")
    public BaseResponse<String> vnPayReturn(HttpServletRequest request) {
        log.info("VNPay callback received with query: {}", request.getQueryString());

        BaseResponse<String> response = new BaseResponse<>();
        Map<String, String> vnpParams = new HashMap<>();

        try {
            // --- Lấy toàn bộ tham số từ query string ---
            String query = request.getQueryString();
            if (query != null) {
                for (String param : query.split("&")) {
                    String[] pair = param.split("=", 2);
                    if (pair.length == 2) {
                        vnpParams.put(pair[0], pair[1]);
                    }
                }
            }

            // --- Lấy và loại bỏ chữ ký ---
            String vnp_SecureHash = vnpParams.remove("vnp_SecureHash");
            vnpParams.remove("vnp_SecureHashType");

            log.debug("VNPay parameters: {}", vnpParams);

            // --- Xác minh chữ ký ---
            boolean isValid = vnPayService.validateSignature(vnpParams, vnp_SecureHash);
            if (!isValid) {
                log.warn("Invalid VNPay signature detected!");
                response.setCode(1001);
                response.setMessage("Chữ ký không hợp lệ - nghi ngờ giả mạo dữ liệu!");
                response.setRequestId(MDC.get("requestId"));
                response.setResponseTime(LocalDateTime.now());
                return response;
            }

            // --- Lấy các thông tin cần thiết ---
            String responseCode = vnpParams.get("vnp_ResponseCode");
            String saleIdStr = vnpParams.get("vnp_TxnRef");
            String amountStr = vnpParams.get("vnp_Amount");
            String transactionNo = vnpParams.get("vnp_TransactionNo");

            if (responseCode == null || saleIdStr == null) {
                response.setCode(1002);
                response.setMessage("Thiếu dữ liệu từ VNPay callback!");
                response.setRequestId(MDC.get("requestId"));
                response.setResponseTime(LocalDateTime.now());
                return response;
            }

            // --- Xử lý kết quả thanh toán ---
            if ("00".equals(responseCode)) {
                Long saleId = Long.parseLong(saleIdStr);
                BigDecimal amount = new BigDecimal(amountStr).divide(BigDecimal.valueOf(100));

                paymentService.savePaymentSuccess(saleId, transactionNo);

                response.setMessage("Payment successful");
                response.setBody("Thanh toán thành công cho hóa đơn #" + saleId + " - Số tiền: " + amount + " VND");

                log.info("Payment success for saleId={} amount={} transactionNo={}",
                        saleId, amount, transactionNo);
            } else {
                response.setCode(1003);
                response.setMessage("Thanh toán thất bại! Mã lỗi: " + responseCode);
                log.warn("Payment failed for responseCode={}", responseCode);
            }

        } catch (Exception e) {
            log.error("Error processing VNPay callback", e);
            response.setCode(1999);
            response.setMessage("Lỗi xử lý callback từ VNPay: " + e.getMessage());
        }

        response.setRequestId(MDC.get("requestId"));
        response.setResponseTime(LocalDateTime.now());
        return response;
    }
}
