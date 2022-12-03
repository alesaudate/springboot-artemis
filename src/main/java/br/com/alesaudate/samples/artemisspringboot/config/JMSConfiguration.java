package br.com.alesaudate.samples.artemisspringboot.config;

import org.apache.activemq.artemis.jndi.ActiveMQInitialContextFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.util.ErrorHandler;

import javax.jms.ConnectionFactory;
import javax.naming.Context;
import javax.naming.NamingException;
import java.util.Hashtable;

@Configuration
public class JMSConfiguration {


    @Bean
    public Context getJNDIContext() throws NamingException {
        ActiveMQInitialContextFactory initialContextFactory = new ActiveMQInitialContextFactory();
        Hashtable<String, String> properties = new Hashtable<>();
        properties.put(Context.PROVIDER_URL, "tcp://localhost:61616?ha=true&retryInterval=1000&retryIntervalMultiplier=1.0&reconnectAttempts=-1");

        return initialContextFactory.getInitialContext(properties);
    }

    @Bean
    public ConnectionFactory connectionFactory(Context context) throws NamingException {
        return (ConnectionFactory) context.lookup("ConnectionFactory");
    }


    @Bean
    public DefaultJmsListenerContainerFactory primaryListenerJmsContainerFactory(DefaultJmsListenerContainerFactory defaultJmsListenerContainerFactory) {
        defaultJmsListenerContainerFactory.setErrorHandler(new ErrorHandler() {
            @Override
            public void handleError(Throwable t) {
                //System.err.println(t.getMessage());
            }
        });
        return defaultJmsListenerContainerFactory;
    }
}