package com.rahul.springbootsftpintegration.config;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SFTPCredentialsConfig {

    @Value("${sftp.host}")
    private String sftpHost;
    @Value("${sftp.port}")
    private String sftpPort;
    @Value("${sftp.user}")
    private String sftpUser;
    @Value("${sftp.password}")
    private String sftpPasword;

    public Session createSFTPSession(){
        JSch jsch = new JSch();
        Session session = null;
        try {
            session = jsch.getSession(sftpUser, sftpHost, Integer.parseInt(sftpPort));
            session.setPassword(sftpPasword);
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig("PreferredAuthentications",
                    "publickey,keyboard-interactive,password");
            session.setConfig(config);
        } catch (JSchException e) {
            e.printStackTrace();
        }
        return session;
    }
}

