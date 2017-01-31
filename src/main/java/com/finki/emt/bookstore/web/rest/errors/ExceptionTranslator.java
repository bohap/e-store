package com.finki.emt.bookstore.web.rest.errors;

import com.finki.emt.bookstore.util.exceptions.EmptyBasketException;
import com.finki.emt.bookstore.util.exceptions.ModelNotFoundException;
import com.finki.emt.bookstore.web.rest.validators.converter.ValidationErrorConverter;
import com.finki.emt.bookstore.web.rest.validators.converter.ValidationErrorConverter.Error;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpServerErrorException;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@ControllerAdvice
@RestController
public class ExceptionTranslator {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Map<String, List<Error>>> handleMethodNotValid(MethodArgumentNotValidException exp) {
        List<FieldError> errors = exp.getBindingResult().getFieldErrors();
        Map<String, List<Error>> grouped = ValidationErrorConverter.groupErrors(errors);
        return Collections.singletonMap("errors", grouped);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public Map<String, String> handleTokenExpired() {
        return Collections.singletonMap("jwt_error", "Token expired");
    }

    @ExceptionHandler(ModelNotFoundException.class)
    @ResponseStatus( HttpStatus.NOT_FOUND)
    public ErrorResponse handleModelNotFound(ModelNotFoundException exp) {
        return new ErrorResponse(404, "Not Found", exp.getMessage());
    }

    @ExceptionHandler(EmptyBasketException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public ErrorResponse handleEmptyBasket(EmptyBasketException exp) {
        HttpStatus status = HttpStatus.NOT_ACCEPTABLE;
        return new ErrorResponse(status.value(), status.getReasonPhrase(), exp.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleAccessDenied() {
        HttpStatus status = HttpStatus.FORBIDDEN;
        return new ErrorResponse(status.value(), status.getReasonPhrase(),
                "You don't have permissions to access to this resource");
    }

    @ExceptionHandler(HttpServerErrorException.class)
    public ResponseEntity<ErrorResponse> handleHttpServerError(HttpServerErrorException exp) {
        HttpStatus status = exp.getStatusCode();
        ErrorResponse response = new ErrorResponse(status.value(),
                status.getReasonPhrase(), exp.getMessage());
        return new ResponseEntity<>(response, status);
    }

    @ExceptionHandler(Exception.class)
    public ErrorResponse handleRuntimeException(Exception exp) {
        // TODO Returan a response entity so that the rewsponse status can tbe set
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        return new ErrorResponse(status.value(), status.getReasonPhrase(), exp.getMessage());
    }
}
