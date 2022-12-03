package br.com.alesaudate.samples.artemisspringboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jms.JndiConnectionFactoryAutoConfiguration;
import org.springframework.boot.autoconfigure.jms.artemis.ArtemisAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication(exclude = {ArtemisAutoConfiguration.class, JndiConnectionFactoryAutoConfiguration.class})
public class ArtemisSpringbootApplication {

    private static final int NUM_MESSAGES = 30;

    public static void main(String[] args) throws Exception {

        ConfigurableApplicationContext server0 = startApp("server0");
        ConfigurableApplicationContext server1 = startApp("server1");
        ConfigurableApplicationContext server2 = startApp("server2");
        ConfigurableApplicationContext client = startApp("client");

        // Wait until everything is setup
        Thread.sleep(5000);
        Publisher publisher = client.getBean(Publisher.class);

        for (int i = 0; i < NUM_MESSAGES; i++) {
            publisher.publishMessage(i);
        }

        server0.getBean(Consumer.class).consumeAndAcknowledgeMessage();

    }

    private static ConfigurableApplicationContext startApp(String profile) {
        return SpringApplication.run(ArtemisSpringbootApplication.class, new String[]{"--spring.profiles.active=" + profile, "--example.queue=exampleQueue"});
    }

}
