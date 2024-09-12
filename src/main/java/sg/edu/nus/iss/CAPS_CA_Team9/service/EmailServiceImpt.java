package sg.edu.nus.iss.CAPS_CA_Team9.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

@Component
public class EmailServiceImpt implements EmailService {

    @Autowired
    private JavaMailSenderImpl mailSender;

    @Override
    public void sendMail(String to, String from, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage(); 
        message.setFrom("noreply@gmail.com");
        message.setTo(to); 
        message.setSubject(subject); 
        message.setText(text);
        mailSender.send(message);
    }
    
}
