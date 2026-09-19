package com.example.status.validation;

import com.example.status.error.InvalidStatusException;
import com.example.status.model.ServiceStatus;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class StatusFilterValidatorTest {
    private final StatusFilterValidator validator = new StatusFilterValidator();
    @Test void absentAndBlankMeanNoFilter() {
        assertNull(validator.validate(null));
        assertNull(validator.validate(" \t "));
    }
    @Test void acceptsEveryStatusIgnoringCaseAndWhitespace() {
        assertEquals(ServiceStatus.OPERATIONAL, validator.validate(" operational "));
        assertEquals(ServiceStatus.DEGRADED, validator.validate("DeGrAdEd"));
        assertEquals(ServiceStatus.DOWN, validator.validate("DOWN"));
    }
    @Test void rejectsUnknownValues() {
        assertThrows(InvalidStatusException.class, () -> validator.validate("broken"));
    }
}
