package uk.ac.ox.oucs.oauth.exception;

/**
 * @author Colin Hebert
 */
public class InvalidVerifierException extends OAuthException {
    public InvalidVerifierException() {
    }

    public InvalidVerifierException(Throwable cause) {
        super(cause);
    }

    public InvalidVerifierException(String message) {
        super(message);
    }

    public InvalidVerifierException(String message, Throwable cause) {
        super(message, cause);
    }
}
