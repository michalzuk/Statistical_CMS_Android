package io.michalzuk.horton.models;

public class ApiCredentials {

    private String domain;
    private String username;
    private String apiKey;

    public ApiCredentials(String domain, String username, String apiKey) {
        this.domain = domain;
        this.username = username;
        this.apiKey = apiKey;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }
}
