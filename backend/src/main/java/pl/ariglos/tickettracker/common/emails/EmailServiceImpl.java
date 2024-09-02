package pl.ariglos.tickettracker.common.emails;

import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import pl.ariglos.tickettracker.common.api.exceptions.TicketTrackerException;
import pl.ariglos.tickettracker.common.translations.LanguageController;

@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final LanguageController languageController;

    public EmailServiceImpl(JavaMailSender mailSender, LanguageController languageController) {
        this.mailSender = mailSender;
        this.languageController = languageController;
    }

    @Override
    public void sendEmail(String receiver, String subject, String body) throws TicketTrackerException {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(receiver);
        mailMessage.setSubject(subject);
        mailMessage.setText(body);

        try {
            mailSender.send(mailMessage);
        } catch (MailException e) {
            String errorCode = "EXC_019";
            String message = languageController.get(errorCode);
            throw new TicketTrackerException(errorCode, message);
        }
    }
}
