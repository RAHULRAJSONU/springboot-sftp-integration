package com.rahul.springbootsftpintegration.config;

public class SFTPConfigDomain {
    private String sftpHost;
    private String sftpPort;
    private String user;
    private String password;

    public SFTPConfigDomain(){}

    public SFTPConfigDomain(String sftpAddress, String sftpPort, String user, String password) {
        this.sftpHost = sftpAddress;
        this.sftpPort = sftpPort;
        this.user = user;
        this.password = password;
    }

    public String getSftpHost() {
        return sftpHost;
    }

    public void setSftpHost(String sftpHost) {
        this.sftpHost = sftpHost;
    }

    public String getSftpPort() {
        return sftpPort;
    }

    public void setSftpPort(String sftpPort) {
        this.sftpPort = sftpPort;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
