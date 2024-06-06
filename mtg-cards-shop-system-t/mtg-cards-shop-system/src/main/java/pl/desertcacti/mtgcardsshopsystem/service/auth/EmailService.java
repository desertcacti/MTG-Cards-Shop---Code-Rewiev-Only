package pl.desertcacti.mtgcardsshopsystem.service.auth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/** EmailService class provides service declaration of methods for sending specific emails
 such as account confirmation and order realization status messages. */
@Slf4j
@Service
public class EmailService {
    private final JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private String fromEmail;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    /** sendConfirmationEmail()
     /*  Sends a confirmation email with a provided confirmation link. */
    public void sendConfirmationEmail(String to, String confirmationLink) {
        String subject = "Registration confirmation: ";
        String text = "Click on the following link to confirm your account: " + confirmationLink;
        sendEmail(to, subject, text);
    }

    /** sendOrderConfirmation()
     /*  Sends an order confirmation email with the provided order ID. */
    public void sendOrderConfirmation(String to, Long orderId) {
        String subject = "Order confirmation: ";
        String text = "Order number: " + orderId + " has been accepted for realization process.";
        sendEmail(to, subject, text);
    }

    /** sendOrderStatusUpdate()
     /*  Sends an email with an update on the order status. */
    public void sendOrderStatusUpdate(String to, Long orderId, String status) {
        String subject = "Order status update: ";
        String text = "Status information, order number" + orderId + " has been changed to: " + status;
        sendEmail(to, subject, text);
    }

    /** sendEmail()
     /*  Sends an email with the specified recipient, subject, and text. */
    private void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = createMessage(to, subject, text);
        try {
            mailSender.send(message);
            log.info("Email sent successfully to: {}", to);
        } catch (MailException e) {
            log.error("Failed to send email to: {}", to, e);
            throw new EmailSendingException("Failed to send email to " + to, e);
        }
    }

    /** createMessage()
     /*  Creates a SimpleMailMessage object with the specified recipient, subject, and text. */
    private SimpleMailMessage createMessage(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        return message;
    }

    /** Custom exception for email sending failures. */
    public static class EmailSendingException extends RuntimeException {
        public EmailSendingException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
