package com.bird;

import org.glassfish.jersey.media.sse.EventInput;
import org.glassfish.jersey.media.sse.InboundEvent;
import org.glassfish.jersey.media.sse.SseFeature;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SseventApplication.class)
@WebAppConfiguration
@IntegrationTest("server.port:${local.server.port}")
public class CommunicationTest {

    @Value("${local.server.port}")
    int port;

    String address;

    @Before
    public void setup() {
        address = "http://localhost:" + port + "/event";
    }


    @Test
    public void contextLoads() {
    }

    @Test
    public void oneClient() {
        Client client = ClientBuilder.newBuilder()
                .register(SseFeature.class).build();

        WebTarget target = client.target(address);

        EventInput eventInput = target.request().get(EventInput.class);
        while (!eventInput.isClosed()) {
            final InboundEvent inboundEvent = eventInput.read();
            if (inboundEvent == null) {
                // connection has been closed
                break;
            }
            System.out.println(inboundEvent.getName() + "; "
                    + inboundEvent.readData(String.class));
        }
    }

}
