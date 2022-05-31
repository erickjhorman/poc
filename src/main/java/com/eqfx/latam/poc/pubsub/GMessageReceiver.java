package com.eqfx.latam.poc.pubsub;

import com.google.cloud.pubsub.v1.AckReplyConsumer;
import com.google.cloud.pubsub.v1.MessageReceiver;
import com.google.pubsub.v1.PubsubMessage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class GMessageReceiver implements MessageReceiver {

    public static final String OBJECT_FINALIZE = "OBJECT_FINALIZE"; /* it can also be added in an Enum class */
    private Log log = LogFactory.getLog(GMessageReceiver.class);

    @Override
    public void receiveMessage(PubsubMessage pubsubMessage, AckReplyConsumer consumer) {
        String eventType = pubsubMessage.getAttributesOrThrow("eventType");
        String filename = pubsubMessage.getAttributesOrThrow("objectId");
        consumer.ack();

        if(OBJECT_FINALIZE.equals(eventType)) {
            //write pipeline logic and use filename variable to read the file from Pub/sub...
            log.info("here running pipeline");
        }
    }
}
