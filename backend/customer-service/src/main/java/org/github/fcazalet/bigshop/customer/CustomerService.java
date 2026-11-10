package org.github.fcazalet.bigshop.customer;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.ansi.AnsiColor;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class CustomerService {
    public static void main(String[] args) {
        new SpringApplicationBuilder(CustomerService.class)
                .web(WebApplicationType.SERVLET)
                .run(args);
    }
}
