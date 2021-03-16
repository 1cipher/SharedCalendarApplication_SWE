package model;

import utils.Private;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class Mailer extends Thread{

    private String mailText;

    @Override
    public void run() {
        sendMail();
    }

    public void setMailText(String text){
        mailText = text;
    }

    private void sendMail() {
        String host = "smtp.gmail.com";
        String from = "sweprogettodemo@gmail.com";
        String password = Private.mailPassword;

        Properties props = System.getProperties();

        props.put("mail.smtp.starttls.enable", "true"); // added this line
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.user", from);
        props.put("mail.smtp.password", password);
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");

        String to = "sweprogettodemo@gmail.com"; // added this line
        try {
            Session session = Session.getDefaultInstance(props, null);
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));

            InternetAddress toAddress = new InternetAddress(to);

            message.addRecipient(Message.RecipientType.TO, toAddress);

            message.setSubject("reminder");
            message.setText(mailText);
            Transport transport = session.getTransport("smtp");
            transport.connect(host, from, password);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();

        } catch (MessagingException mx) {
            mx.printStackTrace();
        }

    }
}