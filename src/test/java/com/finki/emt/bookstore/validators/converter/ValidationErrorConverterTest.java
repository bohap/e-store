package com.finki.emt.bookstore.validators.converter;

import com.finki.emt.bookstore.web.rest.validators.converter.ValidationErrorConverter;
import com.finki.emt.bookstore.web.rest.validators.converter.ValidationErrorConverter.Error;
import org.junit.Test;
import org.springframework.validation.FieldError;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ValidationErrorConverterTest {

    private FieldError mockFieldError(int field, int code) {
        return new FieldError("test", "field-" + field, null, false,
                new String[]{"code-" + code}, null, "Message-" + field + "-" + code);
    }

    @Test
    public void testGroupErrors() {
        List<FieldError> errors = Arrays.asList(mockFieldError(1, 1),
                mockFieldError(1, 2), mockFieldError(1, 3), mockFieldError(2, 1),
                mockFieldError(3, 1), mockFieldError(3, 2));
        Map<String, List<Error>> result = ValidationErrorConverter.groupErrors(errors);
    }
}
