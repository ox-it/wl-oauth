package uk.ac.ox.oucs.oauth.domain;

import java.util.Date;

/**
 * @author Colin Hebert
 */
public class Accessor {
    private String token;
    private Type type;
    private Status status;
    private String verifier;
    private String secret;
    private String consumerId;
    private Date creationDate;
    private Date expirationDate;
    private String callbackUrl;
    private String userId;

    public static enum Type {
        REQUEST,
        REQUEST_AUTHORISING,
        REQUEST_AUTHORISED,
        ACCESS
    }

    public static enum Status {
        VALID,
        REVOKED,
        EXPIRED
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getVerifier() {
        return verifier;
    }

    public void setVerifier(String verifier) {
        this.verifier = verifier;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getConsumerId() {
        return consumerId;
    }

    public void setConsumerId(String consumerId) {
        this.consumerId = consumerId;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
