package com.bird;

import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

/**
 * Created by wuqunfei on 2015/12/12.
 */
@Component
@Path("/hello")
public class HelloResource {


    @GET
    public String message() {
        return "hello world";
    }
}
