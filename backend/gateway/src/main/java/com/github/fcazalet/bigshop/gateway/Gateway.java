package com.github.fcazalet.bigshop.gateway;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class Gateway {
    public static void main(String[] args) {
        new SpringApplicationBuilder(Gateway.class).web(WebApplicationType.SERVLET).run(args);
    }
}
