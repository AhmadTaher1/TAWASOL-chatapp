package shared.utils;

import java.util.Properties;


import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

public class JakartaMail {

    public static void mailService(String phone, String email, String name) {

        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.ssl.trust", "smtp.gmail.com");


        Session mailSession = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("poshspareparts@gmail.com", "aykcdlzamsatwcab");
            }
        });

        String content = name.concat(" has sent a friend request to you with phone number: ").concat(phone);

        try {
            MimeMessage message = new MimeMessage(mailSession);
            message.setFrom(new InternetAddress("poshspareparts@gmail.com"));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress("ahmad.t.yassen@gmail.com"));
            message.setSubject("New Friend Request Sent to you");
            message.setText(content);

            Transport.send(message);
            System.out.println("Message Send Successfully");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

    }
}
