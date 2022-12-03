package br.com.alesaudate.samples.artemisspringboot.config;

import org.apache.activemq.artemis.util.ServerUtil;
import org.springframework.core.io.ClassPathResource;

import java.io.File;

public class ArtemisServer {


    private final String serverName;


    private Process process;
;
    public ArtemisServer(String serverName) {
        this.serverName = serverName;
    }

    public void start() throws Exception {
        ClassPathResource classPathResource = new ClassPathResource("");
        String path = classPathResource.getFile().getParent() + File.separator + serverName;
        this.process = ServerUtil.startServer(path, serverName, 0, 5000);
    }

    public void stop() throws Exception {
        ServerUtil.killServer(process);
    }
}
