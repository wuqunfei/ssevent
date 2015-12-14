package com.bird.resources;

import org.glassfish.jersey.media.sse.EventOutput;
import org.glassfish.jersey.media.sse.OutboundEvent;
import org.glassfish.jersey.media.sse.SseFeature;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.io.IOException;

/**
 * @author Qunfei Wu on 14/12/15.
 */
@Component
@Path("/event")
public class EventResource {

    @GET
    @Produces(SseFeature.SERVER_SENT_EVENTS)
    public EventOutput getServerSentEvents() {
        final EventOutput eventOutput = new EventOutput();
        new Thread(() -> {
            try {
                for (int i = 0; i < 10; i++) {
                    final OutboundEvent.Builder eventBuilder
                            = new OutboundEvent.Builder();
                    eventBuilder.name("message-to-client");
                    eventBuilder.data(String.class,
                            "Hello world " + i + "!");
                    final OutboundEvent event = eventBuilder.build();
                    eventOutput.write(event);
                }
            } catch (IOException e) {
                throw new RuntimeException(
                        "Error when writing the event.", e);
            } finally {
                try {
                    eventOutput.close();
                } catch (IOException ioClose) {
                    throw new RuntimeException(
                            "Error when closing the event output.", ioClose);
                }
            }
        }).start();
        return eventOutput;
    }
}