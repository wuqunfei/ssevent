package com.bird;

import org.easetech.easytest.annotation.DataLoader;
import org.easetech.easytest.annotation.Parallel;
import org.easetech.easytest.annotation.Param;
import org.easetech.easytest.loader.LoaderType;
import org.easetech.easytest.runner.DataDrivenTestRunner;
import org.glassfish.jersey.media.sse.EventInput;
import org.glassfish.jersey.media.sse.InboundEvent;
import org.glassfish.jersey.media.sse.SseFeature;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import java.util.Date;

/**
 * Created by wuqunfei on 2015/12/19.
 */
@RunWith(DataDrivenTestRunner.class)
@DataLoader(filePaths = "2k.csv", loaderType = LoaderType.CSV)
@Parallel(threads = 16)
public class Performance2KTest {
    private Logger logger = LoggerFactory.getLogger(Performance2KTest.class);
    private WebTarget target;

    @Before
    public void setup() {
        Client client = ClientBuilder.newBuilder()
                .register(SseFeature.class).build();
        String address = "http://localhost:8080/event";
        target = client.target(address);
    }

    @Test
    public void test2KRequest(@Param(name = "terminalName") String terminalName, @Param(name = "isSuccess") Boolean isSuccess, @Param(name = "startTime") Date startTime, @Param(name = "endTime") Date endTime) {
        EventInput eventInput = target.request().get(EventInput.class);
        while (!eventInput.isClosed()) {
            final InboundEvent inboundEvent = eventInput.read();
            if (inboundEvent == null) {
                // connection has been closed
                break;
            }
            logger.info(terminalName + ": " + inboundEvent.getName() + "; " + inboundEvent.readData(String.class));
        }
    }

}
