package br.com.alesaudate.samples.artemisspringboot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@Profile("!client")
public class Consumer {


    @Value("${spring.profiles.active}")
    private String[] activeProfiles;

    private AtomicInteger processedMessages = new AtomicInteger(0);

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
            System.out.println(getServerName() + " got message: " + text + " . Redelivered? " + message.getJMSRedelivered());
            latestMessage = message;
            return message;
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }


   /* @JmsListener(destination = "${example.queue}", containerFactory = "primaryListenerJmsContainerFactory")
    public void receiveMessage(Message message, Session session) throws JMSException, InterruptedException {
        TextMessage textMessage = (TextMessage) message;
        String text = textMessage.getText();
        checkMessage(text);
        System.out.println(getServerName() + " got message: " + text + " . Redelivered? " + message.getJMSRedelivered());
    }


    private void checkMessage(String text) {
        int processed = processedMessages.addAndGet(1);

        if (processed >= 5) {
            throw new RuntimeException(text + " : not processed on server " + getServerName());
        }
    }*/


    private String getServerName() {
        return activeProfiles[0];
    }


}
