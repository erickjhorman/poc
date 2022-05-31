package com.eqfx.latam.poc.pubsub;

import com.google.cloud.ServiceOptions;
import com.google.cloud.pubsub.v1.Subscriber;
import com.google.pubsub.v1.ProjectSubscriptionName;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class GMessageSubscriber {
    private Log log = LogFactory.getLog(GMessageSubscriber.class);

    public void consumeNotification() {
        String projectId = ServiceOptions.getDefaultProjectId();
        String subscriptionId = System.getenv("SUBSCRIPTION_ID");
        log.info(String.format("Project: %s", projectId));
        log.info(String.format("SubscriptionId: %s", subscriptionId));

        createSubscriber(ProjectSubscriptionName.of(projectId, subscriptionId));
    }

    private void createSubscriber(ProjectSubscriptionName subscriptionName) {
        Subscriber subscriber;
        try {
            subscriber = Subscriber.newBuilder(subscriptionName, new GMessageReceiver()).build();
            subscriber.startAsync().awaitRunning(); /* starting the subscriber and make sure keep listening*/
            subscriber.awaitTerminated(); //terminate the operation when the message is already consumed /*

        } catch (IllegalStateException i) {
            i.printStackTrace();
        }
    }
}
