package ru.pishemzapuskayem.backendbookservice.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import ru.pishemzapuskayem.backendbookservice.model.entity.AccountAddress;
import ru.pishemzapuskayem.backendbookservice.model.entity.ConfirmToken;

@Service
@Slf4j
@RequiredArgsConstructor
public class MailService {
    private static final String CONFIRM_TOKEN_SUBJECT = "Активируйте аккаунт";
    private static final String SEND_ACCOUNT_ADDRESS = "Куда отправлять";

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
    public void trySendToken(String email, ConfirmToken token) {
        try {
            sendMessage(
                email,
                CONFIRM_TOKEN_SUBJECT,
                buildConfirmTokenMsg(token)
            );
        } catch (Exception e){
            log.info("Cant send token. Token is "+ email + " : "+ token.getToken());
        }
    }

    private String buildConfirmTokenMsg(ConfirmToken token) {
        return "<a href=\"" + frontendUrl +
                "?token=" + token.getToken() +
                "\">Подтвердить регистрацию<a/>";
    }

    @Async
    public void sendAddressWish(AccountAddress address, String email) {
        try {
            sendMessage(
                    email,
                    SEND_ACCOUNT_ADDRESS,
                    buildAddressMsg(address)
            );
        } catch (Exception e){
            log.info("Cant send token. Address is "+ email);
        }
    }

    private String buildAddressMsg(AccountAddress address) {
        return "Адрес для отправки книги: " + "</br>"
                + "Индекс : " + address.getAddrIndex() + "</br>"
                + "Город : " + address.getAddrCity() + "</br>"
                + "Улица : " + address.getAddrStreet() + "</br>"
                + "Дом : " + address.getAddrHouse() + "</br>"
                + "Структура : " + address.getAddrStructure() + "</br>"
                + "Квартира : " + address.getAddrApart();
    }
}