package uk.ac.ox.oucs.oauth.domain;

import java.io.Serializable;
import java.util.Set;

/**
 * Consumer (client) allowed to connect through an OAuth authentication.
 *
 * @author Colin Hebert
 */
public class Consumer implements Serializable {
    /**
     * Unique identifier of the consumer, also used as consumer's key.
     */
    private String id;
    /**
     * Name of the consumer, used for display.
     */
    private String name;
    /**
     * Consumer's description, contains messages addressed to the user during the authorisation phase.
     */
    private String description;
    /**
     * Consumer's URL, not the callback URL, a simple URL to allow the user to access the consumer's web site.
     */
    private String URL;
    /**
     * Callback URL, used during the authorisation phase.
     */
    private String callbackURL;
    /**
     * Consumer's secret (password), used to sign oauth messages.
     */
    private String secret;
    /**
     * Consumer's accessor secret, as defined in http://wiki.oauth.net/w/page/12238502/AccessorSecret.
     */
    private String accessorSecret;
    /**
     * Set of rights available for this specific consumer.
     */
    private Set<String> rights;
    /**
     * Default access token validity in minutes. The token won't expire if the value is 0.
     */
    private int defaultValidity;
    /**
     * Enable or disable the record mode. The default value should always be false for security reasons.
     */
    private boolean recordModeEnabled;

    public String getCallbackURL() {
        return callbackURL;
    }

    public void setCallbackURL(String callbackURL) {
        this.callbackURL = callbackURL;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getAccessorSecret() {
        return accessorSecret;
    }

    public void setAccessorSecret(String accessorSecret) {
        this.accessorSecret = accessorSecret;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public Set<String> getRights() {
        return rights;
    }

    public void setRights(Set<String> rights) {
        this.rights = rights;
    }

    public int getDefaultValidity() {
        return defaultValidity;
    }

    public void setDefaultValidity(int defaultValidity) {
        this.defaultValidity = defaultValidity;
    }

    public boolean isRecordModeEnabled() {
        return recordModeEnabled;
    }

    public void setRecordModeEnabled(boolean recordModeEnabled) {
        this.recordModeEnabled = recordModeEnabled;
    }

    @Override
    public String toString() {
        return "Consumer{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", URL='" + URL + '\'' +
                '}';
    }
}
