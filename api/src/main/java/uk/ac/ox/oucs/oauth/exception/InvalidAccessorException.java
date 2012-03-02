package uk.ac.ox.oucs.oauth.exception;

/**
 * @author Colin Hebert
 */
public class InvalidAccessorException extends OAuthException {
    public InvalidAccessorException() {
        super();
    }

    public InvalidAccessorException(Throwable cause) {
        super(cause);
    }

    public InvalidAccessorException(String message) {
        super(message);
    }

    public InvalidAccessorException(String message, Throwable cause) {
        super(message, cause);
    }
}
