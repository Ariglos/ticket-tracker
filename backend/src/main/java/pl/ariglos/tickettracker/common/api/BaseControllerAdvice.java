package pl.ariglos.tickettracker.common.api;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pl.ariglos.tickettracker.common.api.exceptions.TicketTrackerException;
import pl.ariglos.tickettracker.common.translations.LanguageController;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice()
public class BaseControllerAdvice {
    private final LanguageController languageController;

    public BaseControllerAdvice(LanguageController languageController) {
        this.languageController = languageController;
    }

    @ExceptionHandler()
    public ResponseEntity<String> handleException(TicketTrackerException ex) {
        String errorCode = ex.getErrorCode();
        HttpStatus status = resolveHttpStatus(errorCode);
        return ResponseEntity.status(status).body(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex) {
        String errorCode = "EXC_000";
        String errorMessage = languageController.get(errorCode);
        HttpStatus status = resolveHttpStatus(errorCode);
        return ResponseEntity.status(status).body(errorMessage);
    }

    private HttpStatus resolveHttpStatus(String errorCode) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        return status;
    }
}
