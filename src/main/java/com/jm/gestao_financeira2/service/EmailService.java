package com.jm.gestao_financeira2.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    // Remetente configurado no application.properties
    @Value("${spring.mail.username:}")
    private String fromEmail;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    /**
     * Envia código de redefinição de senha por e-mail.
     * O código enviado é sempre o valor em texto puro.
     */
    public void enviarCodigoReset(String toEmail, String code) {
        try {
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setTo(toEmail);

            // Define remetente quando disponível
            if (fromEmail != null && !fromEmail.isBlank()) {
                msg.setFrom(fromEmail);
            }

            msg.setSubject("Gestão Financeira - Código para redefinir senha");
            msg.setText(
                    "Seu código para redefinir a senha é: " + code + "\n\n" +
                    "⚠️ Este código expira em 60 segundos.\n" +
                    "Se você não solicitou esta ação, ignore este e-mail."
            );

            mailSender.send(msg);

        } catch (MailException e) {
            // Falha no envio SMTP
            System.err.println("[EmailService] Falha ao enviar e-mail de redefinição");
            System.err.println("[EmailService] Destinatário: " + toEmail);
            System.err.println("[EmailService] Erro: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Alias para compatibilidade com código em inglês.
     */
    public void sendResetCode(String toEmail, String code) {
        enviarCodigoReset(toEmail, code);
    }
}
