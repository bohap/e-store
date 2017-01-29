package com.finki.emt.bookstore.web.rest.errors;

import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.stream.Stream;

@RestController
public class ApiErrorController implements ErrorController {

    private static final String PATH = "/error";

    @Inject
    private ErrorAttributes errorAttributes;

    @RequestMapping(value = PATH)
    public ErrorResponse error(HttpServletRequest request, HttpServletResponse response) {
        HttpStatus status = getHttpStatus(response.getStatus());
        return new ErrorResponse(status.value(), status.getReasonPhrase(), getMessage(request));
    }

    private HttpStatus getHttpStatus(int code) {
        return Stream.of(HttpStatus.values())
                .filter(s -> s.value() == code)
                .findFirst()
                .orElse(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private String getMessage(HttpServletRequest request) {
        RequestAttributes requestAttributes = new ServletRequestAttributes(request);
        Map<String, Object> attributes = errorAttributes
                .getErrorAttributes(requestAttributes, false);
        return (String) attributes.get("message");
    }

    @Override
    public String getErrorPath() {
        return PATH;
    }
}
