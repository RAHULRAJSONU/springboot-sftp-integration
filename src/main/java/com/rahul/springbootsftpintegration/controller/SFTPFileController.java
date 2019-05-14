package com.rahul.springbootsftpintegration.controller;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import com.rahul.springbootsftpintegration.config.SFTPConfigDomain;
import com.rahul.springbootsftpintegration.service.SFTPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("file")
public class SFTPFileController {

    @Autowired
    private SFTPConfigDomain sftpConfigDomain;

    private SFTPService sftpService;

    @RequestMapping(value = "/get-config", method = RequestMethod.GET)
    public SFTPConfigDomain getSFTPConfig(){
        return sftpConfigDomain;
    }

    @Autowired
    public SFTPFileController(SFTPService sftpService) {
        this.sftpService = sftpService;
    }

    @PostMapping(value = "/upload")
    public void uploadFile(@RequestParam("file") MultipartFile file) throws JSchException, SftpException, IOException {
        sftpService.uploadFile(file);
    }

    @GetMapping(value = "/download")
    public void readFile() throws IOException {
        sftpService.download();
    }
}