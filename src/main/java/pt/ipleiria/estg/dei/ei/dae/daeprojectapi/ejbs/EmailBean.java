package pt.ipleiria.estg.dei.ei.dae.daeprojectapi.ejbs;

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

import static java.util.logging.Level.SEVERE;

@Stateless
public class EmailBean {
    @Resource(name = "java:/jboss/mail/fakeSMTP")
    private Session mailSession;

    private static final Logger LOGGER = Logger.getLogger("EmailBean.logger");

    public void send(String to, String subject, String body) throws MessagingException {
        var emailJob = new Thread(() -> {

            var msg = new MimeMessage(mailSession);
            var timestamp = new Date();

            try {
                msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to, false));
                msg.setSubject(subject);
                msg.setText(body);
                msg.setSentDate(timestamp);
                Transport.send(msg);
            } catch (MessagingException e) {
                LOGGER.log(SEVERE, e.getMessage());
            }
        });

        emailJob.start();
    }
}
