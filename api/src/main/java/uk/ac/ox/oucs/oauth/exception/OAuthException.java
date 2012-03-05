package uk.ac.ox.oucs.oauth.exception;

/**
 * @author Colin Hebert
 */
public class OAuthException extends RuntimeException {
    public OAuthException() {
    }

    public OAuthException(Throwable cause) {
        super(cause);
    }

    public OAuthException(String message) {
        super(message);
    }

    public OAuthException(String message, Throwable cause) {
        super(message, cause);
    }
}