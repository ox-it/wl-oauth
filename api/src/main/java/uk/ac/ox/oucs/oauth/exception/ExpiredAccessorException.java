package uk.ac.ox.oucs.oauth.exception;

/**
 * @author Colin Hebert
 */
public class ExpiredAccessorException extends InvalidAccessorException {
    public ExpiredAccessorException() {
        super();
    }

    public ExpiredAccessorException(Throwable cause) {
        super(cause);
    }

    public ExpiredAccessorException(String message) {
        super(message);
    }

    public ExpiredAccessorException(String message, Throwable cause) {
        super(message, cause);
    }
}
