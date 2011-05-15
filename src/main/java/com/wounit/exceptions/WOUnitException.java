package com.wounit.exceptions;

/**
 * Basic exception thrown if a problem occur in the internal logic of WOUnit.
 * 
 * @author <a href="mailto:hprange@gmail.com">Henrique Prange</a>
 * @since 1.1
 */
public class WOUnitException extends RuntimeException {
    public WOUnitException(String message) {
	super(message);
    }

    public WOUnitException(String message, Throwable cause) {
	super(message, cause);
    }
}
