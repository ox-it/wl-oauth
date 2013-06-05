package org.sakaiproject.oauth.exception;

/**
 * Exception thrown when the given verifier doesn't match the expected verifier.
 *
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
