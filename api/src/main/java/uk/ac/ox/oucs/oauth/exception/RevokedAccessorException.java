package uk.ac.ox.oucs.oauth.exception;

/**
 * Thrown when an accessor is considered as revoked
 *
 * @author Colin Hebert
 */
public class RevokedAccessorException extends InvalidAccessorException {
    public RevokedAccessorException() {
        super();
    }

    public RevokedAccessorException(Throwable cause) {
        super(cause);
    }

    public RevokedAccessorException(String message) {
        super(message);
    }

    public RevokedAccessorException(String message, Throwable cause) {
        super(message, cause);
    }
}
