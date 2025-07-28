package com.smm.adapterservice.errorhandling;

import com.smm.adapterservice.errorhandling.exception.NotFoundException;
import com.smm.adapterservice.errorhandling.exception.RemoteServiceUnavailableException;
import com.smm.adapterservice.errorhandling.model.ErrorResponse;
import com.smm.adapterservice.errorhandling.model.Violation;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE;

@Log4j2
@RestControllerAdvice
public class DefaultExceptionHandler extends ResponseEntityExceptionHandler {

    public static final String INVALID_FIELDS_MESSAGE = "Some fields are not valid";

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(final ConstraintViolationException exception) {

        final List<Violation> violations = exception.getConstraintViolations()
                .stream()
                .map(constraint -> Violation.builder().field(constraint.getPropertyPath().toString()).message(constraint.getMessage()).build())
                .collect(toList());

        return new ResponseEntity<>(buildErrorResponse(INVALID_FIELDS_MESSAGE, violations), BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(final NotFoundException exception) {

        return new ResponseEntity<>(buildErrorResponse("Not Found"), NOT_FOUND);
    }

    @ExceptionHandler(RemoteServiceUnavailableException.class)
    public ResponseEntity<ErrorResponse> handleRemoteServiceUnavailableException(final RemoteServiceUnavailableException exception) {

        return new ResponseEntity<>(buildErrorResponse("Remote Service is currently unavailable"), SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(final Exception exception) {

        log.error("Unexpected Exception captured: {}", exception.getMessage(), exception);
        return new ResponseEntity<>(buildErrorResponse("An unexpected error occurred"), INTERNAL_SERVER_ERROR);
    }

    @Override
    protected ResponseEntity<Object> handleBindException(final BindException exception, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {

        return new ResponseEntity<>(buildErrorResponse(INVALID_FIELDS_MESSAGE, getViolations(exception)), BAD_REQUEST);
    }

    private ErrorResponse buildErrorResponse(final String message) {

        return buildErrorResponse(message, null);
    }

    private ErrorResponse buildErrorResponse(final String message, final List<Violation> violations) {

        return ErrorResponse.builder()
                .message(message)
                .violations(violations)
                .build();
    }

    private List<Violation> getViolations(final BindException exception) {

        return exception.getBindingResult().getFieldErrors().stream()
                .map(constraint -> Violation.builder().field(constraint.getField()).message(constraint.getDefaultMessage()).build())
                .collect(toList());
    }
}
