package pl.ariglos.tickettracker.tickets.services.implementations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.text.StringSubstitutor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.ariglos.tickettracker.common.api.exceptions.TicketTrackerException;
import pl.ariglos.tickettracker.common.emails.EmailService;
import pl.ariglos.tickettracker.tickets.enumerations.Currency;
import pl.ariglos.tickettracker.tickets.services.TicketMailService;

@Service
public class TicketMailServiceImpl implements TicketMailService {

  public static final String TEMPLATE_VAR_PREFIX = "{";
  public static final String TEMPLATE_VAR_SUFFIX = "}";

  private final EmailService emailService;

  @Value("${email.receiver}")
  private String emailReceiver;

  @Value("${email.template.subject}")
  private String emailSubject;

  @Value("${email.template.content}")
  private String emailTemplate;

  @Value("${application.frontend.url.tickets}")
  private String ticketsUrl;

  public TicketMailServiceImpl(EmailService emailService) {
    this.emailService = emailService;
  }

  @Override
  public void notifyEmployeeAboutTicket(
      Long ticketId,
      String ticketSignature,
      LocalDate dueDate,
      BigDecimal fineAmount,
      Currency fineCurrency)
      throws TicketTrackerException {

    String specificTicketUrl = ticketsUrl + "/" + ticketId;

    Map<String, String> emailTemplateVariables = new HashMap<>() {};
    emailTemplateVariables.put("ticketSignature", ticketSignature);
    emailTemplateVariables.put("ticketDueDate", dueDate.toString());
    emailTemplateVariables.put("ticketUrl", specificTicketUrl);
    emailTemplateVariables.put("fineAmount", fineAmount.toString());
    emailTemplateVariables.put("currency", fineCurrency.toString());

    StringSubstitutor sub = new StringSubstitutor(emailTemplateVariables, TEMPLATE_VAR_PREFIX, TEMPLATE_VAR_SUFFIX);

    String formattedEmailSubject = sub.replace(emailSubject);
    String formattedEmailTemplate = sub.replace(emailTemplate);

    emailService.sendEmail(emailReceiver, formattedEmailSubject, formattedEmailTemplate);
  }
}
