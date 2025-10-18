package com.example.demo.controller;

import com.example.demo.service.PaymentService;
import com.example.demo.service.VnPayService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/vnpay")
@RequiredArgsConstructor
public class VnPayController {

    private final VnPayService vnPayService;
    private final PaymentService paymentService;

    // -------------------------
    // 🧩 Tạo link thanh toán
    // -------------------------
    @GetMapping("/create")
    public void createPayment(@RequestParam Long saleId,
                              HttpServletRequest request,
                              HttpServletResponse response) throws IOException {
        String paymentUrl = vnPayService.createPaymentUrl(saleId, request);
        response.sendRedirect(paymentUrl);
    }

    // -------------------------
    // 🧩 Xử lý callback từ VNPay
    // -------------------------
    @GetMapping("/return")
    public String vnPayReturn(HttpServletRequest request) {
        // --- Lấy tất cả params từ VNPay ---
        // --- Lấy toàn bộ params từ query string gốc (raw, không decode) ---
        Map<String, String> vnpParams = new HashMap<>();
        String query = request.getQueryString();
        if (query != null) {
            for (String param : query.split("&")) {
                String[] pair = param.split("=", 2);
                if (pair.length == 2) {
                    vnpParams.put(pair[0], pair[1]);
                }
            }
        }


        // --- Lấy chữ ký ---
        String vnp_SecureHash = vnpParams.remove("vnp_SecureHash");
        vnpParams.remove("vnp_SecureHashType");

        // --- Log toàn bộ params để debug ---
        System.out.println("🔸 Callback parameters from VNPay:");
        vnpParams.forEach((k, v) -> System.out.println(k + " = " + v));
        System.out.println("🔸 Received vnp_SecureHash: " + vnp_SecureHash);

        // --- Xác minh chữ ký ---
        boolean isValid = vnPayService.validateSignature(vnpParams, vnp_SecureHash);
        if (!isValid) {
            return "Chữ ký không hợp lệ - nghi ngờ giả mạo dữ liệu!";
        }

        String responseCode = vnpParams.get("vnp_ResponseCode");
        String saleIdStr = vnpParams.get("vnp_TxnRef");
        String amountStr = vnpParams.get("vnp_Amount");
        String transactionNo = vnpParams.get("vnp_TransactionNo");

        if ("00".equals(responseCode)) {
            Long saleId = Long.parseLong(saleIdStr);
            BigDecimal amount = new BigDecimal(amountStr).divide(BigDecimal.valueOf(100));
            paymentService.savePaymentSuccess(saleId, transactionNo);

            return "Thanh toán thành công cho hóa đơn #" + saleId
                    + " - Số tiền: " + amount + " VND";
        } else {
            return "Thanh toán thất bại! Mã lỗi: " + responseCode;
        }
    }
}
