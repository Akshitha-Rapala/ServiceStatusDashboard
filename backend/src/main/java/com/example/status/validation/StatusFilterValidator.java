package com.example.status.validation;

import com.example.status.error.InvalidStatusException;
import com.example.status.model.ServiceStatus;
import java.util.Locale;
import org.springframework.stereotype.Component;

@Component
public class StatusFilterValidator {
    public ServiceStatus validate(String status) {
        if (status == null || status.isBlank()) return null;
        try {
            return ServiceStatus.valueOf(status.strip().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException exception) {
            throw new InvalidStatusException();
        }
    }
}
