package uk.ac.ox.oucs.oauth.tool;

import java.util.Date;

/**
 * @author Colin Hebert
 */
public class SimpleAccessor implements Comparable<SimpleAccessor> {
    private String token;
    private Date creationDate;
    private Date expirationDate;
    private String consumerName;
    private String consumerDescription;
    private String consumerUrl;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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

    public String getConsumerName() {
        return consumerName;
    }

    public void setConsumerName(String consumerName) {
        this.consumerName = consumerName;
    }

    public String getConsumerDescription() {
        return consumerDescription;
    }

    public void setConsumerDescription(String consumerDescription) {
        this.consumerDescription = consumerDescription;
    }

    public String getConsumerUrl() {
        return consumerUrl;
    }

    public void setConsumerUrl(String consumerUrl) {
        this.consumerUrl = consumerUrl;
    }

    @Override
    public int compareTo(SimpleAccessor o) {
        if (expirationDate == null)
            return (o.expirationDate == null) ? 0 : 1;
        if (o.expirationDate == null) return -1;
        return expirationDate.compareTo(o.expirationDate);
    }
}
