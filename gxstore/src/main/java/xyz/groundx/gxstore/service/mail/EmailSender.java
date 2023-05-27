package xyz.groundx.gxstore.service.mail;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class EmailSender {
    public void sendEmail(SimpleMailMessage message) {
        System.out.printf("""
                        from: %s
                        to: %s
                        title: %s
                                
                        %s%n
                        """,
                message.getFrom(), Arrays.toString(message.getTo()), message.getSubject(), message.getText());
    }
}
