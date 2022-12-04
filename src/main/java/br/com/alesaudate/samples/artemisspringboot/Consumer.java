package br.com.alesaudate.samples.artemisspringboot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.TextMessage;

@Component
@Profile("!client")
public class Consumer {


    @Autowired
    private MessageConsumer messageConsumer;

    private Message latestMessage;


    public void consumeAndAcknowledgeMessage() {
        try {
            Message message = consumeMessage();
            message.acknowledge();
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }

    public void acknowledgeLatestMessage() {
        if (latestMessage != null) {
            try {
                latestMessage.acknowledge();
                latestMessage = null;
            } catch (JMSException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public Message consumeMessage() {
        try {
            Message message = messageConsumer.receive();
            TextMessage textMessage = (TextMessage) message;
            String text = textMessage.getText();
            System.out.println("Got message: " + text + " . Redelivered? " + message.getJMSRedelivered());
            latestMessage = message;
            return message;
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }
}
