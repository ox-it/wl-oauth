package uk.ac.ox.oucs.oauth.domain;

import java.util.Set;

/**
 * @author Colin Hebert
 */
public class Consumer {
    private String id;
    private String name;
    private String description;
    private String URL;
    private String callbackURL;
    private String secret;
    private String accessorSecret;
    private Set<String> rights;
    /**
     * Default access token validity in minutes
     */
    private int defaultValidity;

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

    @Override
    public String toString() {
        return "Consumer{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", URL='" + URL + '\'' +
                '}';
    }
}
