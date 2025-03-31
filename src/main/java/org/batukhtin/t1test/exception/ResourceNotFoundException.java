package org.batukhtin.t1test.exception;

import org.batukhtin.t1test.exception.base.AbstractExceptionHandler;

public class ResourceNotFoundException extends AbstractExceptionHandler {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
