package br.com.alesaudate.samples.artemisspringboot.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("!client")
public class ArtemisMasterConfig {


    @Value("${spring.profiles.active}")
    private String[] activeProfiles;


    @Bean(initMethod = "start", destroyMethod = "stop")
    public ArtemisServer createServer() {
        return new ArtemisServer(activeProfiles[0]);
    }
}
