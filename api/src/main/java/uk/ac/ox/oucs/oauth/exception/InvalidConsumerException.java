package uk.ac.ox.oucs.oauth.exception;

/**
 * @author Colin Hebert
 */
public class InvalidConsumerException extends OAuthException{
    public InvalidConsumerException() {
    }

    public InvalidConsumerException(Throwable cause) {
        super(cause);
    }

    public InvalidConsumerException(String message) {
        super(message);
    }

    public InvalidConsumerException(String message, Throwable cause) {
        super(message, cause);
    }
}
