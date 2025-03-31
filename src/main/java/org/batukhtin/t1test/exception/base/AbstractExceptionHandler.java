package org.batukhtin.t1test.exception.base;

import java.io.Serial;
import java.io.Serializable;

public class AbstractExceptionHandler extends RuntimeException implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    public AbstractExceptionHandler(String message) {
        super(String.format("%s", message));
    }

}