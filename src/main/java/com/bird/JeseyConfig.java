package com.bird;

import com.bird.resources.EventResource;
import com.bird.resources.HelloResource;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

/**
 * Created by wuqunfei on 2015/12/12.
 */
@Component
public class JeseyConfig extends ResourceConfig {

    public JeseyConfig() {
        register(HelloResource.class);
        register(EventResource.class);
    }
}
