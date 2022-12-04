package br.com.alesaudate.samples.artemisspringboot.config;

import org.apache.activemq.artemis.jms.client.ActiveMQQueue;
import org.apache.activemq.artemis.jndi.ActiveMQInitialContextFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.jms.ConnectionFactory;
import javax.jms.Queue;
import javax.naming.Context;
import javax.naming.NamingException;
import java.util.Hashtable;

@Configuration
public class JMSConfiguration {


    @Value("${jms.queue}")
    private String exampleQueueName;

    @Value("${jms.jndi.url}")
    private String jndiUrl;

    @Value("${jms.connectionFactory}")
    private String connectionFactory;

    @Bean
    public Context getJNDIContext() throws NamingException {
        ActiveMQInitialContextFactory initialContextFactory = new ActiveMQInitialContextFactory();
        Hashtable<String, String> properties = new Hashtable<>();
        properties.put(Context.PROVIDER_URL, jndiUrl);
        return initialContextFactory.getInitialContext(properties);
    }

    @Bean
    public ConnectionFactory connectionFactory(Context context) throws NamingException {
        return (ConnectionFactory) context.lookup(connectionFactory);
    }

    @Bean
    @Qualifier("exampleQueue")
    public Queue exampleQueue(Context context) throws NamingException, InterruptedException {
        return new ActiveMQQueue(exampleQueueName);
    }
}
