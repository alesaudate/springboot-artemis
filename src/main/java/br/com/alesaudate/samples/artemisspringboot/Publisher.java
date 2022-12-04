package br.com.alesaudate.samples.artemisspringboot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.jms.Queue;

@Component
public class Publisher {

    private final JmsTemplate jmsTemplate;

    @Autowired
    @Qualifier("exampleQueue")
    private Queue exampleQueue;

    @Autowired
    public Publisher(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public void publishMessage(int i) {
        jmsTemplate.convertAndSend(exampleQueue, "This is text message " + i);
    }
}
