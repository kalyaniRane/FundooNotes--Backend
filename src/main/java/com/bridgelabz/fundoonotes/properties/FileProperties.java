package com.bridgelabz.fundoonotes.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "application")
public class FileProperties {
    private String jwtSecret;

    private int verificationMs;
    private int jwtExpirationMs;

    public int getJwtExpirationMs() {
        return jwtExpirationMs;
    }

    public void setJwtExpirationMs(int jwtExpirationMs) {
        this.jwtExpirationMs = jwtExpirationMs;
    }

    public String getJwtSecret() {
        return jwtSecret;
    }

    public void setJwtSecret(String jwtSecret) {
        this.jwtSecret = jwtSecret;
    }

    public int getVerificationMs() {
        return verificationMs;
    }

    public void setVerificationMs(int verificationMs) {
        this.verificationMs = verificationMs;
    }

}
