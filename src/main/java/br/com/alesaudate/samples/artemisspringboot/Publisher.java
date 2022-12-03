package br.com.alesaudate.samples.artemisspringboot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
@Profile("client")
public class Publisher {

    private final JmsTemplate jmsTemplate;

    @Value("${example.queue}")
    private String queueName;

    @Autowired
    public Publisher(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public void publishMessage(int i) {
        jmsTemplate.convertAndSend(queueName, "This is text message " + i);
    }
}
