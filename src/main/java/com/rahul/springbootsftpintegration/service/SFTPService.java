package com.rahul.springbootsftpintegration.service;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import com.rahul.springbootsftpintegration.config.SFTPCredentialsConfig;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

@Service
public class SFTPService {

    @Autowired()
    private SFTPCredentialsConfig sftpCredentialsConfig;

    public void uploadFile(String remotePath, MultipartFile file) throws JSchException, SftpException, IOException {
        Session session = null;
        ChannelSftp sftpChannel = null;
        try {
            session = sftpCredentialsConfig.createSFTPSession();
            session.connect();
            sftpChannel = (ChannelSftp) session.openChannel("sftp");
            sftpChannel.connect();
            sftpChannel.cd(remotePath);
            sftpChannel.put(file.getInputStream(), file.getOriginalFilename(), ChannelSftp.OVERWRITE);

            sftpChannel.disconnect();
            session.disconnect();
        } finally {
            if (sftpChannel != null && sftpChannel.isConnected()) {
                sftpChannel.disconnect();
            }

            if (session != null && session.isConnected()) {
                session.disconnect();
            }
        }
    }

    public void readFile(String filePath, String fileName) throws IOException {
        Session session = null;
        ChannelSftp sftpChannel = null;
        try {
            session = sftpCredentialsConfig.createSFTPSession();
            session.connect();
            sftpChannel = (ChannelSftp) session.openChannel("sftp");
            sftpChannel.connect();

            InputStream stream = sftpChannel.get(filePath+fileName);
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(stream));
                String line;
                while ((line = br.readLine()) != null) {
                    System.out.println(line);
                }

            } catch (IOException io) {
                System.out.println("Exception occurred during reading file from SFTP server due to " + io.getMessage());
                io.getMessage();

            } catch (Exception e) {
                System.out.println("Exception occurred during reading file from SFTP server due to " + e.getMessage());
                e.getMessage();

            }
            sftpChannel.exit();
            session.disconnect();
        } catch (JSchException e) {
            e.printStackTrace();
        } catch (SftpException e) {
            e.printStackTrace();
        } finally {
            if (sftpChannel != null && sftpChannel.isConnected()) {
                sftpChannel.disconnect();
            }

            if (session != null && session.isConnected()) {
                session.disconnect();
            }
        }
    }

    public byte[] getFileByteArray(String sourcePath, String sourceFileName)
            throws JSchException, SftpException, IOException {
        Session session = null;
        ChannelSftp sftpChannel = null;
        InputStream inputStream = null;
        byte[] byteArray = null;
        try {
            session = sftpCredentialsConfig.createSFTPSession();
            session.connect();
            sftpChannel = (ChannelSftp) session.openChannel("sftp");
            sftpChannel.connect();
            inputStream = sftpChannel.get(sourcePath+sourceFileName);
            byteArray = IOUtils.toByteArray(inputStream);
            sftpChannel.exit();
            sftpChannel.disconnect();
            session.disconnect();
        } finally {
            if (sftpChannel != null && sftpChannel.isConnected()) {
                sftpChannel.disconnect();
            }

            if (session != null && session.isConnected()) {
                session.disconnect();
            }
        }
        return byteArray;
    }

    public ArrayList<ChannelSftp.LsEntry> getFileList(String remotePath) throws JSchException, SftpException {
        Session session = null;
        ChannelSftp sftpChannel = null;
        ArrayList<ChannelSftp.LsEntry> lsEntries= null;
        try {
            session = sftpCredentialsConfig.createSFTPSession();
            session.connect();
            sftpChannel = (ChannelSftp) session.openChannel("sftp");
            sftpChannel.connect();
            sftpChannel.cd(remotePath);
            Vector filelist = sftpChannel.ls(remotePath);
            lsEntries=new ArrayList<>(filelist);
            sftpChannel.exit();
            sftpChannel.disconnect();
            session.disconnect();
        } finally {
            if (sftpChannel != null && sftpChannel.isConnected()) {
                sftpChannel.disconnect();
            }

            if (session != null && session.isConnected()) {
                session.disconnect();
            }
        }
        return lsEntries;
    }
}