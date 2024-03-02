package ru.pishemzapuskayem.backendbookservice.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import ru.pishemzapuskayem.backendbookservice.model.entity.Account;
import ru.pishemzapuskayem.backendbookservice.model.entity.ConfirmToken;

@Service
@RequiredArgsConstructor
public class MailService {
    private static final String CONFIRM_TOKEN_SUBJECT = "Активируйте аккаунт";

    private final JavaMailSender javaMailSender;

    @Value("${mail.username}")
    private String mailFrom;
    @Value("${urls.frontend}")
    private String frontendUrl;

    public void sendMessage(String email, String subject, String msg) {
        MimeMessage mimeMailMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMailMessage, "UTF-8");

        try {
            helper.setFrom(mailFrom);
            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(msg, true);
            javaMailSender.send(mimeMailMessage);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    @Async
    public void sendToken(String email, ConfirmToken token) {
        sendMessage(
                email,
                CONFIRM_TOKEN_SUBJECT,
                buildConfirmTokenMsg(token)
        );
    }

    private String buildConfirmTokenMsg(ConfirmToken token) {
        return "<a href=\"" + frontendUrl +
                "?token=" + token.getToken() +
                "\">Подтвердить регистрацию<a/>";
    }
}