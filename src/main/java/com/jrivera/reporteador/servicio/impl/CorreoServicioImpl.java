package com.jrivera.reporteador.servicio.impl;

import com.jrivera.reporteador.servicio.CorreoServicio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

@Service
public class CorreoServicioImpl implements CorreoServicio {

    private final static Logger logger = LoggerFactory.getLogger(CorreoServicioImpl.class);
    @Value("${mail.host}")
    private String host;
    @Value("${mail.port}")
    private String port;
    @Value("${mail.username}")
    private String usuario;
    @Value("${mail.password}")
    private String password;
    @Value("${mail.enable}")
    private Boolean enable;

    private Properties properties;

    @PostConstruct
    public void init() {
        properties = new Properties();
        properties.put("mail.smtp.auth", true);
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);
    }

    @Override
    public void enviaCorreo(String asunto, String contenido, String destinatario) {
        if (!enable) {
            return;
        }
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(usuario, password);
            }
        });
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(usuario));
            message.setRecipients(
                    Message.RecipientType.TO, InternetAddress.parse(destinatario));
            message.setSubject("Reporteador (" + asunto + ")");

            String msg = "<h3>" + asunto + "</h3><div>" + contenido + "</div>";

            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setContent(msg, "text/html; charset=utf-8");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(mimeBodyPart);

            message.setContent(multipart);

            Transport.send(message);
            logger.info("Se envió un correo a " + destinatario + " con asunto " + asunto);
        } catch (AddressException addressException) {
            logger.error("Error -> " + addressException.getMessage());
        } catch (MessagingException messagingException) {
            logger.error("Error -> " + messagingException.getMessage());
        } catch (Exception e) {
            logger.error("Error -> " + e.getMessage());
        }

    }
}
