//package com.rahul.springbootsftpintegration.config;
//
//import com.jcraft.jsch.ChannelSftp;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.integration.annotation.InboundChannelAdapter;
//import org.springframework.integration.annotation.Poller;
//import org.springframework.integration.annotation.ServiceActivator;
//import org.springframework.integration.core.MessageSource;
//import org.springframework.integration.file.filters.AcceptOnceFileListFilter;
//import org.springframework.integration.file.remote.session.CachingSessionFactory;
//import org.springframework.integration.file.remote.session.SessionFactory;
//import org.springframework.integration.sftp.filters.SftpSimplePatternFileListFilter;
//import org.springframework.integration.sftp.inbound.SftpInboundFileSynchronizer;
//import org.springframework.integration.sftp.inbound.SftpInboundFileSynchronizingMessageSource;
//import org.springframework.integration.sftp.session.DefaultSftpSessionFactory;
//import org.springframework.messaging.Message;
//import org.springframework.messaging.MessageHandler;
//import org.springframework.messaging.MessagingException;
//
//import java.io.File;
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.nio.file.attribute.BasicFileAttributes;
//
//@Configuration
//public class SftpConfig {
//    @Value("${sftp.host}")
//    private String sftpHost;
//    @Value("${sftp.port}")
//    private int sftpPort;
//    @Value("${sftp.user}")
//    private String sftpUser;
//    @Value("${sftp.password}")
//    private String sftpPasword;
//    @Value("${sftp.remote.directory.download}")
//    private String sftpRemoteDirectoryDownload;
//    @Value("${sftp.local.directory.download}")
//    private String sftpLocalDirectoryDownload;
//    @Value("${sftp.remote.directory.download.filter}")
//    private String sftpRemoteDirectoryDownloadFilter;
//    public static final String INTERVAL = "50000";
//
//    @Bean
//    public SessionFactory<ChannelSftp.LsEntry> sftpSessionFactory() {
//        DefaultSftpSessionFactory factory = new DefaultSftpSessionFactory(true);
//        factory.setHost(sftpHost);
//        factory.setPort(sftpPort);
//        factory.setUser(sftpUser);
//        factory.setPassword(sftpPasword);
//        factory.setAllowUnknownKeys(true);
//        return new CachingSessionFactory<ChannelSftp.LsEntry>(factory);
//    }
//
//    @Bean
//    public SftpInboundFileSynchronizer sftpInboundFileSynchronizer() {
//        SftpInboundFileSynchronizer fileSynchronizer = new SftpInboundFileSynchronizer(sftpSessionFactory());
//        fileSynchronizer.setDeleteRemoteFiles(false);
//        fileSynchronizer.setRemoteDirectory(sftpRemoteDirectoryDownload);
//        fileSynchronizer.setFilter(new SftpSimplePatternFileListFilter(sftpRemoteDirectoryDownloadFilter));
//        return fileSynchronizer;
//    }
//
//    @Bean
//    @InboundChannelAdapter(channel = "sftpChannel", poller = @Poller(fixedDelay = INTERVAL))
//    public MessageSource<File> sftpMessageSource() {
//        SftpInboundFileSynchronizingMessageSource source = new SftpInboundFileSynchronizingMessageSource(
//                sftpInboundFileSynchronizer());
//        source.setLocalDirectory(new File(sftpLocalDirectoryDownload));
//        source.setAutoCreateLocalDirectory(true);
//        source.setLocalFilter(new AcceptOnceFileListFilter<File>());
//        printFileMetadata();
//        return source;
//    }
//
//    @Bean
//    @ServiceActivator(inputChannel = "sftpChannel")
//    public MessageHandler handler() {
//        return new MessageHandler() {
//
//            @Override
//            public void handleMessage(Message<?> message) throws MessagingException {
//                System.out.println(message.getPayload());
//            }
//
//        };
//    }
//
//    public void printFileMetadata(){
//
//        BasicFileAttributes attr = null;
//        try {
//            Path files = getUsersProjectRootDirectory();
//            System.out.println("----------------");
//            System.out.println(files);
//            attr = Files.readAttributes(files, BasicFileAttributes.class);
//            System.out.println("creationTime: " + attr.creationTime());
//            System.out.println("lastAccessTime: " + attr.lastAccessTime());
//            System.out.println("lastModifiedTime: " + attr.lastModifiedTime());
//
//            System.out.println("isDirectory: " + attr.isDirectory());
//            System.out.println("isOther: " + attr.isOther());
//            System.out.println("isRegularFile: " + attr.isRegularFile());
//            System.out.println("isSymbolicLink: " + attr.isSymbolicLink());
//            System.out.println("size: " + attr.size());
//        } catch (IOException e) {
//            System.out.println("Error occurred while reading the file");
//            e.printStackTrace();
//        }
//    }
//
//    public Path getUsersProjectRootDirectory() {
//        String envRootDir = System.getProperty("user.dir");
//        Path rootDir = Paths.get(sftpRemoteDirectoryDownload+"\\emp_details.xlsx").normalize().toAbsolutePath();
//        if ( rootDir.startsWith(envRootDir) ) {
//            return rootDir;
//        } else {
//            throw new RuntimeException("Root dir not found in user directory.");
//        }
//    }
//}
