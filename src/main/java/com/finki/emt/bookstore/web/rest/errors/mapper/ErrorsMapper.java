package com.finki.emt.bookstore.web.rest.errors.mapper;

import com.finki.emt.bookstore.web.rest.errors.ModelNotFoundException;
import com.finki.emt.bookstore.web.rest.response.ErrorResponse;
import com.finki.emt.bookstore.web.rest.validators.converter.ValidationErrorConverter;
import com.finki.emt.bookstore.web.rest.validators.converter.ValidationErrorConverter.Error;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
public class ErrorsMapper {

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
    public String test(ModelNotFoundException exp) {
        return exp.getMessage();
    }

    @ExceptionHandler(HttpServerErrorException.class)
    public ResponseEntity<ErrorResponse> handleHttpServerError(HttpServerErrorException exp) {
        HttpStatus status = exp.getStatusCode();
        ErrorResponse response = new ErrorResponse(status.value(),
                status.getReasonPhrase(), exp.getMessage());
        return new ResponseEntity<>(response, status);
    }
}
