package sg.edu.nus.iss.CAPS_CA_Team9.service;

public interface EmailService {

    public void sendMail(String to, String from, String title, String content);
    
}
