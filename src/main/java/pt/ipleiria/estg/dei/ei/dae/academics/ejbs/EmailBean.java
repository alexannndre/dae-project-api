package pt.ipleiria.estg.dei.ei.dae.academics.ejbs;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

@Stateless(name = "EmailEJB")
public class EmailBean {
    @Resource(name = "java:/jboss/mail/fakeSMTP")
    private Session mailSession;
    private static final Logger LOGGER = Logger.getLogger("EmailBean.LOGGER");

    public void send(String to, String subject, String text) throws MessagingException {
        Thread emailJob = new Thread(() -> {
            Message message = new MimeMessage(mailSession);
            Date timestamp = new Date();
            try {
                // Adjust the recipients. Here we have only one recipient.
                // The recipient's address must be an object of the InternetAddress class.
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to, false));
                // Set the message's subject
                message.setSubject(subject);
                // Insert the message's body
                message.setText(text);
                // Adjust the date of sending the message
                message.setSentDate(timestamp);
                // Use the 'send' static method of the Transport class to send the message
                Transport.send(message);
            } catch (MessagingException e) {
                LOGGER.log(Level.SEVERE, e.getMessage());
            }
        });
        emailJob.start();
    }
}
