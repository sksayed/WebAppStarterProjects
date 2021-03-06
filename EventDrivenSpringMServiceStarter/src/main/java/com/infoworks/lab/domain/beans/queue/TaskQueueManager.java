package com.infoworks.lab.domain.beans.queue;

import com.infoworks.lab.beans.tasks.definition.QueuedTaskLifecycleListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.TextMessage;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class TaskQueueManager extends AbstractTaskQueueManager {

    private static final Logger logger = Logger.getLogger("TaskQueueManager");

    public TaskQueueManager(@Autowired QueuedTaskLifecycleListener listener) {
        super(listener);
    }

    @JmsListener(destination = "exeQueue", concurrency = "1-5")
    public void startListener(javax.jms.Message message) throws JMSException {
        // retrieve the message content
        TextMessage textMessage = (TextMessage) message;
        String text = textMessage.getText();
        logger.log(Level.INFO, "EXE-QUEUE: Message received {0} ", text);
        if (handleTextOnStart(text)){
            message.acknowledge();
        }
    }

    @JmsListener(destination = "abortQueue", concurrency = "1-3")
    public void abortListener(javax.jms.Message message) throws JMSException {
        // retrieve the message content
        TextMessage textMessage = (TextMessage) message;
        String text = textMessage.getText();
        logger.log(Level.INFO, "ABORT-QUEUE: Message received {0} ", text);
        if (handleTextOnStop(text)) {
            message.acknowledge();
        }
    }

}
