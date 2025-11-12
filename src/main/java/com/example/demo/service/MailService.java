package com.example.demo.service;

import com.example.demo.dto.InventoryHistoryMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailService {

    private final JavaMailSender mailSender;

    @Value("${app.mail.default-recipient}")
    private String defaultRecipient;

    public void sendInventoryHistoryEmail(InventoryHistoryMessage message) {
        String action = message.getChangeQuantity() > 0 ? "Nhập hàng" : "Bán / Hoàn hàng";
        String subject = " Cập nhật tồn kho - " + action;
        String body = "Xin chào,\n\n" +
                "Sản phẩm ID: " + message.getProductId() + "\n" +
                "Số lượng thay đổi: " + message.getChangeQuantity() + "\n" +
                "Lý do: " + message.getReason() + "\n" +
                "Thời gian: " + message.getDate() + "\n\n" +
                "Vui lòng kiểm tra lại tồn kho trong hệ thống.\n\nTrân trọng.";

        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(defaultRecipient);
        mail.setSubject(subject);
        mail.setText(body);

        mailSender.send(mail);

        log.info(" Đã gửi email thông báo thay đổi tồn kho đến {}", defaultRecipient);
    }
}
