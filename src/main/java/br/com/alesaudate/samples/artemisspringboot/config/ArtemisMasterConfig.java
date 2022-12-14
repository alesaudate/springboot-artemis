package br.com.alesaudate.samples.artemisspringboot.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Queue;
import javax.jms.Session;

@Configuration
public class ArtemisMasterConfig {

    @Bean(initMethod = "start", destroyMethod = "stop")
    @Primary
    public ArtemisServer server0() {
        return new ArtemisServer("server0");
    }

    @Bean(initMethod = "start", destroyMethod = "stop")
    public ArtemisServer server1() {
        return new ArtemisServer("server1");
    }

    @Bean(initMethod = "start", destroyMethod = "stop")
    public ArtemisServer server2() {
        return new ArtemisServer("server2");
    }

    @Bean
    public MessageConsumer messageConsumer(@Qualifier("exampleQueue") Queue exampleQueue, ConnectionFactory connectionFactory, ArtemisServer artemisServer) throws JMSException {
        Connection connection = connectionFactory.createConnection();
        Session session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
        connection.start();
        return session.createConsumer(exampleQueue);
    }
}
