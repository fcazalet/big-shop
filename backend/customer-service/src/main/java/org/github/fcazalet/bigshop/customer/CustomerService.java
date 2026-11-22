package io.github.fcazalet.bigshop.customer;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.ansi.AnsiColor;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@ConfigurationPropertiesScan
public class CustomerService {
    public static void main(String[] args) {
        new SpringApplicationBuilder(CustomerService.class)
                .web(WebApplicationType.SERVLET)
                .run(args);
    }
}
