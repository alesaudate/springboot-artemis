package br.com.alesaudate.samples.artemisspringboot.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Queue;
import javax.jms.Session;

@Configuration
@Profile("!client")
public class ArtemisMasterConfig {


    @Value("${spring.profiles.active}")
    private String[] activeProfiles;


    @Bean(initMethod = "start", destroyMethod = "stop")
    public ArtemisServer createServer() {
        return new ArtemisServer(activeProfiles[0]);
    }


    @Bean
    public MessageConsumer messageConsumer(@Qualifier("exampleQueue") Queue exampleQueue, ConnectionFactory connectionFactory, ArtemisServer artemisServer) throws JMSException {
        Connection connection = connectionFactory.createConnection();
        Session session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
        connection.start();
        return session.createConsumer(exampleQueue);
    }
}
