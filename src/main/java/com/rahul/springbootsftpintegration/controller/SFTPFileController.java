package com.rahul.springbootsftpintegration.controller;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import com.rahul.springbootsftpintegration.service.SFTPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("file")
public class SFTPFileController {

    private SFTPService sftpService;

    @Autowired
    public SFTPFileController(SFTPService sftpService) {
        this.sftpService = sftpService;
    }

    @PostMapping(value = "/upload")
    public void uploadFile(@RequestParam("remotePath") String remotePath,
                           @RequestParam("file") MultipartFile file) throws JSchException, SftpException, IOException {
        sftpService.uploadFile(remotePath,file);
    }

    @RequestMapping(path = "/download", method = RequestMethod.GET)
    public ResponseEntity<Resource> download(@RequestParam("sourcePath") String sourcePath,
                                             @RequestParam("sourceFileName") String sourceFileName)
            throws IOException, SftpException, JSchException {
        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+sourceFileName);
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");
        ByteArrayResource resource = new ByteArrayResource(sftpService.getFileByteArray(sourcePath,sourceFileName));
        System.out.println("content length------- "+resource.contentLength());
        return ResponseEntity.ok()
                .headers(header)
                .contentLength(resource.contentLength())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(resource);
    }

    @RequestMapping(path="/listFile", method = RequestMethod.GET)
    public ResponseEntity<List> listDirFiles(@RequestParam("remotePath") String remotePath) throws SftpException, JSchException {
        ArrayList<ChannelSftp.LsEntry> lsEntries = sftpService.getFileList(remotePath);
        return ResponseEntity.ok()
                .headers(new HttpHeaders())
                .contentType(MediaType.parseMediaType("application/json"))
                .body(lsEntries);
    }
}