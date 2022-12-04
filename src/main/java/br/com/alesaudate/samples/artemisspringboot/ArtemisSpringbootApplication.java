package br.com.alesaudate.samples.artemisspringboot;

import br.com.alesaudate.samples.artemisspringboot.config.ArtemisServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jms.JndiConnectionFactoryAutoConfiguration;
import org.springframework.boot.autoconfigure.jms.artemis.ArtemisAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication(exclude = {ArtemisAutoConfiguration.class, JndiConnectionFactoryAutoConfiguration.class})
public class ArtemisSpringbootApplication {

    private static final int NUM_MESSAGES = 30;

    public static void main(String[] args) throws Exception {

        ConfigurableApplicationContext server = startApp("server");
        ConfigurableApplicationContext client = startApp("client");

        // Wait until everything is setup
        Thread.sleep(5000);
        Publisher publisher = client.getBean(Publisher.class);

        // Step 7
        for (int i = 0; i < NUM_MESSAGES; i++) {
            publisher.publishMessage(i);
        }

        Consumer consumer = server.getBean(Consumer.class);

        // Step 8
        for (int i = 0; i < NUM_MESSAGES / 3; i++) {
            consumer.consumeAndAcknowledgeMessage();
        }

        // Step 9
        for (int i = 0; i < NUM_MESSAGES / 3; i++) {
            consumer.consumeMessage();
        }

        // Step 10
        server.getBean("server0", ArtemisServer.class).stop();
        Thread.sleep(5000);

        // Step 11
        try {
            consumer.acknowledgeLatestMessage();
        } catch (RuntimeException e) {
            System.err.println("Got exception while acknowledging message: " + e.getMessage());
        }


        // Step 12
        for (int i = 0; i < NUM_MESSAGES / 3; i++) {
            consumer.consumeAndAcknowledgeMessage();
        }

        // Step 13
        client.stop();
        server.stop();

    }

    private static ConfigurableApplicationContext startApp(String profile) {
        return SpringApplication.run(ArtemisSpringbootApplication.class, new String[]{"--spring.profiles.active=" + profile, "--example.queue=exampleQueue"});
    }

}
