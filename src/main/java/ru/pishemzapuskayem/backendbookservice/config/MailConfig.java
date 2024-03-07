package ru.pishemzapuskayem.backendbookservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfig {
    @Value("${mail.username}")
    private String username;
    @Value("${mail.password}")
    private String password;
    @Value("${mail.host}")
    private String host;
    @Value("${mail.port}")
    private String port;
    @Value("${mail.protocol}")
    private String protocol;
    @Value("${mail.smtps.auth}")
    private String smtpsAuth;

    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = configureMailSender();
        mailSender.setJavaMailProperties(properties(mailSender));
        return mailSender;
    }

    private JavaMailSenderImpl configureMailSender(){
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);
        mailSender.setPort(Integer.parseInt(port));
        mailSender.setProtocol(protocol);
        mailSender.setUsername(username);
        mailSender.setPassword(password);
        return mailSender;
    }

    private Properties properties(JavaMailSenderImpl mailSender){
        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", protocol);
        props.put("mail.smtp.auth", smtpsAuth);
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");
        return props;
    }
}
