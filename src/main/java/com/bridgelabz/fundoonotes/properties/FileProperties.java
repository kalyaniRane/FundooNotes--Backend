package com.bridgelabz.fundoonotes.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "application")
public class FileProperties {
    private String jwtSecret;
    private int verificationMs;
    private int jwtExpirationMs;
    private String end_point_url;
    private String access_key;
    private String secret_key;
    private String bucket_name;

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

    public String getEnd_point_url() {
        return end_point_url;
    }

    public void setEnd_point_url(String end_point_url) {
        this.end_point_url = end_point_url;
    }

    public String getAccess_key() {
        return access_key;
    }

    public void setAccess_key(String access_key) {
        this.access_key = access_key;
    }

    public String getSecret_key() {
        return secret_key;
    }

    public void setSecret_key(String secret_key) {
        this.secret_key = secret_key;
    }

    public String getBucket_name() {
        return bucket_name;
    }

    public void setBucket_name(String bucket_name) {
        this.bucket_name = bucket_name;
    }
}
