package com.smm.adapterservice.errorhandling;

import com.smm.adapterservice.errorhandling.exception.NotFoundException;
import com.smm.adapterservice.errorhandling.exception.RemoteServiceUnavailableException;
import com.smm.adapterservice.errorhandling.model.ErrorResponse;
import com.smm.adapterservice.errorhandling.model.Violation;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import javax.validation.metadata.ConstraintDescriptor;
import java.util.List;
import java.util.Set;

import static com.smm.adapterservice.errorhandling.DefaultExceptionHandler.INVALID_FIELDS_MESSAGE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.MockitoAnnotations.openMocks;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE;

class DefaultExceptionHandlerTest {

    @InjectMocks
    private DefaultExceptionHandler defaultExceptionHandler;

    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    @Test
    void should_ReturnNotFoundAndErrorResponse_When_HandleNotFoundException() {

        // Given
        final ErrorResponse errorResponse = ErrorResponse.builder()
                .message("Not Found")
                .build();

        final ResponseEntity<ErrorResponse> expected = ResponseEntity
                .status(NOT_FOUND)
                .body(errorResponse);

        // When
        final ResponseEntity<ErrorResponse> response = defaultExceptionHandler.handleNotFoundException(new NotFoundException());

        // Then
        assertThat(response).isNotNull().usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void should_ReturnServiceUnavailableAndErrorResponse_When_HandleRemoteServiceUnavailableException() {

        // Given
        final ErrorResponse errorResponse = ErrorResponse.builder()
                .message("Remote Service is currently unavailable")
                .build();

        final ResponseEntity<ErrorResponse> expected = ResponseEntity
                .status(SERVICE_UNAVAILABLE)
                .body(errorResponse);

        // When
        final ResponseEntity<ErrorResponse> response = defaultExceptionHandler.handleRemoteServiceUnavailableException(new RemoteServiceUnavailableException());

        // Then
        assertThat(response).isNotNull().usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void should_ReturnInternalServerErrorAndErrorResponse_When_HandleException() {

        // Given
        final ErrorResponse errorResponse = ErrorResponse.builder()
                .message("An unexpected error occurred")
                .build();

        final ResponseEntity<ErrorResponse> expected = ResponseEntity
                .internalServerError()
                .body(errorResponse);

        // When
        final ResponseEntity<ErrorResponse> response = defaultExceptionHandler.handleException(new RuntimeException());

        // Then
        assertThat(response).isNotNull().usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void should_ReturnBadRequestAndErrorResponse_When_HandleConstraintViolationException() {

        // Given
        final Violation violation = Violation.builder()
                .field("field")
                .message("message")
                .build();
        final ConstraintViolationException constraintViolation = new ConstraintViolationException(Set.of(
                new ConstraintViolation<Object>() {
                    @Override
                    public String getMessage() {
                        return violation.getMessage();
                    }

                    @Override
                    public String getMessageTemplate() {
                        return null;
                    }

                    @Override
                    public Object getRootBean() {
                        return null;
                    }

                    @Override
                    public Class<Object> getRootBeanClass() {
                        return null;
                    }

                    @Override
                    public Object getLeafBean() {
                        return null;
                    }

                    @Override
                    public Object[] getExecutableParameters() {
                        return new Object[0];
                    }

                    @Override
                    public Object getExecutableReturnValue() {
                        return null;
                    }

                    @Override
                    public Path getPropertyPath() {
                        return PathImpl.createPathFromString(violation.getField());
                    }

                    @Override
                    public Object getInvalidValue() {
                        return null;
                    }

                    @Override
                    public ConstraintDescriptor<?> getConstraintDescriptor() {
                        return null;
                    }

                    @Override
                    public <U> U unwrap(final Class<U> type) {
                        return null;
                    }
                }
        ));

        final ErrorResponse errorResponse = ErrorResponse.builder()
                .message(INVALID_FIELDS_MESSAGE)
                .violations(List.of(violation))
                .build();
        final ResponseEntity<ErrorResponse> expected = ResponseEntity
                .badRequest()
                .body(errorResponse);

        // When
        final ResponseEntity<ErrorResponse> response = defaultExceptionHandler.handleConstraintViolationException(constraintViolation);

        // Then
        assertThat(response).isNotNull().usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void should_ReturnBadRequestAndErrorResponse_When_HandleBindException() {

        // Given
        final FieldError error = new FieldError("objectName", "field", "defaultMessage");
        final BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "object");
        final BindException bindException = new BindException(bindingResult, "bindingResult");
        bindException.getBindingResult().addError(error);

        final ErrorResponse expectedErrorResponse = ErrorResponse.builder()
                .message(INVALID_FIELDS_MESSAGE)
                .violations(List.of(Violation.builder().field(error.getField()).message(error.getDefaultMessage()).build()))
                .build();
        final ResponseEntity<ErrorResponse> expected = ResponseEntity
                .badRequest()
                .body(expectedErrorResponse);

        // When
        final ResponseEntity<Object> response = defaultExceptionHandler.handleBindException(bindException, null, null, null);

        // Then
        assertThat(response).isNotNull().usingRecursiveComparison().isEqualTo(expected);
    }
}